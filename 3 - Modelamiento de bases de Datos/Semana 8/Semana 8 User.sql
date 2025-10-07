-- 1. LIMPIEZA DE OBJETOS
DROP TABLE ADMINISTRATIVO CASCADE CONSTRAINTS;
DROP TABLE VENDEDOR CASCADE CONSTRAINTS;
DROP TABLE DETALLE_VENTA CASCADE CONSTRAINTS;
DROP TABLE PRODUCTO CASCADE CONSTRAINTS;
DROP TABLE PROVEEDOR CASCADE CONSTRAINTS;
DROP TABLE VENTA CASCADE CONSTRAINTS;
DROP TABLE EMPLEADO CASCADE CONSTRAINTS;
DROP TABLE MARCA CASCADE CONSTRAINTS;
DROP TABLE CATEGORIA CASCADE CONSTRAINTS;
DROP TABLE MEDIO_PAGO CASCADE CONSTRAINTS;
DROP TABLE AFP CASCADE CONSTRAINTS;
DROP TABLE SALUD CASCADE CONSTRAINTS;
DROP TABLE COMUNA CASCADE CONSTRAINTS;
DROP TABLE REGION CASCADE CONSTRAINTS;
DROP SEQUENCE SEQ_SALUD;
DROP SEQUENCE SEQ_EMPLEADO;

-- 2. DDL: CREACIÓN DE ESTRUCTURAS BÁSICAS (Tablas Fuertes)

-- REGION
CREATE TABLE REGION (
    id_region NUMBER(4),
    nom_region VARCHAR2(255) NOT NULL,
    CONSTRAINT PK_REGION PRIMARY KEY (id_region)
);

-- COMUNA
CREATE TABLE COMUNA (
    id_comuna NUMBER(4),
    nom_comuna VARCHAR2(100) NOT NULL,
    cod_region NUMBER(4) NOT NULL,
    CONSTRAINT PK_COMUNA PRIMARY KEY (id_comuna),
    CONSTRAINT UN_COMUNA_NOMBRE UNIQUE (nom_comuna),
    CONSTRAINT FK_COMUNA_REGION FOREIGN KEY (cod_region) REFERENCES REGION (id_region)
);

-- PROVEEDOR
CREATE TABLE PROVEEDOR (
    id_proveedor NUMBER(5),
    nombre_proveedor VARCHAR2(150) NOT NULL,
    rut_proveedor VARCHAR2(12) NOT NULL,
    telefono VARCHAR2(10),
    email VARCHAR2(200) NOT NULL,
    direccion VARCHAR2(200) NOT NULL,
    cod_comuna NUMBER(4) NOT NULL,
    CONSTRAINT PK_PROVEEDOR PRIMARY KEY (id_proveedor),
    CONSTRAINT UN_PROVEEDOR_RUT UNIQUE (rut_proveedor),
    CONSTRAINT UN_PROVEEDOR_EMAIL UNIQUE (email),
    CONSTRAINT FK_PROVEEDOR_COMUNA FOREIGN KEY (cod_comuna) REFERENCES COMUNA (id_comuna)
);

-- MARCA
CREATE TABLE MARCA (
    id_marca NUMBER(3),
    nombre_marca VARCHAR2(25) NOT NULL,
    CONSTRAINT PK_MARCA PRIMARY KEY (id_marca),
    CONSTRAINT UN_MARCA_NOMBRE UNIQUE (nombre_marca)
);

-- CATEGORIA
CREATE TABLE CATEGORIA (
    id_categoria NUMBER(3),
    nombre_categoria VARCHAR2(255) NOT NULL,
    CONSTRAINT PK_CATEGORIA PRIMARY KEY (id_categoria)
);

