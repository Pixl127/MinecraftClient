package de.pixl.pixlclient.cosmetics.capes;

import net.minecraft.resources.ResourceLocation;

public interface Cape {

    String name();

    ResourceLocation getResourceLocation();

    static Cape fromName(String name) {
        return switch (name.toLowerCase()) {
            case "test" -> new TestCape();
            default -> null;
        };
    }
}
