package de.pixl.pixlclient.input.keys;

import com.mojang.blaze3d.platform.InputConstants;
import de.pixl.pixlclient.Client;
import de.pixl.pixlclient.input.IKey;
import net.minecraft.client.KeyMapping;

public class ZoomKey implements IKey {

    @Override
    public void tick() {
        Client.minecraft.options.smoothCamera = getMapping().isDown();
    }

    @Override
    public void keyPressed() { }

    @Override
    public void keyReleased() { }

    private static final KeyMapping mapping = new KeyMapping("key.zoom", InputConstants.KEY_C, "key.categories.pixlclient");

    public static KeyMapping getMapping() {
        return mapping;
    }
}
