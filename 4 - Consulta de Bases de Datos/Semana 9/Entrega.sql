/* ========================================================================== */
/* CONECTADO COMO: ADMIN                                                      */
/* ========================================================================== */

-- 1. CREACIÓN DE USUARIOS (Dueño, Desarrollador, Consultor)
CREATE USER PRY2205_EFT IDENTIFIED BY "PRY2205.semana_9"
DEFAULT TABLESPACE data TEMPORARY TABLESPACE temp QUOTA UNLIMITED ON data;

CREATE USER PRY2205_DES IDENTIFIED BY "PRY2205.semana_9"
DEFAULT TABLESPACE data TEMPORARY TABLESPACE temp QUOTA UNLIMITED ON data;

CREATE USER PRY2205_CON IDENTIFIED BY "PRY2205.semana_9"
DEFAULT TABLESPACE data TEMPORARY TABLESPACE temp QUOTA UNLIMITED ON data;

-- 2. PERMISOS DE CONEXIÓN
GRANT CREATE SESSION TO PRY2205_EFT;
GRANT CREATE SESSION TO PRY2205_DES;
GRANT CREATE SESSION TO PRY2205_CON;

-- 3. Creación de Roles
CREATE ROLE PRY2205_ROL_D;
CREATE ROLE PRY2205_ROL_C;

-- 4. ASIGNACIÓN DE PRIVILEGIOS A LOS ROLES
-- ROL_DES: Necesita crear tablas, procedimientos, vistas, secuencias, etc.
GRANT CREATE VIEW, CREATE USER, CREATE PROFILE TO PRY2205_EFT_DES;
-- Nota: "Create user/profile" es un permiso muy alto, se otorga porque la instrucción lo pide literalmente.

-- ROL_CON: Solo lectura (se asignarán los SELECT en el Bloque 2)
-- Por ahora no le damos permisos de creación, solo de conexión ya otorgado.

-- Permisos extra para el Dueño (EFT)
GRANT CREATE TABLE, CREATE VIEW, CREATE SEQUENCE, CREATE PROCEDURE, CREATE TRIGGER, CREATE SYNONYM, CREATE PUBLIC SYNONYM, CREATE ANY INDEX TO PRY2205_EFT;

-- 4. Asignación de Roles a Usuarios
GRANT PRY2205_ROL_D TO PRY2205_DES;
GRANT PRY2205_ROL_C TO PRY2205_CON;

/* ========================================================================== */
/* CONECTADO COMO: PRY2205_EFT                                                */
/* ========================================================================== */

-- 1. CREA ESQUEMA POBLADO

-- 2. Crear Sinónimos Públicos para enmascarar los nombres reales de las tablas
CREATE OR REPLACE PUBLIC SYNONYM SYN_PROFESIONAL FOR PRY2205_EFT.PROFESIONAL;
CREATE OR REPLACE PUBLIC SYNONYM SYN_PROFESION FOR PRY2205_EFT.PROFESION;
CREATE OR REPLACE PUBLIC SYNONYM SYN_ISAPRE FOR PRY2205_EFT.ISAPRE;
CREATE OR REPLACE PUBLIC SYNONYM SYN_TIPO_CONTRATO FOR PRY2205_EFT.TIPO_CONTRATO;
CREATE OR REPLACE PUBLIC SYNONYM SYN_RANGOS_SUELDOS FOR PRY2205_EFT.RANGOS_SUELDOS;
CREATE OR REPLACE PUBLIC SYNONYM SYN_CARTOLA FOR PRY2205_EFT.CARTOLA_PROFESIONALES;
CREATE OR REPLACE PUBLIC SYNONYM SYN_EMPRESA FOR PRY2205_EFT.EMPRESA;
CREATE OR REPLACE PUBLIC SYNONYM SYN_ASESORIA FOR PRY2205_EFT.ASESORIA;
CREATE OR REPLACE PUBLIC SYNONYM SYN_EMP_ASESORADAS FOR PRY2205_EFT.VW_EMPRESAS_ASESORADAS;

