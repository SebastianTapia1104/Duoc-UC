-- ============================================================
-- CASO 1 | Implementación base (secuencial)
-- ============================================================

-- 1) BORRADO DE OBJETOS (solo los que se van a construir)
DROP TABLE PAGO CASCADE CONSTRAINTS;
DROP TABLE DOSIS CASCADE CONSTRAINTS;
DROP TABLE RECETA CASCADE CONSTRAINTS;
DROP TABLE MEDICAMENTO CASCADE CONSTRAINTS;
DROP TABLE TIPO_MEDICAMENTO CASCADE CONSTRAINTS;
DROP TABLE TIPO_RECETA CASCADE CONSTRAINTS;
DROP TABLE DIAGNOSTICO CASCADE CONSTRAINTS;
DROP TABLE PACIENTE CASCADE CONSTRAINTS;
DROP TABLE COMUNA CASCADE CONSTRAINTS;
DROP TABLE CIUDAD CASCADE CONSTRAINTS;
DROP TABLE REGION CASCADE CONSTRAINTS;
DROP TABLE MEDICO CASCADE CONSTRAINTS;
DROP TABLE ESPECIALIDAD CASCADE CONSTRAINTS;
DROP TABLE DIGITADOR CASCADE CONSTRAINTS;

-- 2) CREACIÓN DE TABLAS Y PK

-- Ubicación
CREATE TABLE REGION (
    cod_region   NUMBER(5)     NOT NULL,
    nombre       VARCHAR2(50)  NOT NULL
);
ALTER TABLE REGION ADD CONSTRAINT REGION_PK PRIMARY KEY (cod_region);

CREATE TABLE CIUDAD (
    cod_ciudad   NUMBER(5)     NOT NULL,
    nombre       VARCHAR2(50)  NOT NULL,
    id_region    NUMBER(5)     NOT NULL
);
ALTER TABLE CIUDAD ADD CONSTRAINT CIUDAD_PK PRIMARY KEY (cod_ciudad);

CREATE TABLE COMUNA (
    cod_comuna   NUMBER(5) GENERATED ALWAYS AS IDENTITY
                   (START WITH 1101 INCREMENT BY 1),
    nombre       VARCHAR2(50)  NOT NULL,
    id_ciudad    NUMBER(5)     NOT NULL
);
ALTER TABLE COMUNA ADD CONSTRAINT COMUNA_PK PRIMARY KEY (cod_comuna);

-- Personas y roles
CREATE TABLE PACIENTE (
    rut_pac      NUMBER(8)     NOT NULL,
    dv_pac       CHAR(1)       NOT NULL,
    pnombre      VARCHAR2(25)  NOT NULL,
    snombre      VARCHAR2(25),
    papellido    VARCHAR2(25)  NOT NULL,
    sapellido    VARCHAR2(25)  NOT NULL,
    edad         NUMBER(3)     NOT NULL,         -- se elimina en CASO 2
    telefono     NUMBER(11)    NOT NULL,
    calle        VARCHAR2(80)  NOT NULL,
    numeracion   NUMBER(6)     NOT NULL,
    id_comuna    NUMBER(5)     NOT NULL
);
ALTER TABLE PACIENTE ADD CONSTRAINT PACIENTE_PK PRIMARY KEY (rut_pac);

CREATE TABLE ESPECIALIDAD (
    cod_especialidad  NUMBER(5) GENERATED ALWAYS AS IDENTITY,
    nombre            VARCHAR2(50) NOT NULL
);
ALTER TABLE ESPECIALIDAD ADD CONSTRAINT ESPECIALIDAD_PK PRIMARY KEY (cod_especialidad);

CREATE TABLE MEDICO (
    rut_med        NUMBER(8)     NOT NULL,
    dv_med         CHAR(1)       NOT NULL,
    pnombre        VARCHAR2(25)  NOT NULL,
    snombre        VARCHAR2(25),
    papellido      VARCHAR2(25)  NOT NULL,
    sapellido      VARCHAR2(25),
    telefono       NUMBER(11)    NOT NULL,       -- único
    id_especialidad NUMBER(5)    NOT NULL
);
ALTER TABLE MEDICO ADD CONSTRAINT MEDICO_PK PRIMARY KEY (rut_med);

