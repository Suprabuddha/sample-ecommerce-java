package com.example.ecommerce.service;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.OrderItem;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.OrderRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Order service with technical debt patterns
 */
@Service
public class OrderService {

    // TECHNICAL DEBT: Using deprecated Log4j
    private static final Logger logger = Logger.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    /**
     * Get all orders - TECHNICAL DEBT: No pagination, loads all data
     */
    public List<Order> getAllOrders() {
        logger.info("Fetching all orders");
        return orderRepository.findAll();
    }

    /**
     * Get order by ID
     */
    public Optional<Order> getOrderById(Long id) {
        logger.info("Fetching order with ID: " + id);
        return orderRepository.findById(id);
    }

    /**
     * Create new order - TECHNICAL DEBT: No proper validation or transaction management
     */
    public Order createOrder(Long userId, String shippingAddress) {
        logger.info("Creating new order for user ID: " + userId);
        
        Optional<User> optionalUser = userService.getUserById(userId);
        if (!optionalUser.isPresent()) {
            throw new RuntimeException("User not found with ID: " + userId);
        }
        
        User user = optionalUser.get();
        Order order = new Order(user, shippingAddress);
        
        return orderRepository.save(order);
    }

    /**
     * Add item to order - TECHNICAL DEBT: No stock validation, no concurrency control
     */
    public Order addItemToOrder(Long orderId, Long productId, Integer quantity) {
        logger.info("Adding item to order " + orderId + ": product " + productId + ", quantity " + quantity);
        
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (!optionalOrder.isPresent()) {
            throw new RuntimeException("Order not found with ID: " + orderId);
        }
        
        Optional<Product> optionalProduct = productService.getProductById(productId);
        if (!optionalProduct.isPresent()) {
            throw new RuntimeException("Product not found with ID: " + productId);
        }
        
        Order order = optionalOrder.get();
        Product product = optionalProduct.get();
        
        // TECHNICAL DEBT: No stock validation
        if (product.getStockQuantity() < quantity) {
            logger.warn("Insufficient stock for product " + productId + ". Available: " + 
                       product.getStockQuantity() + ", Requested: " + quantity);
            // Continue anyway - this is technical debt
        }
        
        OrderItem orderItem = new OrderItem(order, product, quantity, product.getPrice());
        order.addOrderItem(orderItem);
        
        return orderRepository.save(order);
    }

    /**
     * Update order status
     */
    public Order updateOrderStatus(Long orderId, Order.OrderStatus status) {
        logger.info("Updating order " + orderId + " status to: " + status);
        
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (!optionalOrder.isPresent()) {
            throw new RuntimeException("Order not found with ID: " + orderId);
        }
        
        Order order = optionalOrder.get();
        order.setStatus(status);
        
        // TECHNICAL DEBT: Simple status updates without proper workflow validation
        if (status == Order.OrderStatus.SHIPPED) {
            order.setShippedDate(new Date());
        } else if (status == Order.OrderStatus.DELIVERED) {
            order.setDeliveredDate(new Date());
        }
        
        return orderRepository.save(order);
    }

    /**
     * Get orders by user - TECHNICAL DEBT: Potential N+1 query problem
     */
    public List<Order> getOrdersByUser(Long userId) {
        logger.info("Fetching orders for user ID: " + userId);
        return orderRepository.findByUserId(userId);
    }

    /**
     * Get orders by status
     */
    public List<Order> getOrdersByStatus(Order.OrderStatus status) {
        logger.info("Fetching orders with status: " + status);
        return orderRepository.findByStatus(status);
    }

    /**
     * Calculate order total - TECHNICAL DEBT: Simple calculation without tax, shipping, discounts
     */
    public BigDecimal calculateOrderTotal(Long orderId) {
        logger.info("Calculating total for order: " + orderId);
        
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (!optionalOrder.isPresent()) {
            throw new RuntimeException("Order not found with ID: " + orderId);
        }
        
        Order order = optionalOrder.get();
        BigDecimal total = BigDecimal.ZERO;
        
        for (OrderItem item : order.getOrderItems()) {
            if (item.getSubtotal() != null) {
                total = total.add(item.getSubtotal());
            }
        }
        
        order.setTotalAmount(total);
        orderRepository.save(order);
        
        return total;
    }

    /**
     * Cancel order - TECHNICAL DEBT: No proper cancellation logic
     */
    public Order cancelOrder(Long orderId) {
        logger.info("Cancelling order: " + orderId);
        
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (!optionalOrder.isPresent()) {
            throw new RuntimeException("Order not found with ID: " + orderId);
        }
        
        Order order = optionalOrder.get();
        
        // TECHNICAL DEBT: No validation of order status before cancellation
        order.setStatus(Order.OrderStatus.CANCELLED);
        
        // TECHNICAL DEBT: No stock restoration logic
        
        return orderRepository.save(order);
    }

    /**
     * Get recent orders - TECHNICAL DEBT: Hardcoded limit, no pagination
     */
    public List<Order> getRecentOrders() {
        logger.info("Fetching recent orders");
        return orderRepository.findRecentOrders();
    }

    /**
     * Process order payment - TECHNICAL DEBT: Dummy implementation
     */
    public boolean processPayment(Long orderId, BigDecimal amount) {
        logger.info("Processing payment for order " + orderId + ": $" + amount);
        
        // TECHNICAL DEBT: Dummy payment processing
        // In real implementation, this would integrate with payment gateway
        
        try {
            Thread.sleep(1000); // Simulate payment processing delay
            
            // TECHNICAL DEBT: Always returns true (no real payment validation)
            logger.info("Payment processed successfully for order: " + orderId);
            return true;
            
        } catch (InterruptedException e) {
            logger.error("Payment processing interrupted for order: " + orderId, e);
            return false;
        }
    }
}