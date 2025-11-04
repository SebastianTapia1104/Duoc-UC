-- ========================================================================
-- I. VARIABLES DE SUSTITUCIÓN (REQUERIDO PARA CASO 3 PARAMÉTRICO)
-- USAR F5
-- ========================================================================
ACCEPT TIPOCAMBIO_DOLAR PROMPT 'Ingrese el Tipo de Cambio Dólar (Ej. 950): '
ACCEPT UMBRAL_BAJO PROMPT 'Ingrese el Umbral de Stock Bajo (Ej. 40): '
ACCEPT UMBRAL_ALTO PROMPT 'Ingrese el Umbral de Stock Alto (Ej. 60): '

-- ========================================================================
-- CASO 1: ANÁLISIS DE FACTURAS
-- Requerimiento: Filtro por año anterior, Formatos, Clasificación y Ordenamiento.
-- ========================================================================
SELECT 
    f.NUMFACTURA AS "N Factura",
    TO_CHAR(f.FECHA, 'DD "de" Month YYYY', 'NLS_DATE_LANGUAGE=SPANISH') AS "Fecha Emisión",
    LPAD(f.RUTCLIENTE, 10, '0') AS "RUT Cliente",
    TO_CHAR(f.NETO, 'FM99G999G999', 'NLS_NUMERIC_CHARACTERS = ''.,''') AS "Monto Neto",
    TO_CHAR(f.IVA, 'FM99G999G999', 'NLS_NUMERIC_CHARACTERS = ''.,''') AS "Monto Iva",
    TO_CHAR(f.TOTAL, 'FM99G999G999', 'NLS_NUMERIC_CHARACTERS = ''.,''') AS "Total Factura",
    CASE
        WHEN f.TOTAL BETWEEN 0 AND 50000 THEN 'Bajo'
        WHEN f.TOTAL BETWEEN 50001 AND 100000 THEN 'Medio'
        ELSE 'Alto'
    END AS "Categoria Monto",
    CASE f.CODPAGO
        WHEN 1 THEN 'EFECTIVO'
        WHEN 2 THEN 'TARJETA DEBITO'
        WHEN 3 THEN 'TARJETA CREDITO'
        ELSE 'CHEQUE'
    END AS "Forma de pago"
FROM 
    FACTURA f
WHERE 
    EXTRACT(YEAR FROM f.FECHA) = EXTRACT(YEAR FROM SYSDATE) - 1
ORDER BY 
    f.FECHA DESC, f.NETO DESC;

-- ========================================================================
-- CASO 2: CLASIFICACIÓN DE CLIENTES
-- Requerimiento: Formatos de RUT, Manejo de Nulos, Categorización de Crédito y Filtro.
-- ========================================================================
SELECT
    RPAD(c.RUTCLIENTE, 10, '*') AS "RUT Cliente",
    c.NOMBRE AS "Cliente",
    NVL(TO_CHAR(c.TELEFONO), 'Sin teléfono') AS "TELÉFONO",
    NVL(TO_CHAR(c.CODCOMUNA), 'Sin comuna') AS "COMUNA",
    c.ESTADO AS "ESTADO",
    CASE
        -- Bueno
        WHEN ROUND((c.SALDO / c.CREDITO) * 100) < 50 THEN 
            'Bueno (' || '$' || TO_CHAR(c.CREDITO - c.SALDO, 'FM9G999G999', 'NLS_NUMERIC_CHARACTERS = ''.,''') || ')'
        -- Regular
        WHEN ROUND((c.SALDO / c.CREDITO) * 100) BETWEEN 50 AND 80 THEN 
            'Regular (' || '$' || TO_CHAR(c.SALDO, 'FM9G999G999', 'NLS_NUMERIC_CHARACTERS = ''.,''') || ')'
        -- Crítico
        ELSE 'Critico'
    END AS "Estado Crédito",
    NVL(SUBSTR(c.MAIL, INSTR(c.MAIL, '@') + 1), 'Correo no registrado') AS "Dominio Correo"
FROM 
    CLIENTE c
WHERE 
    c.ESTADO = 'A'
    AND c.CREDITO > 0
ORDER BY 
    c.NOMBRE ASC;

-- ========================================================================
-- CASO 3: STOCK DE PRODUCTOS
-- Requerimiento: Filtro, Conversión con variables, Alertas de Stock y Formatos.
-- ========================================================================
SELECT 
    p.CODPRODUCTO AS "ID",
    p.DESCRIPCION AS "Descripción de Producto",
    
    -- Manejo de NULL para 'Compra en USD'
    NVL2(
        p.VALORCOMPRADOLAR,
        TO_CHAR(p.VALORCOMPRADOLAR) || ' USD',
        'Sin registro'
    ) AS "Compra en USD",

    -- Conversión a CLP usando &TIPOCAMBIO_DOLAR
    NVL2(
        p.VALORCOMPRADOLAR,
        '$' || TO_CHAR(ROUND(p.VALORCOMPRADOLAR * &TIPOCAMBIO_DOLAR), 'FM9G999G999', 'NLS_NUMERIC_CHARACTERS = ''.,''') || ' PESOS',
        'Sin registro'
    ) AS "USD convertida",

    p.TOTALSTOCK AS "Stock",

    -- Alertas de Stock usando &UMBRAL_BAJO y &UMBRAL_ALTO
    CASE
        WHEN p.TOTALSTOCK IS NULL THEN 'Sin datos'
        WHEN p.TOTALSTOCK < &UMBRAL_BAJO THEN 'ALERTA stock muy bajo!'
        WHEN p.TOTALSTOCK BETWEEN &UMBRAL_BAJO AND &UMBRAL_ALTO THEN 'Reabastecer pronto!'
        ELSE 'OK'
    END AS "Alerta Stock",

    -- Precio Oferta
    CASE
        WHEN p.TOTALSTOCK > 80 THEN 
            '$' || TO_CHAR(ROUND(p.VUNITARIO * 0.9), 'FM99G999', 'NLS_NUMERIC_CHARACTERS = ''.,''')
        ELSE 'N/A'
    END AS "Precio Oferta"

FROM 
    PRODUCTO p
WHERE 
    UPPER(p.DESCRIPCION) LIKE '%ZAPATO%'
    AND p.PROCEDENCIA = 'I'
ORDER BY 
    p.CODPRODUCTO DESC;