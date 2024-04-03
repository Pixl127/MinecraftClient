package de.pixl.pixlclient.gui.overlay;

import de.pixl.pixlclient.Client;
import de.pixl.pixlclient.utils.Manager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class OverlayManager extends Manager<String, Overlay> {

    public void setActive(String key, boolean active) {
        key = key.trim().toLowerCase();
        if (!contains(key)) return;
        get(key).active = active;
    }

    public boolean isActive(String key) {
        return get(key).active;
    }

    public void toggleActive(String key) {
        setActive(key, !isActive(key));
    }

    public void tick() {
        values().forEach(overlay -> {
            if (overlay.active) overlay.tick();
        });
    }

    public void render(GuiGraphics graphics) {
        Minecraft minecraft = Client.minecraft;
        values().forEach(overlay -> {
            if (overlay.active && (overlay.shouldRenderInGui() || minecraft.level != null) && !minecraft.options.renderDebug)
                overlay.render(graphics, minecraft.font);
        });
    }
}