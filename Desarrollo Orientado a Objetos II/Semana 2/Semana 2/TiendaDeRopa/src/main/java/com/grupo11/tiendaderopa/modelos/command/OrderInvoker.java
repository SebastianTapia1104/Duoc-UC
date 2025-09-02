package com.grupo11.tiendaderopa.modelos.command;

import com.grupo11.tiendaderopa.interfaces.Command;
import java.util.ArrayList;
import java.util.List;

public class OrderInvoker {
    private final List<Command> commandList = new ArrayList<>();

    public void addCommand(Command command) {
        commandList.add(command);
    }

    public void executeCommands() {
        for (Command command : commandList) {
            command.ejecutar();
        }
    }
}