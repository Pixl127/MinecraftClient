package de.pixl.pixlclient.cosmetics.capes;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import de.craftsblock.craftscore.json.JsonParser;
import de.pixl.pixlclient.Client;
import de.pixl.pixlclient.cosmetics.CosmeticManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.UUID;

public class CapeManager implements CosmeticManager<Cape> {

    private Cape cape;
    private Minecraft minecraft;

    @Override
    public void setActive(Cape cape) {
        this.cape = cape;

        assert Minecraft.getInstance().player != null;
        UUID self = Minecraft.getInstance().player.getUUID();
        Client.backend.send(JsonParser.parse("{}")
                .set("uuid", self.toString())
                .set("cape", (cape != null ? "null" : cape.name())));
    }

    @Override
    public Cape getCurrent() {
        return cape;
    }

    @Override
    public void tick() {
        if (minecraft == null) minecraft = Minecraft.getInstance();
    }

    @Override
    public void render() {
        if (cape == null) return;
        AbstractClientPlayer player = minecraft.player;
        if (player == null) return;
        setForPlayer(player.getUUID(), cape);
    }

    @Override
    public void setForPlayer(UUID uuid, Cape cape) {
        ClientPacketListener connection = minecraft.getConnection();
        if (connection == null) return;
        PlayerInfo info = connection.getPlayerInfo(uuid);
        if (info == null) return;
        ResourceLocation location = cape.getResourceLocation();
        if (info.hasTextureLocation(MinecraftProfileTexture.Type.CAPE) && info.getTextureLocation(MinecraftProfileTexture.Type.CAPE).getPath().equals(location.getPath()))
            return;
        info.setTextureLocation(MinecraftProfileTexture.Type.CAPE, cape.getResourceLocation());
    }
}