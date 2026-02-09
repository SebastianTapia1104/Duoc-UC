/******************************************************************************
 * CASO 1: CÁLCULO DE PUNTOS CÍRCULO ALL THE BEST
 * Procesa transacciones del año anterior, calcula puntos base
   y extras, y pobla tablas de detalle y resumen.
 ******************************************************************************/

-- 1. Definición de Variables Bind para los tramos (Paramétricas)
VARIABLE b_rango1_min NUMBER;
VARIABLE b_rango1_max NUMBER;
VARIABLE b_rango2_min NUMBER;
VARIABLE b_rango2_max NUMBER;
VARIABLE b_rango3_min NUMBER;

-- Inicialización de tramos (Ejemplo: 500k-700k, 700k-900k, >900k)
EXEC :b_rango1_min := 500000;
EXEC :b_rango1_max := 700000;
EXEC :b_rango2_min := 700001;
EXEC :b_rango2_max := 900000;
EXEC :b_rango3_min := 900001;

DECLARE
    -- Definición del VARRAY para puntos (Normal, Extra1, Extra2, Extra3)
    TYPE t_arr_puntos IS VARRAY(4) OF NUMBER;
    v_puntos_arr t_arr_puntos := t_arr_puntos(250, 300, 550, 700);

    -- Registro PL/SQL para almacenar datos de la transacción [cite: 881]
    TYPE r_transaccion IS RECORD (
        numrun          CLIENTE.numrun%TYPE,
        dvrun           CLIENTE.dvrun%TYPE,
        nro_tarjeta     TARJETA_CLIENTE.nro_tarjeta%TYPE,
        nro_transaccion TRANSACCION_TARJETA_CLIENTE.nro_transaccion%TYPE,
        fecha_trans     TRANSACCION_TARJETA_CLIENTE.fecha_transaccion%TYPE,
        monto_trans     TRANSACCION_TARJETA_CLIENTE.monto_transaccion%TYPE,
        cod_tptran      TRANSACCION_TARJETA_CLIENTE.cod_tptran_tarjeta%TYPE,
        nom_tptran      TIPO_TRANSACCION_TARJETA.nombre_tptran_tarjeta%TYPE,
        cod_tp_cliente  CLIENTE.cod_tipo_cliente%TYPE
    );
    v_reg_trans r_transaccion;

    -- Variable de Cursor (REF CURSOR) para el bucle principal (Meses del año anterior)
    TYPE t_ref_cursor IS REF CURSOR;
    c_meses t_ref_cursor;
    v_mes_anno VARCHAR2(6);
    
    -- Cursor Explícito CON PARÁMETRO para las transacciones del mes
    CURSOR c_detalle_trans (p_mes_anno VARCHAR2) IS
        SELECT 
            c.numrun,
            c.dvrun,
            tc.nro_tarjeta,
            ttc.nro_transaccion,
            ttc.fecha_transaccion,
            ttc.monto_transaccion,
            ttc.cod_tptran_tarjeta,
            ttt.nombre_tptran_tarjeta,
            c.cod_tipo_cliente
        FROM TRANSACCION_TARJETA_CLIENTE ttc
        JOIN TARJETA_CLIENTE tc ON ttc.nro_tarjeta = tc.nro_tarjeta
        JOIN CLIENTE c ON tc.numrun = c.numrun
        JOIN TIPO_TRANSACCION_TARJETA ttt ON ttc.cod_tptran_tarjeta = ttt.cod_tptran_tarjeta
        WHERE TO_CHAR(ttc.fecha_transaccion, 'MMYYYY') = p_mes_anno
        ORDER BY ttc.fecha_transaccion, c.numrun, ttc.nro_transaccion; -- Ordenamiento solicitado

    -- Variables para cálculos y acumuladores
    v_anno_anterior NUMBER;
    v_puntos_base   NUMBER;
    v_puntos_extra  NUMBER;
    v_puntos_total  NUMBER;
    
    -- Acumuladores para la tabla RESUMEN
    v_tot_monto_compra  NUMBER := 0;
    v_tot_pts_compra    NUMBER := 0;
    v_tot_monto_avance  NUMBER := 0;
    v_tot_pts_avance    NUMBER := 0;
    v_tot_monto_savance NUMBER := 0;
    v_tot_pts_savance   NUMBER := 0;

