/* ========================================================================== */
/*                           CONECTADO COMO ADMIN                             */
/* ========================================================================== */

-- 1.1 Creación de Usuarios
CREATE USER PRY2205_USER1 IDENTIFIED BY "PRY2204.semana_8"
DEFAULT TABLESPACE users QUOTA UNLIMITED ON users;

CREATE USER PRY2205_USER2 IDENTIFIED BY "PRY2204.semana_8"
DEFAULT TABLESPACE users QUOTA UNLIMITED ON users;

-- Permisos básicos de conexión
GRANT CREATE SESSION TO PRY2205_USER1;
GRANT CREATE SESSION TO PRY2205_USER2;

-- 1.2 Creación de Roles (Caso 1)
CREATE ROLE PRY2205_ROL_D; -- Rol para el Dueño
CREATE ROLE PRY2205_ROL_P; -- Rol para el Programador/User2

-- 1.3 Asignación de Privilegios a los Roles
-- PRY2205_ROL_D (Permisos para User1: Crear tablas, indices, vistas, sinónimos)
GRANT CREATE TABLE, CREATE VIEW, CREATE ANY INDEX, CREATE PUBLIC SYNONYM, CREATE SYNONYM, CREATE SEQUENCE, CREATE TRIGGER TO PRY2205_ROL_D;

-- PRY2205_ROL_P (Permisos para User2: Crear tablas, secuencias, triggers para sus reportes)
GRANT CREATE TABLE, CREATE SEQUENCE, CREATE TRIGGER TO PRY2205_ROL_P;

-- 1.4 Asignación de Roles a Usuarios
GRANT PRY2205_ROL_D TO PRY2205_USER1;
GRANT PRY2205_ROL_P TO PRY2205_USER2;

-- Definir roles por defecto
ALTER USER PRY2205_USER1 DEFAULT ROLE PRY2205_ROL_D;
ALTER USER PRY2205_USER2 DEFAULT ROLE PRY2205_ROL_P;

/* ========================================================================== */
/* CONECTADO COMO: PRY2205_USER1                                             */
/* ========================================================================== */

-- --------------------------------------------------------------------------
-- CASO 1: ESTRATEGIA DE SEGURIDAD
-- --------------------------------------------------------------------------
-- Crear Sinónimos Públicos para facilitar el acceso a User2
CREATE OR REPLACE PUBLIC SYNONYM SYN_LIBRO FOR PRY2205_USER1.LIBRO;
CREATE OR REPLACE PUBLIC SYNONYM SYN_EJEMPLAR FOR PRY2205_USER1.EJEMPLAR;
CREATE OR REPLACE PUBLIC SYNONYM SYN_PRESTAMO FOR PRY2205_USER1.PRESTAMO;
CREATE OR REPLACE PUBLIC SYNONYM SYN_ALUMNO FOR PRY2205_USER1.ALUMNO;
CREATE OR REPLACE PUBLIC SYNONYM SYN_EMPLEADO FOR PRY2205_USER1.EMPLEADO;
CREATE OR REPLACE PUBLIC SYNONYM SYN_CARRERA FOR PRY2205_USER1.CARRERA;
CREATE OR REPLACE PUBLIC SYNONYM SYN_REBAJA FOR PRY2205_USER1.REBAJA_MULTA;
CREATE OR REPLACE PUBLIC SYNONYM SYN_EDITORIAL FOR PRY2205_USER1.EDITORIAL;

-- Otorgar permisos de lectura al Rol del Programador (User2)
GRANT SELECT ON LIBRO TO PRY2205_ROL_P;
GRANT SELECT ON EJEMPLAR TO PRY2205_ROL_P;
GRANT SELECT ON PRESTAMO TO PRY2205_ROL_P;
GRANT SELECT ON ALUMNO TO PRY2205_ROL_P;
GRANT SELECT ON EMPLEADO TO PRY2205_ROL_P;
GRANT SELECT ON CARRERA TO PRY2205_ROL_P;
GRANT SELECT ON REBAJA_MULTA TO PRY2205_ROL_P;
GRANT SELECT ON EDITORIAL TO PRY2205_ROL_P;

-- --------------------------------------------------------------------------
-- CASO 3.1: CREACIÓN DE VISTA (VW_DETALLE_MULTAS)
-- --------------------------------------------------------------------------

