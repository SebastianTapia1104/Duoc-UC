-- ========================================================================
-- CASO 1: REPORTERÍA DE ASESORÍAS (BANCA Y RETAIL)
-- Lógica: Identificar profesionales que trabajaron en Banca y Retail.
-- ========================================================================

SELECT 
    t1.id_profesional AS "ID",
    INITCAP(t2.appaterno) || ' ' || INITCAP(t2.apmaterno) || ' ' || INITCAP(t2.nombre) AS "PROFESIONAL",
    ROUND(SUM(t1.nro_asesoria_banca)) AS "NRO ASESORIA BANCA",
    TO_CHAR(ROUND(SUM(t1.monto_total_banca)), 'fm$999g999g999') AS "MONTO_TOTAL_BANCA",
    ROUND(SUM(t1.nro_asesoria_retail)) AS "NRO ASESORIA RETAIL",
    TO_CHAR(ROUND(SUM(t1.monto_total_retail)), 'fm$999g999g999') AS "MONTO_TOTAL_RETAIL",
    ROUND(SUM(t1.nro_asesoria_banca) + SUM(t1.nro_asesoria_retail)) AS "TOTAL ASESORIAS",
    TO_CHAR(ROUND(SUM(t1.monto_total_banca) + SUM(t1.monto_total_retail)), 'fm$999g999g999') AS "TOTAL HONORARIOS"
FROM
(
    -- Subconsulta Banca
    SELECT
        a.id_profesional,
        COUNT(a.honorario) AS nro_asesoria_banca,
        SUM(a.honorario) AS monto_total_banca,
        0 AS nro_asesoria_retail,
        0 AS monto_total_retail
    FROM asesoria a
    JOIN empresa e ON a.cod_empresa = e.cod_empresa
    WHERE e.cod_sector = 3 AND a.fin_asesoria < TRUNC(SYSDATE, 'MM')
    GROUP BY a.id_profesional
    HAVING COUNT(a.honorario) > 0

    UNION ALL

    -- Subconsulta Retail
    SELECT
        a.id_profesional,
        0 AS nro_asesoria_banca,
        0 AS monto_total_banca,
        COUNT(a.honorario) AS nro_asesoria_retail,
        SUM(a.honorario) AS monto_total_retail
    FROM asesoria a
    JOIN empresa e ON a.cod_empresa = e.cod_empresa
    WHERE e.cod_sector = 4 AND a.fin_asesoria < TRUNC(SYSDATE, 'MM')
    GROUP BY a.id_profesional
    HAVING COUNT(a.honorario) > 0
) t1
JOIN profesional t2 ON t1.id_profesional = t2.id_profesional
GROUP BY
    t1.id_profesional,
    INITCAP(t2.appaterno) || ' ' || INITCAP(t2.apmaterno) || ' ' || INITCAP(t2.nombre)
HAVING
    SUM(t1.nro_asesoria_banca) > 0 AND SUM(t1.nro_asesoria_retail) > 0
ORDER BY
    t1.id_profesional ASC;

-- ========================================================================
-- CASO 2: RESUMEN DE HONORARIOS (TABLA REPORTE_MES)
-- Lógica: Creación y poblado de tabla con asesorías de Abril del año pasado.
-- ========================================================================

-- 1. Eliminar tabla anterior para evitar errores
DROP TABLE REPORTE_MES;

-- 2. Crear tabla con los encabezados exactos de la instrucción
CREATE TABLE REPORTE_MES (
    "ID PROF"                NUMBER(10),
    "NOMBRE COMPLETO"        VARCHAR2(100),
    "NOMBRE PROFESION"       VARCHAR2(50),
    "NOM COMUNA"             VARCHAR2(50),
    "NRO ASESORIAS"          NUMBER(5),
    "MONTO TOTAL HONORARIOS" NUMBER(10),
    "PROMEDIO HONORARIO"     NUMBER(10),
    "HONORARIO MINIMO"       NUMBER(10),
    "HONORARIO MAXIMO"       NUMBER(10)
);

