@echo off
echo ========================================
echo Testing Notification Service
echo ========================================

echo.
echo 1. Starting Kafka...
docker-compose up -d zookeeper kafka kafka-ui

echo.
echo 2. Waiting for Kafka to be ready...
timeout /t 10 /nobreak

echo.
echo 3. Starting Notification Service with local profile...
mvn spring-boot:run -pl notification-service -Dspring-boot.run.profiles=local

echo.
echo Done! 