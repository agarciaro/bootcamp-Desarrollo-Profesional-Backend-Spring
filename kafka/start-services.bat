@echo off
echo Starting Spring Boot Microservices Demo...
echo.

echo Starting Discovery Service...
start "Discovery Service" cmd /k "cd discovery-service && mvn spring-boot:run"

echo Waiting for Discovery Service to start...
timeout /t 10 /nobreak > nul

echo Starting API Gateway...
start "API Gateway" cmd /k "cd api-gateway && mvn spring-boot:run"

echo Waiting for API Gateway to start...
timeout /t 10 /nobreak > nul

echo Starting User Service...
start "User Service" cmd /k "cd user-service && mvn spring-boot:run"

echo Waiting for User Service to start...
timeout /t 10 /nobreak > nul

echo Starting Order Service...
start "Order Service" cmd /k "cd order-service && mvn spring-boot:run"

echo Waiting for Order Service to start...
timeout /t 10 /nobreak > nul

echo Starting Notification Service...
start "Notification Service" cmd /k "cd notification-service && mvn spring-boot:run"

echo.
echo All services are starting...
echo.
echo Service URLs:
echo - Eureka Dashboard: http://localhost:8761
echo - API Gateway: http://localhost:8080
echo - User Service: http://localhost:8081
echo - Order Service: http://localhost:8082
echo - Notification Service: http://localhost:8083
echo - Kafka UI: http://localhost:8080 (if using docker-compose)
echo.
echo Press any key to exit...
pause > nul 