# Microservices Demo - Development Guide

## 🚀 Quick Start Options

### Option 1: Local Development (Recommended for Development)
```bash
# Start Kafka and Zookeeper only
docker-compose up -d zookeeper kafka kafka-ui

# Start all microservices locally
start-local-dev.bat

# Or test just the notification service
test-notification-service.bat
```

### Option 2: Full Docker Deployment
```bash
# Build and start all services in Docker
start-all-services.bat
```

## 📋 Prerequisites

- Java 21
- Maven 3.8+
- Docker Desktop
- Windows (for .bat scripts)

## 🔧 Configuration

### Kafka Configuration
The services use environment variables for Kafka configuration:

- **Local Development**: `localhost:9092` (default)
- **Docker**: `kafka:9092` (set via environment variable)

### Service URLs
- **Eureka Dashboard**: http://localhost:8761
- **API Gateway**: http://localhost:8080
- **User Service**: http://localhost:8081
- **Order Service**: http://localhost:8082
- **Notification Service**: http://localhost:8083
- **Kafka UI**: http://localhost:80

## 🏗️ Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   API Gateway   │    │  Discovery      │    │   Config        │
│   (Port 8080)   │    │  Service        │    │   Service       │
│                 │    │  (Port 8761)    │    │   (Port 8888)   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         └───────────────────────┼───────────────────────┘
                                 │
         ┌───────────────────────┼───────────────────────┐
         │                       │                       │
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   User Service  │    │  Order Service  │    │ Notification    │
│   (Port 8081)   │    │  (Port 8082)    │    │ Service         │
│                 │    │                 │    │ (Port 8083)     │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         └───────────────────────┼───────────────────────┘
                                 │
                    ┌─────────────────┐
                    │      Kafka      │
                    │   (Port 9092)   │
                    └─────────────────┘
```

## 🔄 Event Flow

1. **User Service** publishes events to Kafka topic `user-events`
2. **Notification Service** consumes events and sends notifications
3. **Order Service** can also publish order-related events

## 🛠️ Development Workflow

### 1. Start Infrastructure
```bash
docker-compose up -d zookeeper kafka kafka-ui
```

### 2. Start Services Individually
```bash
# Discovery Service
mvn spring-boot:run -pl discovery-service

# User Service
mvn spring-boot:run -pl user-service

# Order Service
mvn spring-boot:run -pl order-service

# Notification Service (with local profile)
mvn spring-boot:run -pl notification-service -Dspring-boot.run.profiles=local

# API Gateway
mvn spring-boot:run -pl api-gateway
```

### 3. Test the System
1. Create a user via API Gateway: `POST http://localhost:8080/api/users`
2. Check Kafka UI: http://localhost:80
3. Verify notification was sent (check logs)

## 🐛 Troubleshooting

### Kafka Connection Issues
- Ensure Kafka is running: `docker ps | grep kafka`
- Check Kafka logs: `docker logs kafka`
- Verify bootstrap servers configuration

### Service Discovery Issues
- Check Eureka dashboard: http://localhost:8761
- Ensure all services are registered
- Check service logs for connection errors

### Port Conflicts
- Ensure ports 8080-8083, 8761, 8888, 9092 are available
- Stop any conflicting services

## 📝 Environment Variables

| Variable | Local Default | Docker Value | Description |
|----------|---------------|--------------|-------------|
| `KAFKA_BOOTSTRAP_SERVERS` | `localhost:9092` | `kafka:9092` | Kafka broker address |
| `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE` | `http://localhost:8761/eureka/` | `http://discovery-service:8761/eureka/` | Eureka server URL |

## 🔍 Monitoring

- **Kafka UI**: http://localhost:80 - Monitor topics, messages, and consumers
- **Eureka Dashboard**: http://localhost:8761 - Service discovery status
- **Actuator Endpoints**: Each service exposes health, metrics, and info endpoints

## 🧪 Testing

### Manual Testing
```bash
# Create a user
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "firstName": "Test",
    "lastName": "User"
  }'

# Get all users
curl http://localhost:8080/api/users
```

### Automated Testing
```bash
# Run all tests
mvn test

# Run specific service tests
mvn test -pl user-service
``` 