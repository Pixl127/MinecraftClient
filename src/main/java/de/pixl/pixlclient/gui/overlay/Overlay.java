package de.pixl.pixlclient.gui.overlay;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public abstract class Overlay {

    public boolean active  = false;
    public abstract void tick();
    public abstract void render(GuiGraphics graphics, Font font);

    public boolean shouldRenderInGui() {
        return false;
    }

}