-- 3. Dar permisos a los Roles sobre las tablas originales (o vía sinónimos)
GRANT SELECT ON PROFESIONAL TO PRY2205_ROL_D;
GRANT SELECT ON PROFESION TO PRY2205_ROL_D;
GRANT SELECT ON ISAPRE TO PRY2205_ROL_D;
GRANT SELECT ON TIPO_CONTRATO TO PRY2205_ROL_D;
GRANT SELECT ON RANGOS_SUELDOS TO PRY2205_ROL_D;
GRANT SELECT, INSERT ON CARTOLA_PROFESIONALES TO PRY2205_ROL_D; -- El desarrollador debe insertar datos
GRANT SELECT ON EMPRESA TO PRY2205_ROL_D; -- Para el caso 3
GRANT SELECT ON ASESORIA TO PRY2205_ROL_D; -- Para el caso 3
GRANT SELECT, INSERT, DELETE ON CARTOLA_PROFESIONALES TO PRY2205_DES WITH GRANT OPTION;

-- 4. Limpia la vista para la re ejecucion
DROP VIEW VW_EMPRESAS_ASESORADAS;

-- 5. Realizar la vista
CREATE OR REPLACE VIEW VW_EMPRESAS_ASESORADAS AS
SELECT 
    -- 1. RUT EMPRESA
    e.RUT_EMPRESA || '-' || e.DV_EMPRESA AS "RUT EMPRESA",

    -- 2. NOMBRE EMPRESA
    e.NOMEMPRESA AS "NOMBRE EMPRESA",

    -- 3. IVA
    TO_CHAR(e.IVA_DECLARADO, '$999G999G999') AS "IVA",

    -- 4. ANIOS EXISTENCIA
    TRUNC(MONTHS_BETWEEN(SYSDATE, e.FECHA_INICIACION_ACTIVIDADES)/12) AS "ANIOS EXISTENCIA",

    -- 5. TOTAL ASESORIAS ANUALES (Cálculo Numérico del Promedio)
    ROUND(COUNT(a.IDEMPRESA)/12, 0) AS "TOTAL ASESORIAS ANUALES",

    -- 6. DEVOLUCION IVA (Cálculo Monetario)
    ROUND(e.IVA_DECLARADO * ((COUNT(a.IDEMPRESA)/12)/100), 0) AS "DEVOLUCION IVA",

    -- 7. TIPO CLIENTE (Clasificación)
    CASE 
        WHEN (COUNT(a.IDEMPRESA)/12) > 5 THEN 'CLIENTE PREMIUM'
        WHEN (COUNT(a.IDEMPRESA)/12) BETWEEN 3 AND 5 THEN 'CLIENTE'
        ELSE 'CLIENTE POCO CONCURRIDO'
    END AS "TIPO CLIENTE",

    -- 8. CORRESPONDE (Texto de la Promoción)
    CASE
        -- Lógica Premium (>5)
        WHEN (COUNT(a.IDEMPRESA)/12) > 5 THEN
            CASE 
                WHEN COUNT(a.IDEMPRESA) >= 7 THEN '1 ASESORIA GRATIS'
                ELSE '1 ASESORIA 40% DE DESCUENTO'
            END
        -- Lógica Cliente (3 a 5)
        WHEN (COUNT(a.IDEMPRESA)/12) BETWEEN 3 AND 5 THEN
            CASE
                WHEN COUNT(a.IDEMPRESA) = 5 THEN '1 ASESORIA 30% DE DESCUENTO'
                ELSE '1 ASESORIA 20% DE DESCUENTO'
            END
        -- Lógica Poco Concurrido (<3)
        ELSE 'CAPTAR CLIENTE'
    END AS "CORRESPONDE"

FROM SYN_EMPRESA e
JOIN SYN_ASESORIA a ON e.IDEMPRESA = a.IDEMPRESA

-- FILTRO DE FECHA: "Año anterior al de ejecución"
-- (Si hoy es 2025, busca 2024. Si la base solo tiene 2022, saldrá vacío y es correcto).
WHERE EXTRACT(YEAR FROM a.FIN) = EXTRACT(YEAR FROM SYSDATE) - 1

