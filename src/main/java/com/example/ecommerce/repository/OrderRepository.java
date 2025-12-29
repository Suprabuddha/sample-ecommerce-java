package com.example.ecommerce.repository;

import com.example.ecommerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Order repository with technical debt patterns
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Find order by order number
     */
    Optional<Order> findByOrderNumber(String orderNumber);

    /**
     * Find orders by user ID
     */
    List<Order> findByUserId(Long userId);

    /**
     * Find orders by status
     */
    List<Order> findByStatus(Order.OrderStatus status);

    /**
     * TECHNICAL DEBT: Inefficient query - should use pagination
     */
    @Query("SELECT o FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate ORDER BY o.orderDate DESC")
    List<Order> findOrdersByDateRange(@Param("startDate") Date startDate, 
                                    @Param("endDate") Date endDate);

    /**
     * TECHNICAL DEBT: N+1 query problem - fetches orders and then items separately
     */
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId")
    List<Order> findUserOrdersWithPotentialN1Problem(@Param("userId") Long userId);

    /**
     * Find recent orders - TECHNICAL DEBT: Hardcoded limit
     */
    @Query(value = "SELECT * FROM orders ORDER BY order_date DESC LIMIT 10", nativeQuery = true)
    List<Order> findRecentOrders();

    /**
     * TECHNICAL DEBT: Using string concatenation in native query
     */
    @Query(value = "SELECT COUNT(*) FROM orders WHERE status = '" + ":status" + "'", nativeQuery = true)
    Long countOrdersByStatusUnsafe(@Param("status") String status);
}