BEGIN
    -- 1. Limpieza de tablas (TRUNCATE)
    EXECUTE IMMEDIATE 'TRUNCATE TABLE DETALLE_PUNTOS_TARJETA_CATB';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE RESUMEN_PUNTOS_TARJETA_CATB';
    
    -- Obtener el año anterior dinámicamente 
    v_anno_anterior := EXTRACT(YEAR FROM SYSDATE) - 1;

    -- 2. Abrir REF CURSOR para obtener los meses distintos con transacciones del año anterior
    OPEN c_meses FOR 
        SELECT DISTINCT TO_CHAR(fecha_transaccion, 'MMYYYY')
        FROM TRANSACCION_TARJETA_CLIENTE
        WHERE EXTRACT(YEAR FROM fecha_transaccion) = v_anno_anterior
        ORDER BY 1; -- Ordenar por mes para la tabla resumen

    LOOP
        FETCH c_meses INTO v_mes_anno;
        EXIT WHEN c_meses%NOTFOUND;

        -- Reiniciar acumuladores por mes
        v_tot_monto_compra := 0; v_tot_pts_compra := 0;
        v_tot_monto_avance := 0; v_tot_pts_avance := 0;
        v_tot_monto_savance := 0; v_tot_pts_savance := 0;

        -- 3. Abrir Cursor Explícito con el parámetro del mes actual
        OPEN c_detalle_trans(v_mes_anno);
        
        LOOP
            FETCH c_detalle_trans INTO v_reg_trans;
            EXIT WHEN c_detalle_trans%NOTFOUND;

            -- A. Cálculo de Puntos Base (Index 1 del Varray = 250)
            -- Regla: 250 puntos por cada $100.000 
            v_puntos_base := TRUNC(v_reg_trans.monto_trans / 100000) * v_puntos_arr(1);
            v_puntos_extra := 0;

            -- B. Cálculo de Puntos Extras (Lógica PL/SQL con IF) 
            -- Solo aplica a Dueña de Casa (40) y Pensionados (50) segun script de inserción
            IF v_reg_trans.cod_tp_cliente IN (40, 50) THEN
                -- Evaluar tramos usando variables BIND
                IF v_reg_trans.monto_trans BETWEEN :b_rango1_min AND :b_rango1_max THEN
                    v_puntos_extra := TRUNC(v_reg_trans.monto_trans / 100000) * v_puntos_arr(2); -- 300 pts
                ELSIF v_reg_trans.monto_trans BETWEEN :b_rango2_min AND :b_rango2_max THEN
                    v_puntos_extra := TRUNC(v_reg_trans.monto_trans / 100000) * v_puntos_arr(3); -- 550 pts
                ELSIF v_reg_trans.monto_trans > :b_rango3_min THEN
                    v_puntos_extra := TRUNC(v_reg_trans.monto_trans / 100000) * v_puntos_arr(4); -- 700 pts
                END IF;
            END IF;

            v_puntos_total := v_puntos_base + v_puntos_extra;

            -- C. Insertar en tabla DETALLE 
            INSERT INTO DETALLE_PUNTOS_TARJETA_CATB (
                numrun, dvrun, nro_tarjeta, nro_transaccion, fecha_transaccion, 
                tipo_transaccion, monto_transaccion, puntos_allthebest
            ) VALUES (
                v_reg_trans.numrun, v_reg_trans.dvrun, v_reg_trans.nro_tarjeta, 
                v_reg_trans.nro_transaccion, v_reg_trans.fecha_trans, 
                v_reg_trans.nom_tptran, v_reg_trans.monto_trans, v_puntos_total
            );

            -- D. Acumular Totales para el Resumen (Separar por tipo de transacción)
            -- Asumimos IDs según script: 101/102/103 (Compra/Avance/Súper Avance)
            IF v_reg_trans.nom_tptran LIKE 'Avance%' THEN
                v_tot_monto_avance := v_tot_monto_avance + v_reg_trans.monto_trans;
                v_tot_pts_avance := v_tot_pts_avance + v_puntos_total;
            ELSIF v_reg_trans.nom_tptran LIKE 'Súper%' THEN
                v_tot_monto_savance := v_tot_monto_savance + v_reg_trans.monto_trans;
                v_tot_pts_savance := v_tot_pts_savance + v_puntos_total;
            ELSE -- Compras
                v_tot_monto_compra := v_tot_monto_compra + v_reg_trans.monto_trans;
                v_tot_pts_compra := v_tot_pts_compra + v_puntos_total;
            END IF;

        END LOOP;
        CLOSE c_detalle_trans; -- Cerrar cursor explícito

        -- E. Insertar en tabla RESUMEN al cambio de mes
        INSERT INTO RESUMEN_PUNTOS_TARJETA_CATB (
            mes_anno, monto_total_compras, total_puntos_compras, 
            monto_total_avances, total_puntos_avances, 
            monto_total_savances, total_puntos_savances
        ) VALUES (
            v_mes_anno, v_tot_monto_compra, v_tot_pts_compra,
            v_tot_monto_avance, v_tot_pts_avance,
            v_tot_monto_savance, v_tot_pts_savance
        );

    END LOOP;
    CLOSE c_meses; -- Cerrar REF CURSOR

    COMMIT; -- Confirmar transacciones
    DBMS_OUTPUT.PUT_LINE('Proceso Caso 1 completado exitosamente.');
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Error en Caso 1: ' || SQLERRM);
END;
/

