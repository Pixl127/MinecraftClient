package de.pixl.pixlclient.cosmetics.cape.capes;

import de.pixl.pixlclient.cosmetics.cape.Cape;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.ConcurrentLinkedQueue;

public class JsonCape implements Cape {

    private final String display;

    private final String name;
    private final ConcurrentLinkedQueue<ResourceLocation> locations;
    private final boolean animated;
    private final int delay;

    public JsonCape(String display, String name, int delay, ConcurrentLinkedQueue<ResourceLocation> locations) {
        this.display = display;
        this.name = name;
        this.locations = locations;
        this.animated = locations.size() >= 2;
        this.delay = delay;
    }

    public String getDisplayName() {
        return display;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        if (!animated) return locations.peek();
        ResourceLocation current = locations.poll();
        locations.add(current);
        return current;
    }

    @Override
    public int getDelay() {
        return delay;
    }

    @Override
    public boolean isAnimated() {
        return animated ;
    }
}
