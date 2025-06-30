package com.sebastian.comiccollectorsystem.servicios;

import com.sebastian.comiccollectorsystem.Excepciones.EntradaInvalidaException;
import com.sebastian.comiccollectorsystem.Excepciones.RutInvalidoException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ServicioValidadores {

    private static final Pattern SOLO_LETRAS_PATTERN = Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$");

    public static int leerEntero(Scanner sc) throws EntradaInvalidaException {
        try {
            int numero = sc.nextInt();
            sc.nextLine(); // Consumir el salto de línea
            return numero;
        } catch (InputMismatchException e) {
            sc.nextLine(); // Limpiar el buffer del scanner
            throw new EntradaInvalidaException("La entrada no es un número entero válido.");
        }
    }

   public static void validarRut(String rut) throws RutInvalidoException {
        if (rut == null || rut.trim().isEmpty()) {
            throw new RutInvalidoException("El RUT no puede estar vacío.");
        }
        // Eliminar espacios en blanco alrededor del RUT
        rut = rut.trim();
        // Validar que el RUT contenga solo números
        if (!rut.matches("\\d+")) {
            throw new RutInvalidoException("El RUT debe contener solo números (sin puntos, guiones ni dígito verificador).");
        }
        // Validar la longitud del RUT (mínimo 7 y máximo 8 dígitos)
        if (rut.length() < 7 || rut.length() > 8) {
            throw new RutInvalidoException("El RUT debe tener entre 7 y 8 dígitos.");
        }
    }

    public static boolean esNombreValido(String nombre) {
        return nombre != null && nombre.trim().length() >= 3 && SOLO_LETRAS_PATTERN.matcher(nombre).matches();
    }
}