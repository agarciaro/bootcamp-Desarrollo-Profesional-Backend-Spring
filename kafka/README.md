# Spring Boot Microservices Demo

A comprehensive microservices demonstration project built with Spring Boot 3.5.0 and Maven, designed for educational purposes in a bootcamp environment.

## 🏗️ Architecture Overview

This project demonstrates a complete **reactive microservices architecture** with the following components:

### Core Services
- **Discovery Service** (Port: 8761) - Eureka Server for service registration and discovery
- **Config Service** (Port: 8888) - Spring Cloud Config Server for centralized configuration
- **API Gateway** (Port: 8080) - Spring Cloud Gateway for routing and cross-cutting concerns

### Business Services
- **User Service** (Port: 8081) - User management with CRUD operations
- **Order Service** (Port: 8082) - Order management with service-to-service communication
- **Notification Service** (Port: 8083) - Event-driven notifications

### Communication Patterns
- **Reactive REST APIs** - Non-blocking synchronous communication between services using Spring WebFlux
- **Kafka** - Asynchronous event-driven communication
- **Service Discovery** - Dynamic service location via Eureka
- **Reactive Programming** - Using Project Reactor (Mono/Flux) for non-blocking operations

## 🚀 Prerequisites

Before running this project, ensure you have the following installed:

- **Java 21** or higher
- **Maven 3.6** or higher
- **Apache Kafka** (for event-driven communication)
- **Git** (for version control)

## 📋 Setup Instructions

### 1. Clone the Repository
```bash
git clone <repository-url>
cd kafka
```

### 2. Start Apache Kafka
Make sure Kafka is running on `localhost:9092`. You can use Docker:

```bash
# Using Docker Compose (create docker-compose.yml if needed)
docker-compose up -d

# Or start Kafka manually
# Download Kafka and start Zookeeper and Kafka servers
```

### 3. Build the Project
```bash
mvn clean install
```

### 4. Start Services in Order
Start the services in the following order to ensure proper initialization:

```bash
# 1. Start Discovery Service
cd discovery-service
mvn spring-boot:run

# 2. Start Config Service (in a new terminal)
cd ../config-service
mvn spring-boot:run

# 3. Start API Gateway (in a new terminal)
cd ../api-gateway
mvn spring-boot:run

# 4. Start User Service (in a new terminal)
cd ../user-service
mvn spring-boot:run

# 5. Start Order Service (in a new terminal)
cd ../order-service
mvn spring-boot:run

# 6. Start Notification Service (in a new terminal)
cd ../notification-service
mvn spring-boot:run
```

## 🌐 Service Endpoints

### Discovery Service
- **Eureka Dashboard**: http://localhost:8761
- **Health Check**: http://localhost:8761/actuator/health

### Config Service
- **Health Check**: http://localhost:8888/actuator/health

### API Gateway
- **Health Check**: http://localhost:8080/actuator/health
- **Gateway Routes**: http://localhost:8080/actuator/gateway/routes

### User Service
- **Base URL**: http://localhost:8081/users
- **Create User**: POST http://localhost:8081/users
- **Get All Users**: GET http://localhost:8081/users
- **Get User by ID**: GET http://localhost:8081/users/{id}
- **Get User by Username**: GET http://localhost:8081/users/username/{username}
- **Update User**: PUT http://localhost:8081/users/{id}
- **Delete User**: DELETE http://localhost:8081/users/{id}
- **Health Check**: http://localhost:8081/users/health
- **H2 Console**: http://localhost:8081/h2-console

### Order Service
- **Base URL**: http://localhost:8082/orders
- **Health Check**: http://localhost:8082/actuator/health
- **H2 Console**: http://localhost:8082/h2-console

### Notification Service
- **Health Check**: http://localhost:8083/actuator/health
- **H2 Console**: http://localhost:8083/h2-console

## 📝 API Examples

### Create a User
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "firstName": "John",
    "lastName": "Doe"
  }'
```

### Get All Users
```bash
curl -X GET http://localhost:8080/api/users
```

### Get User by ID
```bash
curl -X GET http://localhost:8080/api/users/1
```

### Update User
```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john.updated@example.com",
    "firstName": "John",
    "lastName": "Smith"
  }'
