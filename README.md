# TechMart - Legacy E-commerce Application

A complete Spring Boot e-commerce application built with **intentional technical debt** for testing upgrade scenarios using ATX custom CLI. This application provides a full shopping experience with a modern frontend while maintaining legacy backend code patterns.

## � Aepplication Features

### Customer Experience
- **Modern E-commerce Interface**: Professional shopping website with responsive design
- **Product Catalog**: Browse products with filtering by category, price, and search
- **Product Details**: Detailed product pages with specifications and related items
- **Shopping Cart**: Add items, manage quantities, and proceed to checkout
- **Order Management**: View order history and track order status
- **Checkout Process**: Complete order placement with customer information

### Technical Implementation
- **Frontend**: Bootstrap 4.6, jQuery 3.5.1, Font Awesome icons
- **Backend**: Spring Boot 2.1.18 with JPA/Hibernate
- **Database**: H2 in-memory database with sample data
- **Architecture**: MVC pattern with REST APIs

## 🌐 Application URLs

- **Home Page**: http://localhost:8080/legacy-ecommerce/
- **Products**: http://localhost:8080/legacy-ecommerce/products
- **Product Details**: http://localhost:8080/legacy-ecommerce/product/{id}
- **Shopping Cart**: http://localhost:8080/legacy-ecommerce/cart
- **My Orders**: http://localhost:8080/legacy-ecommerce/orders
- **Database Console**: http://localhost:8080/legacy-ecommerce/h2-console

## 🚨 Technical Debt Overview

This application intentionally contains various forms of technical debt to simulate real-world legacy applications:

### Framework & Dependencies
- **Spring Boot 2.1.18** (EOL - should upgrade to 3.x)
- **Java 8** (should upgrade to Java 17+)
- **Old dependency versions:**
  - Jackson 2.9.10 (vulnerable)
  - Commons Lang 2.6 (deprecated)
  - Log4j 1.2.17 (deprecated, security issues)
  - H2 1.4.199 (old version)
  - JUnit 4.12 (should use JUnit 5)
  - Bootstrap 4.6 (should upgrade to 5.x)
  - jQuery 3.5.1 (should use modern frameworks)

### Security Issues
- Plain text password storage
- Disabled CSRF protection
- Deprecated `WebMvcConfigurerAdapter`
- Insecure authentication methods
- Exposed sensitive endpoints
- Weak password validation (4 characters minimum)
- Hardcoded credentials in configuration

### Performance Issues
- No pagination on list endpoints
- N+1 query problems with JPA relationships
- Inefficient database queries
- Loading all data without limits
- No caching mechanisms
- Synchronous processing only

### Code Quality Issues
- Deprecated API usage (`WebMvcConfigurerAdapter`)
- Circular reference handling with `@JsonIgnoreProperties`
- Poor error handling and validation
- Hardcoded values in configuration
- Missing input sanitization
- Inline CSS instead of external stylesheets

## 🏗️ Architecture

```
src/
├── main/
│   ├── java/com/example/ecommerce/
│   │   ├── LegacyEcommerceApplication.java
│   │   ├── config/
│   │   │   └── SecurityConfig.java
│   │   ├── controller/
│   │   │   ├── HomeController.java          # Frontend page routing
│   │   │   ├── ProductController.java       # Product REST API
│   │   │   ├── UserController.java          # User management API
│   │   │   └── OrderController.java         # Order management API
│   │   ├── model/
│   │   │   ├── Product.java                 # Product entity
│   │   │   ├── User.java                    # User entity with orders
│   │   │   ├── Order.java                   # Order entity with items
│   │   │   └── OrderItem.java               # Order line items
│   │   ├── repository/
│   │   │   ├── ProductRepository.java       # Product data access
│   │   │   ├── UserRepository.java          # User data access
│   │   │   └── OrderRepository.java         # Order data access
│   │   └── service/
│   │       ├── ProductService.java          # Product business logic
│   │       ├── UserService.java             # User business logic
│   │       └── OrderService.java            # Order business logic
│   └── resources/
│       ├── application.properties           # Application configuration
│       ├── data.sql                         # Sample data initialization
│       ├── log4j.properties                 # Legacy logging config
│       └── templates/                       # Thymeleaf templates
│           ├── index.html                   # Homepage
│           ├── products.html                # Product catalog
│           ├── product-detail.html          # Product details
│           ├── cart.html                    # Shopping cart & checkout
│           └── orders.html                  # Order history
```

## 🚀 Getting Started

### Prerequisites
- Java 8+ (tested with Java 17)
- Maven 3.6+

### Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Run the application:

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080/legacy-ecommerce`

### Database Access
- H2 Console: `http://localhost:8080/legacy-ecommerce/h2-console`
- JDBC URL: `jdbc:h2:mem:ecommerce`
- Username: `sa`
- Password: (empty)

## 📊 Sample Data

The application comes pre-loaded with sample data:

