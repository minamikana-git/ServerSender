package org.hotal.serversender;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ServerSender extends JavaPlugin implements Listener {
    private String destinationServer = "destination_server_name";
    private Location destinationLocation = new Location(Bukkit.getWorld("world"), 0, 64, 0);

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.getLocation().distance(destinationLocation) < 1) {
            teleportPlayer(player);
        }
    }

    private void teleportPlayer(Player player) {
        // Send player to destination server
        player.sendMessage("Teleporting to " + destinationServer);
        new BukkitRunnable() {
            @Override
            public void run() {
                // Get destination server's Velocity and send to player
                player.sendPluginMessage(ServerTeleportPlugin.this, "Velocity", destinationServer.getBytes());
                // Teleport player to destination location
                player.teleport(destinationLocation);
            }
        }.runTaskLater(this, 20L);
    }
}
