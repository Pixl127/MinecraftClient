package de.pixl.pixlclient.input;

import net.minecraft.client.KeyMapping;

public interface IKey {

    void tick();
    void keyPressed();
    void keyReleased();
}
