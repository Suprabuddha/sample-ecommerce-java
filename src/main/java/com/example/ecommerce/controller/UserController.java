package com.example.ecommerce.controller;

import com.example.ecommerce.model.User;
import com.example.ecommerce.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * User REST controller with security issues and technical debt
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * Get all users - TECHNICAL DEBT: Exposes sensitive user data, no pagination
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        logger.info("GET /api/users - Fetching all users");
        
        try {
            List<User> users = userService.getAllUsers();
            // TECHNICAL DEBT: Returns all user data including passwords
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            logger.error("Error fetching users", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get user by ID - TECHNICAL DEBT: No authorization check
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        logger.info("GET /api/users/{}", id);
        
        try {
            Optional<User> user = userService.getUserById(id);
            if (user.isPresent()) {
                // TECHNICAL DEBT: Returns user data including password
                return ResponseEntity.ok(user.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error fetching user with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Create new user - TECHNICAL DEBT: No proper validation, stores plain text password
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        logger.info("POST /api/users - Creating user: {}", user.getUsername());
        
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid user data: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Error creating user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Update user - TECHNICAL DEBT: No authorization check
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        logger.info("PUT /api/users/{}", id);
        
        try {
            User updatedUser = userService.updateUser(id, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            logger.warn("User not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error updating user with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Delete user - TECHNICAL DEBT: Hard delete, no authorization check
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        logger.info("DELETE /api/users/{}", id);
        
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            logger.warn("User not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error deleting user with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * User login - TECHNICAL DEBT: Insecure authentication, plain text password
     */
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest loginRequest) {
        logger.info("POST /api/users/login - User: {}", loginRequest.getUsername());
        
        try {
            Optional<User> user = userService.authenticateUser(
                loginRequest.getUsername(), 
                loginRequest.getPassword()
            );
            
            if (user.isPresent()) {
                // TECHNICAL DEBT: Returns full user object including password
                return ResponseEntity.ok(user.get());
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            logger.error("Error during login", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get user by username - TECHNICAL DEBT: Potential information disclosure
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        logger.info("GET /api/users/username/{}", username);
        
        try {
            Optional<User> user = userService.findByUsername(username);
            if (user.isPresent()) {
                return ResponseEntity.ok(user.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error fetching user by username: {}", username, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get active users - TECHNICAL DEBT: No pagination
     */
    @GetMapping("/active")
    public ResponseEntity<List<User>> getActiveUsers() {
        logger.info("GET /api/users/active");
        
        try {
            List<User> users = userService.getActiveUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            logger.error("Error fetching active users", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Change password - TECHNICAL DEBT: No old password verification
     */
    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> changePassword(@PathVariable Long id, @RequestBody PasswordChangeRequest request) {
        logger.info("PATCH /api/users/{}/password", id);
        
        try {
            userService.changePassword(id, request.getNewPassword());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            logger.warn("Error changing password: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Error changing password for user ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Deactivate user
     */
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id) {
        logger.info("PATCH /api/users/{}/deactivate", id);
        
        try {
            userService.deactivateUser(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            logger.warn("User not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error deactivating user with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // TECHNICAL DEBT: Inner classes for request objects (should be separate DTOs)
    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class PasswordChangeRequest {
        private String newPassword;

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }
}