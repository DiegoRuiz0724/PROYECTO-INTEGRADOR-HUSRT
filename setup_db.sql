-- 1. Crear la base de datos
CREATE DATABASE IF NOT EXISTS proyecto_hurst;
USE proyecto_hurst;

-- 2. Crear el usuario y otorgar permisos
-- Nota: Si usas MySQL 8.0+, asegúrate de ejecutar esto como root.
CREATE USER IF NOT EXISTS 'user_java'@'localhost' IDENTIFIED BY '0000';
GRANT ALL PRIVILEGES ON proyecto_hurst.* TO 'user_java'@'localhost';
FLUSH PRIVILEGES;

-- 3. Tabla de Usuarios (Opcional, Hibernate la creará si se prefiere)
-- CREATE TABLE IF NOT EXISTS usuario (
--     idUsuario BIGINT AUTO_INCREMENT PRIMARY KEY,
--     username VARCHAR(50) UNIQUE NOT NULL,
--     password VARCHAR(100) NOT NULL,
--     rol VARCHAR(20) NOT NULL
-- );

-- 4. Datos maestros básicos (Ejecutar DESPUÉS de abrir la app por primera vez)
-- Hibernate creará las tablas automáticamente al iniciar la aplicación.
-- Una vez que las tablas existan, puedes ejecutar estos inserts:

/*
INSERT INTO universidad (nombre) VALUES ('Universidad Nacional'), ('Universidad de Antioquia'), ('Universidad del Valle');

INSERT INTO programa (nombre, id_universidad) VALUES 
('Medicina', 1), ('Enfermería', 1), 
('Fisioterapia', 2), ('Instrumentación Quirúrgica', 3);

INSERT INTO servicio (nombre, capacidad_maxima, area_medica) VALUES 
('Urgencias', '10', 'URGENCIAS'),
('UCI Adultos', '5', 'UCI'),
('Pediatría', '8', 'PEDIATRIA'),
('Cirugía', '6', 'CIRUGIA');

INSERT INTO docente (nombre, apellido, documento) VALUES 
('Juan', 'Perez', '12345678'),
('Maria', 'Lopez', '87654321');
*/
