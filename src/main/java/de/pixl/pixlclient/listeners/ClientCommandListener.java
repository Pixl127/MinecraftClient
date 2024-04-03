package de.pixl.pixlclient.listeners;

import de.craftsblock.craftscore.event.EventHandler;
import de.craftsblock.craftscore.event.EventPriority;
import de.craftsblock.craftscore.event.ListenerAdapter;
import de.pixl.pixlclient.Client;
import de.pixl.pixlclient.events.ChatSendEvent;

import java.util.Arrays;

public class ClientCommandListener implements ListenerAdapter {

    public final String COMMAND_PREFIX = "!";

    @EventHandler(priority = EventPriority.LOWEST)
    public void handleChatSend(ChatSendEvent event) {
        String message = replaceSpaceAtStringStart(event.getMessage());
        if (message.startsWith(COMMAND_PREFIX)) {
            event.setCancelled(true);
            message = message.replaceFirst(COMMAND_PREFIX, "");
            String[] args = message.split(" ");
            String command = args[0];
            args = Arrays.stream(args).skip(1).toArray(String[]::new);
            Client.commandManager.perform(command, args);
        }
    }

    private  String replaceSpaceAtStringStart(String s) {
        while (s.startsWith(" ")) s = s.substring(1);
        return s;
    }
}
