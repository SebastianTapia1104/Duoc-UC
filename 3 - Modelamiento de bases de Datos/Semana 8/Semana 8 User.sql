/* =========================================================
   LIMPIEZA DE TABLAS
   ========================================================= */
DROP TABLE DETALLE_VENTA CASCADE CONSTRAINTS;
DROP TABLE VENTA CASCADE CONSTRAINTS;
DROP TABLE VENDEDOR CASCADE CONSTRAINTS;
DROP TABLE ADMINISTRATIVO CASCADE CONSTRAINTS;
DROP TABLE EMPLEADO CASCADE CONSTRAINTS;
DROP TABLE AFP CASCADE CONSTRAINTS;
DROP TABLE SALUD CASCADE CONSTRAINTS;
DROP TABLE MEDIO_PAGO CASCADE CONSTRAINTS;


/* =========================================================
   CASO 1: IMPLEMENTACIÓN DEL MODELO (DDL)
   ========================================================= */

-- 1. SALUD
CREATE TABLE SALUD (
    id_salud NUMBER(4),
    nom_salud VARCHAR2(40) NOT NULL,
    CONSTRAINT SALUD_PK PRIMARY KEY (id_salud)
);

-- 2. AFP (IDENTITY START WITH 210 INCREMENT BY 6)
CREATE TABLE AFP (
    id_afp NUMBER(3) 
        GENERATED ALWAYS AS IDENTITY (START WITH 210 INCREMENT BY 6),
    nom_afp VARCHAR2(25) NOT NULL,
    CONSTRAINT AFP_PK PRIMARY KEY (id_afp)
);

-- 3. MEDIO_PAGO
CREATE TABLE MEDIO_PAGO (
    id_mpago NUMBER(3),
    nombre_mpago VARCHAR2(50) NOT NULL,
    CONSTRAINT MEDIO_PAGO_PK PRIMARY KEY (id_mpago)
);

-- 4. EMPLEADO
CREATE TABLE EMPLEADO (
    id_empleado NUMBER(4),
    rut_empleado VARCHAR2(10) NOT NULL,
    nombre_empleado VARCHAR2(25) NOT NULL,
    apellido_paterno VARCHAR2(25) NOT NULL,
    apellido_materno VARCHAR2(25) NOT NULL,
    fecha_contratacion DATE NOT NULL,
    sueldo_base NUMBER(10) NOT NULL,
    bono_jefatura NUMBER(10),
    activo CHAR(1) NOT NULL,
    tipo_empleado VARCHAR2(25) NOT NULL,
    cod_empleado NUMBER(4), -- FK Auto-referencia (Encargado)
    cod_salud NUMBER(4) NOT NULL,
    cod_afp NUMBER(3) NOT NULL,
    CONSTRAINT EMPLEADO_PK PRIMARY KEY (id_empleado),
    CONSTRAINT EMPLEADO_FK_EMPLEADO FOREIGN KEY (cod_empleado) REFERENCES EMPLEADO (id_empleado),
    CONSTRAINT EMPLEADO_FK_SALUD FOREIGN KEY (cod_salud) REFERENCES SALUD (id_salud),
    CONSTRAINT EMPLEADO_FK_AFP FOREIGN KEY (cod_afp) REFERENCES AFP (id_afp)
);

-- 5. VENDEDOR
CREATE TABLE VENDEDOR (
    id_empleado NUMBER(4),
    comision_venta NUMBER(5,2),
    CONSTRAINT VENDEDOR_PK PRIMARY KEY (id_empleado),
    CONSTRAINT VENDEDOR_FK_EMPLEADO FOREIGN KEY (id_empleado) REFERENCES EMPLEADO (id_empleado)
);