-- 0. Limpieza de datos para re ejecución
DROP INDEX IDX_PRESTAMO_ANIO_TERM;
DROP INDEX IDX_PRESTAMO_FECHAS;
DROP INDEX IDX_ALUMNO_CARRERA_FK;
DROP INDEX IDX_PRESTAMO_ALUMNO_FK;

-- 1. Crear la vista
CREATE OR REPLACE VIEW VW_DETALLE_MULTAS AS
SELECT 
    p.prestamoid AS "ID PRESTAMO",
    a.nombre || ' ' || a.apaterno AS "NOMBRE ALUMNO",
    c.descripcion AS "NOMBRE CARRERA",
    l.libroid AS "ID LIBRO",
    TO_CHAR(l.precio, '$999G999G999') AS "PRECIO LIBRO",
    TO_CHAR(p.fecha_termino, 'DD/MM/YYYY') AS "FECHA TERMINO",
    TO_CHAR(p.fecha_entrega, 'DD/MM/YYYY') AS "FECHA ENTREGA",
    (p.fecha_entrega - p.fecha_termino) AS "DIAS ATRASO",
    -- CAMBIO 3: Formato de dinero en la Multa
    TO_CHAR(
        ROUND(l.precio * 0.03 * (p.fecha_entrega - p.fecha_termino)), 
        '$999G999G999'
    ) AS "VALOR MULTA",
    -- Porcentaje de rebaja
    NVL(r.porc_rebaja_multa, 0) / 100 AS "PORCENTAJE REBAJA",
    -- CAMBIO 4: Formato de dinero en Valor Rebajado
    TO_CHAR(
        ROUND(
            (l.precio * 0.03 * (p.fecha_entrega - p.fecha_termino)) - 
            ((l.precio * 0.03 * (p.fecha_entrega - p.fecha_termino)) * (NVL(r.porc_rebaja_multa, 0) / 100))
        ), 
        '$999G999G999'
    ) AS "VALOR REBAJADO"
FROM 
    SYN_PRESTAMO p
    JOIN SYN_ALUMNO a ON p.alumnoid = a.alumnoid
    JOIN SYN_CARRERA c ON a.carreraid = c.carreraid
    JOIN SYN_EJEMPLAR e ON p.libroid = e.libroid AND p.ejemplarid = e.ejemplarid
    JOIN SYN_LIBRO l ON e.libroid = l.libroid
    LEFT JOIN SYN_REBAJA r ON c.carreraid = r.carreraid
WHERE 
    EXTRACT(YEAR FROM p.fecha_termino) = EXTRACT(YEAR FROM SYSDATE) - 2
    AND p.fecha_entrega > p.fecha_termino
ORDER BY 
    p.fecha_entrega DESC;

-- --------------------------------------------------------------------------
-- CASO 3.2: OPTIMIZACIÓN (Creación de Índices)
-- --------------------------------------------------------------------------
-- Índice basado en función para mejorar el filtro de año
CREATE INDEX IDX_PRESTAMO_ANIO_TERM ON PRESTAMO(EXTRACT(YEAR FROM fecha_termino));

-- Índice compuesto para mejorar el filtro de fechas y cálculo de atraso
CREATE INDEX IDX_PRESTAMO_FECHAS ON PRESTAMO(fecha_entrega, fecha_termino);

-- Índices para mejorar los JOINs
CREATE INDEX IDX_ALUMNO_CARRERA_FK ON ALUMNO(carreraid);
CREATE INDEX IDX_PRESTAMO_ALUMNO_FK ON PRESTAMO(alumnoid);

-- --------------------------------------------------------------------------
-- VERIFICACIÓN: VISUALIZAR LA VISTA
-- --------------------------------------------------------------------------
SELECT * FROM VW_DETALLE_MULTAS;

/* ========================================================================== */
/* CONECTADO COMO: PRY2205_USER2                                             */
/* ========================================================================== */

--------------------------------------------------------------------------
-- CASO 2: CREACIÓN DE INFORME (CONTROL_STOCK_LIBROS)
--------------------------------------------------------------------------

-- 0. Limpieza de datos para re ejecución
DROP TABLE CONTROL_STOCK_LIBROS PURGE;
DROP SEQUENCE SEQ_CONTROL_STOCK;

-- 1. Crear la secuencia
CREATE SEQUENCE SEQ_CONTROL_STOCK
START WITH 1
INCREMENT BY 1;

