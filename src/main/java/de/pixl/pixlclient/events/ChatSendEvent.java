package de.pixl.pixlclient.events;

import de.craftsblock.craftscore.event.Cancelable;
import de.craftsblock.craftscore.event.Event;

public class ChatSendEvent extends Event implements Cancelable {

    private final String message;
    private boolean cancelled = false;

    public ChatSendEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}