-- PRODUCTO
CREATE TABLE PRODUCTO (
    id_producto NUMBER(4),
    nombre_producto VARCHAR2(100) NOT NULL,
    precio_unitario NUMBER NOT NULL,
    origen_nacional CHAR(1) NOT NULL, -- S/N
    stock_minimo NUMBER NOT NULL,
    activo CHAR(1) NOT NULL, -- S/N
    cod_marca NUMBER(3) NOT NULL,
    cod_categoria NUMBER(3) NOT NULL,
    cod_proveedor NUMBER(5) NOT NULL,
    CONSTRAINT PK_PRODUCTO PRIMARY KEY (id_producto),
    CONSTRAINT UN_PRODUCTO_NOMBRE UNIQUE (nombre_producto),
    CONSTRAINT CK_PRODUCTO_ORIGEN CHECK (origen_nacional IN ('S', 'N')),
    CONSTRAINT CK_PRODUCTO_ACTIVO CHECK (activo IN ('S', 'N')),
    CONSTRAINT CK_PRODUCTO_STOCK_MIN CHECK (stock_minimo >= 3),
    CONSTRAINT FK_PROD_MARCA FOREIGN KEY (cod_marca) REFERENCES MARCA (id_marca),
    CONSTRAINT FK_PROD_CAT FOREIGN KEY (cod_categoria) REFERENCES CATEGORIA (id_categoria),
    CONSTRAINT FK_PROD_PROV FOREIGN KEY (cod_proveedor) REFERENCES PROVEEDOR (id_proveedor)
);

-- AFP (Usa IDENTITY: inicia en 210, incrementa en 6)
CREATE TABLE AFP (
    id_afp NUMBER(4) 
        GENERATED ALWAYS AS IDENTITY (START WITH 210 INCREMENT BY 6),
    nom_afp VARCHAR2(255) NOT NULL,
    CONSTRAINT PK_AFP PRIMARY KEY (id_afp)
);

-- SALUD
CREATE TABLE SALUD (
    id_salud NUMBER(4),
    nom_salud VARCHAR2(40) NOT NULL,
    CONSTRAINT PK_SALUD PRIMARY KEY (id_salud)
);

-- MEDIO_PAGO
CREATE TABLE MEDIO_PAGO (
    id_mpago NUMBER(3),
    nombre_mpago VARCHAR2(50) NOT NULL,
    CONSTRAINT PK_MEDIO_PAGO PRIMARY KEY (id_mpago)
);

-- EMPLEADO
CREATE TABLE EMPLEADO (
    id_empleado NUMBER(4),
    rut_empleado VARCHAR2(12) NOT NULL,
    nombre_empleado VARCHAR2(25) NOT NULL,
    apellido_paterno VARCHAR2(25) NOT NULL,
    apellido_materno VARCHAR2(25) NOT NULL,
    fecha_contratacion DATE NOT NULL,
    sueldo_base NUMBER(10) NOT NULL,
    bono_jefatura NUMBER(10),
    activo CHAR(1) NOT NULL,
    tipo_empleado VARCHAR2(25) NOT NULL,
    cod_empleado NUMBER(4), -- FK para Jefe directo (NULLable)
    cod_salud NUMBER(4) NOT NULL,
    cod_afp NUMBER(4) NOT NULL,
    CONSTRAINT PK_EMPLEADO PRIMARY KEY (id_empleado),
    CONSTRAINT UN_EMPLEADO_RUT UNIQUE (rut_empleado),
    CONSTRAINT CK_EMPLEADO_ACTIVO CHECK (activo IN ('S', 'N')),
    CONSTRAINT CK_EMPLEADO_TIPO CHECK (tipo_empleado IN ('Administrativo', 'Vendedor')),
    CONSTRAINT FK_EMP_JEFE FOREIGN KEY (cod_empleado) REFERENCES EMPLEADO (id_empleado),
    CONSTRAINT FK_EMP_SALUD FOREIGN KEY (cod_salud) REFERENCES SALUD (id_salud),
    CONSTRAINT FK_EMP_AFP FOREIGN KEY (cod_afp) REFERENCES AFP (id_afp)
);

-- VENTA (Usa IDENTITY: inicia en 5050, incrementa en 3)
CREATE TABLE VENTA (
    id_venta NUMBER(4) 
        GENERATED ALWAYS AS IDENTITY (START WITH 5050 INCREMENT BY 3),
    fecha_venta DATE NOT NULL,
    total_venta NUMBER(10) NOT NULL,
    cod_mpago NUMBER(3) NOT NULL,
    cod_empleado NUMBER(4) NOT NULL,
    CONSTRAINT PK_VENTA PRIMARY KEY (id_venta),
    CONSTRAINT FK_VENTA_MPAGO FOREIGN KEY (cod_mpago) REFERENCES MEDIO_PAGO (id_mpago),
    CONSTRAINT FK_VENTA_EMP FOREIGN KEY (cod_empleado) REFERENCES EMPLEADO (id_empleado)
);

