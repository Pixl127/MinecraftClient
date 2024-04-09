package de.pixl.pixlclient.cosmetics.cape;

import net.minecraft.resources.ResourceLocation;

public interface Cape {

    String name();

    ResourceLocation getResourceLocation();

    default int getDelay() {
        return 0;
    }

    default boolean isAnimated() {
        return false;
    }

    static Cape fromName(String name) {
        return new CapeLoader().load(name);
    }
}