-- 2. Crear la tabla del informe
CREATE TABLE CONTROL_STOCK_LIBROS (
    ID_CONTROL NUMBER PRIMARY KEY,
    LIBRO_ID NUMBER,
    NOMBRE_LIBRO VARCHAR2(100),
    TOTAL_EJEMPLARES NUMBER,
    EN_PRESTAMO NUMBER,
    DISPONIBLES NUMBER,
    PORCENTAJE_PRESTAMO VARCHAR2(20),
    STOCK_CRITICO VARCHAR2(1)
);

-- 3. Insertar datos
INSERT INTO CONTROL_STOCK_LIBROS (
    ID_CONTROL, LIBRO_ID, NOMBRE_LIBRO, TOTAL_EJEMPLARES, 
    EN_PRESTAMO, DISPONIBLES, PORCENTAJE_PRESTAMO, STOCK_CRITICO
)
SELECT 
    SEQ_CONTROL_STOCK.NEXTVAL,
    TEMP.*
FROM (
    SELECT 
        LIB.LIBROID,
        LIB.NOMBRE_LIBRO,
        COUNT(EJE.EJEMPLARID) AS TOTAL_EJEMPLARES,
        -- Subconsulta: Libros prestados hace 2 años por los empleados indicados
        (
            SELECT COUNT(*)
            FROM SYN_PRESTAMO P
            WHERE P.LIBROID = LIB.LIBROID
              AND EXTRACT(YEAR FROM P.FECHA_INICIO) = EXTRACT(YEAR FROM SYSDATE) - 2
              AND P.EMPLEADOID IN (190, 180, 150)
        ) AS EN_PRESTAMO,
        -- Cálculo: Disponibles = Total - Prestados
        (
            COUNT(EJE.EJEMPLARID) - 
            (
                SELECT COUNT(*)
                FROM SYN_PRESTAMO P
                WHERE P.LIBROID = LIB.LIBROID
                  AND EXTRACT(YEAR FROM P.FECHA_INICIO) = EXTRACT(YEAR FROM SYSDATE) - 2
                  AND P.EMPLEADOID IN (190, 180, 150)
            )
        ) AS DISPONIBLES,
        -- Cálculo Porcentaje: (Prestados / Total) * 100
        TO_CHAR(ROUND(
            (
                (
                    SELECT COUNT(*)
                    FROM SYN_PRESTAMO P
                    WHERE P.LIBROID = LIB.LIBROID
                      AND EXTRACT(YEAR FROM P.FECHA_INICIO) = EXTRACT(YEAR FROM SYSDATE) - 2
                      AND P.EMPLEADOID IN (190, 180, 150)
                ) / COUNT(EJE.EJEMPLARID)
            ) * 100, 2
        )) || '%' AS PORCENTAJE_PRESTAMO,
        -- Lógica Stock Crítico: Si Disponibles > 2 es 'S', sino 'N'
        CASE 
            WHEN (
                COUNT(EJE.EJEMPLARID) - 
                (
                    SELECT COUNT(*)
                    FROM SYN_PRESTAMO P
                    WHERE P.LIBROID = LIB.LIBROID
                      AND EXTRACT(YEAR FROM P.FECHA_INICIO) = EXTRACT(YEAR FROM SYSDATE) - 2
                      AND P.EMPLEADOID IN (190, 180, 150)
                )
            ) > 2 THEN 'S'
            ELSE 'N'
        END AS STOCK_CRITICO
    FROM 
        SYN_LIBRO LIB
        JOIN SYN_EJEMPLAR EJE ON LIB.LIBROID = EJE.LIBROID
    GROUP BY 
        LIB.LIBROID, LIB.NOMBRE_LIBRO
    -- Muestra libros que tuvieron movimiento bajo esas condiciones
    HAVING 
        (
            SELECT COUNT(*)
            FROM SYN_PRESTAMO P
            WHERE P.LIBROID = LIB.LIBROID
              AND EXTRACT(YEAR FROM P.FECHA_INICIO) = EXTRACT(YEAR FROM SYSDATE) - 2
              AND P.EMPLEADOID IN (190, 180, 150)
        ) > 0
    ORDER BY 
        LIB.LIBROID
) TEMP;

COMMIT;

-- Verificación final
SELECT * FROM CONTROL_STOCK_LIBROS;