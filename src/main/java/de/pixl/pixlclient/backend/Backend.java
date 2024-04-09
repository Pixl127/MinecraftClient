package de.pixl.pixlclient.backend;

import com.google.gson.JsonElement;
import de.craftsblock.craftscore.event.ListenerRegistry;
import de.craftsblock.craftscore.json.Json;
import de.craftsblock.craftscore.json.JsonParser;
import de.pixl.pixlclient.Client;
import de.pixl.pixlclient.backend.events.BackendFailureEvent;
import de.pixl.pixlclient.backend.events.BackendMessageRecievedEvent;
import de.pixl.pixlclient.backend.events.BackendReadyEvent;
import de.pixl.pixlclient.cosmetics.layer.layers.CreeperLayer;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class Backend {

    private OkHttpClient client;
    private WebSocket socket;
    private ListenerRegistry listenerRegistry = Client.listeners;

    public void connect() {
        if (isConnected()) return;
        if (listenerRegistry == null) listenerRegistry = Client.listeners;
        client = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(0, TimeUnit.MILLISECONDS)
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .build();
        Request request = new Request.Builder().url("ws://pixlserver.ignorelist.com:5001/v1/cosmetic").build();
        Backend backend = this;
        socket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
                disconnect();
            }

            @Override
            public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
                try {
                    listenerRegistry.call(new BackendFailureEvent());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
                if (text.equalsIgnoreCase("ping")) {
                    send("{}");
                    return;
                }
                try {
                    System.out.println("MESSAGE: " + text);
                    listenerRegistry.call(new BackendMessageRecievedEvent(text));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
                try {
                    listenerRegistry.call(new BackendReadyEvent(backend));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void disconnect() {
        if (!isConnected()) return;
        if (listenerRegistry == null) listenerRegistry = Client.listeners;
        try {
            if (socket != null) socket.close(1000, "Goodbye!");
            client.dispatcher().executorService().shutdown();
            boolean success = client.dispatcher().executorService().awaitTermination(2, TimeUnit.SECONDS);
            if (!success)
                throw new IllegalStateException("The WebSocket could not be terminated properly!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        socket = null;
        client = null;
    }

    public void sendCosmetics() {
        sendCosmetics(null);
    }

    public void sendCosmetics(ConcurrentHashMap<String, Object> additional) {
        assert Client.minecraft.player != null;
        UUID uuid = Client.minecraft.player.getUUID();
        ConcurrentHashMap<String, Object> cosmetics = new ConcurrentHashMap<>();
        cosmetics.put("cape", Client.capeManager.getCurrent() == null ? "-1" : Client.capeManager.getCurrent().name());
        cosmetics.put("creeper", Client.layerManager.isActive(CreeperLayer.class));
        Json json = JsonParser.parse("{}");
        json.set("uuid", uuid.toString());
        json.set("cosmetics", bakeData(cosmetics));
        if (additional != null) additional.forEach(json::set);
        send(json);
    }

    public void send(Json json) {
        System.out.println("SEND: " + json.toString());
        send(json.asString());
    }

    public void send(String message) {
        if (socket != null) socket.send(message);
    }

    public boolean isConnected() {
        return client != null;
    }

    private JsonElement bakeData(ConcurrentHashMap<String, Object> elements) {
        Json object = JsonParser.parse("{}");
        elements.forEach(object::set);
        return object.getObject();
    }
}