package de.pixl.pixlclient.command.commands;

import de.pixl.pixlclient.Client;
import de.pixl.pixlclient.command.AbstractCommand;
import de.pixl.pixlclient.cosmetics.layer.LayerManager;
import de.pixl.pixlclient.cosmetics.layer.layers.CreeperLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;

import java.util.UUID;

public class CreeperCommand extends AbstractCommand {

    @Override
    protected void execute(String command, String[] args) {
        LayerManager layerManager = Client.layerManager;
        if (args.length != 1) {
            layerManager.setActive(CreeperLayer.class, !layerManager.isActive(CreeperLayer.class));
            chat("&aToggled your Creeper layer!");
            return;
        }
        Minecraft minecraft = Client.minecraft;
        if (minecraft.getConnection() == null) return;
        PlayerInfo info = minecraft.getConnection().getPlayerInfo(args[0]);
        if (info == null) {
            chat("&c This player is not online!");
            return;
        }
        UUID uuid = info.getProfile().getId();
        layerManager.setActive(uuid, CreeperLayer.class, !layerManager.isActive(uuid, CreeperLayer.class));
        chat("&a Toggled Creeper layer for " + args[0] + "!");
    }
}