-- 6. VENTA (IDENTITY START WITH 5050 INCREMENT BY 3)
CREATE TABLE VENTA (
    id_venta NUMBER(4) 
        GENERATED ALWAYS AS IDENTITY (START WITH 5050 INCREMENT BY 3),
    fecha_venta DATE NOT NULL,
    total_venta NUMBER(10) NOT NULL,
    cod_mpago NUMBER(3) NOT NULL,
    cod_empleado NUMBER(4) NOT NULL,
    CONSTRAINT VENTA_PK PRIMARY KEY (id_venta),
    CONSTRAINT VENTA_FK_MPAGO FOREIGN KEY (cod_mpago) REFERENCES MEDIO_PAGO (id_mpago),
    CONSTRAINT VENTA_FK_EMPLEADO FOREIGN KEY (cod_empleado) REFERENCES EMPLEADO (id_empleado)
);

-- Nota: DETALLE_VENTA, ADMINISTRATIVO (subtipo), y otras tablas mencionadas no se crean
-- porque no se especificó su estructura DDL en el PDF, y no son necesarias para las FKs
-- de las tablas principales aquí definidas.


/* =========================================================
   CASO 2: MODIFICACIÓN DEL MODELO (ALTER TABLE)
   Implementación de reglas de negocio
   ========================================================= */

-- 1. EMPLEADO: Sueldo base >= $400.000
ALTER TABLE EMPLEADO
ADD CONSTRAINT CK_EMPLEADO_SUELDO CHECK (sueldo_base >= 400000);

-- 2. VENDEDOR: Comisión entre 0 y 0.25
ALTER TABLE VENDEDOR
ADD CONSTRAINT CK_VENDEDOR_COMISION CHECK (comision_venta BETWEEN 0 AND 0.25);

-- Se omiten los ALTER TABLE para PRODUCTO, PROVEEDOR, MARCA, DETALLE_VENTA por no haber sido creadas.


/* =========================================================
   CASO 3: POBLAMIENTO DEL MODELO (DML)
   ========================================================= */

-- AFP (IDENTITY 210, +6)
INSERT INTO AFP (nom_afp) VALUES ('AFP Habitat'); -- ID = 210
INSERT INTO AFP (nom_afp) VALUES ('AFP Cuprum'); -- ID = 216
INSERT INTO AFP (nom_afp) VALUES ('AFP Provida'); -- ID = 222
INSERT INTO AFP (nom_afp) VALUES ('AFP PlanVital'); -- ID = 228

-- Secuencia para SALUD
DROP SEQUENCE SEQ_SALUD;
CREATE SEQUENCE SEQ_SALUD START WITH 2050 INCREMENT BY 10;

-- SALUD (SEQUENCE 2050, +10)
INSERT INTO SALUD (id_salud, nom_salud) VALUES (SEQ_SALUD.NEXTVAL, 'Fonasa'); -- ID = 2050
INSERT INTO SALUD (id_salud, nom_salud) VALUES (SEQ_SALUD.NEXTVAL, 'Isapre Colmena'); -- ID = 2060
INSERT INTO SALUD (id_salud, nom_salud) VALUES (SEQ_SALUD.NEXTVAL, 'Isapre Banmédica'); -- ID = 2070
INSERT INTO SALUD (id_salud, nom_salud) VALUES (SEQ_SALUD.NEXTVAL, 'Isapre Cruz Blanca'); -- ID = 2080

-- MEDIO_PAGO
INSERT INTO MEDIO_PAGO (id_mpago, nombre_mpago) VALUES (11, 'Efectivo');
INSERT INTO MEDIO_PAGO (id_mpago, nombre_mpago) VALUES (12, 'Tarjeta Débito');
INSERT INTO MEDIO_PAGO (id_mpago, nombre_mpago) VALUES (13, 'Tarjeta Crédito');
INSERT INTO MEDIO_PAGO (id_mpago, nombre_mpago) VALUES (14, 'Cheque');

-- Secuencia para EMPLEADO
DROP SEQUENCE SEQ_EMPLEADO;
CREATE SEQUENCE SEQ_EMPLEADO START WITH 750 INCREMENT BY 3;

-- EMPLEADO (SEQUENCE 750, +3)
INSERT INTO EMPLEADO (id_empleado, rut_empleado, nombre_empleado, apellido_paterno, apellido_materno, fecha_contratacion, sueldo_base, bono_jefatura, activo, tipo_empleado, cod_empleado, cod_salud, cod_afp)
VALUES (SEQ_EMPLEADO.NEXTVAL, '75011111-1', 'Marcela', 'González', 'Pérez', DATE '2022-03-15', 950000, 80000, 'S', 'Administrativo', NULL, 2070, 210); -- ID 750

