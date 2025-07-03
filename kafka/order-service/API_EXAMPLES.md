# Usage Examples - CQRS Order Service API

## DTO Structure

The Order Service uses Data Transfer Objects (DTOs) for API communication:

### OrderRequest DTO
Located in `com.bootcamp.order.dto.OrderRequest`
- Used for order creation requests
- Includes validation annotations for data integrity
- Contains: userId, items, shippingAddress, notes

### StatusRequest DTO  
Located in `com.bootcamp.order.dto.StatusRequest`
- Used for order status update requests
- Includes validation for allowed status values
- Contains: status

## Command Endpoints

### 1. Create Order
```bash
curl -X POST http://localhost:8082/orders \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "items": [
      {
        "productId": 101,
        "productName": "Dell XPS 13 Laptop",
        "quantity": 1,
        "unitPrice": 1299.99,
        "totalPrice": 1299.99
      },
      {
        "productId": 102,
        "productName": "Wireless Mouse",
        "quantity": 2,
        "unitPrice": 29.99,
        "totalPrice": 59.98
      }
    ],
    "shippingAddress": "Main Street 123, City, Country",
    "notes": "Deliver during office hours"
  }'
```

**Validation Rules:**
- `userId`: Required, must not be null
- `items`: Required, must not be empty
- `shippingAddress`: Optional
- `notes`: Optional

### 2. Update Order Status
```bash
curl -X PUT http://localhost:8082/orders/1/status \
  -H "Content-Type: application/json" \
  -d '{
    "status": "CONFIRMED"
  }'
```

**Validation Rules:**
- `status`: Required, must be one of: PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED

### 3. Delete Order
```bash
curl -X DELETE http://localhost:8082/orders/1
```

## Query Endpoints

### 1. Get All Active Orders
```bash
curl -X GET http://localhost:8082/orders
```

**Response:**
```json
[
  {
    "id": 1,
    "userId": 1,
    "totalAmount": 1359.97,
    "status": "PENDING",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00",
    "shippingAddress": "Main Street 123, City, Country",
    "notes": "Deliver during office hours",
    "isDeleted": false,
    "userUsername": "john_doe",
    "userEmail": "john@example.com",
    "itemsCount": 2
  }
]
```

### 2. Get Order by ID
```bash
curl -X GET http://localhost:8082/orders/1
```

### 3. Get Orders by User
```bash
curl -X GET http://localhost:8082/orders/user/1
```

### 4. Get Orders by Status
```bash
curl -X GET http://localhost:8082/orders/status/PENDING
```

### 5. Get Orders by Date Range
```bash
curl -X GET "http://localhost:8082/orders/date-range?startDate=2024-01-01T00:00:00&endDate=2024-01-31T23:59:59"
```

### 6. Get Orders by User and Status
```bash
curl -X GET http://localhost:8082/orders/user/1/status/PENDING
```

### 7. Get Order Statistics
```bash
curl -X GET http://localhost:8082/orders/statistics
```

**Response:**
```json
{
  "pendingCount": 5,
  "confirmedCount": 12,
  "shippedCount": 8,
  "deliveredCount": 25,
  "cancelledCount": 2,
  "totalCount": 52
}
```

### 8. Search Orders by Username
```bash
curl -X GET "http://localhost:8082/orders/search/username?username=john"
```

### 9. Get Orders with Amount Greater Than a Value
```bash
curl -X GET "http://localhost:8082/orders/amount-greater-than?minAmount=1000.00"
```

## Use Case Examples

### Case 1: Admin Dashboard
```bash
# Get general statistics
curl -X GET http://localhost:8082/orders/statistics

# Get pending orders
curl -X GET http://localhost:8082/orders/status/PENDING

# Get high-value orders
curl -X GET "http://localhost:8082/orders/amount-greater-than?minAmount=5000.00"
```

### Case 2: Customer Panel
```bash
# Get all customer orders
curl -X GET http://localhost:8082/orders/user/1

# Get customer orders in process
curl -X GET http://localhost:8082/orders/user/1/status/CONFIRMED

# Get customer delivered orders
curl -X GET http://localhost:8082/orders/user/1/status/DELIVERED
```

### Case 3: Sales Reports
```bash
# Get current month orders
curl -X GET "http://localhost:8082/orders/date-range?startDate=2024-01-01T00:00:00&endDate=2024-01-31T23:59:59"

# Get high-value orders for the month
curl -X GET "http://localhost:8082/orders/amount-greater-than?minAmount=1000.00"
```

## CQRS Benefits in these Examples

### 1. **Optimized Queries**
- The `/orders/statistics` endpoint calculates statistics in a single query
- `/orders/user/{userId}/status/{status}` combines filters without complex joins
- `/orders/amount-greater-than` uses specific indexes for ranges

### 2. **Enriched Data**
- The Read Model includes denormalized `userUsername` and `userEmail`
- `itemsCount` avoids counting items in each query
- `isDeleted` allows efficient filtering of deleted elements

### 3. **Responsibility Separation**
- Commands (POST, PUT, DELETE) only modify state
- Queries (GET) only read optimized data
- No conflicts between read and write operations

### 4. **Eventual Consistency**
- Queries may show slightly outdated data
- System continues working despite synchronization delays
- Events ensure data eventually synchronizes

## Monitoring and Debugging

### Verify Synchronization Status
```bash
# Verify that an order exists in the Write Model
curl -X GET http://localhost:8082/orders/1

# Verify that the order exists in the Read Model
curl -X GET http://localhost:8082/orders/1
```

### Important Logs
- **CommandService**: Command execution logs
- **QueryService**: Query execution logs
- **EventProcessor**: Event processing logs
- **Kafka**: Event publication and consumption logs

### Metrics to Monitor
- Latency between Write Model and Read Model
- Event processing time
- Number of events in queue
- Event processing error rate 