package com.sebastian.drivequestrentals.modelos;

import java.io.Serializable;

public class VehiculoCarga extends Vehiculo implements Serializable {
    
    private double capacidadCargaKg;

    public VehiculoCarga(String patente, String marca, String modelo, int anio, double precioArriendoDia, int diasArriendo, double capacidadCargaKG) {
        super(patente, marca, modelo, anio, precioArriendoDia, diasArriendo);
        this.capacidadCargaKg = capacidadCargaKG;
    }

    // GETTER
    public double getCapacidadCargaKG() {
        return capacidadCargaKg;
    }

    // SETTER
    public void setCapacidadCargaKG(double capacidadCargaKG) {
        this.capacidadCargaKg = capacidadCargaKG;
    }

    // OVERRRIDE MÉTODO ABSTRACTO
    @Override
    public String[] mostrarDatos() {
        // Retorna un array de String con los datos en el orden deseado para la tabla
        return new String[]{
            "Carga",
            getPatente(),
            getMarca(),
            getModelo(),
            String.valueOf(getAnio()),
            String.format("%.2f", getPrecioArriendoDia()),
            String.valueOf(getDiasArriendo()),
            String.format("%.0f KG", capacidadCargaKg), // Dato específico de carga
            isEstaArrendado() ? "Arrendado" : "Disponible"
        };
    }

}