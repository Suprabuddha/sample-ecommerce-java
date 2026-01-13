package com.example.ecommerce.service;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Product service with technical debt patterns
 * Uses deprecated Log4j and commons-lang
 */
@Service
public class ProductService {

    // TECHNICAL DEBT: Using deprecated Log4j instead of SLF4J
    private static final Logger logger = Logger.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    /**
     * Get all products - TECHNICAL DEBT: No pagination
     */
    public List<Product> getAllProducts() {
        logger.info("Fetching all products");
        return productRepository.findAll();
    }

    /**
     * Get product by ID
     */
    public Optional<Product> getProductById(Long id) {
        logger.info("Fetching product with ID: " + id);
        return productRepository.findById(id);
    }

    /**
     * Create new product with validation using deprecated commons-lang
     */
    public Product createProduct(Product product) {
        logger.info("Creating new product: " + product.getName());
        
        // TECHNICAL DEBT: Basic validation using deprecated StringUtils
        if (StringUtils.isBlank(product.getName())) {
            throw new IllegalArgumentException("Product name cannot be blank");
        }
        
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Product price must be greater than zero");
        }

        product.setCreatedDate(new Date());
        product.setUpdatedDate(new Date());
        
        return productRepository.save(product);
    }

    /**
     * Update product - TECHNICAL DEBT: No proper validation or error handling
     */
    public Product updateProduct(Long id, Product productDetails) {
        logger.info("Updating product with ID: " + id);
        
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (!optionalProduct.isPresent()) {
            throw new RuntimeException("Product not found with ID: " + id);
        }
        
        Product product = optionalProduct.get();
        
        // TECHNICAL DEBT: Direct field updates without validation
        if (StringUtils.isNotBlank(productDetails.getName())) {
            product.setName(productDetails.getName());
        }
        if (StringUtils.isNotBlank(productDetails.getDescription())) {
            product.setDescription(productDetails.getDescription());
        }
        if (productDetails.getPrice() != null) {
            product.setPrice(productDetails.getPrice());
        }
        if (productDetails.getStockQuantity() != null) {
            product.setStockQuantity(productDetails.getStockQuantity());
        }
        
        product.setUpdatedDate(new Date());
        
        return productRepository.save(product);
    }

    /**
     * Delete product - TECHNICAL DEBT: No cascade handling
     */
    public void deleteProduct(Long id) {
        logger.info("Deleting product with ID: " + id);
        
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with ID: " + id);
        }
        
        productRepository.deleteById(id);
    }

    /**
     * Search products by name - TECHNICAL DEBT: Using unsafe native query
     */
    public List<Product> searchProductsByName(String name) {
        logger.info("Searching products by name: " + name);
        
        if (StringUtils.isBlank(name)) {
            return getAllProducts(); // TECHNICAL DEBT: Returns all products if no search term
        }
        
        return productRepository.findByNameContainingNative(name);
    }

    /**
     * Get products by category
     */
    public List<Product> getProductsByCategory(Long categoryId) {
        logger.info("Fetching products for category: " + categoryId);
        return productRepository.findByCategoryId(categoryId);
    }

    /**
     * Get products by price range
     */
    public List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        logger.info("Fetching products in price range: " + minPrice + " - " + maxPrice);
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }

    /**
     * Get available products - TECHNICAL DEBT: No pagination, loads all data
     */
    public List<Product> getAvailableProducts() {
        logger.info("Fetching all available products");
        return productRepository.findAllAvailableProducts();
    }

    /**
     * Update stock quantity - TECHNICAL DEBT: No concurrency control
     */
    public void updateStock(Long productId, Integer quantity) {
        logger.info("Updating stock for product " + productId + " to " + quantity);
        
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setStockQuantity(quantity);
            product.setUpdatedDate(new Date());
            productRepository.save(product);
        } else {
            throw new RuntimeException("Product not found with ID: " + productId);
        }
    }
}