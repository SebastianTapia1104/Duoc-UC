-- ==================================================================
-- 1. Creación de Secuencia
-- ==================================================================
CREATE SEQUENCE seq_region
START WITH 21
INCREMENT BY 1
NOCACHE;

-- ==================================================================
-- 2. Creación de Tablas (Sin Claves Foráneas)
-- ==================================================================
CREATE TABLE REGION (
    id_region       NUMBER(2) NOT NULL,
    nombre_region   VARCHAR2(100) NOT NULL
);
CREATE TABLE COMUNA (
    id_comuna       NUMBER(4) GENERATED ALWAYS AS IDENTITY (START WITH 1050 INCREMENT BY 5),
    nombre_comuna   VARCHAR2(100) NOT NULL,
    region_id       NUMBER(2) NOT NULL
);
CREATE TABLE PLANTA (
    id_planta       NUMBER(2) NOT NULL,
    nombre_planta   VARCHAR2(100) NOT NULL,
    direccion       VARCHAR2(200) NOT NULL,
    comuna_id       NUMBER(4) NOT NULL
);
CREATE TABLE TIPO_MAQUINA (
    id_tipo_maquina     NUMBER(3) NOT NULL,
    nombre_tipo_maquina VARCHAR2(100) NOT NULL
);
CREATE TABLE AFP (
    id_afp          NUMBER(2) NOT NULL,
    nombre_afp      VARCHAR2(50) NOT NULL
);
CREATE TABLE SISTEMA_SALUD (
    id_salud        NUMBER(2) NOT NULL,
    nombre_salud    VARCHAR2(50) NOT NULL
);
CREATE TABLE TURNO (
    id_turno        VARCHAR2(5) NOT NULL,
    nombre_turno    VARCHAR2(50) NOT NULL,
    hora_inicio     CHAR(5) NOT NULL,
    hora_fin        CHAR(5) NOT NULL
);
CREATE TABLE EMPLEADO (
    id_empleado         NUMBER(4) NOT NULL,
    rut                 VARCHAR2(10) NOT NULL,
    nombres             VARCHAR2(100) NOT NULL,
    apellidos           VARCHAR2(100) NOT NULL,
    fecha_contratacion  DATE NOT NULL,
    sueldo_base         NUMBER(10) NOT NULL,
    estado              CHAR(1) DEFAULT 'S' NOT NULL,
    planta_id           NUMBER(2) NOT NULL,
    afp_id              NUMBER(2) NOT NULL,
    salud_id            NUMBER(2) NOT NULL,
    jefe_directo_id     NUMBER(4)
);
CREATE TABLE JEFE_TURNO (
    empleado_id         NUMBER(4) NOT NULL,
    area_responsabilidad VARCHAR2(100) NOT NULL,
    max_operarios       NUMBER(3) NOT NULL
);
CREATE TABLE OPERARIO (
    empleado_id         NUMBER(4) NOT NULL,
    categoria_proceso   VARCHAR2(50) NOT NULL,
    certificacion       VARCHAR2(100),
    horas_turno         NUMBER(2) DEFAULT 8 NOT NULL
);
CREATE TABLE TECNICO_MANTENCION (
    empleado_id         NUMBER(4) NOT NULL,
    especialidad        VARCHAR2(50) NOT NULL,
    nivel_certificacion VARCHAR2(50),
    tiempo_respuesta_std NUMBER(3) NOT NULL
);
CREATE TABLE MAQUINA (
    num_maquina         NUMBER(4) NOT NULL,
    planta_id           NUMBER(2) NOT NULL,
    nombre_maquina      VARCHAR2(100) NOT NULL,
    estado              CHAR(1) NOT NULL,
    tipo_maquina_id     NUMBER(3) NOT NULL
);
CREATE TABLE ORDEN_MANTENCION (
    id_orden            NUMBER(6) NOT NULL,
    fecha_programada    DATE NOT NULL,
    fecha_ejecucion     DATE,
    descripcion         VARCHAR2(500) NOT NULL,
    num_maquina         NUMBER(4) NOT NULL,
    planta_id           NUMBER(2) NOT NULL,
    tecnico_id          NUMBER(4) NOT NULL
);
CREATE TABLE ASIGNACION_TURNO (
    id_asignacion       NUMBER(8) NOT NULL,
    fecha_asignacion    DATE NOT NULL,
    rol_desempenado     VARCHAR2(50),
    empleado_id         NUMBER(4) NOT NULL,
    turno_id            VARCHAR2(5) NOT NULL,
    num_maquina         NUMBER(4) NOT NULL,
    planta_id           NUMBER(2) NOT NULL
);

