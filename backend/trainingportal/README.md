# Training Portal - Backend

API REST para la gestión de cursos de capacitación.

## Tecnologías

- **Spring Boot** 3.5.10
- **Java** 21 LTS
- **PostgreSQL** 18.1

## Funcionalidades

- Autenticación y autorización con JWT
- Control de acceso por roles (ADMIN, USER)
- Gestión de cursos (creación, actualización, eliminación)
- Inscripción y seguimiento del progreso de usuarios
- Generación de certificados PDF
- Estadísticas del dashboard
- Respuestas uniformes y manejo centralizado de errores

## Configuración

### Variables de entorno

Crear un archivo `.env.ps1` en la raíz del proyecto con las siguientes variables:

```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/trainingportal"
$env:DB_USERNAME="postgres_user_ejemplo"
$env:DB_PASSWORD="postgres_password_ejemplo"
$env:JWT_SECRET="secretkey_token_ejemplo"
$env:JWT_EXPIRATION="3600000"
$env:FRONTEND_URL="http://localhost:4200"
```

Cargar las variables:

```powershell
. .\.env.ps1
```

### Base de datos

Ejecutar el script `database/TrainingPortalDB.sql` para crear el esquema, tablas e insertar datos iniciales.

## Ejecución

Instalar dependencias:

```bash
.\mvnw clean install
```

Ejecutar en desarrollo:

```bash
.\mvnw spring-boot:run
```

## Producción

Backend y base de datos desplegados en Render.
