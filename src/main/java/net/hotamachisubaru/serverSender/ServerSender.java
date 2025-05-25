package net.hotamachisubaru.serverSender;

import javax.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.plugin.Plugin;
import net.hotamachisubaru.serverSender.command.SendCommand;


@Plugin(id = "sendcommand", name = "ServerSender", version = "3.4.0-SNAPSHOT")
public class ServerSender {

    private final ProxyServer server;
    private final CommandManager commandManager;

    @Inject
    public ServerSender(ProxyServer server, CommandManager commandManager) {
        this.server = server;
        this.commandManager = commandManager;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {

        this.commandManager.register("send",
                new SendCommand(server)

        );
    }
}
