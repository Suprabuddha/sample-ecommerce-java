package com.example.ecommerce.controller;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Order REST controller with technical debt patterns
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    /**
     * Get all orders - TECHNICAL DEBT: No pagination, loads all data
     */
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        logger.info("GET /api/orders - Fetching all orders");
        
        try {
            List<Order> orders = orderService.getAllOrders();
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            logger.error("Error fetching orders", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get order by ID - TECHNICAL DEBT: No authorization check
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        logger.info("GET /api/orders/" + id);
        
        try {
            Optional<Order> order = orderService.getOrderById(id);
            if (order.isPresent()) {
                return ResponseEntity.ok(order.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error fetching order with ID: " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Create new order - TECHNICAL DEBT: No proper validation
     */
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderRequest request) {
        logger.info("POST /api/orders - Creating order for user: " + request.getUserId());
        
        try {
            Order createdOrder = orderService.createOrder(request.getUserId(), request.getShippingAddress());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
        } catch (RuntimeException e) {
            logger.warn("Error creating order: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Error creating order", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Add item to order - TECHNICAL DEBT: No stock validation
     */
    @PostMapping("/{orderId}/items")
    public ResponseEntity<Order> addItemToOrder(@PathVariable Long orderId, @RequestBody AddItemRequest request) {
        logger.info("POST /api/orders/" + orderId + "/items - Adding product: " + request.getProductId());
        
        try {
            Order updatedOrder = orderService.addItemToOrder(
                orderId, 
                request.getProductId(), 
                request.getQuantity()
            );
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException e) {
            logger.warn("Error adding item to order: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Error adding item to order", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Update order status
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long id, @RequestBody UpdateStatusRequest request) {
        logger.info("PATCH /api/orders/" + id + "/status - New status: " + request.getStatus());
        
        try {
            Order.OrderStatus status = Order.OrderStatus.valueOf(request.getStatus());
            Order updatedOrder = orderService.updateOrderStatus(id, status);
            return ResponseEntity.ok(updatedOrder);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid order status: " + request.getStatus());
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            logger.warn("Order not found: " + e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error updating order status", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get orders by user - TECHNICAL DEBT: No authorization check
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable Long userId) {
        logger.info("GET /api/orders/user/" + userId);
        
        try {
            List<Order> orders = orderService.getOrdersByUser(userId);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            logger.error("Error fetching orders for user: " + userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get orders by status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Order>> getOrdersByStatus(@PathVariable String status) {
        logger.info("GET /api/orders/status/" + status);
        
        try {
            Order.OrderStatus orderStatus = Order.OrderStatus.valueOf(status.toUpperCase());
            List<Order> orders = orderService.getOrdersByStatus(orderStatus);
            return ResponseEntity.ok(orders);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid order status: " + status);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Error fetching orders by status", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Calculate order total
     */
    @GetMapping("/{id}/total")
    public ResponseEntity<BigDecimal> calculateOrderTotal(@PathVariable Long id) {
        logger.info("GET /api/orders/" + id + "/total");
        
        try {
            BigDecimal total = orderService.calculateOrderTotal(id);
            return ResponseEntity.ok(total);
        } catch (RuntimeException e) {
            logger.warn("Order not found: " + e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error calculating order total", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Cancel order
     */
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long id) {
        logger.info("PATCH /api/orders/" + id + "/cancel");
        
        try {
            Order cancelledOrder = orderService.cancelOrder(id);
            return ResponseEntity.ok(cancelledOrder);
        } catch (RuntimeException e) {
            logger.warn("Order not found: " + e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error cancelling order", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Process payment - TECHNICAL DEBT: Dummy implementation
     */
    @PostMapping("/{id}/payment")
    public ResponseEntity<PaymentResponse> processPayment(@PathVariable Long id, @RequestBody PaymentRequest request) {
        logger.info("POST /api/orders/" + id + "/payment - Amount: " + request.getAmount());
        
        try {
            boolean success = orderService.processPayment(id, request.getAmount());
            PaymentResponse response = new PaymentResponse();
            response.setSuccess(success);
            response.setMessage(success ? "Payment processed successfully" : "Payment failed");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error processing payment", e);
            PaymentResponse response = new PaymentResponse();
            response.setSuccess(false);
            response.setMessage("Payment processing error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get recent orders - TECHNICAL DEBT: Hardcoded limit
     */
    @GetMapping("/recent")
    public ResponseEntity<List<Order>> getRecentOrders() {
        logger.info("GET /api/orders/recent");
        
        try {
            List<Order> orders = orderService.getRecentOrders();
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            logger.error("Error fetching recent orders", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // TECHNICAL DEBT: Inner classes for request/response objects (should be separate DTOs)
    public static class CreateOrderRequest {
        private Long userId;
        private String shippingAddress;

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getShippingAddress() {
            return shippingAddress;
        }

        public void setShippingAddress(String shippingAddress) {
            this.shippingAddress = shippingAddress;
        }
    }

    public static class AddItemRequest {
        private Long productId;
        private Integer quantity;

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }

    public static class UpdateStatusRequest {
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public static class PaymentRequest {
        private BigDecimal amount;

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }
    }

    public static class PaymentResponse {
        private boolean success;
        private String message;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}