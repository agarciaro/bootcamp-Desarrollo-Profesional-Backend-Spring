# Config Repository - Configuración Centralizada

Este directorio contiene la configuración centralizada para todos los microservicios del proyecto, almacenada en el filesystem.

## Estructura de Archivos

- `application.yml` - Configuración global para todos los microservicios
- `config-service.yml` - Configuración específica del servicio de configuración
- `discovery-service.yml` - Configuración del servicio de descubrimiento
- `api-gateway.yml` - Configuración del API Gateway
- `user-service.yml` - Configuración del servicio de usuarios
- `order-service.yml` - Configuración del servicio de órdenes
- `notification-service.yml` - Configuración del servicio de notificaciones

## Puertos de los Servicios

- **Discovery Service**: 8761
- **Config Service**: 8888
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

## Ventajas de usar Filesystem

1. **Simplicidad**: No requiere configuración de Git
2. **Rapidez**: Acceso directo a archivos locales
3. **Flexibilidad**: Fácil modificación de configuraciones
4. **Independencia**: No depende de servicios externos 