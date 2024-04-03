package de.pixl.pixlclient.events;

import de.craftsblock.craftscore.event.Cancelable;
import de.craftsblock.craftscore.event.Event;

public class CommandSendEvent extends Event implements Cancelable {

    private final String command;
    private boolean cancelled = false;

    public CommandSendEvent(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}
