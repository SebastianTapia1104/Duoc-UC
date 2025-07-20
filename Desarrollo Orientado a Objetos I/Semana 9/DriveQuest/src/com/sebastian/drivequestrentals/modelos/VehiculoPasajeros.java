package com.sebastian.drivequestrentals.modelos;

import java.io.Serializable;

public class VehiculoPasajeros extends Vehiculo implements Serializable  {

    // ATRIBUTOS HIJO
    private int numeroMaximoPasajeros; // Número máximo de pasajeros

    // CONSTRUCTOR VACÍO
    public VehiculoPasajeros() {
        super();
    }

    // CONSTRUCTOR SOBRECARGADO
    public VehiculoPasajeros(String patente, String marca, String modelo, int anio, double precioArriendoDia, int diasArriendo, int numeroMaximoPasajeros) {
        super(patente, marca, modelo, anio, precioArriendoDia, diasArriendo);
        this.numeroMaximoPasajeros = numeroMaximoPasajeros;
    }

    // GETTER
    public int getNumeroMaximoPasajeros() {
        return numeroMaximoPasajeros;
    }

    //SETTER
    public void setNumeroMaximoPasajeros(int numeroMaximoPasajeros) {
        this.numeroMaximoPasajeros = numeroMaximoPasajeros;
    }

    // OVERRIDE MÉTODO ABSTRACTO
    @Override
    public String[] mostrarDatos() {
        return new String[]{
            "Pasajeros",
            getPatente(),
            getMarca(),
            getModelo(),
            String.valueOf(getAnio()),
            String.format("%.2f", getPrecioArriendoDia()),
            String.valueOf(getDiasArriendo()),
            String.format("%d P.", numeroMaximoPasajeros), // Dato específico de pasajeros
            isEstaArrendado() ? "Arrendado" : "Disponible"
        };
    }
}