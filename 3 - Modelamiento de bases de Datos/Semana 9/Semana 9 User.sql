-- LIMPIEZA DE OBJETOS (Para ejecución repetida)
DROP TABLE ASIGNACION_TURNO CASCADE CONSTRAINTS;
DROP TABLE ORDEN_MANTENCION CASCADE CONSTRAINTS;
DROP TABLE OPERARIO CASCADE CONSTRAINTS;
DROP TABLE TECNICO CASCADE CONSTRAINTS;
DROP TABLE JEFE_TURNO CASCADE CONSTRAINTS;
DROP TABLE EMPLEADO CASCADE CONSTRAINTS;
DROP TABLE MAQUINA CASCADE CONSTRAINTS;
DROP TABLE TIPO_MAQUINA CASCADE CONSTRAINTS;
DROP TABLE TURNO CASCADE CONSTRAINTS;
DROP TABLE PLANTA CASCADE CONSTRAINTS;
DROP TABLE SISTEMA_SALUD CASCADE CONSTRAINTS;
DROP TABLE AFP CASCADE CONSTRAINTS;
DROP TABLE ESPECIALIDAD CASCADE CONSTRAINTS;
DROP TABLE COMUNA CASCADE CONSTRAINTS;
DROP TABLE REGION CASCADE CONSTRAINTS;
DROP SEQUENCE SEQ_REGION;


/* ----------------- CREACIÓN DE ESTRUCTURAS (DDL) ----------------- */

-- Tablas de Catálogo y Jerarquía de Dependencia
-- 1. REGION (id_region usa SEQUENCE y nombre es UNIQUE)
CREATE TABLE REGION (
    id_region NUMBER(3),
    nombre_region VARCHAR2(100) NOT NULL,
    CONSTRAINT PK_REGION PRIMARY KEY (id_region),
    CONSTRAINT UN_REGION_NOMBRE UNIQUE (nombre_region)
);

-- Secuencia para REGION (inicia en 21, incrementa en 1) [cite: 58]
CREATE SEQUENCE SEQ_REGION START WITH 21 INCREMENT BY 1;

-- 2. COMUNA (id_comuna usa IDENTITY y nombre es UNIQUE) [cite: 56]
CREATE TABLE COMUNA (
    id_comuna NUMBER(5) 
        GENERATED ALWAYS AS IDENTITY (START WITH 1050 INCREMENT BY 5),
    nombre_comuna VARCHAR2(100) NOT NULL,
    cod_region NUMBER(3) NOT NULL,
    CONSTRAINT PK_COMUNA PRIMARY KEY (id_comuna),
    CONSTRAINT UN_COMUNA_NOMBRE UNIQUE (nombre_comuna),
    CONSTRAINT FK_COMUNA_REGION FOREIGN KEY (cod_region) REFERENCES REGION (id_region)
);

-- 3. PLANTA (Infraestructura productiva)
CREATE TABLE PLANTA (
    id_planta NUMBER(2),
    nombre_planta VARCHAR2(100) NOT NULL,
    direccion VARCHAR2(250) NOT NULL,
    cod_comuna NUMBER(5) NOT NULL,
    CONSTRAINT PK_PLANTA PRIMARY KEY (id_planta),
    CONSTRAINT UN_PLANTA_NOMBRE UNIQUE (nombre_planta),
    CONSTRAINT FK_PLANTA_COMUNA FOREIGN KEY (cod_comuna) REFERENCES COMUNA (id_comuna)
);

-- 4. TURNO (Define los turnos de trabajo - nombre es UNIQUE) [cite: 44, 57]
CREATE TABLE TURNO (
    id_turno VARCHAR2(10), -- M0715, N2307, T1523
    nombre_turno VARCHAR2(50) NOT NULL, -- Mañana, Noche, Tarde
    hora_inicio CHAR(5) NOT NULL, -- HH:MM [cite: 45]
    hora_fin CHAR(5) NOT NULL,
    CONSTRAINT PK_TURNO PRIMARY KEY (id_turno),
    CONSTRAINT UN_TURNO_NOMBRE UNIQUE (nombre_turno)
);

-- 5. TIPO_MAQUINA (nombre es UNIQUE) [cite: 42, 57]
CREATE TABLE TIPO_MAQUINA (
    id_tipo NUMBER(2),
    nombre_tipo VARCHAR2(100) NOT NULL,
    CONSTRAINT PK_TIPO_MAQUINA PRIMARY KEY (id_tipo),
    CONSTRAINT UN_TIPO_MAQUINA_NOMBRE UNIQUE (nombre_tipo)
);

