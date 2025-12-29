package com.example.ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Home controller for serving the main page
 */
@Controller
public class HomeController {

    /**
     * Serve the main index page
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }

    /**
     * Alternative mapping for home
     */
    @GetMapping("/home")
    public String home() {
        return "index";
    }

    /**
     * Serve the products page
     */
    @GetMapping("/products")
    public String products() {
        return "products";
    }

    /**
     * Serve the orders page
     */
    @GetMapping("/orders")
    public String orders() {
        return "orders";
    }

    /**
     * Serve the product detail page
     */
    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable Long id) {
        return "product-detail";
    }

    /**
     * Serve the cart page
     */
    @GetMapping("/cart")
    public String cart() {
        return "cart";
    }
}