INSERT INTO EMPLEADO (id_empleado, rut_empleado, nombre_empleado, apellido_paterno, apellido_materno, fecha_contratacion, sueldo_base, bono_jefatura, activo, tipo_empleado, cod_empleado, cod_salud, cod_afp)
VALUES (SEQ_EMPLEADO.NEXTVAL, '22222222-2', 'José', 'Muñoz', 'Ramírez', DATE '2021-07-10', 900000, 75000, 'S', 'Administrativo', 750, 2060, 216); -- ID 753

INSERT INTO EMPLEADO (id_empleado, rut_empleado, nombre_empleado, apellido_paterno, apellido_materno, fecha_contratacion, sueldo_base, bono_jefatura, activo, tipo_empleado, cod_empleado, cod_salud, cod_afp)
VALUES (SEQ_EMPLEADO.NEXTVAL, '33333333-3', 'Verónica', 'Soto', 'Alarcón', DATE '2020-01-05', 880000, 70000, 'S', 'Vendedor', 750, 2060, 228); -- ID 756

INSERT INTO EMPLEADO (id_empleado, rut_empleado, nombre_empleado, apellido_paterno, apellido_materno, fecha_contratacion, sueldo_base, bono_jefatura, activo, tipo_empleado, cod_empleado, cod_salud, cod_afp)
VALUES (SEQ_EMPLEADO.NEXTVAL, '44444444-4', 'Luis', 'Reyes', 'Fuentes', DATE '2023-04-01', 560000, NULL, 'S', 'Vendedor', 750, 2070, 228); -- ID 759

INSERT INTO EMPLEADO (id_empleado, rut_empleado, nombre_empleado, apellido_paterno, apellido_materno, fecha_contratacion, sueldo_base, bono_jefatura, activo, tipo_empleado, cod_empleado, cod_salud, cod_afp)
VALUES (SEQ_EMPLEADO.NEXTVAL, '55555555-5', 'Claudia', 'Fernández', 'Lagos', DATE '2023-04-15', 600000, NULL, 'S', 'Vendedor', 753, 2060, 210); -- ID 762

INSERT INTO EMPLEADO (id_empleado, rut_empleado, nombre_empleado, apellido_paterno, apellido_materno, fecha_contratacion, sueldo_base, bono_jefatura, activo, tipo_empleado, cod_empleado, cod_salud, cod_afp)
VALUES (SEQ_EMPLEADO.NEXTVAL, '66666666-6', 'Carlos', 'Navarro', 'Vega', DATE '2023-05-01', 610000, NULL, 'S', 'Administrativo', 753, 2050, 210); -- ID 765

INSERT INTO EMPLEADO (id_empleado, rut_empleado, nombre_empleado, apellido_paterno, apellido_materno, fecha_contratacion, sueldo_base, bono_jefatura, activo, tipo_empleado, cod_empleado, cod_salud, cod_afp)
VALUES (SEQ_EMPLEADO.NEXTVAL, '77777777-7', 'Javiera', 'Pino', 'Rojas', DATE '2023-05-10', 650000, NULL, 'S', 'Administrativo', 750, 2060, 210); -- ID 768

INSERT INTO EMPLEADO (id_empleado, rut_empleado, nombre_empleado, apellido_paterno, apellido_materno, fecha_contratacion, sueldo_base, bono_jefatura, activo, tipo_empleado, cod_empleado, cod_salud, cod_afp)
VALUES (SEQ_EMPLEADO.NEXTVAL, '88888888-8', 'Diego', 'Mella', 'Contreras', DATE '2023-05-12', 620000, NULL, 'S', 'Vendedor', 750, 2060, 216); -- ID 771

