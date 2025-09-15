-- Generado por Oracle SQL Developer Data Modeler 24.3.1.351.0831
--   en:        2025-09-15 19:45:12 CLST
--   sitio:      Oracle Database 11g
--   tipo:      Oracle Database 11g



-- predefined type, no DDL - MDSYS.SDO_GEOMETRY

-- predefined type, no DDL - XMLTYPE

CREATE TABLE AFP 
    ( 
     id_afp     NUMBER (3)  NOT NULL , 
     nombre_afp VARCHAR2 (50)  NOT NULL 
    ) 
;

ALTER TABLE AFP 
    ADD CONSTRAINT pk_afp PRIMARY KEY ( id_afp ) ;

CREATE TABLE ATENCION 
    ( 
     id_atencion    NUMBER (10)  NOT NULL , 
     fecha_atencion DATE  NOT NULL , 
     tipo_atencion  VARCHAR2 (50)  NOT NULL , 
     diagnostico    VARCHAR2 (500) , 
     rut_paciente   VARCHAR2 (10)  NOT NULL , 
     rut_medico     VARCHAR2 (10)  NOT NULL 
    ) 
;

ALTER TABLE ATENCION 
    ADD CONSTRAINT chk_tipo_atencion 
    CHECK (tipo_atencion IN ('general', 'urgencia', 'preventiva'))
;
ALTER TABLE ATENCION 
    ADD CONSTRAINT pk_atencion PRIMARY KEY ( id_atencion ) ;

CREATE TABLE COMUNA 
    ( 
     id_comuna     NUMBER (5)  NOT NULL , 
     nombre_comuna VARCHAR2 (50)  NOT NULL , 
     id_region     NUMBER (3)  NOT NULL 
    ) 
;

ALTER TABLE COMUNA 
    ADD CONSTRAINT pk_comuna PRIMARY KEY ( id_comuna ) ;

CREATE TABLE ESPECIALIDAD 
    ( 
     id_especialidad     NUMBER (3)  NOT NULL , 
     nombre_especialidad VARCHAR2 (50)  NOT NULL 
    ) 
;

ALTER TABLE ESPECIALIDAD 
    ADD CONSTRAINT pk_especialidad PRIMARY KEY ( id_especialidad ) ;

CREATE TABLE EXAMEN_LABORATORIO 
    ( 
     codigo_examen           VARCHAR2 (20)  NOT NULL , 
     nombre_examen           VARCHAR2 (100)  NOT NULL , 
     tipo_muestra            VARCHAR2 (50)  NOT NULL , 
     condiciones_preparacion VARCHAR2 (200) 
    ) 
;

ALTER TABLE EXAMEN_LABORATORIO 
    ADD CONSTRAINT pk_examen PRIMARY KEY ( codigo_examen ) ;

CREATE TABLE EXAMEN_SOLICITADO 
    ( 
     id_solicitud    NUMBER (10)  NOT NULL , 
     id_atencion     NUMBER (10)  NOT NULL , 
     codigo_examen   VARCHAR2 (20)  NOT NULL , 
     fecha_solicitud DATE  NOT NULL , 
     resultado       CLOB 
    ) 
;

ALTER TABLE EXAMEN_SOLICITADO 
    ADD CONSTRAINT pk_solicitud PRIMARY KEY ( id_solicitud ) ;

CREATE TABLE INSTITUCION_SALUD 
    ( 
     id_isapre     NUMBER (3)  NOT NULL , 
     nombre_isapre VARCHAR2 (50)  NOT NULL 
    ) 
;

ALTER TABLE INSTITUCION_SALUD 
    ADD CONSTRAINT pk_isapre PRIMARY KEY ( id_isapre ) ;

CREATE TABLE MEDICO 
    ( 
     rut_medico      VARCHAR2 (10)  NOT NULL , 
     nombre_completo VARCHAR2 (100)  NOT NULL , 
     fecha_ingreso   DATE  NOT NULL , 
     id_especialidad NUMBER (3)  NOT NULL , 
     id_afp          NUMBER (3)  NOT NULL , 
     id_isapre       NUMBER (3)  NOT NULL 
    ) 
;

ALTER TABLE MEDICO 
    ADD CONSTRAINT pk_medico PRIMARY KEY ( rut_medico ) ;

CREATE TABLE PACIENTE 
    ( 
     rut_paciente     VARCHAR2 (10)  NOT NULL , 
     nombre_completo  VARCHAR2 (100)  NOT NULL , 
     sexo             CHAR (1)  NOT NULL , 
     fecha_nacimiento DATE  NOT NULL , 
     direccion        VARCHAR2 (200)  NOT NULL , 
     id_comuna        NUMBER (5)  NOT NULL , 
     tipo_usuario     VARCHAR2 (20)  NOT NULL 
    ) 
;

ALTER TABLE PACIENTE 
    ADD CONSTRAINT chk_tipo_usuario 
    CHECK (tipo_usuario IN ('estudiante', 'funcionario', 'externo'))
;
ALTER TABLE PACIENTE 
    ADD CONSTRAINT pk_paciente PRIMARY KEY ( rut_paciente ) ;

