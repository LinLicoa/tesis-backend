# Web API - Sistema de Recomendación de Salud Mental

Este proyecto es una API RESTful construida con **Java 21** y **Spring Boot 3**, diseñada para gestionar un sistema de evaluaciones psicológicas, seguimiento de pacientes y recomendaciones de salud mental basadas en lógica neutrosófica.

## 📋 Características Principales

*   **Autenticación y Seguridad**:
    *   Seguridad basada en **JWT (JSON Web Tokens)**.
    *   Protección de endpoints por roles (`ADMIN`, `USUARIO`).
    *   Manejo personalizado de errores de autenticación (401) y autorización (403) con respuestas JSON.
    *   Encriptación de contraseñas con BCrypt.
    *   Bloqueo temporal de cuentas tras múltiples intentos fallidos de login (configurable).
*   **Gestión de Usuarios**:
    *   Registro de usuarios (Médicos/Profesionales).
    *   Gestión de sesiones y recuperación de contraseñas.
*   **Gestión de Pacientes**:
    *   Registro de pacientes con encriptación de nombres (`nombre_encriptado`) para privacidad.
    *   Historial de evaluaciones.
*   **Evaluaciones Psicológicas**:
    *   Soporte para instrumentos estandarizados: **GAD-7** (Ansiedad), **PHQ-9** (Depresión), **PSS-10** (Estrés).
    *   Cálculo automático de puntajes y niveles de riesgo.
    *   Integración de **Lógica Neutrosófica** (Tripletas T, I, F) para mayor precisión en el diagnóstico.
*   **Sistema de Recomendaciones**:
    *   Motor de recomendaciones basado en los resultados de las evaluaciones.
    *   Gestión de recomendaciones por categoría (Manejo emocional, Hábitos, etc.).
*   **Configuración Dinámica**:
    *   Módulo de `ParametroSistema` para modificar configuraciones (ej. duración del token, intentos de login) sin redeploy.
*   **Auditoría y Logs**:
    *   Trazabilidad de acciones críticas del sistema.

## 🛠️ Stack Tecnológico

*   **Lenguaje**: Java 21 (JDK 21)
*   **Framework**: Spring Boot 3.3.5
*   **Seguridad**: Spring Security 6, JWT (io.jsonwebtoken)
*   **Base de Datos**: Microsoft SQL Server (Azure SQL Database)
*   **ORM**: Hibernate / Spring Data JPA
*   **Herramientas**: Maven, Lombok, Swagger/OpenAPI

## 🏗️ Arquitectura

El proyecto sigue una arquitectura en capas clásica y limpia:

1.  **Controller Layer (`com.application.webapi.controller`)**: Maneja las peticiones HTTP y la validación de entrada (`dtos`).
2.  **Service Layer (`com.application.webapi.service`)**: Contiene la lógica de negocio. Utiliza `Mappers` para transformar entre Entidades y DTOs.
3.  **Repository Layer (`com.application.webapi.repository`)**: Capa de persistencia usando interfaces `JpaRepository`.
4.  **Domain Layer (`com.application.webapi.domain.entity`)**: Entidades JPA que mapean a las tablas de la base de datos.
5.  **Security Layer (`com.application.webapi.security`)**: Configuración de filtros JWT, UserDetails y protección de rutas.

## 🚀 Configuración y Ejecución

### Prerrequisitos
*   JDK 21 instalado.
*   Maven instalado.
*   Acceso a una instancia de SQL Server.

### Variables de Entorno / `application.properties`
Asegúrate de configurar la conexión a la base de datos en `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:sqlserver://<HOST>;databaseName=<DB_NAME>;encrypt=true;trustServerCertificate=true
spring.datasource.username=<USER>
spring.datasource.password=<PASSWORD>
```

### Ejecutar la aplicación
```bash
./mvnw spring-boot:run
```
La aplicación iniciará generalmente en `http://localhost:8080`.

### Documentación API (Swagger)
Una vez iniciada la aplicación, puedes explorar y probar los endpoints en:
`http://localhost:8080/swagger-ui.html`

## 🔒 Endpoints Clave

*   **Auth**:
    *   `POST /api/auth/register`: Registro de nuevos usuarios.
    *   `POST /api/auth/login`: Inicio de sesión (Retorna JWT).
*   **Usuarios**:
    *   `POST /api/usuarios/admin`: Crear admin (requiere rol ADMIN).
    *   `GET /api/usuarios/{id}`: Obtener perfil.
*   **Evaluaciones**:
    *   `POST /api/evaluaciones`: Crear nueva evaluación.
    *   `GET /api/evaluaciones/paciente/{id}`: Historial por paciente.
*   **Parámetros**:
    *   `GET /api/parametros-sistema/{clave}`: Obtener configuración.

## 📦 Estructura de Base de Datos
El sistema utiliza las siguientes tablas principales: `usuarios`, `pacientes`, `evaluaciones`, `recomendaciones`, `parametros_sistema`, `logs_auditoria`.
