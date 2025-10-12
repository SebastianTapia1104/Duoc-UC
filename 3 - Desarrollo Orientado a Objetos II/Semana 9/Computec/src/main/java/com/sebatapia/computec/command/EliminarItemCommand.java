// package com.sebatapia.computec.command;
package com.sebatapia.computec.command;

import com.sebatapia.computec.interfaces.Command;

public class EliminarItemCommand implements Command {

    private final VentaManager ventaManager;
    private String resultado; // CAMBIO: Atributo para guardar el mensaje

    public EliminarItemCommand(VentaManager ventaManager) {
        this.ventaManager = ventaManager;
    }

    @Override
    public void execute() {
        // CAMBIO: Captura el mensaje devuelto por el manager
        this.resultado = ventaManager.eliminarEquipo();
    }

    // CAMBIO: Nuevo método para que la UI obtenga el resultado
    public String getResultado() {
        return resultado;
    }
}