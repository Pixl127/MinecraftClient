package de.pixl.pixlclient.backend.events;

import de.craftsblock.craftscore.event.Event;
import de.pixl.pixlclient.backend.Backend;

public class BackendReadyEvent extends Event {

    private final Backend backend;

    public BackendReadyEvent(Backend backend) {
        this.backend = backend;

    }

    public Backend getBackend() {
        return backend;
    }
}
