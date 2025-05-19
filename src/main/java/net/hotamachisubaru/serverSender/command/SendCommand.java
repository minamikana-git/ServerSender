package net.hotamachisubaru.serverSender.command;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import net.kyori.adventure.text.Component;

import java.util.*;
import java.util.stream.Collectors;

public class SendCommand implements SimpleCommand {

    private final ProxyServer server;

    public SendCommand(ProxyServer server) {
        this.server = server;
    }

    @Override
    public void execute(Invocation invocation) {

        if (!(invocation.source() instanceof Player)) {
            invocation.source().sendMessage(Component.text("このコマンドはプレイヤーのみ実行できます。"));
            return;
        }
        Player player = (Player) invocation.source();
        String[] args = invocation.arguments();

        if (args.length != 1) {
            player.sendMessage(Component.text("使い方:/send <server>"));
            return;
        }

        String targetName = args[0];
        Optional<RegisteredServer> optional = server.getServer(targetName);

        if (optional.isEmpty()) {
            player.sendMessage(Component.text("サーバーが見つかりません: "));
            return;
        }

        RegisteredServer target = optional.get();

        player.createConnectionRequest(target).fireAndForget();
        player.sendMessage(Component.text("サーバー" + targetName + "へ接続しました。"));

    }

    @Override
    public List<String> suggest(Invocation invocation) {
        String[] args = invocation.arguments();

        if (args.length <= 1) {
            String partial = args.length == 0 ? "" : args[0].toLowerCase();
            return server.getAllServers().stream()
                    .map(RegisteredServer::getServerInfo)
                    .map(ServerInfo::getName)
                    .filter(name -> name.toLowerCase().startsWith(partial))
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}

