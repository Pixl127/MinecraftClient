package de.pixl.pixlclient.listeners;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.craftsblock.craftscore.event.EventHandler;
import de.craftsblock.craftscore.event.ListenerAdapter;
import de.craftsblock.craftscore.json.Json;
import de.craftsblock.craftscore.json.JsonParser;
import de.pixl.pixlclient.Client;
import de.pixl.pixlclient.backend.events.BackendMessageRecievedEvent;
import de.pixl.pixlclient.backend.events.BackendReadyEvent;
import de.pixl.pixlclient.cosmetics.capes.Cape;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;

import java.util.Collection;
import java.util.UUID;

public class CosmeticListener implements ListenerAdapter {

    @EventHandler
    public void handleBackendConnect(BackendReadyEvent event)  {
        System.out.println("BACKEND IS READY");
        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        assert connection != null;
        Collection<UUID> uuids = connection.getOnlinePlayerIds();
        assert Minecraft.getInstance().player != null;
        UUID self = Minecraft.getInstance().player.getUUID();
        event.getBackend().send(JsonParser.parse("{}")
                .set("uuid", self.toString())
                .set("cape", (Client.capeManager.getCurrent() != null ? "null" : Client.capeManager.getCurrent().name()))
                .set("data", uuids.stream().filter(uuid -> !uuids.contains(self)).map(UUID::toString).toList()));
    }

    @EventHandler
    public void handleMessage(BackendMessageRecievedEvent event) {
        System.out.println("BACKEND MESSAGE: " + event.getMessage());
        Json json = JsonParser.parse(event.getMessage());
        if (!json.contains("data") && !json.contains("uuid")) return;
        if (json.contains("uuid")) {
            UUID uuid = UUID.fromString(json.getString("uuid"));
            Cape cape = Cape.fromName(json.getString("cape"));
            if (cape == null) return;
            Client.capeManager.setForPlayer(uuid, cape);
            return;
        }
        JsonElement data = json.get("data");
        if (!data.isJsonArray()) return;
        for (JsonElement element : data.getAsJsonArray()) {
            JsonObject object = element.getAsJsonObject();
            UUID uuid = UUID.fromString(object.getAsJsonPrimitive("uuid").getAsString());
            Cape cape = Cape.fromName(object.getAsJsonPrimitive("cape").getAsString());
            if (cape == null) return;
            Client.capeManager.setForPlayer(uuid, cape);
        }
    }
}