-- ==================================================================
-- 3. Añadir Restricciones (Primary Keys, Unique, Check)
-- ==================================================================
ALTER TABLE REGION ADD CONSTRAINT pk_region PRIMARY KEY (id_region);
ALTER TABLE REGION ADD CONSTRAINT un_nombre_region UNIQUE (nombre_region);
ALTER TABLE COMUNA ADD CONSTRAINT pk_comuna PRIMARY KEY (id_comuna);
ALTER TABLE PLANTA ADD CONSTRAINT pk_planta PRIMARY KEY (id_planta);
ALTER TABLE TIPO_MAQUINA ADD CONSTRAINT pk_tipo_maquina PRIMARY KEY (id_tipo_maquina);
ALTER TABLE TIPO_MAQUINA ADD CONSTRAINT un_nombre_tipo_maquina UNIQUE (nombre_tipo_maquina);
ALTER TABLE AFP ADD CONSTRAINT pk_afp PRIMARY KEY (id_afp);
ALTER TABLE AFP ADD CONSTRAINT un_nombre_afp UNIQUE (nombre_afp);
ALTER TABLE SISTEMA_SALUD ADD CONSTRAINT pk_sistema_salud PRIMARY KEY (id_salud);
ALTER TABLE SISTEMA_SALUD ADD CONSTRAINT un_nombre_salud UNIQUE (nombre_salud);
ALTER TABLE TURNO ADD CONSTRAINT pk_turno PRIMARY KEY (id_turno);
ALTER TABLE TURNO ADD CONSTRAINT un_nombre_turno UNIQUE (nombre_turno);
ALTER TABLE EMPLEADO ADD CONSTRAINT pk_empleado PRIMARY KEY (id_empleado);
ALTER TABLE EMPLEADO ADD CONSTRAINT un_rut_empleado UNIQUE (rut);
ALTER TABLE EMPLEADO ADD CONSTRAINT ck_estado_empleado CHECK (estado IN ('S', 'N'));
ALTER TABLE JEFE_TURNO ADD CONSTRAINT pk_jefe_turno PRIMARY KEY (empleado_id);
ALTER TABLE OPERARIO ADD CONSTRAINT pk_operario PRIMARY KEY (empleado_id);
ALTER TABLE TECNICO_MANTENCION ADD CONSTRAINT pk_tecnico_mantencion PRIMARY KEY (empleado_id);
ALTER TABLE MAQUINA ADD CONSTRAINT pk_maquina PRIMARY KEY (num_maquina, planta_id);
ALTER TABLE MAQUINA ADD CONSTRAINT ck_estado_maquina CHECK (estado IN ('S', 'N'));
ALTER TABLE ORDEN_MANTENCION ADD CONSTRAINT pk_orden_mantencion PRIMARY KEY (id_orden);
ALTER TABLE ORDEN_MANTENCION ADD CONSTRAINT ck_fechas_mantencion CHECK (fecha_ejecucion IS NULL OR fecha_ejecucion >= fecha_programada);
ALTER TABLE ASIGNACION_TURNO ADD CONSTRAINT pk_asignacion_turno PRIMARY KEY (id_asignacion);
ALTER TABLE ASIGNACION_TURNO ADD CONSTRAINT un_empleado_fecha UNIQUE (empleado_id, fecha_asignacion);