-- 6. MAQUINA (Clave compuesta: num_maquina + cod_planta) [cite: 43]
CREATE TABLE MAQUINA (
    num_maquina NUMBER(5), -- Parte del código interno único [cite: 43]
    cod_planta NUMBER(2) NOT NULL, -- Parte del código interno único [cite: 43]
    nombre_maquina VARCHAR2(100) NOT NULL,
    estado_activo CHAR(1) DEFAULT 'S' NOT NULL, -- S/N [cite: 42]
    cod_tipo NUMBER(2) NOT NULL,
    CONSTRAINT PK_MAQUINA PRIMARY KEY (num_maquina, cod_planta),
    CONSTRAINT UN_MAQUINA_NOMBRE UNIQUE (nombre_maquina),
    CONSTRAINT CK_MAQUINA_ESTADO CHECK (estado_activo IN ('S', 'N')),
    CONSTRAINT FK_MAQUINA_PLANTA FOREIGN KEY (cod_planta) REFERENCES PLANTA (id_planta),
    CONSTRAINT FK_MAQUINA_TIPO FOREIGN KEY (cod_tipo) REFERENCES TIPO_MAQUINA (id_tipo)
);

-- Tablas de Personal
-- Tablas de Catálogo Adicionales para Empleados (nombre es UNIQUE) [cite: 36, 57]
CREATE TABLE AFP (
    id_afp NUMBER(2),
    nombre_afp VARCHAR2(50) NOT NULL,
    CONSTRAINT PK_AFP PRIMARY KEY (id_afp),
    CONSTRAINT UN_AFP_NOMBRE UNIQUE (nombre_afp)
);

CREATE TABLE SISTEMA_SALUD (
    id_sistema NUMBER(2),
    nombre_sistema VARCHAR2(50) NOT NULL,
    CONSTRAINT PK_SIS_SALUD PRIMARY KEY (id_sistema),
    CONSTRAINT UN_SIS_SALUD_NOMBRE UNIQUE (nombre_sistema)
);

CREATE TABLE ESPECIALIDAD (
    id_especialidad NUMBER(2),
    nombre_especialidad VARCHAR2(50) NOT NULL, -- Eléctrica/Mecánica/Instrumentación [cite: 40]
    CONSTRAINT PK_ESPECIALIDAD PRIMARY KEY (id_especialidad),
    CONSTRAINT UN_ESPECIALIDAD_NOMBRE UNIQUE (nombre_especialidad)
);

-- 7. EMPLEADO (Entidad Padre con Jerarquía) [cite: 35, 41]
CREATE TABLE EMPLEADO (
    id_empleado NUMBER(6), -- Código numérico único [cite: 36]
    rut VARCHAR2(12) NOT NULL,
    nombres VARCHAR2(100) NOT NULL,
    apellidos VARCHAR2(100) NOT NULL,
    fecha_contratacion DATE NOT NULL,
    sueldo_base NUMBER(9) NOT NULL,
    estado_activo CHAR(1) DEFAULT 'S' NOT NULL, -- S/N, por defecto 'S' [cite: 36, 37]
    cod_planta NUMBER(2) NOT NULL,
    cod_afp NUMBER(2) NOT NULL,
    cod_sis_salud NUMBER(2) NOT NULL,
    cod_jefe_directo NUMBER(6), -- Relación jerárquica (jefe directo) [cite: 41]
    perfil VARCHAR2(50) NOT NULL, -- Discriminador: JEFE, OPERARIO, TECNICO
    CONSTRAINT PK_EMPLEADO PRIMARY KEY (id_empleado),
    CONSTRAINT UN_EMPLEADO_RUT UNIQUE (rut),
    CONSTRAINT CK_EMPLEADO_ACTIVO CHECK (estado_activo IN ('S', 'N')),
    CONSTRAINT CK_EMPLEADO_PERFIL CHECK (perfil IN ('JEFE_TURNO', 'OPERARIO', 'TECNICO')),
    CONSTRAINT FK_EMP_PLANTA FOREIGN KEY (cod_planta) REFERENCES PLANTA (id_planta),
    CONSTRAINT FK_EMP_AFP FOREIGN KEY (cod_afp) REFERENCES AFP (id_afp),
    CONSTRAINT FK_EMP_SIS_SALUD FOREIGN KEY (cod_sis_salud) REFERENCES SISTEMA_SALUD (id_sistema),
    CONSTRAINT FK_EMP_JEFE FOREIGN KEY (cod_jefe_directo) REFERENCES EMPLEADO (id_empleado)
);

