package com.sebatapia.computec.command;

import com.sebatapia.computec.interfaces.Command;

public class EliminarItemCommand implements Command {

    private final VentaManager ventaManager;
    private String resultado; 

    public EliminarItemCommand(VentaManager ventaManager) {
        this.ventaManager = ventaManager;
    }

    @Override
    public void execute() {
        this.resultado = ventaManager.eliminarEquipo();
    }

    public String getResultado() {
        return resultado;
    }
}