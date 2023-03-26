package org.hotal.serversender;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to use this command.");
            return false;
        }

        if (args.length < 1) {
            sender.sendMessage("Usage: /teleport <server>");
            return false;
        }

        String destinationServer = args[0];
        Player player = (Player) sender;

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(destinationServer);
        player.sendPluginMessage(plugin, "Velocity", out.toByteArray());

        return true;
    }
}
