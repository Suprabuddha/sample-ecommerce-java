package com.example.ecommerce.repository;

import com.example.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * User repository with technical debt patterns
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by username
     */
    Optional<User> findByUsername(String username);

    /**
     * Find user by email
     */
    Optional<User> findByEmail(String email);

    /**
     * TECHNICAL DEBT: Insecure authentication method
     * Should use proper password hashing
     */
    @Query("SELECT u FROM User u WHERE u.username = :username AND u.password = :password")
    Optional<User> findByUsernameAndPassword(@Param("username") String username, 
                                           @Param("password") String password);

    /**
     * Find active users
     */
    List<User> findByIsActiveTrue();

    /**
     * Find users by role
     */
    List<User> findByRole(User.UserRole role);

    /**
     * TECHNICAL DEBT: Using native SQL with potential security issues
     */
    @Query(value = "SELECT * FROM users WHERE email LIKE %:domain%", nativeQuery = true)
    List<User> findUsersByEmailDomain(@Param("domain") String domain);

    /**
     * TECHNICAL DEBT: Inefficient query - loads all user data
     */
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.orders")
    List<User> findAllUsersWithOrders();
}