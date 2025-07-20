package com.sebastian.drivequestrentals.modelos;

import com.sebastian.drivequestrentals.interfaces.CalculosArriendo;
import java.io.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GestionVehiculos {
    private final ConcurrentHashMap<String, Vehiculo> flotaVehiculos;
    private final ConcurrentHashMap<String, Arrendador> listaArrendadores;
    private List<List<String>> boletasGeneradas;

    private static final String VEHICULOS_INICIALES = "src/com/sebastian/drivequestrentals/resources/VehiculosIniciales.csv";
    private static final String HISTORIAL_ARRIENDO = "src/com/sebastian/drivequestrentals/resources/HistorialArriendo.txt";
    private static final String BOLETAS = "src/com/sebastian/drivequestrentals/resources/Boletas.txt";

    public GestionVehiculos() {
        this.flotaVehiculos = new ConcurrentHashMap<>();
        this.listaArrendadores = new ConcurrentHashMap<>();
        this.boletasGeneradas = new ArrayList<>();
        cargarVehiculosIniciales();
    }
    
    public synchronized boolean agregarVehiculo(Vehiculo vehiculo, String numThread) {
        if (flotaVehiculos.containsKey(vehiculo.getPatente())) {
            System.out.println("Error: El vehículo con patente " + vehiculo.getPatente() + " ya existe.");
            return false;
        }
        flotaVehiculos.put(vehiculo.getPatente(), vehiculo);
        System.out.println("Vehículo con patente " + vehiculo.getPatente() + " agregado correctamente por el hilo " + numThread + ".");
        return true;
    }

    public synchronized boolean agregarVehiculo(Vehiculo vehiculo) {
        return agregarVehiculo(vehiculo, Thread.currentThread().getName());
    }

    public synchronized void listarVehiculos() {
        if (flotaVehiculos.isEmpty()) {
            System.out.println("No hay vehículos registrados en la flota.");
            return;
        }
        System.out.println("\n--- Flota de Vehículos ---");
        String[] titulos = {"Tipo", "Patente", "Marca", "Modelo", "Año", "Precio/Día", "Días Arriendo", "Específico", "Estado Arriendo"}; // Titulos de la tabla
        int[] anchoTitulos = new int[titulos.length];
        for (int i = 0; i < titulos.length; i++) { // Inicializar anchos de columna con la longitud de los encabezados
            anchoTitulos[i] = titulos[i].length();
        }
        List<String[]> filas = new ArrayList<>(); // Calcular los anchos máximos de cada columna
        for (Vehiculo vehiculo : flotaVehiculos.values()) {
            String[] datosFila = vehiculo.mostrarDatos(); // Obtener los datos base del vehículo
            String estadoArriendo = "Disponible";
            if (vehiculo.isEstaArrendado()) {
                Arrendador arrendador = null;
                for (Arrendador arrend : listaArrendadores.values()) {
                    if (arrend.getVehiculoArrendado() != null && arrend.getVehiculoArrendado().getPatente().equalsIgnoreCase(vehiculo.getPatente())) {
                        arrend = arrend;
                        break;
                    }
                }
                if (arrendador != null) {
                    estadoArriendo = "Arrendado a " + arrendador.getNombre() + " (RUT: " + arrendador.getRut() + ")";
                } else {
                    estadoArriendo = "Arrendado (Arrendador Desconocido)";
                }
            }
            String[] datosFilaFinal = new String[titulos.length]; // Crear el array final de la fila incluyendo el estado de arriendo
            System.arraycopy(datosFila, 0, datosFilaFinal, 0, datosFila.length); // Copia los datos de getDatosTabla
            datosFilaFinal[titulos.length - 1] = estadoArriendo; // El último campo siempre es el estado de arriendo
            filas.add(datosFilaFinal);
            for (int i = 0; i < datosFilaFinal.length; i++) {
                if (datosFilaFinal[i] != null && datosFilaFinal[i].length() > anchoTitulos[i]) {
                    anchoTitulos[i] = datosFilaFinal[i].length();
                }
            }
        }
        for (int i = 0; i < anchoTitulos.length; i++) { // Ajustar anchos para asegurar espacio mínimo y padding
            anchoTitulos[i] = Math.max(anchoTitulos[i], titulos[i].length());
            anchoTitulos[i] += 2; // Añadir un poco de padding
        }
        fila(titulos, anchoTitulos); // Imprimir el encabezado
        separador(anchoTitulos);
        for (String[] datosFila : filas) { // Imprimir cada fila de datos
            fila(datosFila, anchoTitulos);
        }
        separador(anchoTitulos); // Cierra la tabla
    }

    private void fila(String[] datos, int[] anchos) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < datos.length; i++) {
            String dato = (datos[i] != null) ? datos[i] : "";
            sb.append(String.format("| %-" + anchos[i] + "s", dato));
        }
        sb.append("|");
        System.out.println(sb.toString());
    }

    private void separador(int[] anchos) {
        StringBuilder sb = new StringBuilder();
        for (int ancho : anchos) {
            sb.append("+-");
            for (int i = 0; i < ancho; i++) {
                sb.append("-");
            }
        }
        sb.append("+");
        System.out.println(sb.toString());
    }

    public synchronized List<Vehiculo> obtenerVehiculosArriendoLargo() {
        List<Vehiculo> arriendosLargos = new ArrayList<>();
        for (Vehiculo vehiculo : flotaVehiculos.values()) {
            if (vehiculo.getDiasArriendo() >= 7 && vehiculo.isEstaArrendado()) { 
                arriendosLargos.add(vehiculo);
            }
        }
        return arriendosLargos;
    }

    private void cargarVehiculosIniciales() {
        ExecutorService executor = Executors.newFixedThreadPool(3); 
        try (BufferedReader br = new BufferedReader(new FileReader(VEHICULOS_INICIALES))) {
            String linea;
            List<String> lineasCSV = new ArrayList<>();
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    lineasCSV.add(linea);
                }
            }
            int totalLineas = lineasCSV.size();
            int lineasPorHilo = (int) Math.ceil((double) totalLineas / 3);
            for (int i = 0; i < 3; i++) {
                int inicio = i * lineasPorHilo;
                int fin = Math.min(inicio + lineasPorHilo, totalLineas);
                if (inicio < fin) {
                    List<String> sublist = lineasCSV.subList(inicio, fin);
                    executor.submit(() -> lineasCSV(sublist, Thread.currentThread().getName()));
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Archivo CSV de vehículos iniciales no encontrado. Asegúrese de que '" + VEHICULOS_INICIALES + "' exista en la raíz del proyecto o en una ubicación accesible.");
        } catch (IOException e) {
            System.err.println("Error de I/O al cargar vehículos desde CSV: " + e.getMessage());
        } finally {
            executor.shutdown(); // Apaga el pool de hilos
            try {
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) { // Espera hasta 60 segundos
                    System.err.println("Algunos hilos de carga no terminaron a tiempo.");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restaura el estado de interrupción
                System.err.println("La carga de vehículos fue interrumpida.");
            }
            System.out.println("Proceso de carga de vehículos desde CSV finalizado.");
        }
    }

    private void lineasCSV(List<String> lineas, String nombreThread) {
        for (String linea : lineas) {
            String[] datos = linea.split(",");
            try {
                if (datos.length >= 7) { 
                    String tipo = datos[0].trim();
                    String patente = datos[1].trim();
                    String marca = datos[2].trim();
                    String modelo = datos[3].trim();
                    int anio = Integer.parseInt(datos[4].trim());
                    double precioArriendoDia = Double.parseDouble(datos[5].trim());
                    int diasArriendo = Integer.parseInt(datos[6].trim());
                    Vehiculo nuevoVehiculo = null;
                    if (tipo.equalsIgnoreCase("carga") && datos.length == 8) { 
                        double capacidadCargaKG = Double.parseDouble(datos[7].trim()); 
                        nuevoVehiculo = new VehiculoCarga(patente, marca, modelo, anio, precioArriendoDia, diasArriendo, capacidadCargaKG);
                    } else if (tipo.equalsIgnoreCase("pasajeros") && datos.length == 8) {
                        int numeroMaximoPasajeros = Integer.parseInt(datos[7].trim()); 
                        nuevoVehiculo = new VehiculoPasajeros(patente, marca, modelo, anio, precioArriendoDia, diasArriendo, numeroMaximoPasajeros);
                    } else {
                        System.err.println("Hilo " + nombreThread + ": Formato de línea incorrecto o tipo de vehículo desconocido: " + linea + ". Longitud esperada: 8. Longitud real: " + datos.length); // ¡MODIFICADO!
                    }
                    if (nuevoVehiculo != null) {
                        agregarVehiculo(nuevoVehiculo);
                    }
                } else {
                    System.err.println("Hilo " + nombreThread + ": Línea incompleta en CSV (esperado 8 campos): " + linea); 
                }
            } catch (NumberFormatException e) {
                System.err.println("Hilo " + nombreThread + ": Error de formato numérico en línea: " + linea + " - " + e.getMessage());
            } catch (ArrayIndexOutOfBoundsException e) {
                System.err.println("Hilo " + nombreThread + ": Error de índice en línea (posiblemente campo faltante): " + linea + " - " + e.getMessage());
            }
        }
    }

    public synchronized void registrarHistorialArriendo(String patente, String tipoEvento) {
        String tiempo = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String registro = String.format("[%s] Vehículo %s: %s%n", tiempo, patente, tipoEvento);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(HISTORIAL_ARRIENDO, true))) {
            bw.write(registro);
        } catch (IOException e) {
            System.err.println("Error al escribir en el historial de arriendo: " + e.getMessage());
        }
    }

    public Vehiculo buscarVehiculoPorPatente(String patente) {
        return flotaVehiculos.get(patente);
    }

    public Arrendador buscarArrendadorPorRut(String rut) {
        return listaArrendadores.get(rut.toLowerCase());
    }

    public boolean agregarArrendador(Arrendador arrendador) {
        if (listaArrendadores.putIfAbsent(arrendador.getRut().toLowerCase(), arrendador) == null) {
            return true;
        } else {
            return false;
        }
    }

    public synchronized boolean arrendarVehiculo(String patente, String rutArrendador, String nombreArrendador, String telefonoArrendador, int diasArriendo) {
        Vehiculo vehiculo = buscarVehiculoPorPatente(patente);
        if (vehiculo == null) {
            System.out.println("Error: Vehículo con patente " + patente + " no encontrado.");
            return false;
        }
        if (vehiculo.isEstaArrendado()) {
            System.out.println("Error: El vehículo con patente " + patente + " ya está arrendado.");
            return false;
        }
        Arrendador arrendador = listaArrendadores.get(rutArrendador);
        if (arrendador == null) {
            arrendador = new Arrendador(rutArrendador, nombreArrendador, telefonoArrendador);
            listaArrendadores.put(rutArrendador, arrendador);
            System.out.println("Arrendador " + nombreArrendador + " (RUT: " + rutArrendador + ") registrado.");
        } else {
            if (arrendador.getVehiculoArrendado() != null) {
                System.out.println("Error: El arrendador " + arrendador.getNombre() + " (RUT: " + arrendador.getRut() + ") ya tiene un vehículo arrendado (Patente: " + arrendador.getVehiculoArrendado().getPatente() + ").");
                return false;
            }
        }
        vehiculo.setDiasArriendo(diasArriendo);
        vehiculo.setEstaArrendado(true);
        arrendador.setVehiculoArrendado(vehiculo);
        registrarHistorialArriendo(patente, "ARRENDADO por " + arrendador.getNombre() + " (RUT: " + arrendador.getRut() + ") por " + diasArriendo + " días.");
        vehiculo.calcularYMostrarBoleta(vehiculo);
        List<String> boleta = generarBoleta(vehiculo);
        boletasGeneradas.add(boleta);
        escribirBoletaEnArchivo(boleta);
        System.out.println("Vehículo " + patente + " arrendado exitosamente a " + arrendador.getNombre() + ".");
        return true;
    }


    private List<String> generarBoleta(Vehiculo vehiculo) {
        List<String> boleta = new ArrayList<>();
        double precioBase = vehiculo.calcularValorBase(vehiculo);
        double descuento = 0;
        if (vehiculo instanceof VehiculoCarga) { // Aplicar el descuento basado en el tipo de vehículo
            descuento = precioBase * CalculosArriendo.DESCUENTO_CARGA;
        } else if (vehiculo instanceof VehiculoPasajeros) {
            descuento = precioBase * CalculosArriendo.DESCUENTO_PASAJEROS;
        }
        double subtotal = precioBase - descuento;
        double ivaCalculado = precioBase * CalculosArriendo.IVA; 
        double totalAPagar = subtotal + ivaCalculado;
        boleta.add("--- Detalle de Boleta de Arriendo ---");
        boleta.add("Patente: " + vehiculo.getPatente());
        boleta.add("Marca: " + vehiculo.getMarca());
        boleta.add("Modelo: " + vehiculo.getModelo());
        boleta.add("Días de Arriendo: " + vehiculo.getDiasArriendo());
        boleta.add(String.format("Precio Base Arriendo: $%.2f", precioBase));
        boleta.add(String.format("Descuento Aplicado: $%.2f (%.0f%%)", descuento, (vehiculo instanceof VehiculoCarga ? CalculosArriendo.DESCUENTO_CARGA : CalculosArriendo.DESCUENTO_PASAJEROS) * 100));
        boleta.add(String.format("Subtotal: $%.2f", subtotal));
        boleta.add(String.format("IVA (Respecto al precio base) (%.0f%%): $%.2f", CalculosArriendo.IVA * 100, ivaCalculado));
        boleta.add(String.format("Total a Pagar: $%.2f", totalAPagar));
        boleta.add("------------------------------------");
        return boleta;
    }

    private void escribirBoletaEnArchivo(List<String> boleta) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(BOLETAS, true))) {
            for (String linea : boleta) {
                bw.write(linea + "\n");
            }
            bw.write("\n"); // Añadir una línea en blanco entre boletas en el archivo para mejor legibilidad
        } catch (IOException e) {
            System.err.println("Error al escribir la boleta en el archivo '" + BOLETAS + "': " + e.getMessage());
        }
    }

    public List<List<String>> obtenerBoletasGeneradas() {
        return boletasGeneradas;
    }

    public synchronized boolean devolverVehiculo(String patente) {
        Vehiculo vehiculo = buscarVehiculoPorPatente(patente);
        if (vehiculo == null) {
            System.out.println("Error: Vehículo con patente " + patente + " no encontrado.");
            return false;
        }

        if (!vehiculo.isEstaArrendado()) {
            System.out.println("Error: El vehículo con patente " + patente + " no estaba arrendado.");
            return false;
        } // Buscar el arrendador que tiene este vehículo
        Arrendador arrendadorActual = null;
        for (Arrendador arr : listaArrendadores.values()) {
            if (arr.getVehiculoArrendado() != null && arr.getVehiculoArrendado().getPatente().equalsIgnoreCase(patente)) {
                arrendadorActual = arr;
                break;
            }
        }
        if (arrendadorActual != null) {
            arrendadorActual.setVehiculoArrendado(null); // Desvincular el vehículo del arrendador
            System.out.println("Vehículo desvinculado de " + arrendadorActual.getNombre() + ".");
        } else {
            System.err.println("Advertencia: Vehículo " + patente + " estaba marcado como arrendado pero no se encontró un arrendador asociado. (Posible inconsistencia)");
        }
        vehiculo.setEstaArrendado(false);
        registrarHistorialArriendo(patente, "DEVUELTO");
        System.out.println("Vehículo " + patente + " devuelto exitosamente.");
        return true;
    }

    public synchronized ConcurrentHashMap<String, Arrendador> getListaArrendadores() {
        return listaArrendadores;
    }
    
    public synchronized void listarArrendadores() {
        if (listaArrendadores.isEmpty()) {
            System.out.println("No hay arrendadores registrados.");
            return;
        }
        System.out.println("\n--- Lista de Arrendadores ---");
        for (Arrendador arrendador : listaArrendadores.values()) {
            arrendador.mostrarDatos();
        }
        System.out.println("-----------------------------");
    }
}