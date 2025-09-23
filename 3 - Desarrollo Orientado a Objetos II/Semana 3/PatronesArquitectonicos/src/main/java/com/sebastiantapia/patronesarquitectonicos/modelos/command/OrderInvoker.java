package com.sebastiantapia.patronesarquitectonicos.modelos.command;

import com.sebastiantapia.patronesarquitectonicos.interfaces.Command;

public class OrderInvoker {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void executeCommand() {
        command.execute();
    }
}