-- 8. JEFE_TURNO (Subtipo de EMPLEADO) [cite: 37]
CREATE TABLE JEFE_TURNO (
    id_empleado NUMBER(6),
    area_responsabilidad VARCHAR2(100) NOT NULL,
    max_operarios NUMBER(3) NOT NULL,
    CONSTRAINT PK_JEFE_TURNO PRIMARY KEY (id_empleado),
    CONSTRAINT FK_JEFE_EMP FOREIGN KEY (id_empleado) REFERENCES EMPLEADO (id_empleado)
);

-- 9. OPERARIO (Subtipo de EMPLEADO) [cite: 38, 39]
CREATE TABLE OPERARIO (
    id_empleado NUMBER(6),
    categoria_proceso VARCHAR2(50) NOT NULL, -- Caliente, frío, inspección
    certificacion VARCHAR2(100),
    horas_estandar NUMBER(2) DEFAULT 8 NOT NULL, -- Por defecto 8 horas [cite: 39]
    CONSTRAINT PK_OPERARIO PRIMARY KEY (id_empleado),
    CONSTRAINT FK_OPERARIO_EMP FOREIGN KEY (id_empleado) REFERENCES EMPLEADO (id_empleado)
);

-- 10. TECNICO (Subtipo de EMPLEADO) [cite: 40]
CREATE TABLE TECNICO (
    id_empleado NUMBER(6),
    cod_especialidad NUMBER(2) NOT NULL,
    nivel_certificacion VARCHAR2(50),
    tiempo_respuesta_estandar NUMBER(3) NOT NULL, -- Minutos
    CONSTRAINT PK_TECNICO PRIMARY KEY (id_empleado),
    CONSTRAINT FK_TECNICO_EMP FOREIGN KEY (id_empleado) REFERENCES EMPLEADO (id_empleado),
    CONSTRAINT FK_TECNICO_ESP FOREIGN KEY (cod_especialidad) REFERENCES ESPECIALIDAD (id_especialidad)
);

-- Tablas Transaccionales
-- 11. ORDEN_MANTENCION (Restricción de fecha de ejecución) [cite: 45, 56]
CREATE TABLE ORDEN_MANTENCION (
    id_orden NUMBER(8),
    cod_maquina NUMBER(5) NOT NULL,
    cod_planta NUMBER(2) NOT NULL,
    cod_tecnico NUMBER(6) NOT NULL,
    fecha_programada DATE NOT NULL,
    fecha_ejecucion DATE,
    descripcion VARCHAR2(500) NOT NULL,
    CONSTRAINT PK_ORDEN_MANT PRIMARY KEY (id_orden),
    -- Clave Foránea a MAQUINA (Clave compuesta)
    CONSTRAINT FK_ORDEN_MAQ FOREIGN KEY (cod_maquina, cod_planta) REFERENCES MAQUINA (num_maquina, cod_planta),
    CONSTRAINT FK_ORDEN_TECNICO FOREIGN KEY (cod_tecnico) REFERENCES TECNICO (id_empleado),
    -- Restricción: fecha_ejecucion >= fecha_programada [cite: 56]
    CONSTRAINT CK_FECHA_EJECUCION CHECK (fecha_ejecucion IS NULL OR fecha_ejecucion >= fecha_programada)
);

-- 12. ASIGNACION_TURNO (Clave compuesta, asignación M:M) [cite: 46, 47]
CREATE TABLE ASIGNACION_TURNO (
    cod_empleado NUMBER(6) NOT NULL,
    cod_maquina NUMBER(5) NOT NULL,
    cod_planta NUMBER(2) NOT NULL,
    cod_turno VARCHAR2(10) NOT NULL,
    fecha_asignacion DATE NOT NULL,
    rol_desempeñado VARCHAR2(100),
    -- PK: Un empleado no puede estar en más de un turno en la misma fecha [cite: 47]
    CONSTRAINT PK_ASIGNACION PRIMARY KEY (cod_empleado, fecha_asignacion),
    CONSTRAINT UN_ASIG_MAQ_TURNO UNIQUE (cod_maquina, cod_planta, fecha_asignacion), -- Restricción Adicional
    CONSTRAINT FK_ASIG_EMP FOREIGN KEY (cod_empleado) REFERENCES EMPLEADO (id_empleado),
    CONSTRAINT FK_ASIG_MAQ FOREIGN KEY (cod_maquina, cod_planta) REFERENCES MAQUINA (num_maquina, cod_planta),
    CONSTRAINT FK_ASIG_TURNO FOREIGN KEY (cod_turno) REFERENCES TURNO (id_turno)
);


