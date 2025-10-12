package com.sebatapia.computec.command;

import com.sebatapia.computec.modelos.Cliente;
import com.sebatapia.computec.modelos.Equipo;
import com.sebatapia.computec.modelos.Venta;

public class VentaManager {

    private Cliente clienteSeleccionado;
    private Equipo equipoSeleccionado;

    public String seleccionarCliente(Cliente cliente) {
        this.clienteSeleccionado = cliente;
        return "Cliente seleccionado: " + cliente.getNombreCompleto();
    }
    
    public String quitarCliente() {
        if(this.clienteSeleccionado != null) {
            String nombre = this.clienteSeleccionado.getNombreCompleto();
            this.clienteSeleccionado = null;
            return "Cliente '" + nombre + "' desvinculado de la venta.";
        }
        return "No hay ningún cliente seleccionado.";
    }

    public String agregarEquipo(Equipo equipo) {
        this.equipoSeleccionado = equipo;
        return "Equipo '" + equipo.getModelo() + "' agregado a la venta.";
    }

    public String eliminarEquipo() {
        if (equipoSeleccionado != null) {
            String modelo = equipoSeleccionado.getModelo();
            this.equipoSeleccionado = null;
            return "Equipo '" + modelo + "' eliminado de la venta.";
        } else {
            return "No se pudo eliminar: no hay ningún equipo seleccionado.";
        }
    }

    public Venta crearVenta() {
        if (clienteSeleccionado == null || equipoSeleccionado == null) {
            return null; // No se puede crear la venta si falta información
        }
        // Crea el objeto Venta que luego será guardado por el controlador
        return new Venta(this.clienteSeleccionado, this.equipoSeleccionado);
    }
    
    public void limpiarVentaActual() {
        this.clienteSeleccionado = null;
        this.equipoSeleccionado = null;
    }

    public Cliente getClienteSeleccionado() {
        return clienteSeleccionado;
    }
    
    public Equipo getEquipoSeleccionado() {
        return equipoSeleccionado;
    }
}