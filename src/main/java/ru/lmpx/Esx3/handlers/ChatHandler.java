package ru.lmpx.Esx3.handlers;

import com.iridium.iridiumcolorapi.IridiumColorAPI;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import ru.lmpx.Esx3.Main;

import java.util.Set;

public class ChatHandler implements Listener {

    private final Main plugin;

    public ChatHandler(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {

        Set<Player> targets = e.getRecipients();
        Player sender = e.getPlayer();

        FileConfiguration config = plugin.getConfig();
        boolean enabled = config.getBoolean("chat.enabled");
        if (!enabled) return;
        boolean local = config.getBoolean("chat.local");
        int radius = config.getInt("chat.radius");
        boolean worldPrefixEnabled = config.getBoolean("chat.prefix.worldPrefixEnabled");
        boolean chatPrefixEnabled = config.getBoolean("chat.prefix.chatPrefixEnabled");
        String globalFormat = config.getString("chat.globalFormat");
        String localFormat = config.getString("chat.localFormat");

        String globalChatPrefix = config.getString("chat.prefix.chatPrefix.global");
        String localChatPrefix = config.getString("chat.prefix.chatPrefix.local");

        String prefixColor = config.getString("chat.prefixColor");

        String message;
        if (e.getMessage().charAt(0) == '!' || !local) {
            message = PlaceholderAPI.setPlaceholders(sender, globalFormat);
        } else {
            message = PlaceholderAPI.setPlaceholders(sender, localFormat);
        }
        message = "<SOLID:" + prefixColor + ">" + message;

        String world = sender.getWorld().getName();
        String wppath = "chat.prefix.worldPrefix.";

        if (local) {

            message = message.replaceAll("\\{player}", sender.getName());
            if (e.getMessage().charAt(0) == '!') {
                message = message.replaceAll("\\{message}", ChatColor.WHITE + e.getMessage().substring(1));
            } else {
                message = message.replaceAll("\\{message}", ChatColor.WHITE + e.getMessage());
            }


            if (e.getMessage().charAt(0) == '!') {
                if(chatPrefixEnabled)message = message.replaceAll("\\{chat_prefix}", globalChatPrefix);
                else {
                    message = message.replaceAll("\\{chat_prefix}", "");
                }
                if (worldPrefixEnabled) {
                    message = message.replaceAll("\\{world_prefix}",
                            "<SOLID:" + config.getString(wppath + "color_" + world) + ">" + config.getString(wppath + world) + "<SOLID:" + prefixColor + ">");
                } else {
                    message = message.replaceAll("\\{world_prefix}", "");
                }

                message = IridiumColorAPI.process(message);
                Bukkit.broadcastMessage(message);

            } else {
                if(chatPrefixEnabled)message = message.replaceAll("\\{chat_prefix}", localChatPrefix);
                else {
                    message = message.replaceAll("\\{chat_prefix}", "");
                }

                for (Player target : targets) {
                    message = IridiumColorAPI.process(message);
                    if(target.getWorld().equals(sender.getWorld())){
                        if (target.getLocation().distance(sender.getLocation()) <= radius) {
                            target.sendMessage(message);
                        }
                    }
                }

            }
        } else {
            message = "<SOLID:" + prefixColor + ">" + message;
            message = message.replaceAll("\\{world_prefix}",
                    "<SOLID:" + config.getString(wppath + "color_" + world) + ">" + config.getString(wppath + world) + "<SOLID:" + prefixColor + ">");

            Bukkit.broadcastMessage(message);
        }

        e.setCancelled(true);

    }

}
