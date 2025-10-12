package com.sebatapia.computec.command;

import com.sebatapia.computec.interfaces.Command;
import com.sebatapia.computec.modelos.Equipo;

public class AgregarItemCommand implements Command {

    private final VentaManager ventaManager;
    private final Equipo equipo;
    private String resultado; 

    public AgregarItemCommand(VentaManager ventaManager, Equipo equipo) {
        this.ventaManager = ventaManager;
        this.equipo = equipo;
    }

    @Override
    public void execute() {
        this.resultado = ventaManager.agregarEquipo(equipo);
    }

    public String getResultado() {
        return resultado;
    }
}