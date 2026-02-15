# Training Portal - Frontend

Aplicación frontend para la gestión de cursos de capacitación.

## Tecnologías

- **Angular** 20.3.16
- **Node.js** 22.18.0
- **npm** 10.9.3

## Seguridad

- **AuthGuard**: Protege rutas que requieren autenticación
- **AdminGuard**: Restringe acceso solo a administradores
- **AuthInterceptor**: Inyecta token JWT automáticamente en cada petición
- **TokenService**: Gestiona almacenamiento del token en localStorage

## Ejecución

Instalar dependencias:

```bash
npm install
```

Ejecutar en desarrollo:

```bash
ng serve
```

Acceder en `http://localhost:4200/`

## Producción

Frontend desplegado como Static Site en Render, conectado a la API del backend.
