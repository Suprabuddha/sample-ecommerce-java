-- Sample data for Legacy Ecommerce Application
-- TECHNICAL DEBT: Using plain text passwords and hardcoded data

-- Insert sample users with plain text passwords (TECHNICAL DEBT)
INSERT INTO users (username, email, password, first_name, last_name, phone_number, role, is_active, created_date) VALUES
('admin', 'admin@example.com', 'admin123', 'Admin', 'User', '555-0001', 'ADMIN', true, CURRENT_TIMESTAMP),
('john_doe', 'john@example.com', 'password123', 'John', 'Doe', '555-0002', 'CUSTOMER', true, CURRENT_TIMESTAMP),
('jane_smith', 'jane@example.com', 'pass456', 'Jane', 'Smith', '555-0003', 'CUSTOMER', true, CURRENT_TIMESTAMP),
('manager1', 'manager@example.com', 'manager123', 'Store', 'Manager', '555-0004', 'MANAGER', true, CURRENT_TIMESTAMP),
('customer1', 'customer1@example.com', '123456', 'Customer', 'One', '555-0005', 'CUSTOMER', true, CURRENT_TIMESTAMP);

-- Insert sample products
INSERT INTO products (name, description, price, stock_quantity, category_id, created_date, updated_date) VALUES
('Laptop Computer', 'High-performance laptop for work and gaming', 999.99, 50, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Wireless Mouse', 'Ergonomic wireless mouse with long battery life', 29.99, 200, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Mechanical Keyboard', 'RGB mechanical keyboard for gaming', 149.99, 75, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Monitor 24 inch', '24-inch Full HD monitor with IPS panel', 199.99, 30, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('USB-C Hub', 'Multi-port USB-C hub with HDMI and USB 3.0', 49.99, 100, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Smartphone', 'Latest smartphone with advanced camera', 699.99, 25, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Tablet', '10-inch tablet for entertainment and productivity', 299.99, 40, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Headphones', 'Noise-cancelling wireless headphones', 199.99, 60, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Bluetooth Speaker', 'Portable Bluetooth speaker with great sound', 79.99, 80, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Fitness Tracker', 'Smart fitness tracker with heart rate monitor', 129.99, 90, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample orders
INSERT INTO orders (user_id, order_number, status, total_amount, shipping_address, billing_address, order_date) VALUES
(2, 'ORD-1001', 'DELIVERED', 1199.98, '123 Main St, Anytown, ST 12345', '123 Main St, Anytown, ST 12345', DATEADD('DAY', -10, CURRENT_TIMESTAMP)),
(3, 'ORD-1002', 'SHIPPED', 249.98, '456 Oak Ave, Another City, ST 67890', '456 Oak Ave, Another City, ST 67890', DATEADD('DAY', -5, CURRENT_TIMESTAMP)),
(5, 'ORD-1003', 'PROCESSING', 79.99, '789 Pine Rd, Third Town, ST 11111', '789 Pine Rd, Third Town, ST 11111', DATEADD('DAY', -2, CURRENT_TIMESTAMP)),
(2, 'ORD-1004', 'PENDING', 329.98, '123 Main St, Anytown, ST 12345', '123 Main St, Anytown, ST 12345', DATEADD('DAY', -1, CURRENT_TIMESTAMP));

-- Insert sample order items
INSERT INTO order_items (order_id, product_id, quantity, unit_price, subtotal) VALUES
-- Order 1 items
(1, 1, 1, 999.99, 999.99),
(1, 2, 1, 29.99, 29.99),
(1, 3, 1, 149.99, 149.99),
(1, 5, 1, 49.99, 49.99),

-- Order 2 items  
(2, 4, 1, 199.99, 199.99),
(2, 5, 1, 49.99, 49.99),

-- Order 3 items
(3, 9, 1, 79.99, 79.99),

-- Order 4 items
(4, 7, 1, 299.99, 299.99),
(4, 2, 1, 29.99, 29.99);

-- TECHNICAL DEBT: Hardcoded test data that should be in separate test files
-- This data will be loaded every time the application starts