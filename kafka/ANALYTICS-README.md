# Analytics Service

A real-time analytics microservice built with Spring Boot and Kafka Streams that processes order and user events to provide real-time insights, metrics, and alerts.

## 🚀 Features

### Real-time Analytics
- **Order Analytics**: Process order events in real-time
- **User Activity Analytics**: Track user behavior and engagement
- **Revenue Analytics**: Calculate real-time revenue metrics
- **Cross-stream Analytics**: Combine order and user data for insights

### Kafka Streams Processing
- **Event Stream Processing**: Real-time processing of Kafka events
- **Windowed Aggregations**: Time-based aggregations (1min, 5min, 10min, 1hour)
- **Stateful Processing**: Maintain state across event streams
- **Exactly-once Processing**: Guaranteed message processing

### Real-time Alerts
- **High-value Order Alerts**: Detect orders above $1000
- **Volume Spike Detection**: Alert on unusual order volumes
- **User Activity Spikes**: Monitor user engagement patterns
- **System Health Monitoring**: Track system performance metrics

### REST API Endpoints
- **Dashboard Metrics**: Real-time KPI dashboard
- **Analytics Data**: Historical and real-time analytics
- **Streaming Endpoints**: Server-sent events for real-time updates
- **Health Monitoring**: System status and Kafka Streams health

## 🏗️ Architecture

### Kafka Topics
- **Input Topics**:
  - `order-events`: Order creation, updates, and deletions
  - `user-events`: User registration, updates, and activity

- **Output Topics**:
  - `analytics-events`: General analytics events
  - `revenue-analytics`: Revenue-related analytics
  - `user-activity-analytics`: User activity metrics
  - `analytics-alerts`: Real-time alerts and notifications

### Stream Processing Topology
```
order-events → Order Processing → analytics-events
                ↓
              Revenue Analytics → revenue-analytics
                ↓
              Volume Alerts → analytics-alerts

user-events → User Activity Processing → user-activity-analytics
                ↓
              Activity Alerts → analytics-alerts

Cross-stream → User Revenue Analysis → revenue-analytics
```

## 📊 Analytics Capabilities

### Order Analytics
- **Real-time Order Counts**: By status, time windows
- **Revenue Tracking**: Total revenue, average order value
- **Order Volume Monitoring**: Spike detection and alerts
- **High-value Order Detection**: Orders above threshold

### User Analytics
- **User Activity Tracking**: Activity patterns and engagement
- **New User Registration**: Registration rate monitoring
- **User Revenue Analysis**: Revenue per user, top performers
- **Activity Spike Detection**: Unusual user activity patterns

### Revenue Analytics
- **Real-time Revenue**: Current period revenue calculations
- **Revenue Trends**: Hourly, daily, weekly, monthly trends
- **User Revenue Analysis**: Revenue per user over time
- **Revenue Alerts**: Low revenue detection

### System Health
- **Performance Metrics**: Processing latency, throughput
- **Error Rate Monitoring**: System error detection
- **Resource Utilization**: CPU, memory, disk usage
- **Kafka Streams Health**: Stream processing status

## 🛠️ Technology Stack

- **Spring Boot 3.2.0**: Application framework
- **Spring WebFlux**: Reactive web framework
- **Kafka Streams 3.6.0**: Stream processing engine
- **Spring Kafka**: Kafka integration
- **R2DBC**: Reactive database connectivity
- **H2 Database**: In-memory database for development
- **Spring Cloud Netflix Eureka**: Service discovery

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven 3.6+
- Kafka cluster running
- Eureka Discovery Service

### Configuration
```yaml
server:
  port: 8084

spring:
  application:
    name: analytics-service
  kafka:
    bootstrap-servers: localhost:9092
    streams:
      application-id: analytics-streams-app
      properties:
        processing.guarantee: exactly_once_v2
        replication.factor: 1
        num.stream.threads: 2
```

