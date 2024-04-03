package de.pixl.pixlclient.cosmetics.capes;

import net.minecraft.resources.ResourceLocation;

public class TestCape implements Cape{
    @Override
    public String name() {
        return "test";
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return new ResourceLocation("pixl/capes/cape.png");
    }
}