/******************************************************************************
 * CASO 2: CÁLCULO DE APORTES SBIF (Código Limpio)
 * Procesa avances del año actual, calcula aporte según tramos
   y pobla tablas de detalle y resumen.
 ******************************************************************************/

DECLARE
    -- Registro para manejo de datos recuperados
    TYPE r_aporte IS RECORD (
        numrun          CLIENTE.numrun%TYPE,
        dvrun           CLIENTE.dvrun%TYPE,
        nro_tarjeta     TARJETA_CLIENTE.nro_tarjeta%TYPE,
        nro_transaccion TRANSACCION_TARJETA_CLIENTE.nro_transaccion%TYPE,
        fecha_trans     TRANSACCION_TARJETA_CLIENTE.fecha_transaccion%TYPE,
        monto_total     TRANSACCION_TARJETA_CLIENTE.monto_total_transaccion%TYPE,
        nom_tptran      TIPO_TRANSACCION_TARJETA.nombre_tptran_tarjeta%TYPE
    );
    v_reg_aporte r_aporte;

    -- Cursor Explícito 1: Obtiene los grupos (Mes y Tipo) del Año Actual
    -- Se usa para el bucle principal y llenar la tabla de Resumen
    CURSOR c_resumen_grupo IS
        SELECT DISTINCT 
            TO_CHAR(ttc.fecha_transaccion, 'MMYYYY') AS mes_anno,
            ttt.nombre_tptran_tarjeta AS nom_tptran,
            ttt.cod_tptran_tarjeta
        FROM TRANSACCION_TARJETA_CLIENTE ttc
        JOIN TIPO_TRANSACCION_TARJETA ttt ON ttc.cod_tptran_tarjeta = ttt.cod_tptran_tarjeta
        WHERE EXTRACT(YEAR FROM ttc.fecha_transaccion) = EXTRACT(YEAR FROM SYSDATE) -- Filtro Año Actual
          AND UPPER(ttt.nombre_tptran_tarjeta) LIKE '%AVANCE%' -- Solo Avances y Súper Avances
        ORDER BY 1, 2;

    -- Cursor Explícito 2 (CON PARÁMETROS): Obtiene el detalle de las transacciones
    -- Se alimenta de los valores del Cursor 1
    CURSOR c_detalle_aporte (p_mes VARCHAR2, p_cod_tipo NUMBER) IS
        SELECT 
            c.numrun,
            c.dvrun,
            tc.nro_tarjeta,
            ttc.nro_transaccion,
            ttc.fecha_transaccion,
            ttc.monto_total_transaccion, -- Se usa el monto total (con intereses)
            ttt.nombre_tptran_tarjeta
        FROM TRANSACCION_TARJETA_CLIENTE ttc
        JOIN TARJETA_CLIENTE tc ON ttc.nro_tarjeta = tc.nro_tarjeta
        JOIN CLIENTE c ON tc.numrun = c.numrun
        JOIN TIPO_TRANSACCION_TARJETA ttt ON ttc.cod_tptran_tarjeta = ttt.cod_tptran_tarjeta
        WHERE TO_CHAR(ttc.fecha_transaccion, 'MMYYYY') = p_mes
          AND ttc.cod_tptran_tarjeta = p_cod_tipo
        ORDER BY ttc.fecha_transaccion, c.numrun;

    -- Variables escalares para cálculos
    v_aporte_indiv   NUMBER;
    v_porcentaje     NUMBER;
    v_acum_monto     NUMBER;
    v_acum_aporte    NUMBER;

