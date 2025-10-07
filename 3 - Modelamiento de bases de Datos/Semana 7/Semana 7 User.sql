/* =========================================================
   PRY2204_S7 - Tarea Completa con Correcciones
   ========================================================= */

/* =========================================================
   CASO 1: IMPLEMENTACIÓN DEL MODELO (DDL)
   Ajustes: Clave compuesta en COMUNA, sueldo ampliado, cod_comuna/cod_region en FKs
   ========================================================= */

-- LIMPIEZA DE TABLAS (Se recomienda el orden inverso de creación)
DROP TABLE DOMINIO CASCADE CONSTRAINTS;
DROP TABLE TITULACION CASCADE CONSTRAINTS;
DROP TABLE PERSONAL CASCADE CONSTRAINTS;
DROP TABLE IDIOMA CASCADE CONSTRAINTS;
DROP TABLE TITULO CASCADE CONSTRAINTS;
DROP TABLE GENERO CASCADE CONSTRAINTS;
DROP TABLE ESTADO_CIVIL CASCADE CONSTRAINTS;
DROP TABLE COMPANIA CASCADE CONSTRAINTS;
DROP TABLE COMUNA CASCADE CONSTRAINTS;
DROP TABLE REGION CASCADE CONSTRAINTS;


-- TABLAS INDEPENDIENTES / FUERTES

-- 1. REGION (IDENTITY START WITH 7 INCREMENT BY 2)
CREATE TABLE REGION (
    id_region NUMBER(2) 
        GENERATED ALWAYS AS IDENTITY (START WITH 7 INCREMENT BY 2),
    nombre_region VARCHAR2(25) NOT NULL,
    CONSTRAINT REGION_PK PRIMARY KEY (id_region) 
);

-- 2. ESTADO_CIVIL
CREATE TABLE ESTADO_CIVIL (
    id_estado_civil VARCHAR2(2),
    descripcion_est_civil VARCHAR2(25) NOT NULL,
    CONSTRAINT ESTADO_CIVIL_PK PRIMARY KEY (id_estado_civil)
);

-- 3. GENERO
CREATE TABLE GENERO (
    id_genero VARCHAR2(3),
    descripcion_genero VARCHAR2(25) NOT NULL,
    CONSTRAINT GENERO_PK PRIMARY KEY (id_genero)
);

-- 4. TITULO
CREATE TABLE TITULO (
    id_titulo VARCHAR2(3),
    descripcion_titulo VARCHAR2(60) NOT NULL,
    CONSTRAINT TITULO_PK PRIMARY KEY (id_titulo)
);

-- 5. IDIOMA (IDENTITY START WITH 25 INCREMENT BY 3)
CREATE TABLE IDIOMA (
    id_idioma NUMBER(3) 
        GENERATED ALWAYS AS IDENTITY (START WITH 25 INCREMENT BY 3),
    nombre_idioma VARCHAR2(30) NOT NULL,
    CONSTRAINT IDIOMA_PK PRIMARY KEY (id_idioma)
);

-- 6. COMUNA
-- CORRECCIÓN: PK compuesta (id_comuna, cod_region)
CREATE TABLE COMUNA (
    id_comuna NUMBER(5),
    comuna_nombre VARCHAR2(25) NOT NULL,
    cod_region NUMBER(2) NOT NULL,
    CONSTRAINT COMUNA_PK PRIMARY KEY (id_comuna, cod_region),
    CONSTRAINT COMUNA_FK_REGION FOREIGN KEY (cod_region) 
        REFERENCES REGION (id_region)
);

-- 7. COMPANIA
-- CORRECCIÓN: FK compuesta a COMUNA (cod_comuna, cod_region)
CREATE TABLE COMPANIA (
    id_empresa NUMBER(2),
    nombre_empresa VARCHAR2(25) NOT NULL,
    calle VARCHAR2(50) NOT NULL,
    numeracion NUMBER(5) NOT NULL,
    renta_promedio NUMBER(10) NOT NULL,
    pct_aumento NUMBER(4,3), -- Opcional
    cod_comuna NUMBER(5) NOT NULL,
    cod_region NUMBER(2) NOT NULL,
    CONSTRAINT COMPANIA_PK PRIMARY KEY (id_empresa),
    CONSTRAINT UN_COMPANIA_NOMBRE UNIQUE (nombre_empresa),
    CONSTRAINT COMPANIA_FK_COMUNA FOREIGN KEY (cod_comuna, cod_region) 
        REFERENCES COMUNA (id_comuna, cod_region)
);

