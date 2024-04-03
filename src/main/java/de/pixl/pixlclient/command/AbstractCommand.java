package de.pixl.pixlclient.command;

import de.pixl.pixlclient.Client;

public abstract class AbstractCommand {

    protected abstract void execute(String command, String[] args);

    public final void chat(String message) {
        Client.addChat(message);

    }
}