BEGIN
    -- 1. Limpieza de tablas (Requisito de proceso repetible)
    EXECUTE IMMEDIATE 'TRUNCATE TABLE DETALLE_APORTE_SBIF';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE RESUMEN_APORTE_SBIF';

    -- Bucle Principal: Recorre cada grupo de Mes y Tipo de Transacción
    FOR r_grupo IN c_resumen_grupo LOOP
        
        -- Reiniciar acumuladores por grupo
        v_acum_monto := 0;
        v_acum_aporte := 0;

        -- Bucle Secundario: Recorre el detalle pasando los parámetros del grupo actual
        OPEN c_detalle_aporte(r_grupo.mes_anno, r_grupo.cod_tptran_tarjeta);
        LOOP
            FETCH c_detalle_aporte INTO v_reg_aporte;
            EXIT WHEN c_detalle_aporte%NOTFOUND;

            -- 2. Lógica de Negocio (Cálculo PL/SQL)
            -- Obtener el porcentaje desde la tabla de tramos
            BEGIN
                SELECT porc_aporte_sbif INTO v_porcentaje
                FROM TRAMO_APORTE_SBIF
                WHERE v_reg_aporte.monto_total BETWEEN tramo_inf_av_sav AND tramo_sup_av_sav;
            EXCEPTION
                WHEN NO_DATA_FOUND THEN
                    v_porcentaje := 0; -- Si no encuentra tramo, asume 0
            END;

            -- Calcular el monto del aporte
            v_aporte_indiv := ROUND(v_reg_aporte.monto_total * (v_porcentaje / 100));

            -- 3. Insertar en tabla de Detalle
            INSERT INTO DETALLE_APORTE_SBIF (
                numrun, dvrun, nro_tarjeta, nro_transaccion, fecha_transaccion, 
                tipo_transaccion, monto_transaccion, aporte_sbif
            ) VALUES ( 
                v_reg_aporte.numrun, v_reg_aporte.dvrun, v_reg_aporte.nro_tarjeta,
                v_reg_aporte.nro_transaccion, v_reg_aporte.fecha_trans,
                v_reg_aporte.nom_tptran, v_reg_aporte.monto_total, v_aporte_indiv
            );

            -- Acumular totales para el resumen
            v_acum_monto := v_acum_monto + v_reg_aporte.monto_total;
            v_acum_aporte := v_acum_aporte + v_aporte_indiv;

        END LOOP;
        CLOSE c_detalle_aporte; -- Cerrar cursor explícito parametrizado

        -- 4. Insertar en tabla de Resumen (Totales del grupo)
        INSERT INTO RESUMEN_APORTE_SBIF (
            mes_anno, tipo_transaccion, monto_total_transacciones, aporte_total_abif
        ) VALUES (
            r_grupo.mes_anno, r_grupo.nom_tptran, v_acum_monto, v_acum_aporte
        );

    END LOOP; -- Fin Bucle Principal

    -- Confirmar cambios
    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Proceso Caso 2 completado exitosamente.');

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Error en ejecución Caso 2: ' || SQLERRM);
END;
/