-- DETALLE_VENTA (Clave Compuesta: cod_venta, cod_producto)
CREATE TABLE DETALLE_VENTA (
    cod_venta NUMBER(4),
    cod_producto NUMBER(4),
    cantidad NUMBER(6) NOT NULL,
    CONSTRAINT PK_DETALLE_VENTA PRIMARY KEY (cod_venta, cod_producto),
    CONSTRAINT FK_DET_VEN_VENTA FOREIGN KEY (cod_venta) REFERENCES VENTA (id_venta),
    CONSTRAINT FK_DET_VEN_PROD FOREIGN KEY (cod_producto) REFERENCES PRODUCTO (id_producto),
    CONSTRAINT CK_DET_VEN_CANTIDAD CHECK (cantidad > 0)
);

-- ADMINISTRATIVO (Subtipo de EMPLEADO)
CREATE TABLE ADMINISTRATIVO (
    id_empleado NUMBER(4),
    admin_fk_empleado NUMBER(4) NOT NULL,
    CONSTRAINT PK_ADMINISTRATIVO PRIMARY KEY (id_empleado),
    CONSTRAINT FK_ADMIN_EMP FOREIGN KEY (id_empleado) REFERENCES EMPLEADO (id_empleado)
);

-- VENDEDOR (Subtipo de EMPLEADO)
CREATE TABLE VENDEDOR (
    id_empleado NUMBER(4),
    comision_venta NUMBER(5, 2) NOT NULL,
    CONSTRAINT PK_VENDEDOR PRIMARY KEY (id_empleado),
    CONSTRAINT CK_VENDEDOR_COMISION CHECK (comision_venta BETWEEN 0 AND 0.25),
    CONSTRAINT FK_VENDEDOR_EMP FOREIGN KEY (id_empleado) REFERENCES EMPLEADO (id_empleado)
);


-- 3. DDL: CREACIÓN DE SECUENCIAS
CREATE SEQUENCE SEQ_SALUD START WITH 2050 INCREMENT BY 10;
CREATE SEQUENCE SEQ_EMPLEADO START WITH 750 INCREMENT BY 3;


-- 4. DDL: ALTER TABLE
-- Restricción 1: Sueldo base >= $400.000 en EMPLEADO
ALTER TABLE EMPLEADO ADD CONSTRAINT CK_EMPLEADO_SUELDO CHECK (sueldo_base >= 400000);


-- 5. DML: POBLAMIENTO DE DATOS

-- REGION
INSERT INTO REGION (id_region, nom_region) VALUES (1, 'Región Metropolitana');
INSERT INTO REGION (id_region, nom_region) VALUES (2, 'Valparaíso');
INSERT INTO REGION (id_region, nom_region) VALUES (3, 'Biobío');
INSERT INTO REGION (id_region, nom_region) VALUES (4, 'Los Lagos');

-- COMUNA
INSERT INTO COMUNA (id_comuna, nom_comuna, cod_region) VALUES (10, 'Santiago', 1);
INSERT INTO COMUNA (id_comuna, nom_comuna, cod_region) VALUES (20, 'Valparaíso', 2);

-- PROVEEDOR, MARCA, CATEGORIA
INSERT INTO PROVEEDOR (id_proveedor, nombre_proveedor, rut_proveedor, telefono, email, direccion, cod_comuna) 
VALUES (10001, 'Distribuidora Central', '76543210-9', '987654321', 'contacto@central.cl', 'Calle Falsa 123', 10);
INSERT INTO PROVEEDOR (id_proveedor, nombre_proveedor, rut_proveedor, telefono, email, direccion, cod_comuna) 
VALUES (10002, 'Lácteos del Sur', '87654321-8', '912345678', 'ventas@lacteos.cl', 'Av. Sur 456', 20);

