/* ========================================================================== 
   CONFIGURACIÓN DE VARIABLE BIND 
   ========================================================================== */
VARIABLE b_fecha_proceso VARCHAR2(20);
EXEC :b_fecha_proceso := TO_CHAR(SYSDATE, 'DD/MM/YYYY');

/* ========================================================================== 
   BLOQUE PL/SQL ANÓNIMO 
   ========================================================================== */
DECLARE
    -- Definición de variables escalares utilizando %TYPE
    v_run_emp        EMPLEADO.NUMRUN_EMP%TYPE;
    v_dv_emp         EMPLEADO.DVRUN_EMP%TYPE;
    v_pnombre        EMPLEADO.PNOMBRE_EMP%TYPE;
    v_appaterno      EMPLEADO.APPATERNO_EMP%TYPE;
    v_sueldo         EMPLEADO.SUELDO_BASE%TYPE;
    v_fecha_nac      EMPLEADO.FECHA_NAC%TYPE;
    v_fecha_con      EMPLEADO.FECHA_CONTRATO%TYPE;
    v_est_civil_id   EMPLEADO.ID_ESTADO_CIVIL%TYPE;
    
    -- Variables escalares para lógica de negocio
    v_est_civil_nom  ESTADO_CIVIL.NOMBRE_ESTADO_CIVIL%TYPE;
    v_nombre_usuario USUARIO_CLAVE.NOMBRE_USUARIO%TYPE;
    v_clave_usuario  USUARIO_CLAVE.CLAVE_USUARIO%TYPE;
    
    -- Variables auxiliares para cálculos
    v_antiguedad     NUMBER(3);
    v_letra_civil    CHAR(1);
    v_nom_completo   USUARIO_CLAVE.NOMBRE_EMPLEADO%TYPE;
    v_aux_sueldo     NUMBER(10);
    v_letras_ape     VARCHAR2(2);
    v_contador       NUMBER(4) := 0; -- Para verificar iteraciones
    v_total_emp      NUMBER(4);