-- 3. Poblar la tabla (Abril año pasado)
INSERT INTO REPORTE_MES (
    "ID PROF", 
    "NOMBRE COMPLETO", 
    "NOMBRE PROFESION", 
    "NOM COMUNA",
    "NRO ASESORIAS", 
    "MONTO TOTAL HONORARIOS", 
    "PROMEDIO HONORARIO",
    "HONORARIO MINIMO", 
    "HONORARIO MAXIMO"
)
SELECT 
    p.id_profesional AS "ID PROF",
    INITCAP(p.appaterno) || ' ' || INITCAP(p.apmaterno) || ' ' || INITCAP(p.nombre) AS "NOMBRE COMPLETO",
    pr.nombre_profesion AS "NOMBRE PROFESION",
    c.nom_comuna AS "NOM COMUNA",
    COUNT(a.honorario) AS "NRO ASESORIAS",
    ROUND(SUM(a.honorario)) AS "MONTO TOTAL HONORARIOS",
    ROUND(AVG(a.honorario)) AS "PROMEDIO HONORARIO",
    ROUND(MIN(a.honorario)) AS "HONORARIO MINIMO",
    ROUND(MAX(a.honorario)) AS "HONORARIO MAXIMO"
FROM asesoria a
JOIN profesional p ON a.id_profesional = p.id_profesional
JOIN profesion pr ON p.cod_profesion = pr.cod_profesion
JOIN comuna c ON p.cod_comuna = c.cod_comuna
WHERE EXTRACT(MONTH FROM a.fin_asesoria) = 4 
  AND EXTRACT(YEAR FROM a.fin_asesoria) = EXTRACT(YEAR FROM SYSDATE) - 1
GROUP BY 
    p.id_profesional,
    INITCAP(p.appaterno) || ' ' || INITCAP(p.apmaterno) || ' ' || INITCAP(p.nombre),
    pr.nombre_profesion,
    c.nom_comuna
ORDER BY p.id_profesional;

-- 4. Validar resultado
COMMIT;
SELECT * FROM REPORTE_MES;

-- ==================================================================================================
-- CASO 3: MODIFICACIÓN DE HONORARIOS (AUMENTO DE SUELDO)
-- Lógica: Actualización de SUELDO en tabla PROFESIONAL basado en honorarios de Marzo del año pasado
-- ==================================================================================================

-- 1. Limpieza de transacciones previas
COMMIT;

-- 2. Reporte ANTES de la modificación
SELECT 
    ROW_NUMBER() OVER (ORDER BY p.id_profesional) AS "HONORARIOID",
    ROUND(SUM(a.honorario)) AS "HONORARIO",
    p.id_profesional AS "PROFESIONAL",
    p.numrun_prof AS "NUMRUN_PROF",
    p.sueldo AS "SUELDO"
FROM asesoria a
JOIN profesional p ON a.id_profesional = p.id_profesional
WHERE EXTRACT(MONTH FROM a.fin_asesoria) = 3 
  AND EXTRACT(YEAR FROM a.fin_asesoria) = EXTRACT(YEAR FROM SYSDATE) - 1
GROUP BY p.id_profesional, p.numrun_prof, p.sueldo
ORDER BY p.id_profesional;

-- 3. Actualización (UPDATE)
UPDATE profesional p
SET sueldo = ROUND(p.sueldo * (
    1 + (
        SELECT 
            CASE 
                -- Honorarios >= 1.000.000 -> +15%
                WHEN SUM(a2.honorario) >= 1000000 THEN 0.15
                -- Honorarios < 1.000.000 -> +10%
                ELSE 0.10
            END
        FROM asesoria a2
        WHERE a2.id_profesional = p.id_profesional
          AND EXTRACT(MONTH FROM a2.fin_asesoria) = 3
          AND EXTRACT(YEAR FROM a2.fin_asesoria) = EXTRACT(YEAR FROM SYSDATE) - 1
    )
))
WHERE EXISTS (
    SELECT 1 FROM asesoria a3
    WHERE a3.id_profesional = p.id_profesional
      AND EXTRACT(MONTH FROM a3.fin_asesoria) = 3
      AND EXTRACT(YEAR FROM a3.fin_asesoria) = EXTRACT(YEAR FROM SYSDATE) - 1
);

-- 4. Confirmación obligatoria para ver cambios en el siguiente reporte
COMMIT;

-- 5. Reporte DESPUÉS de la modificación
SELECT 
    ROW_NUMBER() OVER (ORDER BY p.id_profesional) AS "HONORARIOID",
    ROUND(SUM(a.honorario)) AS "HONORARIO",
    p.id_profesional AS "PROFESIONAL",
    p.numrun_prof AS "NUMRUN_PROF",
    p.sueldo AS "SUELDO"
FROM asesoria a
JOIN profesional p ON a.id_profesional = p.id_profesional
WHERE EXTRACT(MONTH FROM a.fin_asesoria) = 3 
  AND EXTRACT(YEAR FROM a.fin_asesoria) = EXTRACT(YEAR FROM SYSDATE) - 1
GROUP BY p.id_profesional, p.numrun_prof, p.sueldo
ORDER BY p.id_profesional;