@echo off
echo ========================================
echo Starting Local Development Environment
echo ========================================

echo.
echo 1. Starting Kafka and Zookeeper...
docker-compose up -d zookeeper kafka kafka-ui

echo.
echo 2. Waiting for Kafka to be ready...
timeout /t 15 /nobreak

echo.
echo 3. Starting Discovery Service...
start "Discovery Service" cmd /k "cd /d %~dp0 && mvn spring-boot:run -pl discovery-service"

echo.
echo 4. Waiting for Discovery Service...
timeout /t 10 /nobreak

echo.
echo 5. Starting Config Service...
start "Config Service" cmd /k "cd /d %~dp0 && mvn spring-boot:run -pl config-service"

echo.
echo 6. Waiting for Config Service...
timeout /t 5 /nobreak

echo.
echo 7. Starting User Service...
start "User Service" cmd /k "cd /d %~dp0 && mvn spring-boot:run -pl user-service"

echo.
echo 8. Waiting for User Service...
timeout /t 5 /nobreak

echo.
echo 9. Starting Order Service...
start "Order Service" cmd /k "cd /d %~dp0 && mvn spring-boot:run -pl order-service"

echo.
echo 10. Waiting for Order Service...
timeout /t 5 /nobreak

echo.
echo 11. Starting Notification Service...
start "Notification Service" cmd /k "cd /d %~dp0 && mvn spring-boot:run -pl notification-service -Dspring-boot.run.profiles=local"

echo.
echo 12. Waiting for Notification Service...
timeout /t 5 /nobreak

echo.
echo 13. Starting API Gateway...
start "API Gateway" cmd /k "cd /d %~dp0 && mvn spring-boot:run -pl api-gateway"

echo.
echo ========================================
echo Local Development Environment Started!
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
echo Press any key to stop Kafka and Zookeeper...
pause

echo.
echo Stopping Kafka and Zookeeper...
docker-compose down

echo Done! 