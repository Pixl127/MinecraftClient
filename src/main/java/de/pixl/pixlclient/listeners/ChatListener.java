package de.pixl.pixlclient.listeners;

import de.craftsblock.craftscore.event.EventHandler;
import de.craftsblock.craftscore.event.ListenerAdapter;
import de.pixl.pixlclient.Client;
import de.pixl.pixlclient.events.ChatSendEvent;

public class ChatListener implements ListenerAdapter {

    @EventHandler
    public void handleChat(ChatSendEvent event) {
        System.out.println("CHAT: " + event.getMessage());
        if (event.getMessage().equalsIgnoreCase("Hallo")) {
            Client.addChat("&6Hallo");
        }
    }
}
