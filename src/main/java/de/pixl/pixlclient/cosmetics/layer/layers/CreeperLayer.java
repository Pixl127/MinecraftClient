package de.pixl.pixlclient.cosmetics.layer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import de.pixl.pixlclient.Client;
import de.pixl.pixlclient.cosmetics.layer.Layer;
import de.pixl.pixlclient.cosmetics.layer.utils.PlayerEnergySwirlLayer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;

public class CreeperLayer extends PlayerEnergySwirlLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> implements Layer {

    private final ResourceLocation POWER_LOCATION = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    private final PlayerModel<AbstractClientPlayer> model;

    public CreeperLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer, EntityModelSet modelSet) {
        super(renderer);
        boolean slim = renderer.getModel().slim;
        this.model = new PlayerModel<>(modelSet.bakeLayer(slim ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER), slim);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int p_116972_, AbstractClientPlayer player, float p_116974_, float p_116975_, float p_116976_, float p_116977_, float p_116978_, float p_116979_) {
        if (!Client.layerManager.isActive(player.getUUID(), this.getClass())) return;
        this.model.crouching = player.isCrouching();
        super.render(poseStack, multiBufferSource, p_116972_, player, p_116974_, p_116975_, p_116976_, p_116977_, p_116978_, p_116979_);
    }

    @Override
    protected float xOffset(float x) {
        return x * 0.01F;
    }

    @Override
    protected ResourceLocation getTextureLocation() {
        return POWER_LOCATION;
    }

    @Override
    protected EntityModel<AbstractClientPlayer> model() {
        return model;
    }
}
