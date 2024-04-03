package de.pixl.pixlclient.events;

import de.craftsblock.craftscore.event.Cancelable;
import de.craftsblock.craftscore.event.Event;
import net.minecraft.client.multiplayer.PlayerInfo;

public class ChatRecievedEvent extends Event implements Cancelable {

    private final PlayerInfo player;
    private String message;
    private boolean cancelled = false;

    public ChatRecievedEvent(PlayerInfo player, String message) {
        this.player = player;
        this.message = message;
    }

    public PlayerInfo getPlayer() {
        return player;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