```

### Delete User
```bash
curl -X DELETE http://localhost:8080/api/users/1
```

## 🔄 Event-Driven Communication

### Kafka Topics
- **user-events** - User-related events (created, updated, deleted)

### Event Flow
1. **User Service** publishes events to Kafka when users are created, updated, or deleted
2. **Notification Service** consumes these events and sends appropriate notifications reactively
3. **Order Service** can consume user events for order validation

### Example Event Flow
1. Create a user via User Service (reactive endpoint)
2. User Service publishes `USER_CREATED` event to Kafka
3. Notification Service consumes the event and sends welcome notification reactively
4. Check logs to see the event flow

## ⚡ Reactive Programming Features

### Project Reactor Integration
- **Mono<T>** - For single-value reactive streams
- **Flux<T>** - For multi-value reactive streams
- **Schedulers** - For thread pool management
- **Error Handling** - Using onErrorResume and onErrorReturn

### Reactive Patterns Used
- **Non-blocking I/O** - All database and HTTP operations are non-blocking
- **Backpressure handling** - Automatic flow control for data streams
- **Reactive error handling** - Graceful error recovery with reactive operators
- **Thread pool optimization** - Using boundedElastic scheduler for blocking operations

### Benefits of Reactive Architecture
- **Better resource utilization** - Fewer threads for more concurrent requests
- **Improved scalability** - Handle more requests with fewer resources
- **Better responsiveness** - Non-blocking operations prevent thread starvation
- **Modern programming model** - Functional and declarative programming style

## 🛠️ Development Features

### Database
- **H2 In-Memory Database** for each service
- **H2 Console** accessible at `/h2-console` for each service
- **JPA/Hibernate** for data persistence

### Monitoring
- **Spring Boot Actuator** for health checks and metrics
- **Eureka Dashboard** for service discovery monitoring
- **Comprehensive logging** with SLF4J

### Configuration
- **Centralized Configuration** via Config Service
- **Environment-specific** configuration support
- **Externalized** configuration properties

## 🧪 Testing

### Unit Tests
```bash
# Run all tests
mvn test

# Run tests for specific service
cd user-service
mvn test
```

### Integration Tests
```bash
# Test the complete flow
# 1. Start all services
# 2. Create a user
# 3. Verify notification is sent
# 4. Check logs for event flow
```

## 📊 Monitoring and Debugging

### Health Checks
All services expose health endpoints:
- `/actuator/health` - Service health status
- `/actuator/info` - Service information
- `/actuator/metrics` - Service metrics

### Logs
Check application logs for:
- Service startup information
- API request/response details
- Kafka event publishing/consumption
- Error messages and stack traces

### Eureka Dashboard
Visit http://localhost:8761 to see:
- Registered services
- Service instances
- Service health status

## 🔧 Configuration

### Environment Variables
You can override configuration using environment variables:
```bash
export SPRING_PROFILES_ACTIVE=dev
export KAFKA_BOOTSTRAP_SERVERS=localhost:9092
export EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://localhost:8761/eureka/
```

### Application Properties
Each service has its own `application.yml` file with:
- Database configuration
- Kafka configuration
- Eureka client configuration
- Logging configuration

## 🚨 Troubleshooting

### Common Issues

1. **Services not starting**
   - Check if Kafka is running on localhost:9092
   - Ensure ports are not already in use
   - Check Java version (requires Java 21+)

2. **Service discovery issues**
   - Ensure Discovery Service is running first
   - Check Eureka dashboard for service registration
   - Verify network connectivity

3. **Kafka connection issues**
   - Verify Kafka is running and accessible
   - Check Kafka topic creation
   - Review Kafka configuration in application.yml

4. **Database connection issues**
   - H2 databases are in-memory, no external setup required
   - Check H2 console for data verification

### Debug Mode
Enable debug logging by adding to `application.yml`:
```yaml
logging:
  level:
    com.bootcamp: DEBUG
    org.springframework.kafka: DEBUG
