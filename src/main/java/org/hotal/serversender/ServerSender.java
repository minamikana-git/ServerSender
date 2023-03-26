package org.hotal.serversender;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@Plugin(id = "myfirstplugin", name = "My First Plugin", version = "1.0-SNAPSHOT",
        description = "I did it!", authors = {"Me"})
public class ServerSender {
    private final ProxyServer server;
    private final Logger logger;
    public Timer theTimer = new Timer();
    public TimerTask task;
    @Inject
    public ServerSender(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
        logger.info("ServerSender ready.");
    }
    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        task = new Task(this.server, this.logger);
        theTimer.scheduleAtFixedRate(task, new Date(), Config.intervalS * 1000l);
    }
    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        theTimer.cancel();
    }
}