-- ==================================================================
-- 4. Añadir Claves Foráneas (Foreign Keys)
-- ==================================================================
ALTER TABLE COMUNA ADD CONSTRAINT fk_comuna_region FOREIGN KEY (region_id) REFERENCES REGION(id_region);
ALTER TABLE PLANTA ADD CONSTRAINT fk_planta_comuna FOREIGN KEY (comuna_id) REFERENCES COMUNA(id_comuna);
ALTER TABLE EMPLEADO ADD CONSTRAINT fk_empleado_planta FOREIGN KEY (planta_id) REFERENCES PLANTA(id_planta);
ALTER TABLE EMPLEADO ADD CONSTRAINT fk_empleado_afp FOREIGN KEY (afp_id) REFERENCES AFP(id_afp);
ALTER TABLE EMPLEADO ADD CONSTRAINT fk_empleado_salud FOREIGN KEY (salud_id) REFERENCES SISTEMA_SALUD(id_salud);
ALTER TABLE EMPLEADO ADD CONSTRAINT fk_empleado_jefe FOREIGN KEY (jefe_directo_id) REFERENCES EMPLEADO(id_empleado);
ALTER TABLE JEFE_TURNO ADD CONSTRAINT fk_jefe_empleado FOREIGN KEY (empleado_id) REFERENCES EMPLEADO(id_empleado);
ALTER TABLE OPERARIO ADD CONSTRAINT fk_operario_empleado FOREIGN KEY (empleado_id) REFERENCES EMPLEADO(id_empleado);
ALTER TABLE TECNICO_MANTENCION ADD CONSTRAINT fk_tecnico_empleado FOREIGN KEY (empleado_id) REFERENCES EMPLEADO(id_empleado);
ALTER TABLE MAQUINA ADD CONSTRAINT fk_maquina_planta FOREIGN KEY (planta_id) REFERENCES PLANTA(id_planta);
ALTER TABLE MAQUINA ADD CONSTRAINT fk_maquina_tipo FOREIGN KEY (tipo_maquina_id) REFERENCES TIPO_MAQUINA(id_tipo_maquina);
ALTER TABLE ORDEN_MANTENCION ADD CONSTRAINT fk_orden_maquina FOREIGN KEY (num_maquina, planta_id) REFERENCES MAQUINA(num_maquina, planta_id);
ALTER TABLE ORDEN_MANTENCION ADD CONSTRAINT fk_orden_tecnico FOREIGN KEY (tecnico_id) REFERENCES TECNICO_MANTENCION(empleado_id);
ALTER TABLE ASIGNACION_TURNO ADD CONSTRAINT fk_asignacion_empleado FOREIGN KEY (empleado_id) REFERENCES EMPLEADO(id_empleado);
ALTER TABLE ASIGNACION_TURNO ADD CONSTRAINT fk_asignacion_turno FOREIGN KEY (turno_id) REFERENCES TURNO(id_turno);
ALTER TABLE ASIGNACION_TURNO ADD CONSTRAINT fk_asignacion_maquina FOREIGN KEY (num_maquina, planta_id) REFERENCES MAQUINA(num_maquina, planta_id);

-- ==================================================================
-- 5. Poblamiento de Tablas (DML)
-- ==================================================================
INSERT INTO REGION (id_region, nombre_region) VALUES (seq_region.NEXTVAL, 'Región de Valparaíso');
INSERT INTO REGION (id_region, nombre_region) VALUES (seq_region.NEXTVAL, 'Región Metropolitana');
INSERT INTO COMUNA (nombre_comuna, region_id) VALUES ('Quilpué', 21);
INSERT INTO COMUNA (nombre_comuna, region_id) VALUES ('Maipú', 22);
INSERT INTO PLANTA (id_planta, nombre_planta, direccion, comuna_id) VALUES (45, 'Planta Oriente', 'Camino Industrial 1234', 1050);
INSERT INTO PLANTA (id_planta, nombre_planta, direccion, comuna_id) VALUES (46, 'Planta Costa', 'Av. Vidrieras 890', 1055);
INSERT INTO TURNO (id_turno, nombre_turno, hora_inicio, hora_fin) VALUES ('M0715', 'Mañana', '07:00', '15:00');
INSERT INTO TURNO (id_turno, nombre_turno, hora_inicio, hora_fin) VALUES ('N2307', 'Noche', '23:00', '07:00');
INSERT INTO TURNO (id_turno, nombre_turno, hora_inicio, hora_fin) VALUES ('T1523', 'Tarde', '15:00', '23:00');

COMMIT;

-- INFORME 1
SELECT
    id_turno || ' - ' || nombre_turno AS TURNO,
    hora_inicio AS ENTRADA,
    hora_fin AS SALIDA
FROM
    TURNO
WHERE
    hora_inicio > '20:00'
ORDER BY
    ENTRADA DESC;

-- INFORME 2
SELECT
    nombre_turno || ' (' || id_turno || ')' AS TURNO,
    hora_inicio AS ENTRADA,
    hora_fin AS SALIDA
FROM
    TURNO
WHERE
    hora_inicio BETWEEN '06:00' AND '14:59'
ORDER BY
    ENTRADA ASC;