INSERT INTO MARCA (id_marca, nombre_marca) VALUES (10, 'Coca-Cola');
INSERT INTO MARCA (id_marca, nombre_marca) VALUES (11, 'Colun');

INSERT INTO CATEGORIA (id_categoria, nombre_categoria) VALUES (100, 'Bebidas');
INSERT INTO CATEGORIA (id_categoria, nombre_categoria) VALUES (101, 'Lácteos');

-- PRODUCTO
INSERT INTO PRODUCTO (id_producto, nombre_producto, precio_unitario, origen_nacional, stock_minimo, activo, cod_marca, cod_categoria, cod_proveedor)
VALUES (1, 'Coca-Cola 1.5L', 1500, 'N', 5, 'S', 10, 100, 10001);
INSERT INTO PRODUCTO (id_producto, nombre_producto, precio_unitario, origen_nacional, stock_minimo, activo, cod_marca, cod_categoria, cod_proveedor)
VALUES (2, 'Leche Entera 1L', 1200, 'S', 10, 'S', 11, 101, 10002);

-- SALUD
INSERT INTO SALUD (id_salud, nom_salud) VALUES (SEQ_SALUD.NEXTVAL, 'Fonasa');         -- 2050
INSERT INTO SALUD (id_salud, nom_salud) VALUES (SEQ_SALUD.NEXTVAL, 'Isapre Colmena'); -- 2060
INSERT INTO SALUD (id_salud, nom_salud) VALUES (SEQ_SALUD.NEXTVAL, 'Isapre Banmédica'); -- 2070
INSERT INTO SALUD (id_salud, nom_salud) VALUES (SEQ_SALUD.NEXTVAL, 'Isapre Cruz Blanca'); -- 2080

-- AFP
INSERT INTO AFP (nom_afp) VALUES ('AFP Habitat');  -- 210
INSERT INTO AFP (nom_afp) VALUES ('AFP Cuprum');   -- 216
INSERT INTO AFP (nom_afp) VALUES ('AFP Provida');  -- 222
INSERT INTO AFP (nom_afp) VALUES ('AFP PlanVital'); -- 228

-- MEDIO_PAGO
INSERT INTO MEDIO_PAGO (id_mpago, nombre_mpago) VALUES (11, 'Efectivo');
INSERT INTO MEDIO_PAGO (id_mpago, nombre_mpago) VALUES (12, 'Tarjeta Débito');
INSERT INTO MEDIO_PAGO (id_mpago, nombre_mpago) VALUES (13, 'Tarjeta Crédito');
INSERT INTO MEDIO_PAGO (id_mpago, nombre_mpago) VALUES (14, 'Cheque');

-- EMPLEADO (RUTs Ajustados y VERÓNICA SOTO CORREGIDA para incluir bono_jefatura)

-- Jefe 1 (Marcela) - ID 750. Tipo: Administrativo.
INSERT INTO EMPLEADO (id_empleado, rut_empleado, nombre_empleado, apellido_paterno, apellido_materno, fecha_contratacion, sueldo_base, bono_jefatura, activo, tipo_empleado, cod_empleado, cod_salud, cod_afp)
VALUES (SEQ_EMPLEADO.NEXTVAL, '15111222-3', 'Marcela', 'González', 'Pérez', DATE '2022-03-15', 950000, 80000, 'S', 'Administrativo', NULL, 2050, 210); -- ID 750

-- Jefe 2 (José) - ID 753. Tipo: Administrativo.
INSERT INTO EMPLEADO (id_empleado, rut_empleado, nombre_empleado, apellido_paterno, apellido_materno, fecha_contratacion, sueldo_base, bono_jefatura, activo, tipo_empleado, cod_empleado, cod_salud, cod_afp)
VALUES (SEQ_EMPLEADO.NEXTVAL, '14333444-5', 'José', 'Muñoz', 'Ramírez', DATE '2021-07-10', 900000, 75000, 'S', 'Administrativo', NULL, 2060, 216); -- ID 753

