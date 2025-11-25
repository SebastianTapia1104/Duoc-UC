-- ========================================================================
-- PREPARACIÓN: ELIMINACIÓN DE TABLA PARA CASO 2
-- ========================================================================
DROP TABLE CLIENTES_CUPOS_COMPRA;

-- ========================================================================
-- CASO 1: LISTADO DE CLIENTES CON RANGO DE RENTA (PARAMÉTRICO)
-- Lógica: Tipo Cliente Dependiente, Profesión Contador/Vendedor, Año Inscripción > Promedio.
-- ========================================================================

SELECT
    TO_CHAR(c.numrun, 'FM99G999G999') || '-' || c.dvrun AS "RUT Cliente",
    INITCAP(c.pnombre) || ' ' || INITCAP(c.appaterno) AS "Nombre Cliente",
    UPPER(po.nombre_prof_ofic) AS "Profesión Cliente",
    TO_CHAR(c.fecha_inscripcion, 'DD-MM-YYYY') AS "Fecha de Inscripción",
    INITCAP(c.direccion) AS "Dirección Cliente"
FROM
    CLIENTE c
JOIN
    PROFESION_OFICIO po ON c.cod_prof_ofic = po.cod_prof_ofic
WHERE
    c.cod_tipo_cliente = 10
    AND 
    UPPER(po.nombre_prof_ofic) IN ('CONTADOR', 'VENDEDOR')
    AND
    EXTRACT(YEAR FROM c.fecha_inscripcion) > (
        SELECT
            ROUND(AVG(EXTRACT(YEAR FROM fecha_inscripcion)))
        FROM
            CLIENTE
    )
ORDER BY
    c.numrun ASC;

-- ========================================================================
-- CASO 2: AUMENTO DE CRÉDITO
-- Requerimiento: Crear tabla CLIENTES_CUPOS_COMPRA con clientes
-- cuyo cupo disponible es superior o igual al máximo cupo disponible del año anterior al actual (Subconsulta).
-- CORRECCIÓN: RUT sin puntos.
-- ========================================================================

CREATE TABLE CLIENTES_CUPOS_COMPRA AS
SELECT
    TO_CHAR(c.numrun, 'FM9999999999') || '-' || c.dvrun AS RUT_CLIENTE,
    ROUND(MONTHS_BETWEEN(SYSDATE, c.fecha_nacimiento) / 12) AS EDAD,
    TO_CHAR(tc.cupo_disp_compra, 'L9G999G999', 'NLS_CURRENCY=''$''') AS CUPO_DISPONIBLE_COMPRA,
    UPPER(tpc.nombre_tipo_cliente) AS TIPO_CLIENTE
FROM
    CLIENTE c
JOIN
    TARJETA_CLIENTE tc ON c.numrun = tc.numrun
JOIN
    TIPO_CLIENTE tpc ON c.cod_tipo_cliente = tpc.cod_tipo_cliente
WHERE
    tc.cupo_disp_compra >= (
        SELECT
            MAX(cupo_disp_compra)
        FROM
            TARJETA_CLIENTE
        WHERE
            EXTRACT(YEAR FROM fecha_solic_tarjeta) = EXTRACT(YEAR FROM SYSDATE) - 1
    )
ORDER BY
    EDAD ASC;

-- Consulta para mostrar el resultado del Caso 2.
SELECT * FROM CLIENTES_CUPOS_COMPRA;