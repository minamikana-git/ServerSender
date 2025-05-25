package net.hotamachisubaru.serverSender.command;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import net.kyori.adventure.text.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SendCommand implements SimpleCommand {

    private final ProxyServer server;

    public SendCommand(ProxyServer server) {
        this.server = server;
    }

    @Override
    public void execute(Invocation invocation) {
        // 実行者チェック
        if (!(invocation.source() instanceof Player)) {
            invocation.source().sendMessage(Component.text("このコマンドはゲーム内からのみ実行できます。"));
            return;
        }

        String[] args = invocation.arguments();
        Player executor = (Player) invocation.source();

        // 引数チェック
        if (args.length != 2) {
            executor.sendMessage(Component.text("使い方: /send <player> <server>"));
            return;
        }

        String targetPlayerName = args[0];
        String targetServerName = args[1];

        // 対象プレイヤー取得
        server.getPlayer(targetPlayerName).ifPresentOrElse(targetPlayer -> {
            // サーバー取得
            server.getServer(targetServerName).ifPresentOrElse(regSrv -> {
                // 送信処理
                targetPlayer.createConnectionRequest(regSrv).fireAndForget();
                executor.sendMessage(Component.text(
                        targetPlayerName + " を " + targetServerName + " へ送信しました。"
                ));
            }, () -> {
                executor.sendMessage(Component.text("サーバーが見つかりません: " + targetServerName));
            });
        }, () -> {
            executor.sendMessage(Component.text("プレイヤーが見つかりません: " + targetPlayerName));
        });
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        String[] args = invocation.arguments();

        // 引数 0 個：プレイヤー名全候補
        if (args.length == 0) {
            return server.getAllPlayers().stream()
                    .map(Player::getUsername)
                    .collect(Collectors.toList());
        }

        // 引数 1 個：部分一致でプレイヤー名補完
        if (args.length == 1) {
            String partial = args[0].toLowerCase();
            return server.getAllPlayers().stream()
                    .map(Player::getUsername)
                    .filter(name -> name.toLowerCase().startsWith(partial))
                    .collect(Collectors.toList());
        }

        // 引数 2 個：サーバー名補完
        if (args.length == 2) {
            String partial = args[1].toLowerCase();
            return server.getAllServers().stream()
                    .map(RegisteredServer::getServerInfo)
                    .map(ServerInfo::getName)
                    .filter(name -> name.toLowerCase().startsWith(partial))
                    .collect(Collectors.toList());
        }

        // それ以外
        return Collections.emptyList();
    }
}