/* ----------------- POBLAMIENTO DE DATOS (DML) ----------------- */

-- 1. AFP y SISTEMA_SALUD (Catálogos, no se pide poblar, pero se crean para FK)
INSERT INTO AFP (id_afp, nombre_afp) VALUES (1, 'Habitat');
INSERT INTO AFP (id_afp, nombre_afp) VALUES (2, 'Provida');
INSERT INTO SISTEMA_SALUD (id_sistema, nombre_sistema) VALUES (1, 'Fonasa');
INSERT INTO SISTEMA_SALUD (id_sistema, nombre_sistema) VALUES (2, 'Isapre Colmena');
INSERT INTO ESPECIALIDAD (id_especialidad, nombre_especialidad) VALUES (1, 'Eléctrica');
INSERT INTO ESPECIALIDAD (id_especialidad, nombre_especialidad) VALUES (2, 'Mecánica');

-- 2. REGION (Usa SEQUENCE, comienza en 21) [cite: 58, 61]
INSERT INTO REGION (id_region, nombre_region) VALUES (SEQ_REGION.NEXTVAL, 'Región de Valparaíso'); -- ID 21
INSERT INTO REGION (id_region, nombre_region) VALUES (SEQ_REGION.NEXTVAL, 'Región Metropolitana'); -- ID 22

-- 3. COMUNA (Usa IDENTITY, comienza en 1050, incrementa en 5) [cite: 56, 63]
INSERT INTO COMUNA (nombre_comuna, cod_region) VALUES ('Quilpué', 21); -- ID 1050
INSERT INTO COMUNA (nombre_comuna, cod_region) VALUES ('Maipú', 22);   -- ID 1055

-- 4. PLANTA [cite: 60, 65]
INSERT INTO PLANTA (id_planta, nombre_planta, direccion, cod_comuna) 
VALUES (45, 'Planta Oriente', 'Camino Industrial 1234', 1050);
INSERT INTO PLANTA (id_planta, nombre_planta, direccion, cod_comuna) 
VALUES (46, 'Planta Costa', 'Av. Vidrieras 890', 1055);

-- 5. TURNO [cite: 65, 66]
INSERT INTO TURNO (id_turno, nombre_turno, hora_inicio, hora_fin) 
VALUES ('M0715', 'Mañana', '07:00', '15:00');
INSERT INTO TURNO (id_turno, nombre_turno, hora_inicio, hora_fin) 
VALUES ('N2307', 'Noche', '23:00', '07:00');
INSERT INTO TURNO (id_turno, nombre_turno, hora_inicio, hora_fin) 
VALUES ('T1523', 'Tarde', '15:00', '23:00');

-- COMMIT PARA GUARDAR DATOS BASE
COMMIT;


/* ----------------- RECUPERACIÓN DE DATOS (SELECT) ----------------- */

-- INFORME 1: Listar los turnos cuyo horario de inicio sea posterior a "20:00" (orden descendente por hora de inicio) [cite: 69, 70]
SELECT
    T.NOMBRE_TURNO || ' (' || T.ID_TURNO || ')' AS "TURNO",
    T.HORA_INICIO AS "ENTRADA",
    T.HORA_FIN AS "SALIDA"
FROM TURNO T
WHERE
    T.HORA_INICIO > '20:00'
ORDER BY
    "ENTRADA" DESC;

-- INFORME 2: Turnos diurnos con horario de inicio entre "06:00" y "14:59" (ambos incluidos, orden ascendente por hora de inicio) [cite: 74, 75]
SELECT
    T.NOMBRE_TURNO || ' (' || T.ID_TURNO || ')' AS "TURNO",
    T.HORA_INICIO AS "ENTRADA",
    T.HORA_FIN AS "SALIDA"
FROM TURNO T
WHERE
    T.HORA_INICIO BETWEEN '06:00' AND '14:59'
ORDER BY
    "ENTRADA" ASC;