INSERT INTO EMPLEADO (id_empleado, rut_empleado, nombre_empleado, apellido_paterno, apellido_materno, fecha_contratacion, sueldo_base, bono_jefatura, activo, tipo_empleado, cod_empleado, cod_salud, cod_afp)
VALUES (SEQ_EMPLEADO.NEXTVAL, '99999999-9', 'Fernanda', 'Herrera', 'Salas', DATE '2023-05-18', 570000, NULL, 'S', 'Vendedor', 753, 2070, 228); -- ID 774

INSERT INTO EMPLEADO (id_empleado, rut_empleado, nombre_empleado, apellido_paterno, apellido_materno, fecha_contratacion, sueldo_base, bono_jefatura, activo, tipo_empleado, cod_empleado, cod_salud, cod_afp)
VALUES (SEQ_EMPLEADO.NEXTVAL, '10101010-0', 'Tomás', 'Vidal', 'Espinoza', DATE '2023-06-01', 530000, NULL, 'S', 'Vendedor', NULL, 2050, 222); -- ID 777

-- VENDEDOR (Comisión asignada a todos los empleados de tipo Vendedor)
INSERT INTO VENDEDOR (id_empleado, comision_venta) VALUES (756, 0.05);
INSERT INTO VENDEDOR (id_empleado, comision_venta) VALUES (759, 0.05);
INSERT INTO VENDEDOR (id_empleado, comision_venta) VALUES (762, 0.05);
INSERT INTO VENDEDOR (id_empleado, comision_venta) VALUES (771, 0.05);
INSERT INTO VENDEDOR (id_empleado, comision_venta) VALUES (774, 0.05);
INSERT INTO VENDEDOR (id_empleado, comision_venta) VALUES (777, 0.05);

-- VENTA (IDENTITY 5050, +3)
INSERT INTO VENTA (fecha_venta, total_venta, cod_mpago, cod_empleado)
VALUES (DATE '2023-05-12', 225990, 12, 771); -- ID = 5050

INSERT INTO VENTA (fecha_venta, total_venta, cod_mpago, cod_empleado)
VALUES (DATE '2023-10-23', 524990, 13, 777); -- ID = 5053

INSERT INTO VENTA (fecha_venta, total_venta, cod_mpago, cod_empleado)
VALUES (DATE '2023-02-17', 466990, 11, 759); -- ID = 5056


/* =========================================================
   CASO 4: RECUPERACION DE DATOS (SELECT)
   ========================================================= */

-- INFORME 1: Sueldo Total Estimado de Empleados Activos con Bono
-- Filtro: ACTIVO = 'S' AND BONO_JEFATURA IS NOT NULL
-- Orden: SALARIO SIMULADO DESC, APELLIDO_PATERNO DESC
SELECT
    ID_EMPLEADO AS "IDENTIFICADOR",
    NOMBRE_EMPLEADO || ' ' || APELLIDO_PATERNO || ' ' || APELLIDO_MATERNO AS "NOMBRE COMPLETO",
    SUELDO_BASE AS "SALARIO",
    BONO_JEFATURA AS "BONIFICACION",
    SUELDO_BASE + BONO_JEFATURA AS "SALARIO SIMULADO"
FROM EMPLEADO
WHERE 
    ACTIVO = 'S'
    AND BONO_JEFATURA IS NOT NULL
ORDER BY
    "SALARIO SIMULADO" DESC,
    APELLIDO_PATERNO DESC;

-- INFORME 2: Empleados con Sueldo Medio y Simulación de Aumento
-- Filtro: SUELDO_BASE BETWEEN 550000 AND 800000
-- Cálculo: Aumento del 8% (SUELDO * 0.08 y SUELDO * 1.08)
-- Orden: SUELDO_BASE ASC
SELECT
    NOMBRE_EMPLEADO || ' ' || APELLIDO_PATERNO || ' ' || APELLIDO_MATERNO AS "EMPLEADO",
    SUELDO_BASE AS "SUELDO",
    SUELDO_BASE * 0.08 AS "POSIBLE AUMENTO",
    SUELDO_BASE * 1.08 AS "SUELDO SIMULADO"
FROM EMPLEADO
WHERE 
    SUELDO_BASE BETWEEN 550000 AND 800000
ORDER BY
    SUELDO_BASE ASC;