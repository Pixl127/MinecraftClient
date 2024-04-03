package de.pixl.pixlclient.command;

import de.pixl.pixlclient.Client;
import de.pixl.pixlclient.utils.Manager;

public class CommandManager  extends Manager<String, AbstractCommand> {

    public void perform(String command, String[] args) {
        if (!contains(command.toLowerCase().trim())) {
            Client.addChat("&cThis Command does not exist!");
            return;
        }
        get(command.toLowerCase().trim()).execute(command, args);
    }
}