GROUP BY e.RUT_EMPRESA, e.DV_EMPRESA, e.NOMEMPRESA, e.IVA_DECLARADO, e.FECHA_INICIACION_ACTIVIDADES
ORDER BY e.NOMEMPRESA ASC;

-- Permiso para el Consultor
GRANT SELECT ON VW_EMPRESAS_ASESORADAS TO PRY2205_CON;

-- ==========================================================
-- CASO 3.2: OPTIMIZACIÓN (INDICES)
-- ==========================================================

-- Limpieza preventiva del índice (para re-ejecución)
DROP INDEX IDX_ASESORIA_FECHA_EMP;

-- Creación del Índice
CREATE INDEX IDX_ASESORIA_FECHA_EMP ON ASESORIA(FIN, IDEMPRESA);

/* ========================================================================== */
/* CONECTADO COMO: PRY2205_DES                                                */
/* ========================================================================== */

-- 1. Limpieza para la re ejecución
DELETE FROM SYN_CARTOLA;

-- 2. Crear informe
INSERT INTO SYN_CARTOLA (
    RUT_PROFESIONAL,
    NOMBRE_PROFESIONAL,
    PROFESION,
    ISAPRE,
    SUELDO_BASE,
    PORC_COMISION_PROFESIONAL,
    VALOR_TOTAL_COMISION,
    PORCENTATE_HONORARIO, -- (Nota: se mantiene el error ortográfico original de la tabla "PORCENTATE")
    BONO_MOVILIZACION,
    TOTAL_PAGAR
)
SELECT 
    p.RUTPROF,
    INITCAP(p.NOMPRO || ' ' || p.APPPRO || ' ' || p.APMPRO),
    pr.NOMPROFESION,
    i.NOMISAPRE,
    p.SUELDO,
    NVL(p.COMISION, 0),
    ROUND(p.SUELDO * NVL(p.COMISION, 0), 0),
    -- Cálculo Honorario: Sueldo * Porcentaje de tabla Rangos
    ROUND(p.SUELDO * (rs.HONOR_PCT / 100), 0),
    -- Cálculo Bono Movilización
    CASE p.IDTCONTRATO
        WHEN 1 THEN 150000 -- Indefinido Completa
        WHEN 2 THEN 120000 -- Indefinido Parcial
        WHEN 3 THEN 60000  -- Plazo Fijo
        WHEN 4 THEN 50000  -- Honorarios
        ELSE 0
    END,
    -- Total a Pagar
    ROUND(
        p.SUELDO + 
        (p.SUELDO * NVL(p.COMISION, 0)) + 
        (p.SUELDO * (rs.HONOR_PCT / 100)) +
        CASE p.IDTCONTRATO
            WHEN 1 THEN 150000
            WHEN 2 THEN 120000
            WHEN 3 THEN 60000
            WHEN 4 THEN 50000
            ELSE 0
        END
    , 0)
FROM SYN_PROFESIONAL p
JOIN SYN_PROFESION pr ON p.IDPROFESION = pr.IDPROFESION
JOIN SYN_ISAPRE i ON p.IDISAPRE = i.IDISAPRE
JOIN SYN_RANGOS_SUELDOS rs ON p.SUELDO BETWEEN rs.S_MIN AND rs.S_MAX
ORDER BY pr.NOMPROFESION, p.SUELDO DESC, p.COMISION, p.RUTPROF;

COMMIT;

-- 3. Otorgar permiso al consultor para que vea el reporte generado
GRANT SELECT ON SYN_CARTOLA TO PRY2205_CON;

/* ========================================================================== */
/* CONECTADO COMO: PRY2205_CON                                                */
/* ========================================================================== */

-- 1. Ver Informe de Remuneraciones (Caso 2)
SELECT * FROM PRY2205_EFT.CARTOLA_PROFESIONALES; 
-- 2. quí accedemos directo o mediante sinónimo público si existe:
SELECT * FROM SYN_CARTOLA;

-- 3. Ver Vista de Empresas Asesoradas (Caso 3)
SELECT * FROM PRY2205_EFT.VW_EMPRESAS_ASESORADAS;