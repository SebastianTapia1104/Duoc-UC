-- TRIGGER

CREATE OR REPLACE TRIGGER TRG_TOTAL_CONSUMOS
AFTER INSERT OR UPDATE OR DELETE ON CONSUMO
FOR EACH ROW
BEGIN
    IF INSERTING THEN
        UPDATE TOTAL_CONSUMOS
        SET MONTO_CONSUMOS = MONTO_CONSUMOS + :NEW.MONTO
        WHERE ID_HUESPED = :NEW.ID_HUESPED;
        
    ELSIF UPDATING THEN
        IF :OLD.ID_HUESPED = :NEW.ID_HUESPED THEN
            UPDATE TOTAL_CONSUMOS
            SET MONTO_CONSUMOS = MONTO_CONSUMOS - :OLD.MONTO + :NEW.MONTO
            WHERE ID_HUESPED = :NEW.ID_HUESPED;
        ELSE
            UPDATE TOTAL_CONSUMOS
            SET MONTO_CONSUMOS = MONTO_CONSUMOS - :OLD.MONTO
            WHERE ID_HUESPED = :OLD.ID_HUESPED;

            UPDATE TOTAL_CONSUMOS
            SET MONTO_CONSUMOS = MONTO_CONSUMOS + :NEW.MONTO
            WHERE ID_HUESPED = :NEW.ID_HUESPED;
        END IF;
        
    ELSIF DELETING THEN
        UPDATE TOTAL_CONSUMOS
        SET MONTO_CONSUMOS = MONTO_CONSUMOS - :OLD.MONTO
        WHERE ID_HUESPED = :OLD.ID_HUESPED;
    END IF;
END;
/

-- PACKAGE

CREATE OR REPLACE PACKAGE PKG_TOURS IS
    v_monto_tours NUMBER;
    FUNCTION FN_MONTO_TOURS(p_id_huesped NUMBER) RETURN NUMBER;
END PKG_TOURS;
/

-- CUERPO

CREATE OR REPLACE PACKAGE BODY PKG_TOURS IS
    FUNCTION FN_MONTO_TOURS(p_id_huesped NUMBER) RETURN NUMBER IS
        v_total NUMBER := 0;
    BEGIN
        SELECT NVL(SUM(T.VALOR_TOUR * NVL(HT.NUM_PERSONAS, 1)), 0)
        INTO v_total
        FROM HUESPED_TOUR HT
        JOIN TOUR T ON HT.ID_TOUR = T.ID_TOUR
        WHERE HT.ID_HUESPED = p_id_huesped;

        v_monto_tours := v_total; 
        RETURN v_total;
    EXCEPTION
        WHEN OTHERS THEN
            RETURN 0;
    END FN_MONTO_TOURS;
END PKG_TOURS;
/

-- FUNCIÓN AGENCIA

CREATE OR REPLACE FUNCTION FN_AGENCIA(p_id_huesped NUMBER) RETURN VARCHAR2 IS
    v_agencia VARCHAR2(100);
    v_error_msg VARCHAR2(300);
BEGIN
    SELECT A.NOM_AGENCIA
    INTO v_agencia
    FROM HUESPED H
    JOIN AGENCIA A ON H.ID_AGENCIA = A.ID_AGENCIA
    WHERE H.ID_HUESPED = p_id_huesped;

    RETURN v_agencia;
EXCEPTION
    WHEN OTHERS THEN
        v_error_msg := SQLERRM;
        INSERT INTO REG_ERRORES (ID_ERROR, NOMSUBPROGRAMA, MSG_ERROR)
        VALUES (SQ_ERROR.NEXTVAL, 
                'Error en la función EN AGENCIA al recuperar la agencia del huesped con id ' || p_id_huesped, 
                v_error_msg);
        RETURN 'NO REGISTRA AGENCIA';
END FN_AGENCIA;
/

-- FUNCIÓN CONSUMO

CREATE OR REPLACE FUNCTION FN_MONTO_CONSUMOS(p_id_huesped NUMBER) RETURN NUMBER IS
    v_monto NUMBER := 0;
    v_error_msg VARCHAR2(300);
BEGIN
    SELECT MONTO_CONSUMOS
    INTO v_monto
    FROM TOTAL_CONSUMOS
    WHERE ID_HUESPED = p_id_huesped;

    RETURN v_monto;
EXCEPTION
    WHEN OTHERS THEN
        v_error_msg := SQLERRM;
        INSERT INTO REG_ERRORES (ID_ERROR, NOMSUBPROGRAMA, MSG_ERROR)
        VALUES (SQ_ERROR.NEXTVAL, 
                'Error en la función EN CONSUMOS al recuperar los consumos del cliente con id ' || p_id_huesped, 
                v_error_msg);
        RETURN 0;
END FN_MONTO_CONSUMOS;
/

-- PROCEDIMIENTO