### Running the Service
```bash
# Build the project
mvn clean package

# Run the service
java -jar target/analytics-service-1.0.0.jar

# Or with Maven
mvn spring-boot:run
```

## 📡 API Endpoints

### Dashboard Metrics
```bash
GET /analytics/dashboard
```
Returns real-time dashboard metrics including:
- Total orders today
- Total revenue today
- Active users today
- Average order value

### Order Analytics
```bash
GET /analytics/orders/metrics/{metricType}
```
Returns order metrics by type (e.g., ORDER_CREATED, ORDER_UPDATED)

### User Activity Analytics
```bash
GET /analytics/users/{userId}/activity
```
Returns user activity metrics for a specific user

### Revenue Analytics
```bash
GET /analytics/revenue/{timePeriod}
```
Returns revenue analytics for a time period (hourly, daily, weekly, monthly)

### Real-time Stream
```bash
GET /analytics/stream
```
Server-sent events stream for real-time analytics updates

### Alerts
```bash
GET /analytics/alerts
```
Returns active alerts and notifications

### System Health
```bash
GET /analytics/health
GET /analytics/streams/status
```
Returns service health and Kafka Streams status

## 🔧 Development

### Project Structure
```
analytics-service/
├── src/main/java/com/bootcamp/analytics/
│   ├── AnalyticsServiceApplication.java
│   ├── config/
│   │   ├── KafkaStreamsConfig.java
│   │   └── StreamsInitializer.java
│   ├── controller/
│   │   └── AnalyticsController.java
│   ├── consumer/
│   │   └── AnalyticsEventConsumer.java
│   ├── model/
│   │   ├── OrderEvent.java
│   │   ├── UserEvent.java
│   │   ├── OrderMetrics.java
│   │   ├── UserActivityMetrics.java
│   │   └── RevenueAnalytics.java
│   ├── repository/
│   │   └── AnalyticsRepository.java
│   ├── service/
│   │   └── AnalyticsService.java
│   └── streams/
│       └── AnalyticsStreamProcessor.java
├── src/main/resources/
│   ├── application.yml
│   └── schema.sql
└── pom.xml
```

### Adding New Analytics
1. **Define Event Model**: Create new event model in `model/` package
2. **Add Stream Processing**: Implement processing logic in `AnalyticsStreamProcessor`
3. **Create Repository**: Add database operations in `AnalyticsRepository`
4. **Add Service Logic**: Implement business logic in `AnalyticsService`
5. **Expose API**: Add REST endpoints in `AnalyticsController`

### Testing
```bash
# Run unit tests
mvn test

# Run integration tests
mvn verify

# Test with sample data
curl -X GET http://localhost:8084/analytics/dashboard
```

## 📈 Monitoring and Observability

### Metrics
- **Application Metrics**: Available via Spring Boot Actuator
- **Kafka Streams Metrics**: Stream processing performance
- **Custom Business Metrics**: Order volume, revenue, user activity

### Logging
- **Structured Logging**: JSON format for better parsing
- **Log Levels**: DEBUG, INFO, WARN, ERROR
- **Kafka Streams Logging**: Stream processing events

### Health Checks
- **Application Health**: Service status
- **Kafka Streams Health**: Stream processing status
- **Database Health**: Connection status

## 🔒 Security

### Authentication
- Currently configured for development (no authentication)
- Can be integrated with Spring Security for production

### Data Privacy
- Analytics data is anonymized where possible
- User-specific data is protected
- Compliance with data protection regulations

## 🚀 Deployment

### Docker
```dockerfile
FROM openjdk:17-jre-slim
COPY target/analytics-service-1.0.0.jar app.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Kubernetes
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: analytics-service
spec:
  replicas: 2
  selector:
    matchLabels:
      app: analytics-service
  template:
    metadata:
      labels:
        app: analytics-service
    spec:
      containers:
      - name: analytics-service
        image: analytics-service:latest
        ports:
        - containerPort: 8084
```



**Analytics Service** - Real-time analytics powered by Kafka Streams 