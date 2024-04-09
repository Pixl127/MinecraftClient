package de.pixl.pixlclient;

import de.craftsblock.craftscore.event.ListenerRegistry;
import de.craftsblock.craftscore.json.JsonParser;
import de.pixl.pixlclient.backend.Backend;
import de.pixl.pixlclient.command.CommandManager;
import de.pixl.pixlclient.command.commands.ChangeCapeCommand;
import de.pixl.pixlclient.command.commands.CreeperCommand;
import de.pixl.pixlclient.cosmetics.layer.LayerManager;
import de.pixl.pixlclient.cosmetics.layer.layers.CreeperLayer;
import de.pixl.pixlclient.gui.overlay.OverlayManager;
import de.pixl.pixlclient.gui.overlay.overlays.TestOverlay;
import de.pixl.pixlclient.input.KeyManager;
import de.pixl.pixlclient.input.keys.TestKey;
import de.pixl.pixlclient.input.keys.ZoomKey;
import de.pixl.pixlclient.listeners.CosmeticListener;
import de.pixl.pixlclient.cosmetics.cape.CapeManager;
import de.pixl.pixlclient.listeners.ChatListener;
import de.pixl.pixlclient.listeners.ClientCommandListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Client {

    public static Minecraft minecraft;

    public static CapeManager capeManager = new CapeManager();
    public static LayerManager layerManager = new LayerManager();

    public static Backend backend = new Backend();
    private static final ConcurrentLinkedQueue<UUID> players = new ConcurrentLinkedQueue<>();

    public static final ListenerRegistry listeners = new ListenerRegistry();

    public static final CommandManager commandManager = new CommandManager();
    public static final OverlayManager overlayManager = new OverlayManager();
    public static final KeyManager keyManager = new KeyManager();

    static {
        listeners.register(new CosmeticListener());
        listeners.register(new ClientCommandListener());
        listeners.register(new ChatListener());

        commandManager.register("cape", new ChangeCapeCommand());
        commandManager.register("creeper", new CreeperCommand());

        overlayManager.register("test", new TestOverlay());
        overlayManager.toggleActive("test");

        layerManager.addLayer(CreeperLayer.class);
    }

    private static void init() {
        keyManager.register(TestKey.getMapping(), new TestKey());
        keyManager.register(ZoomKey.getMapping(), new ZoomKey());
        keyManager.inject();
    }

    public static void tick() {
        if (minecraft == null) {
            minecraft = Minecraft.getInstance();
            init();
            return;
        }
        ClientPacketListener connection = minecraft.getConnection();
        if (connection != null && backend.isConnected()) {
            ConcurrentLinkedQueue<UUID> current = new ConcurrentLinkedQueue<>(connection.getOnlinePlayerIds());
            players.removeIf(uuid -> !current.contains(uuid));
            current.removeIf(players::contains);
            if (!current.isEmpty()) {
                backend.send(JsonParser.parse("{}")
                        .set("data", current.stream().map(UUID::toString).toList()));
                players.addAll(current);
            }
        }
        if (connection != null && minecraft.player != null) backend.connect();
        else backend.disconnect();
        capeManager.tick();
        overlayManager.tick();
        keyManager.tick();
    }

    public static void addChat(String message) {
        addChat(Component.literal(message.replace("&", "\u00A7")));
    }

    public static void addChat(Component component) {
        minecraft.gui.getChat().addMessage(component);
    }
}
