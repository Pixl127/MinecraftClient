package de.pixl.pixlclient.cosmetics.layer;

import com.mojang.logging.LogUtils;
import de.pixl.pixlclient.Client;
import de.pixl.pixlclient.utils.Manager;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import org.slf4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LayerManager extends Manager<UUID, ConcurrentLinkedQueue<Class<? extends Layer>>> {

    private final Logger LOGGER = LogUtils.getLogger();
    private final  ConcurrentLinkedQueue<Class<? extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>>> layers = new ConcurrentLinkedQueue<>();

    public void addLayer(Class<? extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>> layer) {
        if (!layers.contains(layer)) layers.add(layer);
    }

    public void setActive(Class<? extends Layer> layer, boolean active) {
        assert Client.minecraft.player != null;
        UUID uuid = Client.minecraft.player.getUUID();
        setActive(uuid, layer, active);
        Client.backend.sendCosmetics();
    }

    public void setActive(UUID uuid, Class<? extends Layer> layer, boolean active) {
        if (!contains(uuid)) register(uuid, new ConcurrentLinkedQueue<>());
        if (!get(uuid).contains(layer) && active) get(uuid).add(layer);
        if (get(uuid).contains(layer) && !active) get(uuid).remove(layer);
    }

    public boolean isActive(Class<? extends Layer> layer) {
        assert Client.minecraft.player != null;
        UUID uuid = Client.minecraft.player.getUUID();
        return contains(uuid) && get(uuid).contains(layer);
    }

    public boolean isActive(UUID uuid, Class<? extends Layer> layer) {
        return contains(uuid) && get(uuid).contains(layer);
    }

    public void load(PlayerRenderer renderer, EntityModelSet modelSet) {
        for (Class<? extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>> layer : layers)
            try {
                renderer.addLayer(layer.getDeclaredConstructor(RenderLayerParent.class, EntityModelSet.class).newInstance(renderer, modelSet));
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
                LOGGER.error("Layer {} couldn't be processed", layer, e);
            }
    }
}
