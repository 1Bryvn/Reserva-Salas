-- ========================================
-- BASE DE DATOS: reservasdb
-- ========================================
USE reservasdb;

DROP TABLE IF EXISTS `prestamo`;
DROP TABLE IF EXISTS `libro`;
DROP TABLE IF EXISTS `reserva`;
DROP TABLE IF EXISTS `sala`;
DROP TABLE IF EXISTS `usuario`;

-- ========================================
-- Tabla: USUARIO
-- ========================================
CREATE TABLE `usuario` (id
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` VARCHAR(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `contrasena` VARCHAR(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `rol` ENUM('ADMIN','ESTUDIANTE') COLLATE utf8mb4_unicode_ci NOT NULL,
  `activo` TINYINT(1) DEFAULT '1',password
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

select * from usuario;
SHOW COLUMNS FROM usuario;
ALTER TABLE usuario DROP COLUMN password;
SHOW CREATE TABLE usuario;

select * from salas;
select * from reservas;

-- Insertar usuario Admin
INSERT INTO `usuario` (nombre, email, contrasena, rol) 
VALUES ('Admin General', 'admin@roomly.cl', 'admin123', 'ADMIN');

-- Insertar usuario Estudiante
INSERT INTO `usuario` (nombre, email, contrasena, rol) 
VALUES ('Juan Pérez', 'juan@estudiante.cl', '123456', 'ESTUDIANTE');

-- ========================================
-- Tabla: SALA
-- ========================================
CREATE TABLE `sala` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ubicacion` VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `capacidad` INT DEFAULT NULL,
  `descripcion` TEXT COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Salas iniciales
INSERT INTO `sala` (nombre, ubicacion, capacidad, descripcion) VALUES
('Sala 101', 'Edificio A - Piso 1', 8, 'Sala pequeña con TV y pizarra'),
('Sala 202', 'Edificio B - Piso 2', 12, 'Sala mediana con proyector'),
('Sala 303', 'Edificio C - Piso 3', 20, 'Sala grande equipada para grupos');

select * from sala;

-- ========================================
-- Tabla: RESERVA
-- ========================================
CREATE TABLE `reserva` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `fecha_reserva` DATE NOT NULL,
  `hora_inicio` TIME NOT NULL,
  `hora_fin` TIME NOT NULL,
  `estado` ENUM('RESERVADA','CANCELADA','FINALIZADA') COLLATE utf8mb4_unicode_ci DEFAULT 'RESERVADA',
  `usuario_id` BIGINT DEFAULT NULL,
  `sala_id` BIGINT DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`usuario_id`),
  KEY `sala_id` (`sala_id`),
  CONSTRAINT `reserva_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`) ON DELETE CASCADE,
  CONSTRAINT `reserva_ibfk_2` FOREIGN KEY (`sala_id`) REFERENCES `sala` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO reserva (fecha_reserva, hora_inicio, hora_fin, estado, usuario_id, sala_id) VALUES
('2025-08-10', '10:00:00', '11:00:00', 'RESERVADA', 1, 1),
('2025-08-11', '15:00:00', '16:30:00', 'FINALIZADA', 2, 2);

SELECT
  u.nombre AS nombre_usuario,
  u.email AS email_usuario,
  u.rol AS rol_usuario,
  r.fecha_reserva,
  r.hora_inicio,
  r.hora_fin,
  r.estado,
  s.nombre AS nombre_sala,
  s.ubicacion,
  s.capacidad,
  s.descripcion
FROM
  reserva r
JOIN usuario u ON r.usuario_id = u.id
JOIN sala s ON r.sala_id = s.id
WHERE
  u.rol = 'ESTUDIANTE';
  
  
  INSERT INTO reserva (
  fecha_reserva,
  hora_inicio,
  hora_fin,
  estado,
  usuario_id,
  sala_id
)
VALUES (
  '2025-08-15',
  '14:00:00',
  '15:30:00',
  'RESERVADA',
  (SELECT id FROM usuario WHERE email = 'pedro.torres@estudiante.cl' AND rol = 'ESTUDIANTE'),
  (SELECT id FROM sala WHERE nombre = 'Sala 101')
);

select * from sala;

select * from usuario;
-- ========================================
-- Tabla: LIBRO
-- ========================================
CREATE TABLE `libro` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `titulo` VARCHAR(150) NOT NULL,
  `autor` VARCHAR(100) NOT NULL,
  `categoria` VARCHAR(50),
  `disponible` TINYINT(1) DEFAULT 1,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Libros iniciales
INSERT INTO `libro` (titulo, autor, categoria) VALUES
('Introducción a Java', 'James Gosling', 'Programación'),
('Base de Datos MySQL', 'Michael Widenius', 'Bases de Datos'),
('Arquitectura de Software', 'Martin Fowler', 'Ingeniería de Software');

select * from libro;

-- ========================================
-- Tabla: PRESTAMO
-- ========================================
CREATE TABLE `prestamo` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `usuario_id` BIGINT NOT NULL,
  `libro_id` BIGINT NOT NULL,
  `fecha_prestamo` DATE NOT NULL,
  `fecha_devolucion` DATE DEFAULT NULL,
  `devuelto` TINYINT(1) DEFAULT 0,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`usuario_id`) REFERENCES `usuario`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`libro_id`) REFERENCES `libro`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
