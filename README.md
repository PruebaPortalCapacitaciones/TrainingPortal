# Training Portal

Portal de gestión de cursos de capacitación con autenticación JWT, administración de inscripciones, seguimiento de progreso y generación de certificados PDF.

## Stack Tecnológico

**Backend:**

- Java 21 LTS + Spring Boot 3.5.10
- Spring Security + JWT
- PostgreSQL 18.1
- Maven

**Frontend:**

- Angular 20.3.16
- Node.js 22.18.0 + npm 10.9.3
- TypeScript

**Infraestructura:**

- Render (Backend, Frontend, Base de datos)
- Docker

## Credenciales de Prueba

**Administrador:**

- Documento: 1010123456
- Contraseña: admin123

**Usuario:**

- Crear nueva cuenta mediante registro en la aplicación

## Configuración Inicial

### Base de Datos

1. Crear instancia PostgreSQL en Render o en local
2. Ejecutar el script de inicialización:
   ```bash
   psql -h HOST -p PORT -U USER -d training_bd -f database/TrainingPortalDB.sql
   ```

El script crea las tablas necesarias e inserta datos iniciales.

**Backend:** API REST con Spring Boot 3. Requiere Java 21 y PostgreSQL.

**Frontend:** Aplicación Angular 20. Requiere Node.js 22.

## Despliegue en Producción

### Base de Datos (PostgreSQL en Render)

1. Crear base de datos en Render
2. Configurar credenciales en variables de entorno
3. Ejecutar script TrainingPortalDB.sql

### Backend (Web Service en Render)

- **Directorio raíz:** `backend/trainingportal`
- **Runtime:** Docker
- **Build:** Automático con cada push
- **Variables de entorno requeridas:**
  - `DB_URL`: Conexión PostgreSQL
  - `DB_USERNAME`: Usuario base de datos
  - `DB_PASSWORD`: Contraseña base de datos
  - `JWT_SECRET`: Clave para tokens
  - `JWT_EXPIRATION`: Duración del token
  - `FRONTEND_URL`: URL del frontend

### Frontend (Static Site en Render)

- **Directorio raíz:** `frontend/trainingportal`
- **Build:** `npm install && npm run build -- --configuration production`
- **Directorio de publicación:** `frontend/trainingportal/dist/trainingportal/browser`
- **Reescritura:** Configurar para servir `index.html` en todas las rutas SPA

## Funcionalidades Principales

**Usuarios:**

- Autenticación con usuario y contraseña
- Roles: ADMIN y USER

**Administrador:**

- Gestión completa de cursos (crear, editar, eliminar)
- Visualización de estadísticas
- Control de inscripciones

**Usuarios registrados:**

- Inscripción en cursos
- Seguimiento de progreso
- Descarga de certificados al completar
- Visualización de estadísticas personales

Despliegue automático con cada push a la rama main. Frontend y backend se actualizan en tiempo real. Base de datos persistente independiente.

Estructura del Repositorio

```
PRUEBATRAININGPORTAL/
├── backend/
│ └── trainingportal/
├── frontend/
│ └── trainingportal/
└── database/
└── TrainingPortalDB.sql
```

- **Frontend** https://trainingportalfront.onrender.com
- **Backend API** https://trainingportal-backend.onrender.com
- **Repositorio GitHub** https://github.com/PruebaPortalCapacitaciones/TrainingPortal

**Autor** Giseth Natalia Murcia Monsalve