BEGIN
    -- 1. TRUNCAR LA TABLA 
    EXECUTE IMMEDIATE 'TRUNCATE TABLE USUARIO_CLAVE';
    
    -- Obtener total de empleados para validación final
    SELECT COUNT(*) INTO v_total_emp FROM EMPLEADO;

    -- 2. ITERACIÓN DE EMPLEADOS
    FOR r_emp IN (
        SELECT E.ID_EMP, E.NUMRUN_EMP, E.DVRUN_EMP, E.PNOMBRE_EMP, E.SNOMBRE_EMP, 
               E.APPATERNO_EMP, E.APMATERNO_EMP, E.SUELDO_BASE, E.FECHA_NAC, 
               E.FECHA_CONTRATO, E.ID_ESTADO_CIVIL, EC.NOMBRE_ESTADO_CIVIL
        FROM EMPLEADO E
        JOIN ESTADO_CIVIL EC ON E.ID_ESTADO_CIVIL = EC.ID_ESTADO_CIVIL
        ORDER BY E.ID_EMP ASC
    ) LOOP
        
        -- Asignación a variables locales para manipulación
        v_run_emp       := r_emp.NUMRUN_EMP;
        v_dv_emp        := r_emp.DVRUN_EMP;
        v_pnombre       := r_emp.PNOMBRE_EMP;
        v_appaterno     := r_emp.APPATERNO_EMP;
        v_sueldo        := r_emp.SUELDO_BASE;
        v_fecha_nac     := r_emp.FECHA_NAC;
        v_fecha_con     := r_emp.FECHA_CONTRATO;
        v_est_civil_id  := r_emp.ID_ESTADO_CIVIL;
        v_est_civil_nom := r_emp.NOMBRE_ESTADO_CIVIL;
        
        -- Construcción Nombre Completo para la tabla de salida
        v_nom_completo := r_emp.PNOMBRE_EMP || ' ' || r_emp.SNOMBRE_EMP || ' ' || 
                          r_emp.APPATERNO_EMP || ' ' || r_emp.APMATERNO_EMP;

        /* ------------------------------------------------------------------
           LÓGICA NOMBRE DE USUARIO
           a) 1ra letra estado civil (minúscula)
           b) 3 primeras letras primer nombre
           c) Largo primer nombre
           d) Asterisco (*)
           e) Último dígito sueldo
           f) DV Rut
           g) Años antiguedad (+ 'X' si es < 10 años)
           ------------------------------------------------------------------ */
        
        -- Cálculo antigüedad (Años trabajados)
        v_antiguedad := ROUND(MONTHS_BETWEEN(TO_DATE(:b_fecha_proceso, 'DD/MM/YYYY'), v_fecha_con) / 12, 0);
        
        -- Letra estado civil
        v_letra_civil := LOWER(SUBSTR(v_est_civil_nom, 1, 1));
        
        v_nombre_usuario := v_letra_civil || 
                            SUBSTR(v_pnombre, 1, 3) || 
                            LENGTH(v_pnombre) || 
                            '*' || -- Según requerimiento textual d) "Un ASTERISCO"
                            SUBSTR(TO_CHAR(v_sueldo), -1) || 
                            v_dv_emp || 
                            v_antiguedad;
                            
        -- Agregar 'X' si antigüedad es menor a 10 años
        IF v_antiguedad < 10 THEN
            v_nombre_usuario := v_nombre_usuario || 'X';
        END IF;

        /* ------------------------------------------------------------------
           LÓGICA CLAVE DE USUARIO
           a) 3er dígito del RUN
           b) Año nacimiento + 2
           c) 3 últimos dígitos (sueldo - 1)
           d) 2 letras apellido paterno (minúscula) según estado civil
           e) ID Empleado
           f) Mes y Año de base de datos (numérico)
           ------------------------------------------------------------------ */
           
        -- Lógica condicional para letras del apellido según estado civil
        -- 10:CASADO, 20:DIVORCIADO, 30:SOLTERO, 40:VIUDO, 50:SEPARADO, 60:AUC
        IF v_est_civil_id IN (10, 60) THEN
            -- Casado o AUC: Dos primeras letras
            v_letras_ape := LOWER(SUBSTR(v_appaterno, 1, 2));
            
        ELSIF v_est_civil_id IN (20, 30) THEN
            -- Divorciado o Soltero: Primera y última letra
            v_letras_ape := LOWER(SUBSTR(v_appaterno, 1, 1) || SUBSTR(v_appaterno, -1));
            
        ELSIF v_est_civil_id = 40 THEN
            -- Viudo: Antepenúltima y penúltima (Largo -2 y Largo -1)
            -- Nota: "Antepenúltima" es -3, "Penúltima" es -2. 
            -- Ajuste según lógica estándar: SUBSTR(str, -3, 1) y SUBSTR(str, -2, 1)
            v_letras_ape := LOWER(SUBSTR(v_appaterno, -3, 1) || SUBSTR(v_appaterno, -2, 1));
            
        ELSIF v_est_civil_id = 50 THEN
            -- Separado: Dos últimas letras
            v_letras_ape := LOWER(SUBSTR(v_appaterno, -2));
        END IF;

        -- Cálculo 3 últimos dígitos del (sueldo - 1)
        v_aux_sueldo := TO_NUMBER(SUBSTR(TO_CHAR(v_sueldo - 1), -3));

        v_clave_usuario := SUBSTR(TO_CHAR(v_run_emp), 3, 1) || 
                           (TO_NUMBER(TO_CHAR(v_fecha_nac, 'YYYY')) + 2) || 
                           LPAD(v_aux_sueldo, 3, '0') || -- Asegurar 3 dígitos con ceros a la izq si es necesario
                           v_letras_ape || 
                           r_emp.ID_EMP || 
                           TO_CHAR(SYSDATE, 'MMYYYY');

        -- 3. SENTENCIA DML: INSERTAR DATOS (Documentada)
        -- Se insertan los valores calculados en las variables PL/SQL en la tabla de destino
        INSERT INTO USUARIO_CLAVE (
            ID_EMP, NUMRUN_EMP, DVRUN_EMP, NOMBRE_EMPLEADO, NOMBRE_USUARIO, CLAVE_USUARIO
        ) VALUES (
            r_emp.ID_EMP, v_run_emp, v_dv_emp, v_nom_completo, v_nombre_usuario, v_clave_usuario
        );
        
        -- Incrementar contador
        v_contador := v_contador + 1;
        
    END LOOP;
    
    -- 4. CONFIRMACIÓN DE TRANSACCIÓN (Control de Transacciones)
    -- Se confirma solo si se procesaron empleados
    IF v_contador > 0 THEN
        COMMIT;
        DBMS_OUTPUT.PUT_LINE('Proceso finalizado con éxito. Empleados procesados: ' || v_contador);
    ELSE
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Error: No se procesaron empleados.');
    END IF;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        DBMS_OUTPUT.PUT_LINE('Error en el proceso: ' || SQLERRM);
END;
/

/* ========================================================================== 
   VERIFICACIÓN (Opcional - Para que veas el resultado)
   ========================================================================== */
SELECT * FROM USUARIO_CLAVE ORDER BY ID_EMP;