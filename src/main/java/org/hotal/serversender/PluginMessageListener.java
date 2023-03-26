package org.hotal.serversender;

import org.bukkit.plugin.messaging.PluginMessageListener;

public class VelocityListener implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (channel.equals("Velocity")) {
            // Get velocity information from message
            String destinationServer = new String(message);
            ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(destinationServer);
            player.connect(serverInfo);
        }
    }
}

