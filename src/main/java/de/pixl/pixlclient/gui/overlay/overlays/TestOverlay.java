package de.pixl.pixlclient.gui.overlay.overlays;

import de.pixl.pixlclient.gui.overlay.Overlay;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public class TestOverlay extends Overlay {

    @Override
    public void tick() { }

    @Override
    public void render(GuiGraphics graphics, Font font) {
        graphics.drawString(font, "Hello World!", 10, 10, 16777215);
    }
}
