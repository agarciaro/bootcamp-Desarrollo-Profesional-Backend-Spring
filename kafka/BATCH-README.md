# Spring Batch Service

## Description

The Spring Batch service is a microservice specialized in batch processing of inventory data. It provides robust capabilities for processing CSV files, validating data, updating databases, and generating automatic alerts.

## Main Features

### 🎯 Real-World Processing Scenario
- **Inventory Processing**: Processes CSV files with product data
- **Data Validation**: Validates format, ranges, and data consistency
- **Database Updates**: Inserts/updates inventory records
- **Alert Generation**: Detects products with low stock or out of stock
- **Notifications**: Sends alerts via Kafka to other services

### 🔄 Job Workflow
1. **Reading**: Reads inventory CSV file
2. **Validation**: Validates each record according to business rules
3. **Processing**: Transforms and enriches data
4. **Writing**: Updates database
5. **Alerts**: Generates alerts for critical products
6. **Notifications**: Sends notifications via Kafka

### 🛡️ Fault Tolerance
- **Retries**: Automatic retry configuration
- **Skip Policy**: Skips problematic records
- **Rollback**: Atomic transactions per chunk
- **Logging**: Detailed error logging

### 📊 Monitoring and Observability
- **Metrics**: Prometheus metrics
- **Health Checks**: Health endpoints
- **Logging**: Structured logs
- **Tracing**: Complete execution history

## Architecture

### Main Components

```
batch-service/
├── config/           # Spring Batch configurations
├── controller/       # REST controllers
├── dto/             # Data transfer objects
├── listener/        # Batch event listeners
├── model/           # JPA entities
├── processor/       # Data processors
├── repository/      # Data repositories
├── service/         # Business logic
└── tasklet/         # Tasklets for specific steps
```

### Data Flow

```
CSV Input → Reader → Processor → Writer → Database
                                    ↓
                              Alert Tasklet → Alerts
                                    ↓
                              Notification Tasklet → Kafka
```

## Configuration

### Main Properties

```yaml
batch:
  job:
    inventory:
      name: inventoryProcessingJob
      cron: "0 0 2 * * ?"  # Daily at 2 AM
      chunk-size: 100
      retry-limit: 3
      skip-limit: 10
      input-file: classpath:data/inventory-input.csv
      output-file: classpath:data/inventory-output.csv
      error-file: classpath:data/inventory-errors.csv
```

### Database

- **H2**: In-memory database for development
- **JPA**: Object-relational mapping
- **Transactions**: Complete transaction support

### Kafka

- **Producer**: Sends inventory notifications
- **Topics**: `inventory-notifications`
- **Message Types**: `summary`, `low-stock`, `out-of-stock`

## REST API

### Batch Job Endpoints

#### Execute Job
```http
POST /api/batch/jobs/execute
Content-Type: application/json

{
  "jobName": "inventoryProcessingJob",
  "parameters": {
    "inputFile": "data/inventory.csv",
    "threshold": 10
  }
}
```

#### Get Execution Status
```http
GET /api/batch/jobs/{executionId}
```

#### Restart Failed Job
```http
POST /api/batch/jobs/{executionId}/restart
```

#### Stop Running Job
```http
POST /api/batch/jobs/{executionId}/stop
```

#### Job History
```http
GET /api/batch/jobs/{jobName}/history
GET /api/batch/jobs/{jobName}/history/recent?days=7
```

#### Job Statistics
```http
GET /api/batch/jobs/{jobName}/statistics
```

### Inventory Endpoints

#### Product Management
```http
GET    /api/inventory/items                    # List all
GET    /api/inventory/items/{productCode}      # Get by code
POST   /api/inventory/items                    # Create product
PUT    /api/inventory/items/{productCode}      # Update product
DELETE /api/inventory/items/{productCode}      # Delete product
```

