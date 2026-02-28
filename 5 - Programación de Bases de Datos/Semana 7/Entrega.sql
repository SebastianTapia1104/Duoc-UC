-- 1. ESPECIFICACIÓN DEL PACKAGE
CREATE OR REPLACE PACKAGE PKG_COBROS_CLINICA IS
    -- Variables públicas solicitadas
    v_valor_multa      NUMBER(6) := 0;
    v_valor_descuento  NUMBER(6) := 0;

    -- Función pública para obtener el descuento
    FUNCTION FN_CALCULA_DESCUENTO_EDAD(p_edad NUMBER, p_monto_multa NUMBER) RETURN NUMBER;
END PKG_COBROS_CLINICA;
/

-- 2. CUERPO DEL PACKAGE
CREATE OR REPLACE PACKAGE BODY PKG_COBROS_CLINICA IS

    FUNCTION FN_CALCULA_DESCUENTO_EDAD(p_edad NUMBER, p_monto_multa NUMBER) RETURN NUMBER IS
        v_porcentaje   NUMBER(4) := 0;
        v_descuento    NUMBER(6) := 0;
    BEGIN
        -- Validar si aplica a tercera edad
        IF p_edad > 70 THEN
            -- Obtener el porcentaje de la tabla según rango de edad
            SELECT porcentaje_descto
            INTO v_porcentaje
            FROM PORC_DESCTO_3RA_EDAD
            WHERE p_edad BETWEEN anno_ini AND anno_ter;
            
            -- Calcular el monto de descuento
            v_descuento := ROUND(p_monto_multa * (v_porcentaje / 100));
        ELSE
            v_descuento := 0;
        END IF;

        RETURN v_descuento;
        
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RETURN 0;
        WHEN OTHERS THEN
            RETURN 0;
    END FN_CALCULA_DESCUENTO_EDAD;

END PKG_COBROS_CLINICA;
/

-- 2. FUNCIÓN ALMACENADA INDEPENDIENTE

CREATE OR REPLACE FUNCTION FN_OBTENER_ESPECIALIDAD(p_esp_id NUMBER) RETURN VARCHAR2 IS
    v_nombre_esp VARCHAR2(25);
BEGIN
    SELECT nombre
    INTO v_nombre_esp
    FROM ESPECIALIDAD
    WHERE esp_id = p_esp_id;

    RETURN v_nombre_esp;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RETURN 'Desconocida';
    WHEN OTHERS THEN
        RETURN 'Error';
END FN_OBTENER_ESPECIALIDAD;
/

-- 3. PROCEDIMIENTO ALMACENADO PRINCIPAL

CREATE OR REPLACE PROCEDURE SP_PROCESAR_MOROSOS IS

    -- Tipo VARRAY para almacenar los valores de multas (7 especialidades mencionadas)
    TYPE t_multas_array IS VARRAY(7) OF NUMBER(6);
    v_tarifas_multa t_multas_array;

    -- Variables locales
    v_dias_moro         NUMBER(3);
    v_nombre_esp        VARCHAR2(25);
    v_monto_multa_base  NUMBER(6);
    v_edad_paciente     NUMBER(3);
    v_observacion       VARCHAR2(100);
    
    -- Año dinámico a procesar (el año anterior)
    v_anno_proceso      NUMBER(4) := EXTRACT(YEAR FROM SYSDATE) - 1;

    -- Cursor principal
    CURSOR cur_morosos IS
        SELECT 
            p.pac_run, 
            p.dv_run, 
            p.pnombre || ' ' || p.snombre || ' ' || p.apaterno || ' ' || p.amaterno AS pac_nombre,
            p.fecha_nacimiento,
            a.ate_id, 
            a.fecha_atencion,
            a.costo AS costo_atencion,
            pa.fecha_venc_pago, 
            pa.fecha_pago,
            m.esp_id
        FROM PAGO_ATENCION pa
        JOIN ATENCION a ON pa.ate_id = a.ate_id
        JOIN PACIENTE p ON a.pac_run = p.pac_run
        JOIN MEDICO m ON a.med_run = m.med_run
        WHERE pa.fecha_pago > pa.fecha_venc_pago 
        AND EXTRACT(YEAR FROM pa.fecha_pago) = v_anno_proceso; 

