package de.pixl.pixlclient.command.commands;

import de.pixl.pixlclient.Client;
import de.pixl.pixlclient.command.AbstractCommand;
import de.pixl.pixlclient.cosmetics.cape.Cape;
import de.pixl.pixlclient.listeners.ClientCommandListener;

public class ChangeCapeCommand extends AbstractCommand {

    ClientCommandListener commandListener = new ClientCommandListener();


    @Override
    protected void execute(String command, String[] args) {
        if (args.length != 1) {
            chat("&cUsage: " + commandListener.COMMAND_PREFIX + "cape<CapeName>");
            return;
        }
        Cape cape = Cape.fromName(args[0]);
        if (cape == null) {
            chat("&cThis Cape does not exist!");
            return;
        }
        Client.capeManager.setActive(cape);
        chat("&aSuccessfully equipped " + args[0]);
    }
}