### Users
- `admin` / `admin123` (ADMIN)
- `john_doe` / `password123` (CUSTOMER)
- `jane_smith` / `pass456` (CUSTOMER)
- `manager1` / `manager123` (MANAGER)
- `customer1` / `123456` (CUSTOMER)

### Products (10 items across 4 categories)
- **Computers & Laptops**: Laptop Computer ($999.99), Monitor 24 inch ($199.99)
- **Mobile Devices**: Smartphone ($699.99), Tablet ($299.99)
- **Audio & Accessories**: Wireless Mouse ($29.99), Mechanical Keyboard ($149.99), Headphones ($199.99), Bluetooth Speaker ($79.99)
- **Wearables**: Fitness Tracker ($129.99)
- **General**: USB-C Hub ($49.99)

### Orders (4 sample orders with different statuses)
- Order #ORD-1001: DELIVERED ($1,199.98)
- Order #ORD-1002: SHIPPED ($249.98)
- Order #ORD-1003: PROCESSING ($79.99)
- Order #ORD-1004: PENDING ($329.98)

## 🔌 REST API Endpoints

### Products API
- `GET /api/products` - Get all products
- `GET /api/products/{id}` - Get product by ID
- `POST /api/products` - Create new product
- `PUT /api/products/{id}` - Update product
- `DELETE /api/products/{id}` - Delete product
- `GET /api/products/search?name={name}` - Search products by name
- `GET /api/products/category/{categoryId}` - Get products by category
- `GET /api/products/price-range?minPrice={min}&maxPrice={max}` - Filter by price
- `GET /api/products/available` - Get available products only
- `PATCH /api/products/{id}/stock?quantity={qty}` - Update stock

### Users API
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create new user
- `POST /api/users/login` - User login
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

### Orders API
- `GET /api/orders` - Get all orders
- `GET /api/orders/{id}` - Get order by ID
- `POST /api/orders` - Create new order
- `POST /api/orders/{id}/items` - Add item to order
- `PATCH /api/orders/{id}/status` - Update order status
- `GET /api/orders/user/{userId}` - Get orders by user

## 🧪 Testing the Application

### Frontend Testing
1. Visit http://localhost:8080/legacy-ecommerce/
2. Browse products and add items to cart
3. Proceed through checkout process
4. View order history

### API Testing

#### Get Products
```bash
curl http://localhost:8080/legacy-ecommerce/api/products
```

#### Search Products
```bash
curl "http://localhost:8080/legacy-ecommerce/api/products/search?name=laptop"
```

#### Get Orders
```bash
curl http://localhost:8080/legacy-ecommerce/api/orders
```

#### Create User
```bash
curl -X POST http://localhost:8080/legacy-ecommerce/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "test123",
    "firstName": "Test",
    "lastName": "User"
  }'
```

## 🔧 Upgrade Scenarios

This application is designed to test various upgrade scenarios:

### 1. Framework Upgrades
- Spring Boot 2.1.x → 3.x
- Java 8 → Java 17+
- Thymeleaf template engine updates

### 2. Dependency Upgrades
- Jackson 2.9.x → 2.15.x
- Commons Lang 2.6 → Commons Lang3 3.x
- Log4j 1.x → SLF4J + Logback
- Bootstrap 4.6 → Bootstrap 5.x
- jQuery → Modern JavaScript frameworks

### 3. Security Improvements
- Implement proper password hashing (BCrypt)
- Enable CSRF protection
- Add proper authentication/authorization
- Secure API endpoints
- Input validation and sanitization

### 4. Performance Optimizations
- Add pagination to product listings
- Fix N+1 queries in JPA relationships
- Implement caching (Redis/Hazelcast)
- Optimize database queries
- Add connection pooling

### 5. Code Quality Improvements
- Replace deprecated APIs (`WebMvcConfigurerAdapter`)
- Implement proper exception handling
- Add comprehensive validation
- Extract hardcoded values to configuration
- Implement proper logging
- Add unit and integration tests

### 6. Frontend Modernization
- Replace jQuery with modern frameworks (React/Vue/Angular)
- Implement proper state management
- Add TypeScript for type safety
- Optimize bundle size and loading
- Implement Progressive Web App features

## 🎯 Key Learning Points

This application demonstrates common legacy patterns:
- **Circular References**: Fixed with `@JsonIgnoreProperties`
- **Deprecated APIs**: `WebMvcConfigurerAdapter` usage
- **Security Vulnerabilities**: Plain text passwords, weak validation
- **Performance Issues**: No pagination, inefficient queries
- **Old Frontend Patterns**: jQuery, inline CSS, Bootstrap 4

## 📝 License

This project is created for educational and testing purposes. Use it to practice application upgrades and modernization techniques.

## ⚠️ Disclaimer

**DO NOT USE IN PRODUCTION!** This application contains intentional security vulnerabilities and performance issues for educational purposes only. It is designed specifically for testing upgrade tools and modernization processes.