-- 8. PERSONAL
-- CORRECCIÓN: sueldo NUMBER(9) y FK compuesta a COMUNA (cod_comuna, cod_region)
CREATE TABLE PERSONAL (
    rut_persona NUMBER(8),
    dv_persona CHAR(1) NOT NULL,
    primer_nombre VARCHAR2(25) NOT NULL,
    segundo_nombre VARCHAR2(25), 
    primer_apellido VARCHAR2(25) NOT NULL,
    segundo_apellido VARCHAR2(25) NOT NULL,
    fecha_contratacion DATE NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    email VARCHAR2(100), 
    calle VARCHAR2(50) NOT NULL,
    numeracion NUMBER(5) NOT NULL,
    sueldo NUMBER(9) NOT NULL, -- CORRECCIÓN: Ampliado a NUMBER(9)
    cod_comuna NUMBER(5) NOT NULL,
    cod_region NUMBER(2) NOT NULL,
    id_genero VARCHAR2(3),
    id_estado_civil VARCHAR2(2),
    id_empresa NUMBER(2) NOT NULL,
    encargado_rut NUMBER(8), -- Auto-referencia
    CONSTRAINT PERSONAL_PK PRIMARY KEY (rut_persona),
    CONSTRAINT FK_PERSONAL_EMPRESA FOREIGN KEY (id_empresa) REFERENCES COMPANIA (id_empresa),
    CONSTRAINT FK_PERSONAL_COMUNA FOREIGN KEY (cod_comuna, cod_region) 
        REFERENCES COMUNA (id_comuna, cod_region),
    CONSTRAINT FK_PERSONAL_ESTADOCIVIL FOREIGN KEY (id_estado_civil) REFERENCES ESTADO_CIVIL (id_estado_civil),
    CONSTRAINT FK_PERSONAL_GENERO FOREIGN KEY (id_genero) REFERENCES GENERO (id_genero),
    CONSTRAINT FK_PERSONAL_PERSONAL FOREIGN KEY (encargado_rut) REFERENCES PERSONAL (rut_persona)
);

-- TABLAS DEPENDIENTES / DÉBILES

-- 9. TITULACION
CREATE TABLE TITULACION (
    id_titulo VARCHAR2(3),
    persona_rut NUMBER(8),
    fecha_titulacion DATE NOT NULL,
    CONSTRAINT TITULACION_PK PRIMARY KEY (id_titulo, persona_rut),
    CONSTRAINT FK_TITULACION_PERSONAL FOREIGN KEY (persona_rut) REFERENCES PERSONAL (rut_persona),
    CONSTRAINT FK_TITULACION_TITULO FOREIGN KEY (id_titulo) REFERENCES TITULO (id_titulo)
);

-- 10. DOMINIO
CREATE TABLE DOMINIO (
    id_idioma NUMBER(3),
    persona_rut NUMBER(8),
    nivel VARCHAR2(25) NOT NULL,
    CONSTRAINT DOMINIO_PK PRIMARY KEY (id_idioma, persona_rut),
    CONSTRAINT FK_DOMINIO_IDIONA FOREIGN KEY (id_idioma) REFERENCES IDIOMA (id_idioma),
    CONSTRAINT FK_DOMINIO_PERSONAL FOREIGN KEY (persona_rut) REFERENCES PERSONAL (rut_persona)
);

/* =========================================================
   CASO 2: MODIFICACIÓN DEL MODELO (ALTER TABLE)
   Restricciones de negocio
   ========================================================= */

-- Email único
ALTER TABLE PERSONAL
ADD CONSTRAINT UN_PERSONAL_EMAIL UNIQUE (email);

