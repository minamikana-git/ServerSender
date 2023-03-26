package org.hotal.serversender;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import org.java.util.logging.Logger;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

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

    public void onProxyInitialization(ProxyInitializeEvent event) {
        task = new Task(this.server, this.logger);
        theTimer.scheduleAtFixedRate(task, new Date(), Config.intervalS * 1000l);
    }
        public void onProxyShutdown(ProxyShutdownEvent event) {
        theTimer.cancel();
    }
}
