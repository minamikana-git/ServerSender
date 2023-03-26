package org.hotal.serversender;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import java.util.Collection;
import java.util.Optional;
import java.util.TimerTask;
import java.util.logging.Logger;

public class Task extends TimerTask {
    ProxyServer server;
    Logger log;
    public Task(ProxyServer server, Logger logger) {
        this.server = server;
        this.log = logger;
    }
    @Override
    public void run() {
        log.info("Checking users...");
        RegisteredServer sendHere = null;
        for(RegisteredServer server: server.getAllServers()) {
            if(server.getServerInfo().getName().equals(Config.serverN)) {
                sendHere = server;
                break;
            }
        }
        if(sendHere != null) {
            Collection<Player> players = server.getAllPlayers();
            for(Player player: players) {
                Optional<ServerConnection> serverOpt = player.getCurrentServer();
                if(serverOpt.isPresent()) {
                    RegisteredServer server = serverOpt.get().getServer();
                    if(!server.getServerInfo().getName().equals(Config.serverN)) {
                        log.info("Sending User " + player.getUsername() + " to server " + Config.serverN);
                        player.createConnectionRequest(sendHere).fireAndForget();
                    }
                }
            }
        }
        log.info("Done!");
    }
}
