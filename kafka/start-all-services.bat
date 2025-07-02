@echo off
echo ========================================
echo Starting Microservices Demo
echo ========================================

echo.
echo 1. Building all microservices...
call mvn clean package -DskipTests

echo.
echo 2. Starting Kafka and infrastructure...
docker-compose up -d zookeeper kafka kafka-ui

echo.
echo 3. Waiting for Kafka to be ready...
timeout /t 10 /nobreak

echo.
echo 4. Starting microservices...
docker-compose up -d discovery-service
timeout /t 5 /nobreak

docker-compose up -d user-service
timeout /t 5 /nobreak

docker-compose up -d order-service
timeout /t 5 /nobreak

docker-compose up -d notification-service
timeout /t 5 /nobreak

docker-compose up -d api-gateway

echo.
echo ========================================
echo All services started!
echo ========================================
echo.
echo Services URLs:
echo - Eureka Dashboard: http://localhost:8761
echo - API Gateway: http://localhost:8080
echo - User Service: http://localhost:8081
echo - Order Service: http://localhost:8082
echo - Notification Service: http://localhost:8083
echo - Kafka UI: http://localhost:80
echo.
echo Press any key to stop all services...
pause

echo.
echo Stopping all services...
docker-compose down

echo Done! 