CREATE TABLE PAGO 
    ( 
     id_pago     NUMBER (10)  NOT NULL , 
     monto       NUMBER (10,2)  NOT NULL , 
     tipo_pago   VARCHAR2 (50)  NOT NULL , 
     id_atencion NUMBER (10)  NOT NULL 
    ) 
;

ALTER TABLE PAGO 
    ADD CONSTRAINT chk_tipo_pago 
    CHECK (tipo_pago IN ('efectivo', 'tarjeta', 'convenio'))
;
ALTER TABLE PAGO 
    ADD CONSTRAINT pk_pago PRIMARY KEY ( id_pago ) ;

CREATE TABLE REGION 
    ( 
     id_region     NUMBER (3)  NOT NULL , 
     nombre_region VARCHAR2 (50)  NOT NULL 
    ) 
;

ALTER TABLE REGION 
    ADD CONSTRAINT pk_region PRIMARY KEY ( id_region ) ;

ALTER TABLE ATENCION 
    ADD CONSTRAINT fk_atencion_medico FOREIGN KEY 
    ( 
     rut_medico
    ) 
    REFERENCES MEDICO 
    ( 
     rut_medico
    ) 
;

ALTER TABLE ATENCION 
    ADD CONSTRAINT fk_atencion_paciente FOREIGN KEY 
    ( 
     rut_paciente
    ) 
    REFERENCES PACIENTE 
    ( 
     rut_paciente
    ) 
;

ALTER TABLE COMUNA 
    ADD CONSTRAINT fk_comuna_region FOREIGN KEY 
    ( 
     id_region
    ) 
    REFERENCES REGION 
    ( 
     id_region
    ) 
;

ALTER TABLE MEDICO 
    ADD CONSTRAINT fk_medico_afp FOREIGN KEY 
    ( 
     id_afp
    ) 
    REFERENCES AFP 
    ( 
     id_afp
    ) 
;

ALTER TABLE MEDICO 
    ADD CONSTRAINT fk_medico_especialidad FOREIGN KEY 
    ( 
     id_especialidad
    ) 
    REFERENCES ESPECIALIDAD 
    ( 
     id_especialidad
    ) 
;

ALTER TABLE MEDICO 
    ADD CONSTRAINT fk_medico_isapre FOREIGN KEY 
    ( 
     id_isapre
    ) 
    REFERENCES INSTITUCION_SALUD 
    ( 
     id_isapre
    ) 
;

ALTER TABLE PACIENTE 
    ADD CONSTRAINT fk_paciente_comuna FOREIGN KEY 
    ( 
     id_comuna
    ) 
    REFERENCES COMUNA 
    ( 
     id_comuna
    ) 
;

ALTER TABLE PAGO 
    ADD CONSTRAINT fk_pago_atencion FOREIGN KEY 
    ( 
     id_atencion
    ) 
    REFERENCES ATENCION 
    ( 
     id_atencion
    ) 
;

ALTER TABLE EXAMEN_SOLICITADO 
    ADD CONSTRAINT fk_solicitud_atencion FOREIGN KEY 
    ( 
     id_atencion
    ) 
    REFERENCES ATENCION 
    ( 
     id_atencion
    ) 
;

ALTER TABLE EXAMEN_SOLICITADO 
    ADD CONSTRAINT fk_solicitud_examen FOREIGN KEY 
    ( 
     codigo_examen
    ) 
    REFERENCES EXAMEN_LABORATORIO 
    ( 
     codigo_examen
    ) 
;



-- Informe de Resumen de Oracle SQL Developer Data Modeler: 
-- 
-- CREATE TABLE                            11
-- CREATE INDEX                             0
-- ALTER TABLE                             24
-- CREATE VIEW                              0
-- ALTER VIEW                               0
-- CREATE PACKAGE                           0
-- CREATE PACKAGE BODY                      0
-- CREATE PROCEDURE                         0
-- CREATE FUNCTION                          0
-- CREATE TRIGGER                           0
-- ALTER TRIGGER                            0
-- CREATE COLLECTION TYPE                   0
-- CREATE STRUCTURED TYPE                   0
-- CREATE STRUCTURED TYPE BODY              0
-- CREATE CLUSTER                           0
-- CREATE CONTEXT                           0
-- CREATE DATABASE                          0
-- CREATE DIMENSION                         0
-- CREATE DIRECTORY                         0
-- CREATE DISK GROUP                        0
-- CREATE ROLE                              0
-- CREATE ROLLBACK SEGMENT                  0
-- CREATE SEQUENCE                          0
-- CREATE MATERIALIZED VIEW                 0
-- CREATE MATERIALIZED VIEW LOG             0
-- CREATE SYNONYM                           0
-- CREATE TABLESPACE                        0
-- CREATE USER                              0
-- 
-- DROP TABLESPACE                          0
-- DROP DATABASE                            0
-- 
-- REDACTION POLICY                         0
-- 
-- ORDS DROP SCHEMA                         0
-- ORDS ENABLE SCHEMA                       0
-- ORDS ENABLE OBJECT                       0
-- 
-- ERRORS                                   0
-- WARNINGS                                 0
