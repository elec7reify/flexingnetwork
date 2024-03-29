package com.flexingstudios.flexingnetwork;

import com.flexingstudios.flexingnetwork.api.util.Messages;
import com.flexingstudios.flexingnetwork.impl.player.FlexPlayer;
import com.flexingstudios.flexingnetwork.api.FlexingNetwork;
import com.flexingstudios.flexingnetwork.api.util.ChatUtil;
import com.flexingstudios.flexingnetwork.api.util.Utils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.logging.Logger;

public class FlexingChat implements Listener {
        @EventHandler
        private void onChat(AsyncPlayerChatEvent event) {
            if (FlexingNetwork.INSTANCE.features().CHANGE_CHAT.isEnabled()) {
                event.setCancelled(true);
                Player player = event.getPlayer();
                FlexPlayer flPlayer = FlexPlayer.get(player);
                BaseComponent rank = ChatUtil.createMessage(Utils.colored("&7«" + flPlayer.getRank().getDisplayName() + "&7» "));

                BaseComponent playerName = ChatUtil.createMessage(player.getName(),
                        new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/actions " + player.getName()),
                        new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§7Нажмите, чтобы открыть меню быстрых действий")),
                        ": ", ComponentBuilder.FormatRetention.NONE);

                BaseComponent message = ChatUtil.createMessage(Utils.colored(event.getMessage()));

                BaseComponent[] components = new BaseComponent[]{rank, playerName, message};

                Logger.getGlobal().info(Utils.colored("«" + flPlayer.getRank().getName() + "» " + player.getName() + ": " + event.getMessage()));
                for (Player player1 : Bukkit.getOnlinePlayers()) {
                    player1.sendMessage(Messages.CHAT_FORMAT.toMessage());
                    player1.spigot().sendMessage(ChatMessageType.CHAT, components);
                }
            }
    }
}
