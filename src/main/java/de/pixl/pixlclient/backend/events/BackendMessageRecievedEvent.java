package de.pixl.pixlclient.backend.events;

import de.craftsblock.craftscore.event.Event;

public class BackendMessageRecievedEvent extends Event {

    private final String message;

    public BackendMessageRecievedEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