```

## 📚 Learning Objectives

This project demonstrates:

1. **Reactive Microservices Architecture**
   - Service decomposition with reactive programming
   - Non-blocking service operations
   - Distributed system design with Project Reactor

2. **Spring Boot 3.5.0 Features**
   - Latest Spring Boot capabilities
   - Java 21 features
   - Modern Spring Cloud components
   - Spring WebFlux for reactive web applications

3. **Reactive Service Communication**
   - Non-blocking REST APIs using Spring WebFlux
   - Kafka for asynchronous event-driven communication
   - Service discovery and load balancing
   - Project Reactor (Mono/Flux) patterns

4. **Reactive Data Management**
   - JPA/Hibernate with reactive wrappers
   - Database per service pattern
   - Non-blocking data access patterns
   - Data consistency in distributed systems

5. **Cross-cutting Concerns**
   - API Gateway for routing and filtering
   - Centralized configuration management
   - Service monitoring and health checks
   - Reactive error handling

6. **Event-Driven Architecture**
   - Event sourcing patterns
   - Event-driven communication
   - Loose coupling between services
   - Reactive event processing

7. **Reactive Programming Patterns**
   - Mono and Flux usage
   - Backpressure handling
   - Schedulers for thread management
   - Error handling with onErrorResume

## 🤝 Contributing

This is a demonstration project for educational purposes. Feel free to:
- Fork the repository
- Create feature branches
- Submit pull requests
- Report issues

## 📄 License

This project is created for educational purposes in a bootcamp environment.

## 👨‍🏫 Instructor Notes

### For Bootcamp Students
This project serves as a comprehensive example of modern **reactive microservices development** with Spring Boot. Key learning points:

1. **Start with the basics** - Understand each service individually and reactive patterns
2. **Explore reactive communication patterns** - WebFlux vs Kafka
3. **Practice with reactive APIs** - Use the provided examples with Mono/Flux
4. **Monitor the system** - Use Eureka dashboard and logs
5. **Experiment with reactive changes** - Modify configurations and observe effects
6. **Learn reactive programming** - Understand Mono, Flux, and Project Reactor patterns

### For Instructors
- Use this as a reference implementation for reactive microservices
- Demonstrate each component step by step with reactive patterns
- Encourage students to explore and modify the code
- Use the event flow as a practical example of event-driven architecture
- Teach reactive programming concepts with real-world examples

# Microservices Demo - Configuración Local

Este proyecto demuestra una arquitectura de microservicios usando Spring Boot, Spring Cloud, Kafka y Eureka.

## Arquitectura

- **Discovery Service** (Puerto 8761): Servicio de descubrimiento usando Eureka
- **API Gateway** (Puerto 8080): Gateway para enrutar requests a los microservicios
- **User Service** (Puerto 8081): Gestión de usuarios con base de datos H2
- **Order Service** (Puerto 8082): Gestión de órdenes con comunicación a User Service
- **Notification Service** (Puerto 8083): Servicio de notificaciones que consume eventos de Kafka

## Configuración Local

Cada microservicio tiene su propia configuración local en `src/main/resources/application.yml`:

### Discovery Service
- Puerto: 8761
- Configuración: Eureka Server sin registro automático

### API Gateway
- Puerto: 8080
- Rutas configuradas para cada microservicio
- Circuit breaker para resiliencia

### User Service
- Puerto: 8081
- Base de datos: H2 en memoria
- Kafka: Productor de eventos de usuario
- Endpoints: CRUD de usuarios

### Order Service
- Puerto: 8082
- Base de datos: H2 en memoria
- Feign Client: Comunicación con User Service
- Endpoints: CRUD de órdenes

### Notification Service
- Puerto: 8083
- Kafka: Consumidor de eventos de usuario
- Endpoints: Notificaciones

## Tecnologías Utilizadas

- **Spring Boot 3.5.3**
- **Spring Cloud 2025.0.0**
- **Spring Cloud Gateway**
- **Spring Cloud Netflix Eureka**
- **Spring Cloud OpenFeign**
- **Spring Kafka**
- **Spring Data JPA**
- **H2 Database**
- **Java 21**

## Puertos de los Servicios

- **Discovery Service**: 8761
- **API Gateway**: 8080
- **User Service**: 8081
- **Order Service**: 8082
- **Notification Service**: 8083

## Configuración de Kafka

Todos los servicios que usan Kafka están configurados para conectarse a:
- **Bootstrap Servers**: localhost:9092
- **Auto Offset Reset**: earliest
- **Serializers/Deserializers**: JSON

## Configuración de Eureka

Todos los servicios están configurados para registrarse en:
- **Eureka Server**: http://localhost:8761/eureka/

## Configuración de Base de Datos

Los servicios que usan base de datos están configurados con:
- **H2 Database**: En memoria
- **DDL Auto**: create-drop
- **Show SQL**: true

## Endpoints de Monitoreo

Todos los servicios exponen los siguientes endpoints:
- `/actuator/health` - Estado de salud
- `/actuator/info` - Información del servicio
- `/actuator/metrics` - Métricas del servicio

## Ventajas de la Configuración Local

1. **Simplicidad**: Cada servicio es independiente
2. **Rapidez**: No hay dependencias externas de configuración
3. **Flexibilidad**: Fácil modificación de configuraciones
4. **Independencia**: No depende del config-server 