CREATE OR REPLACE PROCEDURE SP_PROCESO_COBROS(p_fecha_proceso DATE, p_valor_dolar NUMBER) IS
    v_agencia VARCHAR2(100);
    v_consumos_usd NUMBER;
    v_tours_usd NUMBER;
    v_estadia_usd NUMBER;
    v_personas_usd NUMBER;
    v_subtotal_usd NUMBER;
    v_descuento_agencia_usd NUMBER;
    v_total_usd NUMBER;

    -- Cursor modificado con una subconsulta para rescatar la cantidad de personas
    CURSOR c_huespedes IS
        SELECT H.ID_HUESPED,
               H.NOM_HUESPED,
               H.APPAT_HUESPED,
               H.APMAT_HUESPED,
               R.ID_RESERVA,
               R.ESTADIA,
               -- Subconsulta: busca en HUESPED_TOUR. Si no hay registros, asume 1 persona.
               NVL((SELECT MAX(NUM_PERSONAS) FROM HUESPED_TOUR WHERE ID_HUESPED = H.ID_HUESPED), 1) AS CANT_PERSONAS,
               SUM(HAB.VALOR_HABITACION) AS VALOR_HABITACION_TOTAL,
               SUM(HAB.VALOR_MINIBAR) AS VALOR_MINIBAR_TOTAL
        FROM HUESPED H
        JOIN RESERVA R ON H.ID_HUESPED = R.ID_HUESPED
        JOIN DETALLE_RESERVA DR ON R.ID_RESERVA = DR.ID_RESERVA
        JOIN HABITACION HAB ON DR.ID_HABITACION = HAB.ID_HABITACION
        WHERE (R.INGRESO + R.ESTADIA) = p_fecha_proceso
        GROUP BY H.ID_HUESPED, H.NOM_HUESPED, H.APPAT_HUESPED, H.APMAT_HUESPED, R.ID_RESERVA, R.ESTADIA;

BEGIN
    -- Limpieza de las tablas de destino
    EXECUTE IMMEDIATE 'TRUNCATE TABLE DETALLE_DIARIO_HUESPEDES';
    EXECUTE IMMEDIATE 'TRUNCATE TABLE REG_ERRORES';

    FOR r_huesped IN c_huespedes LOOP
        -- 1. Obtener datos externos mediante las funciones y el package
        v_agencia := FN_AGENCIA(r_huesped.ID_HUESPED);
        v_consumos_usd := FN_MONTO_CONSUMOS(r_huesped.ID_HUESPED);
        v_tours_usd := PKG_TOURS.FN_MONTO_TOURS(r_huesped.ID_HUESPED);

        -- 2. Cálculos de Estadía (Alojamiento + Minibar * Días)
        v_estadia_usd := (r_huesped.VALOR_HABITACION_TOTAL + r_huesped.VALOR_MINIBAR_TOTAL) * r_huesped.ESTADIA;
        
        -- 3. Cargo por personas ($35,000 por cada persona hospedada), convertido a USD
        v_personas_usd := (35000 * r_huesped.CANT_PERSONAS) / p_valor_dolar;

        -- 4. Subtotal (Estadía + Consumos + Valor por Personas).
        v_subtotal_usd := v_estadia_usd + v_consumos_usd + v_personas_usd;

        -- 5. Lógica del Descuento (12% sobre el subtotal, solo para VIAJES ALBERTI)
        IF v_agencia = 'VIAJES ALBERTI' THEN
            v_descuento_agencia_usd := v_subtotal_usd * 0.12;
        ELSE
            v_descuento_agencia_usd := 0;
        END IF;

        -- 6. Total a Pagar (Subtotal - Descuento + Tours)
        v_total_usd := v_subtotal_usd - v_descuento_agencia_usd + v_tours_usd;

        -- 7. Insertar el registro final convirtiendo a pesos chilenos y redondeando
        INSERT INTO DETALLE_DIARIO_HUESPEDES (
            ID_HUESPED,
            NOMBRE,
            AGENCIA,
            ALOJAMIENTO,
            CONSUMOS,
            TOURS,
            SUBTOTAL_PAGO,
            DESCUENTO_CONSUMOS, 
            DESCUENTOS_AGENCIA,
            TOTAL
        ) VALUES (
            r_huesped.ID_HUESPED,
            r_huesped.NOM_HUESPED || ' ' || r_huesped.APPAT_HUESPED || ' ' || r_huesped.APMAT_HUESPED,
            v_agencia,
            ROUND(v_estadia_usd * p_valor_dolar),
            ROUND(v_consumos_usd * p_valor_dolar),
            ROUND(v_tours_usd * p_valor_dolar),
            ROUND(v_subtotal_usd * p_valor_dolar),
            0, 
            ROUND(v_descuento_agencia_usd * p_valor_dolar),
            ROUND(v_total_usd * p_valor_dolar)
        );
    END LOOP;
    COMMIT;
END SP_PROCESO_COBROS;
/

-- EJECUCIÓN

BEGIN
    SP_PROCESO_COBROS(TO_DATE('18/08/2021', 'DD/MM/YYYY'), 915);
END;
/

-- REVISIÓN

SELECT * FROM DETALLE_DIARIO_HUESPEDES
ORDER BY NOMBRE;

SELECT * FROM TOTAL_CONSUMOS
ORDER BY ID_HUESPED;

SELECT * FROM REG_ERRORES
ORDER BY ID_ERROR;