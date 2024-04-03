package de.pixl.pixlclient.backend;

import de.craftsblock.craftscore.event.ListenerAdapter;
import de.craftsblock.craftscore.event.ListenerRegistry;
import de.craftsblock.craftscore.json.Json;
import de.pixl.pixlclient.Client;
import de.pixl.pixlclient.backend.events.BackendFailureEvent;
import de.pixl.pixlclient.backend.events.BackendMessageRecievedEvent;
import de.pixl.pixlclient.backend.events.BackendReadyEvent;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;

public class Backend {

    private OkHttpClient client;
    private WebSocket socket;
    private ListenerRegistry listenerRegistry = Client.listeners;

    public void connect() {
        if (isConnected()) return;
        System.out.println("BACKEND CONNECT");
        if (listenerRegistry == null) listenerRegistry = Client.listeners;
        client = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(0, TimeUnit.MILLISECONDS)
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .build();
        Request request = new Request.Builder().url("wss://pixlserver.ignorelist.com/v1/cosmetic").build();
        Backend backend = this;
        socket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
                System.out.println("BACKEND CLOSED");
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
        System.out.println("BACKEND DISCONNECT");
        try {
            if (socket != null) socket.close(1000, "Goodbye!");
            client.dispatcher().executorService().shutdown();
            boolean success = client.dispatcher().executorService().awaitTermination(2, TimeUnit.SECONDS);
            if (!success)
                throw new IllegalStateException("The WenSocket could not be terminated properly!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        socket = null;
        client = null;
    }

    public void send(Json json) {
        send(json.asString());
    }

    public void send(String message) {
        System.out.println("BACKEND SEND MESSAGE" + message);
        if (socket != null) socket.send(message);
    }

    public boolean isConnected() {
        return client != null;
    }

}