CREATE TABLE DIGITADOR (
    rut_dig      NUMBER(8)     NOT NULL,
    dv_dig       CHAR(1)       NOT NULL,
    pnombre      VARCHAR2(25)  NOT NULL,
    snombre      VARCHAR2(25),
    papellido    VARCHAR2(25)  NOT NULL,
    sapellido    VARCHAR2(25)
);
ALTER TABLE DIGITADOR ADD CONSTRAINT DIGITADOR_PK PRIMARY KEY (rut_dig);

-- Catálogos del caso
CREATE TABLE TIPO_MEDICAMENTO (
    cod_tipo_medicamento  NUMBER(3)    NOT NULL,
    descripcion           VARCHAR2(25) NOT NULL   -- p.ej., 'GENÉRICO'/'MARCA'
);
ALTER TABLE TIPO_MEDICAMENTO ADD CONSTRAINT TIPO_MEDICAMENTO_PK
    PRIMARY KEY (cod_tipo_medicamento);

CREATE TABLE TIPO_RECETA (
    cod_tipo_receta  NUMBER(3)    NOT NULL,
    descripcion      VARCHAR2(25) NOT NULL  -- DIGITAL/MAGISTRAL/RETENIDA/GENERAL/VETERINARIA
);
ALTER TABLE TIPO_RECETA ADD CONSTRAINT TIPO_RECETA_PK
    PRIMARY KEY (cod_tipo_receta);

CREATE TABLE DIAGNOSTICO (
    cod_diagnostico  NUMBER(3)    NOT NULL,
    nombre           VARCHAR2(50) NOT NULL
);
ALTER TABLE DIAGNOSTICO ADD CONSTRAINT DIAGNOSTICO_PK
    PRIMARY KEY (cod_diagnostico);

-- Medicamento y su stock
CREATE TABLE MEDICAMENTO (
    cod_medicamento      NUMBER(7)     NOT NULL,
    nombre               VARCHAR2(80)  NOT NULL,
    stock_disponible     NUMBER(6)     NOT NULL,
    id_tipo_medicamento  NUMBER(3)     NOT NULL
    -- precio_unidad se agrega en CASO 2
);
ALTER TABLE MEDICAMENTO ADD CONSTRAINT MEDICAMENTO_PK PRIMARY KEY (cod_medicamento);

-- Receta (1 diagnóstico; N medicamentos vía tabla intermedia)
CREATE TABLE RECETA (
    cod_receta         NUMBER(7)     NOT NULL,
    observaciones      VARCHAR2(500),
    fecha_emision      DATE          NOT NULL,
    fecha_vencimiento  DATE,
    pac_rut            NUMBER(8)     NOT NULL,
    med_rut            NUMBER(8)     NOT NULL,
    dig_rut            NUMBER(8)     NOT NULL,
    id_diagnostico     NUMBER(3)     NOT NULL,
    id_tipo_receta     NUMBER(3)     NOT NULL
);
ALTER TABLE RECETA ADD CONSTRAINT RECETA_PK PRIMARY KEY (cod_receta);

-- Detalle de medicamentos por receta (dosis/indicaciones)
CREATE TABLE DOSIS (
    id_medicamento       NUMBER(7)     NOT NULL,
    id_receta            NUMBER(7)     NOT NULL,
    unidades_medicamento NUMBER(3)     NOT NULL,
    descripcion_dosis    VARCHAR2(100) NOT NULL,
    dias_tratamiento     NUMBER(3)     NOT NULL
);
ALTER TABLE DOSIS ADD CONSTRAINT DOSIS_PK PRIMARY KEY (id_medicamento, id_receta);

-- Pagos vinculados a receta (1..N)
CREATE TABLE PAGO (
    cod_boleta   NUMBER(10)   NOT NULL,
    id_receta    NUMBER(7)    NOT NULL,
    fecha_pago   DATE         NOT NULL,
    monto_total  NUMBER(12)   NOT NULL,
    metodo_pago  VARCHAR2(20) NOT NULL   -- restricción de dominio se agrega en CASO 2
);
ALTER TABLE PAGO ADD CONSTRAINT PAGO_PK PRIMARY KEY (cod_boleta);

-- 3) RELACIONES (FK)

-- Ubicación
ALTER TABLE CIUDAD
  ADD CONSTRAINT CIUDAD_REGION_FK FOREIGN KEY (id_region)
      REFERENCES REGION (cod_region);

ALTER TABLE COMUNA
  ADD CONSTRAINT COMUNA_CIUDAD_FK FOREIGN KEY (id_ciudad)
      REFERENCES CIUDAD (cod_ciudad);