-- Subordinado (Vendedor Verónica) - ID 756. (Tiene bono de 70000)
INSERT INTO EMPLEADO (id_empleado, rut_empleado, nombre_empleado, apellido_paterno, apellido_materno, fecha_contratacion, sueldo_base, bono_jefatura, activo, tipo_empleado, cod_empleado, cod_salud, cod_afp)
VALUES (SEQ_EMPLEADO.NEXTVAL, '18555666-7', 'Verónica', 'Soto', 'Alarcón', DATE '2020-01-05', 880000, 70000, 'S', 'Vendedor', 750, 2060, 228); -- ID 756

-- Subordinado (Vendedor Luis) - ID 759.
INSERT INTO EMPLEADO (id_empleado, rut_empleado, nombre_empleado, apellido_paterno, apellido_materno, fecha_contratacion, sueldo_base, bono_jefatura, activo, tipo_empleado, cod_empleado, cod_salud, cod_afp)
VALUES (SEQ_EMPLEADO.NEXTVAL, '17777888-9', 'Luis', 'Reyes', 'Fuentes', DATE '2023-04-01', 560000, NULL, 'S', 'Vendedor', 750, 2070, 228); -- ID 759

-- Subordinado (Vendedor Claudia) - ID 762.
INSERT INTO EMPLEADO (id_empleado, rut_empleado, nombre_empleado, apellido_paterno, apellido_materno, fecha_contratacion, sueldo_base, bono_jefatura, activo, tipo_empleado, cod_empleado, cod_salud, cod_afp)
VALUES (SEQ_EMPLEADO.NEXTVAL, '19999000-K', 'Claudia', 'Fernández', 'Lagos', DATE '2023-04-15', 600000, NULL, 'S', 'Vendedor', 753, 2070, 228); -- ID 762

-- Subordinado (Administrativo Carlos) - ID 765.
INSERT INTO EMPLEADO (id_empleado, rut_empleado, nombre_empleado, apellido_paterno, apellido_materno, fecha_contratacion, sueldo_base, bono_jefatura, activo, tipo_empleado, cod_empleado, cod_salud, cod_afp)
VALUES (SEQ_EMPLEADO.NEXTVAL, '16101202-3', 'Carlos', 'Navarro', 'Vega', DATE '2023-05-01', 610000, NULL, 'S', 'Administrativo', 750, 2050, 210); -- ID 765

-- Subordinado (Administrativo Javiera) - ID 768.
INSERT INTO EMPLEADO (id_empleado, rut_empleado, nombre_empleado, apellido_paterno, apellido_materno, fecha_contratacion, sueldo_base, bono_jefatura, activo, tipo_empleado, cod_empleado, cod_salud, cod_afp)
VALUES (SEQ_EMPLEADO.NEXTVAL, '18303404-5', 'Javiera', 'Pino', 'Rojas', DATE '2023-05-10', 650000, NULL, 'S', 'Administrativo', 753, 2060, 210); -- ID 768

-- Subordinado (Vendedor Diego) - ID 771.
INSERT INTO EMPLEADO (id_empleado, rut_empleado, nombre_empleado, apellido_paterno, apellido_materno, fecha_contratacion, sueldo_base, bono_jefatura, activo, tipo_empleado, cod_empleado, cod_salud, cod_afp)
VALUES (SEQ_EMPLEADO.NEXTVAL, '20505606-7', 'Diego', 'Mella', 'Contreras', DATE '2023-05-12', 620000, NULL, 'S', 'Vendedor', 750, 2060, 216); -- ID 771

-- Subordinado (Vendedor Fernanda) - ID 774.
INSERT INTO EMPLEADO (id_empleado, rut_empleado, nombre_empleado, apellido_paterno, apellido_materno, fecha_contratacion, sueldo_base, bono_jefatura, activo, tipo_empleado, cod_empleado, cod_salud, cod_afp)
VALUES (SEQ_EMPLEADO.NEXTVAL, '21707808-9', 'Fernanda', 'Herrera', 'Salas', DATE '2023-05-18', 570000, NULL, 'S', 'Vendedor', 753, 2070, 228); -- ID 774

