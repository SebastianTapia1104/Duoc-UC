DECLARE
    -- VARIABLES DE CONTROL
    v_anho_proceso NUMBER := EXTRACT(YEAR FROM SYSDATE); -- Año actual dinámico
    v_aporte_calculado NUMBER(10);
    v_porcentaje_aporte NUMBER(3);
    
    -- Variables acumuladoras para el resumen
    v_total_monto_mes NUMBER(12);
    v_total_aporte_mes NUMBER(12);

    -- CURSOR 1: GENERAL (RESUMEN)
    -- Obtiene los grupos únicos por Mes y Tipo de Transacción
    CURSOR cur_resumen IS
        SELECT DISTINCT
               EXTRACT(MONTH FROM t.fecha_transaccion) AS mes_num,
               TO_CHAR(t.fecha_transaccion, 'MMYYYY') AS mes_anno_formato,
               tt.cod_tptran_tarjeta,
               tt.nombre_tptran_tarjeta
        FROM TRANSACCION_TARJETA_CLIENTE t
        JOIN TIPO_TRANSACCION_TARJETA tt ON t.cod_tptran_tarjeta = tt.cod_tptran_tarjeta
        WHERE EXTRACT(YEAR FROM t.fecha_transaccion) = v_anho_proceso
        ORDER BY mes_num ASC, tt.nombre_tptran_tarjeta ASC;

    -- CURSOR 2: PARAMETRIZADO (DETALLE)
    -- Recibe mes y código de tipo transacción
    CURSOR cur_detalle (p_mes NUMBER, p_cod_tipo NUMBER) IS
        SELECT c.numrun,
               c.dvrun,
               tc.nro_tarjeta,
               t.nro_transaccion,
               t.fecha_transaccion,
               t.monto_transaccion,
               tt.nombre_tptran_tarjeta
        FROM TRANSACCION_TARJETA_CLIENTE t
        JOIN TARJETA_CLIENTE tc ON t.nro_tarjeta = tc.nro_tarjeta
        JOIN CLIENTE c ON tc.numrun = c.numrun
        JOIN TIPO_TRANSACCION_TARJETA tt ON t.cod_tptran_tarjeta = tt.cod_tptran_tarjeta
        WHERE EXTRACT(YEAR FROM t.fecha_transaccion) = v_anho_proceso
          AND EXTRACT(MONTH FROM t.fecha_transaccion) = p_mes
          AND t.cod_tptran_tarjeta = p_cod_tipo
        ORDER BY t.fecha_transaccion ASC, c.numrun ASC;

BEGIN
    -- 1. LIMPIEZA DE TABLAS
    EXECUTE IMMEDIATE 'TRUNCATE TABLE DETALLE_APORTE_SBIF';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE RESUMEN_APORTE_SBIF';

    -- 2. PROCESAMIENTO
    FOR reg_res IN cur_resumen LOOP
        
        -- Reiniciar acumuladores por grupo
        v_total_monto_mes := 0;
        v_total_aporte_mes := 0;

        -- Iterar detalle usando parámetros del cursor resumen
        FOR reg_det IN cur_detalle(reg_res.mes_num, reg_res.cod_tptran_tarjeta) LOOP
            
            BEGIN
                -- BLOQUE ANIDADO: CÁLCULO Y MANEJO DE ERRORES
                
                -- Lógica de Negocio: Obtener porcentaje según tramo
                SELECT porc_aporte_sbif 
                INTO v_porcentaje_aporte
                FROM TRAMO_APORTE_SBIF
                WHERE reg_det.monto_transaccion BETWEEN tramo_inf_av_sav AND tramo_sup_av_sav;

                -- Calcular aporte (Redondeado)
                v_aporte_calculado := ROUND(reg_det.monto_transaccion * v_porcentaje_aporte / 100);

                -- Acumular totales
                v_total_monto_mes := v_total_monto_mes + reg_det.monto_transaccion;
                v_total_aporte_mes := v_total_aporte_mes + v_aporte_calculado;

                -- Insertar Detalle
                INSERT INTO DETALLE_APORTE_SBIF (
                    numrun, dvrun, nro_tarjeta, nro_transaccion, 
                    fecha_transaccion, tipo_transaccion, 
                    monto_transaccion, aporte_sbif
                ) VALUES (
                    reg_det.numrun, reg_det.dvrun, reg_det.nro_tarjeta, reg_det.nro_transaccion,
                    reg_det.fecha_transaccion, reg_det.nombre_tptran_tarjeta,
                    reg_det.monto_transaccion, v_aporte_calculado
                );

            EXCEPTION
                WHEN NO_DATA_FOUND THEN
                    -- Si el monto no cae en ningún tramo, insertamos 0 aporte para no detener el flujo
                    INSERT INTO DETALLE_APORTE_SBIF (
                        numrun, dvrun, nro_tarjeta, nro_transaccion, 
                        fecha_transaccion, tipo_transaccion, 
                        monto_transaccion, aporte_sbif
                    ) VALUES (
                        reg_det.numrun, reg_det.dvrun, reg_det.nro_tarjeta, reg_det.nro_transaccion,
                        reg_det.fecha_transaccion, reg_det.nombre_tptran_tarjeta,
                        reg_det.monto_transaccion, 0
                    );
            END; 
            
        END LOOP; -- Fin Loop Detalle

        -- 3. INSERTAR RESUMEN
        INSERT INTO RESUMEN_APORTE_SBIF (
            mes_anno, tipo_transaccion, monto_total_transacciones, aporte_total_abif
        ) VALUES (
            reg_res.mes_anno_formato, reg_res.nombre_tptran_tarjeta, 
            v_total_monto_mes, v_total_aporte_mes
        );

    END LOOP; -- Fin Loop Resumen

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Proceso completado exitosamente.');

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Error General: ' || SQLERRM);
END;
/