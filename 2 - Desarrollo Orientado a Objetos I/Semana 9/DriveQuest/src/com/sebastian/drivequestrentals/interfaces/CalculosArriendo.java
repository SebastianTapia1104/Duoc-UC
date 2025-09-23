package com.sebastian.drivequestrentals.interfaces;

import com.sebastian.drivequestrentals.modelos.Vehiculo;

public interface CalculosArriendo {

    // CONSTANTES
    double IVA = 0.19;
    double DESCUENTO_CARGA = 0.07;
    double DESCUENTO_PASAJEROS = 0.12;

    default double calcularValorBase(Vehiculo vehiculo) {
        return vehiculo.getPrecioArriendoDia() * vehiculo.getDiasArriendo();
    }

    void calcularYMostrarBoleta(Vehiculo vehiculo);
}
