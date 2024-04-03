package de.pixl.pixlclient.input.keys;

import com.mojang.blaze3d.platform.InputConstants;
import de.pixl.pixlclient.Client;
import de.pixl.pixlclient.input.IKey;
import net.minecraft.client.KeyMapping;

public class TestKey implements IKey {

    @Override
    public void tick() { }

    @Override
    public void keyPressed() {
        Client.overlayManager.toggleActive("test");
    }

    @Override
    public void keyReleased() { }

    private static final KeyMapping mapping = new KeyMapping("key.toggleOverlay", InputConstants.KEY_K, "key.categories.pixlclient");

    public static KeyMapping getMapping() {
        return mapping;
    }
}
