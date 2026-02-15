-- CREAR ESQUEMA
CREATE SCHEMA portal;

-- CREAR TABLA USERS
CREATE TABLE portal.users (
    id BIGSERIAL PRIMARY KEY,
    document VARCHAR(10) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    name VARCHAR(100),
    email VARCHAR(100),
    role VARCHAR(5) NOT NULL DEFAULT 'USER'
);

-- AGREGAR USUARIO ADMIN
INSERT INTO portal.users (
    document, 
    password, 
    name, 
    email, 
    role
) VALUES (
    '1010123456',
    'admin123',
    'Usuario Administrador',
    'admin@training.com',
    'ADMIN'
);

--LISTAR USUARIOS
SELECT * FROM portal.users;

-- CREAR TABLA COURSE
CREATE TABLE portal.course (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    description VARCHAR(200),
    module VARCHAR(50) NOT NULL,
    duration_hours INTEGER,
    level VARCHAR(50),
    active BOOLEAN NOT NULL DEFAULT true
);

--LISTAR CURSOS
SELECT * FROM portal.course;

-- CREAR TABLA ENROLLMENT
CREATE TABLE portal.enrollment (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'INSCRITO',
    progress INTEGER NOT NULL DEFAULT 0,
    
    CONSTRAINT fk_enrollment_user FOREIGN KEY (user_id) 
        REFERENCES portal.users(id) ON DELETE CASCADE,
    CONSTRAINT fk_enrollment_course FOREIGN KEY (course_id) 
        REFERENCES portal.course(id) ON DELETE CASCADE,
    CONSTRAINT uk_user_course UNIQUE (user_id, course_id),
    CONSTRAINT chk_progress_range CHECK (progress >= 0 AND progress <= 100)
);

--LISTAR INSCRIPCIONES
SELECT * FROM portal.enrollment;