#### Specialized Queries
```http
GET /api/inventory/items/category/{category}   # By category
GET /api/inventory/items/status/{status}       # By status
GET /api/inventory/items/low-stock             # Low stock
GET /api/inventory/items/out-of-stock          # Out of stock
GET /api/inventory/items/expensive             # Expensive products
```

#### Stock Management
```http
PUT /api/inventory/items/{productCode}/stock?quantity=50
PUT /api/inventory/items/{productCode}/adjust?adjustment=+10
```

#### Analytics
```http
GET /api/inventory/summary                     # General summary
GET /api/inventory/analytics/total-value       # Total value
GET /api/inventory/analytics/category/{category}/value
GET /api/inventory/analytics/suppliers         # Supplier statistics
GET /api/inventory/analytics/categories/prices # Prices by category
```

## Automatic Scheduling

### Scheduled Jobs

- **Daily Inventory**: `0 0 2 * * ?` (2 AM daily)
- **Health Check**: Every 5 minutes
- **Cleanup**: `0 0 3 * * ?` (3 AM daily)

### Quartz Configuration

```yaml
spring:
  quartz:
    job-store-type: memory
    properties:
      org.quartz.scheduler.instanceName: BatchScheduler
      org.quartz.threadPool.threadCount: 5
```

## Security

### Authentication
- **Spring Security**: Basic configuration
- **JWT**: JWT token support
- **Stateless**: Stateless sessions

### Authorization
- **Health Checks**: Public access
- **APIs**: Require authentication
- **H2 Console**: Public access in development

## Monitoring

### Health Checks
```http
GET /actuator/health
GET /api/batch/jobs/health
GET /api/inventory/health
```

### Prometheus Metrics
```http
GET /actuator/prometheus
```

### Logs
- **Level**: DEBUG for development
- **Format**: Structured
- **Categories**: Batch, Security, Business

## Testing

### Unit Tests
```bash
mvn test
```

### Integration Tests
```bash
mvn test -Dtest=*IntegrationTest
```

### Batch Tests
```bash
mvn test -Dtest=*BatchTest
```

## Deployment

### Docker
```dockerfile
FROM openjdk:21-jdk-slim
COPY target/batch-service-*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Environment Variables
```bash
SPRING_PROFILES_ACTIVE=prod
SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/batchdb
KAFKA_BOOTSTRAP_SERVERS=kafka:9092
```

### Kubernetes
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: batch-service
spec:
  replicas: 2
  selector:
    matchLabels:
      app: batch-service
  template:
    metadata:
      labels:
        app: batch-service
    spec:
      containers:
      - name: batch-service
        image: batch-service:latest
        ports:
        - containerPort: 8086
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "prod"
```

## Scalability

### Partitioning
- **Chunk Processing**: Batch processing
- **Parallel Steps**: Parallel steps
- **Remote Chunking**: Load distribution

### Chunk Configuration
```yaml
batch:
  job:
    inventory:
      chunk-size: 100        # Chunk size
      retry-limit: 3         # Retry limit
      skip-limit: 10         # Skip limit
```

## Troubleshooting

### Common Issues

1. **Job doesn't start**
   - Check cron configuration
   - Review application logs
   - Verify database connectivity

2. **Validation errors**
   - Check CSV file format
   - Verify validation rules
   - Check error logs

3. **Performance issues**
   - Adjust chunk size
   - Optimize database queries
   - Review JVM configuration

### Important Logs
```bash
# Job execution logs
grep "Starting job" logs/application.log

# Error logs
grep "ERROR" logs/application.log

# Batch logs
grep "Batch" logs/application.log
```

## Contributing

### Code Structure
- **Clean Code**: Clean code principles
- **SOLID**: SOLID principles
- **Testing**: Test coverage > 80%

### Conventions
- **Naming**: camelCase for methods and variables
- **Packages**: Structure by functionality
- **Documentation**: JavaDoc for public APIs

## License

This project is under the MIT license. See the LICENSE file for more details. 