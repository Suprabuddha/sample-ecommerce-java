package com.example.ecommerce.service;

import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * User service with intentional security issues and technical debt
 */
@Service
public class UserService {

    // Updated to use SLF4J with Logback (Spring Boot 3 default)
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    /**
     * Get all users - TECHNICAL DEBT: No pagination, exposes sensitive data
     */
    public List<User> getAllUsers() {
        logger.info("Fetching all users");
        return userRepository.findAll();
    }

    /**
     * Get user by ID
     */
    public Optional<User> getUserById(Long id) {
        logger.info("Fetching user with ID: " + id);
        return userRepository.findById(id);
    }

    /**
     * Create new user - TECHNICAL DEBT: Stores plain text password
     */
    public User createUser(User user) {
        logger.info("Creating new user: " + user.getUsername());
        
        // TECHNICAL DEBT: Basic validation using deprecated StringUtils
        if (StringUtils.isBlank(user.getUsername())) {
            throw new IllegalArgumentException("Username cannot be blank");
        }
        
        if (StringUtils.isBlank(user.getEmail())) {
            throw new IllegalArgumentException("Email cannot be blank");
        }
        
        // TECHNICAL DEBT: Weak password validation
        if (StringUtils.isBlank(user.getPassword()) || user.getPassword().length() < 4) {
            throw new IllegalArgumentException("Password must be at least 4 characters");
        }
        
        // TECHNICAL DEBT: No duplicate username/email check
        user.setCreatedDate(new Date());
        
        // TECHNICAL DEBT: Storing password as plain text
        return userRepository.save(user);
    }

    /**
     * Update user
     */
    public User updateUser(Long id, User userDetails) {
        logger.info("Updating user with ID: " + id);
        
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            throw new RuntimeException("User not found with ID: " + id);
        }
        
        User user = optionalUser.get();
        
        // TECHNICAL DEBT: Direct field updates without proper validation
        if (StringUtils.isNotBlank(userDetails.getFirstName())) {
            user.setFirstName(userDetails.getFirstName());
        }
        if (StringUtils.isNotBlank(userDetails.getLastName())) {
            user.setLastName(userDetails.getLastName());
        }
        if (StringUtils.isNotBlank(userDetails.getEmail())) {
            user.setEmail(userDetails.getEmail());
        }
        if (StringUtils.isNotBlank(userDetails.getPhoneNumber())) {
            user.setPhoneNumber(userDetails.getPhoneNumber());
        }
        
        return userRepository.save(user);
    }

    /**
     * Delete user - TECHNICAL DEBT: Hard delete, no soft delete option
     */
    public void deleteUser(Long id) {
        logger.info("Deleting user with ID: " + id);
        
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with ID: " + id);
        }
        
        userRepository.deleteById(id);
    }

    /**
     * Authenticate user - TECHNICAL DEBT: Plain text password comparison
     */
    public Optional<User> authenticateUser(String username, String password) {
        logger.info("Authenticating user: " + username);
        
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return Optional.empty();
        }
        
        // TECHNICAL DEBT: Insecure authentication with plain text password
        Optional<User> user = userRepository.findByUsernameAndPassword(username, password);
        
        if (user.isPresent()) {
            // Update last login
            User authenticatedUser = user.get();
            authenticatedUser.setLastLogin(new Date());
            userRepository.save(authenticatedUser);
            logger.info("User authenticated successfully: " + username);
        } else {
            logger.warn("Authentication failed for user: " + username);
        }
        
        return user;
    }

    /**
     * Find user by username
     */
    public Optional<User> findByUsername(String username) {
        logger.info("Finding user by username: " + username);
        return userRepository.findByUsername(username);
    }

    /**
     * Find user by email
     */
    public Optional<User> findByEmail(String email) {
        logger.info("Finding user by email: " + email);
        return userRepository.findByEmail(email);
    }

    /**
     * Get active users - TECHNICAL DEBT: No pagination
     */
    public List<User> getActiveUsers() {
        logger.info("Fetching active users");
        return userRepository.findByIsActiveTrue();
    }

    /**
     * Change password - TECHNICAL DEBT: No old password verification, plain text storage
     */
    public void changePassword(Long userId, String newPassword) {
        logger.info("Changing password for user ID: " + userId);
        
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            
            // TECHNICAL DEBT: No old password verification
            // TECHNICAL DEBT: Weak password validation
            if (StringUtils.isBlank(newPassword) || newPassword.length() < 4) {
                throw new IllegalArgumentException("Password must be at least 4 characters");
            }
            
            // TECHNICAL DEBT: Storing plain text password
            user.setPassword(newPassword);
            userRepository.save(user);
            
            logger.info("Password changed successfully for user ID: " + userId);
        } else {
            throw new RuntimeException("User not found with ID: " + userId);
        }
    }

    /**
     * Deactivate user instead of deleting
     */
    public void deactivateUser(Long userId) {
        logger.info("Deactivating user with ID: " + userId);
        
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setIsActive(false);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found with ID: " + userId);
        }
    }
}