BEGIN
    -- 1. Limpiar la tabla de destino antes de insertar nuevos datos
    EXECUTE IMMEDIATE 'TRUNCATE TABLE PAGO_MOROSO';

    -- 2. Inicializar el VARRAY con las tarifas de multa
    v_tarifas_multa := t_multas_array(1200, 1300, 1700, 1900, 1100, 2000, 2300);

    -- 3. Recorrer el cursor
    FOR rec IN cur_morosos LOOP
        
        -- Reiniciar variables por iteración
        v_observacion := NULL;
        PKG_COBROS_CLINICA.v_valor_multa := 0;
        PKG_COBROS_CLINICA.v_valor_descuento := 0;

        -- Obtener nombre de la especialidad usando la Función Almacenada
        v_nombre_esp := FN_OBTENER_ESPECIALIDAD(rec.esp_id);

        -- Calcular días de morosidad
        v_dias_moro := rec.fecha_pago - rec.fecha_venc_pago;

        -- Corregido: Nombres sin tildes para que coincida con la base de datos
        IF v_nombre_esp = 'Medicina General' THEN
            v_monto_multa_base := v_tarifas_multa(1) * v_dias_moro;
        ELSIF v_nombre_esp = 'Traumatologia' THEN
            v_monto_multa_base := v_tarifas_multa(2) * v_dias_moro;
        ELSIF v_nombre_esp IN ('Neurologia', 'Pediatria') THEN
            v_monto_multa_base := v_tarifas_multa(3) * v_dias_moro;
        ELSIF v_nombre_esp = 'Oftalmologia' THEN
            v_monto_multa_base := v_tarifas_multa(4) * v_dias_moro;
        ELSIF v_nombre_esp = 'Geriatria' THEN
            v_monto_multa_base := v_tarifas_multa(5) * v_dias_moro;
        ELSIF v_nombre_esp IN ('Ginecologia', 'Gastroenterologia') THEN
            v_monto_multa_base := v_tarifas_multa(6) * v_dias_moro;
        ELSIF v_nombre_esp = 'Dermatologia' THEN
            v_monto_multa_base := v_tarifas_multa(7) * v_dias_moro;
        ELSE
            v_monto_multa_base := 0;
        END IF;

        -- Edad calculada a la fecha de la atención médica
        v_edad_paciente := TRUNC(MONTHS_BETWEEN(rec.fecha_atencion, rec.fecha_nacimiento) / 12);

        -- Obtener descuento usando la función del Package
        PKG_COBROS_CLINICA.v_valor_descuento := PKG_COBROS_CLINICA.FN_CALCULA_DESCUENTO_EDAD(v_edad_paciente, v_monto_multa_base);

        -- Aplicar descuento al monto final en la variable pública del package
        PKG_COBROS_CLINICA.v_valor_multa := v_monto_multa_base - PKG_COBROS_CLINICA.v_valor_descuento;

        -- Generar observación si hubo descuento
        IF PKG_COBROS_CLINICA.v_valor_descuento > 0 THEN
            v_observacion := 'Paciente tenia ' || v_edad_paciente || ' a la fecha de atención. Se aplicó descuento paciente mayor a 70 años';
        END IF;

        -- Insertar datos procesados
        INSERT INTO PAGO_MOROSO (
            pac_run, pac_dv_run, pac_nombre, ate_id, fecha_venc_pago, 
            fecha_pago, dias_morosidad, especialidad_atencion, costo_atencion, 
            monto_multa, observacion
        ) VALUES (
            rec.pac_run, rec.dv_run, rec.pac_nombre, rec.ate_id, rec.fecha_venc_pago, 
            rec.fecha_pago, v_dias_moro, v_nombre_esp, rec.costo_atencion, 
            PKG_COBROS_CLINICA.v_valor_multa, v_observacion
        );

    END LOOP;
    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Proceso finalizado con éxito.');

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Error en el proceso: ' || SQLERRM);
END SP_PROCESAR_MOROSOS;
/

-- 4. REVISIÓN

SET SERVEROUTPUT ON;
EXEC SP_PROCESAR_MOROSOS;

-- Para verificar el resultado, debes correr:
SELECT * FROM PAGO_MOROSO ORDER BY fecha_venc_pago ASC, pac_nombre ASC;