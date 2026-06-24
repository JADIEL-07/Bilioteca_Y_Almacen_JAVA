-- ========================================================
-- Schema de Referencia (biblioteca_sena) para PostgreSQL
-- ========================================================
-- Este script detalla la estructura esperada por el código
-- Java (DashboardDAO.java) para que el Dashboard muestre
-- los datos correctamente.

CREATE DATABASE biblioteca_sena;

\c biblioteca_sena;

-- 1. Tabla de Items (Elementos totales y disponibles)
CREATE TABLE items (
    id_item SERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    categoria VARCHAR(100),
    estado VARCHAR(50) DEFAULT 'Disponible', -- 'Disponible', 'Prestado', 'En Mantenimiento'
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Tabla de Préstamos (Elementos prestados)
CREATE TABLE prestamos (
    id_prestamo SERIAL PRIMARY KEY,
    id_item INT REFERENCES items(id_item),
    id_usuario INT, -- Asumiendo que existe tabla usuarios
    fecha_prestamo TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_devolucion TIMESTAMP,
    estado VARCHAR(50) DEFAULT 'Activo' -- 'Activo', 'Finalizado'
);

-- 3. Tabla de Mantenimientos (Equipos en reparación)
CREATE TABLE mantenimientos (
    id_mantenimiento SERIAL PRIMARY KEY,
    id_item INT REFERENCES items(id_item),
    descripcion TEXT,
    fecha_inicio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_fin TIMESTAMP,
    estado VARCHAR(50) DEFAULT 'En proceso' -- 'En proceso', 'Completado'
);

-- 4. Tabla de Reservas (Reservas activas)
CREATE TABLE reservas (
    id_reserva SERIAL PRIMARY KEY,
    id_item INT REFERENCES items(id_item),
    id_usuario INT,
    fecha_reserva TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(50) DEFAULT 'Pendiente' -- 'Pendiente', 'Aprobada', 'Rechazada'
);

-- ========================================================
-- INSERCIÓN DE DATOS DE PRUEBA (Para ver los números cambiar)
-- ========================================================

-- Insertar 10 items (5 Disponibles, 3 Prestados, 2 En Mantenimiento)
INSERT INTO items (nombre, estado) VALUES 
('Laptop HP Core i5', 'Disponible'),
('Laptop Dell Core i7', 'Disponible'),
('Proyector Epson', 'Disponible'),
('Libro Java Swing', 'Disponible'),
('Osciloscopio', 'Disponible'),
('Multímetro', 'Prestado'),
('Kit Arduino', 'Prestado'),
('Libro Base de Datos', 'Prestado'),
('Impresora 3D', 'En Mantenimiento'),
('Servidor Linux', 'En Mantenimiento');

-- Insertar Préstamos Activos
INSERT INTO prestamos (id_item, estado) VALUES 
(6, 'Activo'),
(7, 'Activo'),
(8, 'Activo');

-- Insertar Mantenimientos en Proceso
INSERT INTO mantenimientos (id_item, estado) VALUES 
(9, 'En proceso'),
(10, 'En proceso');

-- Insertar Reservas
INSERT INTO reservas (id_item, estado) VALUES 
(1, 'Pendiente'),
(2, 'Pendiente'),
(3, 'Aprobada'); -- Solo cuenta las pendientes