-- Dígito verificador permitido
ALTER TABLE PERSONAL
ADD CONSTRAINT CK_PERSONAL_DV CHECK (dv_persona IN ('0','1','2','3','4','5','6','7','8','9','K'));

-- Sueldo mínimo (Funciona con el sueldo ampliado a NUMBER(9))
ALTER TABLE PERSONAL
ADD CONSTRAINT CK_PERSONAL_SUELDO CHECK (sueldo >= 450000);

/* =========================================================
   CASO 3: POBLAMIENTO DEL MODELO (DML)
   ========================================================= */

-- El ID_REGION es autogenerado (IDENTITY)
/* ===== TABLA REGION ===== */
INSERT INTO REGION (nombre_region) VALUES ('ARICA Y PARINACOTA'); -- ID = 7
INSERT INTO REGION (nombre_region) VALUES ('REGION METROPOLITANA'); -- ID = 9
INSERT INTO REGION (nombre_region) VALUES ('LA ARAUCANIA'); -- ID = 11

-- Secuencia para COMUNA
DROP SEQUENCE SEQ_COMUNA;
CREATE SEQUENCE SEQ_COMUNA START WITH 1101 INCREMENT BY 6;

/* ===== TABLA COMUNA (Ajustado a clave compuesta) ===== */
INSERT INTO COMUNA (id_comuna, comuna_nombre, cod_region) VALUES (SEQ_COMUNA.NEXTVAL, 'Arica', 7); -- 1101, 7
INSERT INTO COMUNA (id_comuna, comuna_nombre, cod_region) VALUES (SEQ_COMUNA.NEXTVAL, 'Santiago', 9); -- 1107, 9
INSERT INTO COMUNA (id_comuna, comuna_nombre, cod_region) VALUES (SEQ_COMUNA.NEXTVAL, 'Temuco', 11); -- 1113, 11

-- El ID_IDIOMA es autogenerado (IDENTITY)
/* ===== TABLA IDIOMA ===== */
INSERT INTO IDIOMA (nombre_idioma) VALUES ('Ingles'); -- ID = 25
INSERT INTO IDIOMA (nombre_idioma) VALUES ('Chino'); -- ID = 28
INSERT INTO IDIOMA (nombre_idioma) VALUES ('Aleman'); -- ID = 31
INSERT INTO IDIOMA (nombre_idioma) VALUES ('Espanol'); -- ID = 34
INSERT INTO IDIOMA (nombre_idioma) VALUES ('Frances'); -- ID = 37


-- Secuencia para COMPANIA
DROP SEQUENCE SEQ_COMPANIA;
CREATE SEQUENCE SEQ_COMPANIA START WITH 10 INCREMENT BY 5;

/* ===== TABLA COMPANIA (Ajustado a cod_comuna y cod_region) ===== */
-- Las FKs (1101, 7), (1107, 9) y (1113, 11) deben coincidir con la PK compuesta de COMUNA
INSERT INTO COMPANIA (id_empresa, nombre_empresa, calle, numeracion, renta_promedio, pct_aumento, cod_comuna, cod_region)
VALUES (SEQ_COMPANIA.NEXTVAL, 'CCyRojas', 'Amapolas', 506, 1857000, 0.5, 1101, 7); -- ID = 10

INSERT INTO COMPANIA (id_empresa, nombre_empresa, calle, numeracion, renta_promedio, pct_aumento, cod_comuna, cod_region)
VALUES (SEQ_COMPANIA.NEXTVAL, 'SenTTy', 'Los Alamos', 3490, 897000, 0.025, 1101, 7); -- ID = 15

INSERT INTO COMPANIA (id_empresa, nombre_empresa, calle, numeracion, renta_promedio, pct_aumento, cod_comuna, cod_region)
VALUES (SEQ_COMPANIA.NEXTVAL, 'Praxia LTDA', 'Las Camelias', 11098, 2157000, 0.035, 1107, 9); -- ID = 20

INSERT INTO COMPANIA (id_empresa, nombre_empresa, calle, numeracion, renta_promedio, pct_aumento, cod_comuna, cod_region)
VALUES (SEQ_COMPANIA.NEXTVAL, 'TIC spa', 'FLORES S.A.', 4357, 857000, NULL, 1107, 9); -- ID = 25