-- Vendedor Tomás (sin jefe directo) - ID 777.
INSERT INTO EMPLEADO (id_empleado, rut_empleado, nombre_empleado, apellido_paterno, apellido_materno, fecha_contratacion, sueldo_base, bono_jefatura, activo, tipo_empleado, cod_empleado, cod_salud, cod_afp)
VALUES (SEQ_EMPLEADO.NEXTVAL, '19991101-0', 'Tomás', 'Vidal', 'Espinoza', DATE '2023-06-01', 530000, NULL, 'S', 'Vendedor', NULL, 2050, 222); -- ID 777


-- ADMINISTRATIVO y VENDEDOR (Poblamiento de subtipos)
INSERT INTO ADMINISTRATIVO (id_empleado, admin_fk_empleado) VALUES (750, 750);
INSERT INTO ADMINISTRATIVO (id_empleado, admin_fk_empleado) VALUES (753, 753);
INSERT INTO ADMINISTRATIVO (id_empleado, admin_fk_empleado) VALUES (765, 750);
INSERT INTO ADMINISTRATIVO (id_empleado, admin_fk_empleado) VALUES (768, 753);

INSERT INTO VENDEDOR (id_empleado, comision_venta) VALUES (756, 0.15);
INSERT INTO VENDEDOR (id_empleado, comision_venta) VALUES (759, 0.25);
INSERT INTO VENDEDOR (id_empleado, comision_venta) VALUES (762, 0.10);
INSERT INTO VENDEDOR (id_empleado, comision_venta) VALUES (771, 0.05);
INSERT INTO VENDEDOR (id_empleado, comision_venta) VALUES (774, 0.20);
INSERT INTO VENDEDOR (id_empleado, comision_venta) VALUES (777, 0.01);

-- VENTA
INSERT INTO VENTA (fecha_venta, total_venta, cod_mpago, cod_empleado) 
VALUES (DATE '2023-05-12', 225990, 12, 771); -- ID 5050
INSERT INTO VENTA (fecha_venta, total_venta, cod_mpago, cod_empleado) 
VALUES (DATE '2023-10-23', 524990, 13, 777); -- ID 5053
INSERT INTO VENTA (fecha_venta, total_venta, cod_mpago, cod_empleado) 
VALUES (DATE '2023-02-17', 466990, 11, 759); -- ID 5056

-- DETALLE_VENTA
INSERT INTO DETALLE_VENTA (cod_venta, cod_producto, cantidad) VALUES (5050, 1, 10);
INSERT INTO DETALLE_VENTA (cod_venta, cod_producto, cantidad) VALUES (5050, 2, 5);
INSERT INTO DETALLE_VENTA (cod_venta, cod_producto, cantidad) VALUES (5053, 2, 2);
INSERT INTO DETALLE_VENTA (cod_venta, cod_producto, cantidad) VALUES (5056, 1, 1);

COMMIT;

-- 6. RECUPERACIÓN DE DATOS (INFORME 1 y 2)

-- INFORME 1: Sueldo total estimado de empleados activos con bono de jefatura.
SELECT
    E.id_empleado AS "IDENTIFICADOR",
    E.nombre_empleado || ' ' || E.apellido_paterno || ' ' || E.apellido_materno AS "NOMBRE COMPLETO",
    E.sueldo_base AS "SALARIO",
    E.bono_jefatura AS "BONIFICACION",
    E.sueldo_base + E.bono_jefatura AS "SALARIO SIMULADO"
FROM
    EMPLEADO E
WHERE
    E.activo = 'S'
    AND E.bono_jefatura IS NOT NULL
ORDER BY
    "SALARIO SIMULADO" DESC,
    E.apellido_paterno DESC;

-- INFORME 2: Empleados con sueldo en rango medio ($550.000 y $800.000).
SELECT
    E.nombre_empleado || ' ' || E.apellido_paterno || ' ' || E.apellido_materno AS "EMPLEADO",
    E.sueldo_base AS "SUELDO",
    ROUND(E.sueldo_base * 0.08) AS "POSIBLE AUMENTO",
    ROUND(E.sueldo_base * 1.08) AS "SUELDO SIMULADO"
FROM
    EMPLEADO E
WHERE
    E.sueldo_base BETWEEN 550000 AND 800000
ORDER BY
    "SUELDO" ASC;