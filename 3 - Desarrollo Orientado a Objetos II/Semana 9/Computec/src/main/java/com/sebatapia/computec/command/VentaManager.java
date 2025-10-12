// package com.sebatapia.computec.command;
package com.sebatapia.computec.command;

import com.sebatapia.computec.modelos.Cliente;
import com.sebatapia.computec.modelos.Equipo;
import com.sebatapia.computec.modelos.Venta;

/**
 * Receiver y "Modelo de Carrito": Gestiona el estado de la venta en preparación.
 * Mantiene al cliente y al equipo seleccionados antes de finalizar la venta.
 */
public class VentaManager {

    private Cliente clienteSeleccionado;
    private Equipo equipoSeleccionado;

    // Métodos para el Cliente
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

    // Métodos para el Equipo (los que ya teníamos)
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
    
    // Método para crear el objeto Venta final
    /**
     * Valida y crea un objeto Venta a partir de la información actual.
     * @return Un objeto Venta si los datos son válidos, de lo contrario null.
     */
    public Venta crearVenta() {
        if (clienteSeleccionado == null || equipoSeleccionado == null) {
            return null; // No se puede crear la venta si falta información
        }
        // Crea el objeto Venta que luego será guardado por el controlador
        return new Venta(this.clienteSeleccionado, this.equipoSeleccionado);
    }
    
    /**
     * Limpia la selección actual para preparar una nueva venta.
     */
    public void limpiarVentaActual() {
        this.clienteSeleccionado = null;
        this.equipoSeleccionado = null;
    }

    // Getters para que la UI pueda consultar el estado
    public Cliente getClienteSeleccionado() {
        return clienteSeleccionado;
    }
    
    public Equipo getEquipoSeleccionado() {
        return equipoSeleccionado;
    }
}