-- ========================================================================
-- CASO 1: LISTADO DE CLIENTES CON RANGO DE RENTA (PARAMÉTRICO)
-- Requerimiento: Filtro por Renta Min/Max (Variables de Sustitución), 
-- Formatos (RUT, Renta, Celular corregido), Clasificación y Ordenamiento.
-- ========================================================================
SELECT
    TO_CHAR(c.numrut_cli, '99G999G999')
        || '-'
        || c.dvrut_cli AS "RUT Cliente",
    INITCAP(c.nombre_cli)
        || ' '
        || INITCAP(c.appaterno_cli)
        || ' '
        || INITCAP(c.apmaterno_cli) AS "Nombre Completo Cliente",
    c.direccion_cli AS "Dirección Cliente",
    TO_CHAR(c.renta_cli, 'L9G999G999', 'NLS_NUMERIC_CHARACTERS = ''.,'' NLS_CURRENCY = ''$''') AS "Renta Cliente",
    SUBSTR(LPAD(TO_CHAR(c.celular_cli), 9, '0'), 1, 1)
        || '-'
        || SUBSTR(LPAD(TO_CHAR(c.celular_cli), 9, '0'), 2, 3)
        || '-'
        || SUBSTR(LPAD(TO_CHAR(c.celular_cli), 9, '0'), 5, 4) AS "Celular Cliente",
    CASE
        WHEN c.renta_cli > 500000 THEN 'TRAMO 1'
        WHEN c.renta_cli BETWEEN 400000 AND 500000 THEN 'TRAMO 2'
        WHEN c.renta_cli BETWEEN 200000 AND 399999 THEN 'TRAMO 3'
        ELSE 'TRAMO 4'
    END AS "Trama Renta Cliente"
FROM
    cliente c
WHERE
        c.renta_cli BETWEEN &RENTA_MINIMA AND &RENTA_MAXIMA -- Variables de sustitución
    AND c.celular_cli IS NOT NULL
ORDER BY
    "Nombre Completo Cliente" ASC;


-- ==================================================================================
-- CASO 2: SUELDO PROMEDIO POR CATEGORÍA DE EMPLEADO
-- Requerimiento: Filtro por Sueldo Promedio Mínimo (Variable de Sustitución), 
-- Agrupación por Categoría/Sucursal, Formato Sueldo y Ordenamiento.
-- ==================================================================================
SELECT
    ce.id_categoria_emp AS "CODIGO_CATEGORIA",
    ce.desc_categoria_emp AS "DESCRIPCION_CATEGORIA",
    COUNT(e.numrut_emp)
        || ' '
        || s.desc_sucursal AS "CANTIDAD EMPLEADOS SUCURSAL",
    -- Redondea el promedio a entero y aplica formato de moneda
    TO_CHAR(ROUND(AVG(e.sueldo_emp)), 'L9G999G999', 'NLS_NUMERIC_CHARACTERS = ''.,'' NLS_CURRENCY = ''$''') AS "SUELDO PROMEDIO"
FROM
    empleado e
    JOIN categoria_empleado ce ON e.id_categoria_emp = ce.id_categoria_emp
    JOIN sucursal s ON e.id_sucursal = s.id_sucursal
GROUP BY
    ce.id_categoria_emp,
    ce.desc_categoria_emp,
    s.desc_sucursal
HAVING
    AVG(e.sueldo_emp) > &SUELDO_PROMEDIO_MINIMO -- Variable de sustitución
ORDER BY
    AVG(e.sueldo_emp) DESC;


-- ==============================================================================================
-- CASO 3: ARRIENDO PROMEDIO POR TIPO DE PROPIEDAD
-- Requerimiento: Indicadores (Total, Promedios), Razón Arriendo/m2, Clasificación de tramos, 
-- Restricción de Grupos, Formatos (corregidos para alineación y desbordamiento).
-- ==============================================================================================
SELECT
    tp.id_tipo_propiedad
        || ' '
        || tp.desc_tipo_propiedad AS "CODIGO TIPO DESCRIPCION TIPO",
    COUNT(p.nro_propiedad) AS "TOTAL PROPIEDADES",
    TO_CHAR(ROUND(AVG(p.valor_arriendo)), 'L9G999G999', 'NLS_NUMERIC_CHARACTERS = ''.,'' NLS_CURRENCY = ''$''') AS "PROMEDIO_ARRIENDO",
    TO_CHAR(ROUND(AVG(p.superficie), 2), '999.00') AS "PROMEDIO SUPERFICIE", 
    TO_CHAR(ROUND(AVG(p.valor_arriendo / p.superficie)), 'L999G999', 'NLS_NUMERIC_CHARACTERS = ''.,'' NLS_CURRENCY = ''$''')
        || ' '
        || CASE
            WHEN ROUND(AVG(p.valor_arriendo / p.superficie)) < 5000 THEN 'Económico'
            WHEN ROUND(AVG(p.valor_arriendo / p.superficie)) BETWEEN 5000 AND 10000 THEN 'Medio'
            ELSE 'Alto'
        END AS "VALOR ARRIENDO M2 CLASIFICACION"
FROM
    propiedad p
    JOIN tipo_propiedad tp ON p.id_tipo_propiedad = tp.id_tipo_propiedad
GROUP BY
    tp.id_tipo_propiedad,
    tp.desc_tipo_propiedad
HAVING
    AVG(p.valor_arriendo / p.superficie) > 1000
ORDER BY
    ROUND(AVG(p.valor_arriendo / p.superficie)) DESC;