INSERT INTO COMPANIA (id_empresa, nombre_empresa, calle, numeracion, renta_promedio, pct_aumento, cod_comuna, cod_region)
VALUES (SEQ_COMPANIA.NEXTVAL, 'SANTANA LTDA', 'AVDA VIC. MACKENA', 106, 757000, 0.015, 1101, 7); -- ID = 30

INSERT INTO COMPANIA (id_empresa, nombre_empresa, calle, numeracion, renta_promedio, pct_aumento, cod_comuna, cod_region)
VALUES (SEQ_COMPANIA.NEXTVAL, 'FLORES Y ASOCIADOS', 'PEDRO LATORRE', 557, 589000, 0.015, 1107, 9); -- ID = 35

INSERT INTO COMPANIA (id_empresa, nombre_empresa, calle, numeracion, renta_promedio, pct_aumento, cod_comuna, cod_region)
VALUES (SEQ_COMPANIA.NEXTVAL, 'J.A. HOFFMAN', 'LATINA D.32', 509, 1857000, 0.025, 1113, 11); -- ID = 40

INSERT INTO COMPANIA (id_empresa, nombre_empresa, calle, numeracion, renta_promedio, pct_aumento, cod_comuna, cod_region)
VALUES (SEQ_COMPANIA.NEXTVAL, 'CAGLIARI D.', 'ALAMEDA', 206, 1857000, NULL, 1107, 9); -- ID = 45

INSERT INTO COMPANIA (id_empresa, nombre_empresa, calle, numeracion, renta_promedio, pct_aumento, cod_comuna, cod_region)
VALUES (SEQ_COMPANIA.NEXTVAL, 'Rojas HNOS LTDA', 'SUCRE', 106, 957000, 0.005, 1113, 11); -- ID = 50

INSERT INTO COMPANIA (id_empresa, nombre_empresa, calle, numeracion, renta_promedio, pct_aumento, cod_comuna, cod_region)
VALUES (SEQ_COMPANIA.NEXTVAL, 'FRIENDS P. S.A', 'SUECIA', 506, 857000, 0.015, 1113, 11); -- ID = 55


/* =========================================================
   CASO 4: RECUPERACION DE DATOS (SELECT)
   ========================================================= */

/* =========================================================
   INFORME 1 – Empresas pertenecientes
   CORRECCIÓN: Uso de NVL(pct_aumento, 0) para incluir filas con NULL
   ========================================================= */

SELECT 
    nombre_empresa        AS "Nombre Empresa",
    calle || ' ' || numeracion AS "Dirección",
    renta_promedio        AS "Renta Promedio",
    -- CORRECCIÓN: Usa NVL para tratar NULL en pct_aumento como 0
    renta_promedio * (1 + NVL(pct_aumento, 0)) AS "Simulación de Renta"
FROM COMPANIA
ORDER BY 
    "Renta Promedio" DESC,
    "Nombre Empresa" ASC;

/* =========================================================
   INFORME 2 – Empresas con renta promedio simulada
   (Se utiliza la versión que calcula SOLO el monto del aumento para coincidir
   con la Figura 4 del documento de instrucciones)
   ========================================================= */

SELECT
    id_empresa           AS "CODIGO",
    nombre_empresa       AS "EMPRESA",
    renta_promedio       AS "PROM RENTA ACTUAL",
    CASE
        WHEN pct_aumento IS NOT NULL
        THEN ROUND(pct_aumento + 0.15, 3) -- Porcentaje total a aplicar
        ELSE NULL
    END                  AS "PCT AUMENTADO EN 15%",
    CASE
        WHEN pct_aumento IS NOT NULL
        THEN renta_promedio * (pct_aumento + 0.15)  -- Calcula SOLO el monto del AUMENTO (para Figura 4)
        ELSE NULL
    END                  AS "RENTA AUMENTADA"
FROM COMPANIA
ORDER BY
    "PROM RENTA ACTUAL" ASC,
    "EMPRESA" DESC;