ALTER TABLE PACIENTE
  ADD CONSTRAINT PACIENTE_COMUNA_FK FOREIGN KEY (id_comuna)
      REFERENCES COMUNA (cod_comuna);

-- Médico y especialidad
ALTER TABLE MEDICO
  ADD CONSTRAINT MEDICO_ESPECIALIDAD_FK FOREIGN KEY (id_especialidad)
      REFERENCES ESPECIALIDAD (cod_especialidad);

-- Medicamento y su tipo
ALTER TABLE MEDICAMENTO
  ADD CONSTRAINT MEDICAMENTO_TIPO_MED_FK FOREIGN KEY (id_tipo_medicamento)
      REFERENCES TIPO_MEDICAMENTO (cod_tipo_medicamento);

-- Receta: paciente, médico, digitador, diagnóstico y tipo
ALTER TABLE RECETA
  ADD CONSTRAINT RECETA_PACIENTE_FK FOREIGN KEY (pac_rut)
      REFERENCES PACIENTE (rut_pac);

ALTER TABLE RECETA
  ADD CONSTRAINT RECETA_MEDICO_FK FOREIGN KEY (med_rut)
      REFERENCES MEDICO (rut_med);

ALTER TABLE RECETA
  ADD CONSTRAINT RECETA_DIGITADOR_FK FOREIGN KEY (dig_rut)
      REFERENCES DIGITADOR (rut_dig);

ALTER TABLE RECETA
  ADD CONSTRAINT RECETA_DIAGNOSTICO_FK FOREIGN KEY (id_diagnostico)
      REFERENCES DIAGNOSTICO (cod_diagnostico);

ALTER TABLE RECETA
  ADD CONSTRAINT RECETA_TIPO_RECETA_FK FOREIGN KEY (id_tipo_receta)
      REFERENCES TIPO_RECETA (cod_tipo_receta);

-- Dosis: intermedia Receta–Medicamento
ALTER TABLE DOSIS
  ADD CONSTRAINT DOSIS_MEDICAMENTO_FK FOREIGN KEY (id_medicamento)
      REFERENCES MEDICAMENTO (cod_medicamento);

ALTER TABLE DOSIS
  ADD CONSTRAINT DOSIS_RECETA_FK FOREIGN KEY (id_receta)
      REFERENCES RECETA (cod_receta);

-- Pagos por receta
ALTER TABLE PAGO
  ADD CONSTRAINT PAGO_RECETA_FK FOREIGN KEY (id_receta)
      REFERENCES RECETA (cod_receta);

-- 4) REGLAS DE NEGOCIO (UN / CK)

-- DV (0–9/K) en paciente, médico, digitador
ALTER TABLE PACIENTE
  ADD CONSTRAINT PACIENTE_DV_CK CHECK (dv_pac IN ('0','1','2','3','4','5','6','7','8','9','K'));

ALTER TABLE MEDICO
  ADD CONSTRAINT MEDICO_DV_CK CHECK (dv_med IN ('0','1','2','3','4','5','6','7','8','9','K'));

ALTER TABLE DIGITADOR
  ADD CONSTRAINT DIGITADOR_DV_CK CHECK (dv_dig IN ('0','1','2','3','4','5','6','7','8','9','K'));

-- Teléfono único del médico
ALTER TABLE MEDICO
  ADD CONSTRAINT MEDICO_TELEFONO_UN UNIQUE (telefono);


-- ============================================================
-- CASO 2 | Modificaciones posteriores por ALTER
-- ============================================================

-- Precio unitario de medicamento y su rango [$1.000, $2.000.000]
ALTER TABLE MEDICAMENTO
  ADD precio_unidad NUMBER(7);

ALTER TABLE MEDICAMENTO
  ADD CONSTRAINT MEDICAMENTO_PRECIO_CK
      CHECK (precio_unidad BETWEEN 1000 AND 2000000);

-- Método de pago restringido a {EFECTIVO, TARJETA, TRANSFERENCIA}
ALTER TABLE PAGO
  ADD CONSTRAINT PAGO_METODO_CK
      CHECK (metodo_pago IN ('EFECTIVO','TARJETA','TRANSFERENCIA'));

-- Reemplazo de edad por fecha de nacimiento en PACIENTE
ALTER TABLE PACIENTE DROP COLUMN edad;

ALTER TABLE PACIENTE ADD fecha_nac DATE;
