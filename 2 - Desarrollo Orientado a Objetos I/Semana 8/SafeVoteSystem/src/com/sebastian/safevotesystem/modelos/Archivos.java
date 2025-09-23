package com.sebastian.safevotesystem.modelos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Archivos {
    
    public static List<Integer> lectorArchivos(String rutaArchivos) throws IOException, NumberFormatException {
        List<Integer> numeros = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivos))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] numStrings = linea.split(",");
                for (String numStr : numStrings) {
                    numeros.add(Integer.parseInt(numStr.trim()));
                }
            }
        }
        return numeros;
    }

    public static void escritorArchivos(String rutaArchivos, String mensaje, boolean append) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivos, append))) {
            bw.write(mensaje);
            bw.newLine();
        }
    }

    public static String numeroABinario(int number) { // Codificación de número a binario
        return Integer.toBinaryString(number);
    }
}