package de.pixl.pixlclient.listeners;

import com.google.gson.JsonElement;
import de.craftsblock.craftscore.event.EventHandler;
import de.craftsblock.craftscore.event.ListenerAdapter;
import de.craftsblock.craftscore.json.Json;
import de.craftsblock.craftscore.json.JsonParser;
import de.pixl.pixlclient.Client;
import de.pixl.pixlclient.backend.events.BackendMessageRecievedEvent;
import de.pixl.pixlclient.backend.events.BackendReadyEvent;
import de.pixl.pixlclient.cosmetics.cape.Cape;
import de.pixl.pixlclient.cosmetics.layer.layers.CreeperLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CosmeticListener implements ListenerAdapter {

    @EventHandler
    public void handleBackendConnect(BackendReadyEvent event)  {
        System.out.println("BACKEND IS READY");
        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        assert connection != null;
        Collection<UUID> uuids = connection.getOnlinePlayerIds();
        assert Minecraft.getInstance().player != null;
        UUID self = Minecraft.getInstance().player.getUUID();

        ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<>();
        data.put("data", uuids.stream().filter(uuid -> !uuids.contains(self)).map(UUID::toString).toList());
        event.getBackend().sendCosmetics(data);
    }

    @EventHandler
    public void handleBackendMessage(BackendMessageRecievedEvent event) {
        Json json = JsonParser.parse(event.getMessage());
        if (!json.contains("data") && !json.contains("uuid")) return;
        if (json.contains("uuid")) {
            updateCosmetics(UUID.fromString(json.getString("uuid")), json);
            return;
        }
        JsonElement data = json.get("data");
        if (!data.isJsonArray()) return;
        for (JsonElement element : data.getAsJsonArray()) {
            Json target = JsonParser.parse(element);
            updateCosmetics(UUID.fromString(target.getString("uuid")), target);
        }
    }

    private void updateCosmetics(UUID uuid, Json json) {
        if (!json.contains("cosmetics")) return;
        json = JsonParser.parse(json.get("cosmetics"));
        if (json.contains("cape")) {
            Cape cape = Cape.fromName(json.getString("cape"));
            if (cape != null) Client.capeManager.setForPlayer(uuid, cape);
            else Client.capeManager.setForPlayer(uuid, Cape.fromName("reset"));
        }
        if (json.contains("creeper"))
            Client.layerManager.setActive(uuid, CreeperLayer.class, json.getBoolean("creeper"));
    }
}
