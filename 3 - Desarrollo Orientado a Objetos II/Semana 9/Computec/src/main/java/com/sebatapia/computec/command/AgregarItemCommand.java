// package com.sebatapia.computec.command;
package com.sebatapia.computec.command;

import com.sebatapia.computec.interfaces.Command;
import com.sebatapia.computec.modelos.Equipo;

public class AgregarItemCommand implements Command {

    private final VentaManager ventaManager;
    private final Equipo equipo;
    private String resultado; // CAMBIO: Atributo para guardar el mensaje

    public AgregarItemCommand(VentaManager ventaManager, Equipo equipo) {
        this.ventaManager = ventaManager;
        this.equipo = equipo;
    }

    @Override
    public void execute() {
        // CAMBIO: Captura el mensaje devuelto por el manager
        this.resultado = ventaManager.agregarEquipo(equipo);
    }

    // CAMBIO: Nuevo método para que la UI obtenga el resultado
    public String getResultado() {
        return resultado;
    }
}