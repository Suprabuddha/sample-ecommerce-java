package com.example.ecommerce.repository;

import com.example.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Product repository with some technical debt patterns
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * TECHNICAL DEBT: Using native SQL instead of JPQL
     * This creates database coupling
     */
    @Query(value = "SELECT * FROM products WHERE name LIKE %:name%", nativeQuery = true)
    List<Product> findByNameContainingNative(@Param("name") String name);

    /**
     * Find products by category - using proper JPQL
     */
    List<Product> findByCategoryId(Long categoryId);

    /**
     * Find products by price range
     */
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    /**
     * TECHNICAL DEBT: Inefficient query - should use pagination
     */
    @Query("SELECT p FROM Product p WHERE p.stockQuantity > 0 ORDER BY p.createdDate DESC")
    List<Product> findAllAvailableProducts();

    /**
     * Find products with low stock
     */
    @Query("SELECT p FROM Product p WHERE p.stockQuantity < :threshold")
    List<Product> findLowStockProducts(@Param("threshold") Integer threshold);

    /**
     * TECHNICAL DEBT: String concatenation in query (potential SQL injection risk)
     */
    @Query(value = "SELECT * FROM products WHERE description LIKE '%" + ":keyword" + "%'", nativeQuery = true)
    List<Product> findByDescriptionKeywordUnsafe(@Param("keyword") String keyword);
}