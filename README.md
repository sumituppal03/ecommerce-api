# E-commerce REST API

A full-featured E-commerce REST API built with Spring Boot featuring
product management, order processing, pagination, sorting, and
professional exception handling.

## Tech Stack
- Java 21
- Spring Boot 3.2.5
- Spring Data JPA
- MySQL Database
- Lombok
- Bean Validation
- Maven

## Features
- Complete product CRUD with validation
- Order management with stock tracking
- Pagination and sorting on all lists
- Global exception handling with proper error responses
- DTOs for clean request/response separation
- Stock validation on order placement
- Order status management (PENDING → CONFIRMED → SHIPPED → DELIVERED)
- Search and filter products
- Automatic timestamp tracking

## Database Design
products table       orders table         order_items table
──────────────       ────────────         ─────────────────
id (PK)              id (PK)              id (PK)
name                 customerName         order_id (FK)
description          customerEmail        product_id (FK)
price                totalAmount          quantity
stock                status               price
category             created_at
created_at

## API Endpoints

### Products
| Method | URL | Description |
|--------|-----|-------------|
| GET | /api/products | Get all (paginated) |
| GET | /api/products/{id} | Get by ID |
| POST | /api/products | Create product |
| PUT | /api/products/{id} | Update product |
| DELETE | /api/products/{id} | Delete product |
| GET | /api/products/search?keyword= | Search |
| GET | /api/products/category/{cat} | Filter by category |

### Orders
| Method | URL | Description |
|--------|-----|-------------|
| POST | /api/orders | Place order |
| GET | /api/orders/{id} | Get order |
| GET | /api/orders/customer/{email} | Get by customer |
| PUT | /api/orders/{id}/status | Update status |

## Sample Requests

**Create Product:**
```json
POST /api/products
{
    "name": "iPhone 15",
    "description": "Latest Apple smartphone",
    "price": 79999.99,
    "stock": 50,
    "category": "Electronics"
}
```

**Get Products (Paginated + Sorted):**
GET /api/products?page=0&size=10&sortBy=price

**Place Order:**
```json
POST /api/orders
{
    "customerName": "Ravi Kumar",
    "customerEmail": "ravi@gmail.com",
    "items": [
        {"productId": 1, "quantity": 2}
    ]
}
```

**Error Response Format:**
```json
{
    "timestamp": "2026-04-07T14:30:00",
    "status": 404,
    "error": "Not Found",
    "message": "Product not found with id: '5'"
}
```

## How to Run

1. Create MySQL database
```sql
CREATE DATABASE ecommercedb;
```

2. Update `application.properties`
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommercedb
spring.datasource.username=root
spring.datasource.password=yourpassword
```

3. Run the application
```bash
mvn spring-boot:run
```

## What I Learned
- Global exception handling with @RestControllerAdvice
- Custom exceptions (ResourceNotFoundException, BadRequestException)
- Pagination and sorting with Spring Data
- DTOs for request/response separation
- Bean Validation (@NotBlank, @Min, @DecimalMin, @NotNull)
- @Transactional for atomic operations
- @PrePersist for auto-timestamps
- Business logic in service layer
