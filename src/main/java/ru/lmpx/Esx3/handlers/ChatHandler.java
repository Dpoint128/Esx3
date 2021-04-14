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
import ru.lmpx.Esx3.Functions;
import ru.lmpx.Esx3.Main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ChatHandler implements Listener {

    private final Main plugin = Main.getPlugin(Main.class);


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
        String globalFormat = Functions.colorParse(config.getString("chat.globalFormat"));
        String localFormat = Functions.colorParse(config.getString("chat.localFormat"));

        String globalChatPrefix = config.getString("chat.prefix.chatPrefix.global");
        String localChatPrefix = config.getString("chat.prefix.chatPrefix.local");


        String PH_PLAYER = "\\{player}";
        String PH_MESSAGE = "\\{message}";
        String PH_CHATPREFIX = "\\{chat_prefix}";
        String PH_WORLDPREFIX = "\\{world_prefix}";
        String PH_RECIPIENTS = "\\{recipients}";


        String message;
        if (e.getMessage().charAt(0) == '!' || !local) {
            message = PlaceholderAPI.setPlaceholders(sender, globalFormat);
        } else {
            message = PlaceholderAPI.setPlaceholders(sender, localFormat);
        }

        String world = sender.getWorld().getName();
        String wppath = "chat.prefix.worldPrefix.";

        if (local) {

            message = message.replaceAll(PH_PLAYER, sender.getName());

            if (e.getMessage().charAt(0) == '!') {
                message = message.replaceAll(PH_MESSAGE, ChatColor.WHITE + e.getMessage().substring(1));
                message = message.replaceAll(PH_CHATPREFIX, globalChatPrefix);
                message = message.replaceAll(PH_WORLDPREFIX,
                        "<SOLID:" + config.getString(wppath + "color_" + world) + ">" + config.getString(wppath + world));

                message = Functions.colorParse(message);
                Bukkit.broadcastMessage(message);

            } else {
                message = message.replaceAll(PH_MESSAGE, ChatColor.WHITE + e.getMessage());
                message = message.replaceAll(PH_CHATPREFIX, localChatPrefix);

                List<Player> recipients = new ArrayList<>();
                for (Player target : targets) {
                    message = Functions.colorParse(message);
                    if (target.getWorld().equals(sender.getWorld())) {
                        if(target.getLocation().distance(sender.getLocation()) <= radius){
                            recipients.add(target);
                        }
                    }
                }
                int recipientscount = recipients.size() - 1;
                String rcstring = "";
                if (recipientscount == 0) rcstring += "<SOLID:ff6d2e>";
                rcstring += recipientscount;
                message = message.replaceAll(PH_RECIPIENTS, rcstring);
                Iterator<Player> rci = recipients.iterator();
                while (rci.hasNext()) {
                    rci.next().sendMessage(Functions.colorParse(message));
                }

            }
        } else {
            message = message.replaceAll(PH_WORLDPREFIX,
                    "<SOLID:" + config.getString(wppath + "color_" + world) + ">" + config.getString(wppath + world));

            message = Functions.colorParse(message);
            Bukkit.broadcastMessage(message);
        }

        e.getRecipients().clear();

    }

}
