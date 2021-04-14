package ru.lmpx.Esx3;

import com.iridium.iridiumcolorapi.IridiumColorAPI;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.server.v1_16_R2.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import ru.lmpx.Esx3.placeholders.Placeholder;

import java.util.Iterator;
import java.util.List;

public class TablistThread implements Runnable {

    private boolean running = true;

    private final Main plugin = Main.getPlugin(Main.class);


    public void start() {
        Bukkit.getScheduler().runTaskTimer(plugin, this, 20, 20);
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        FileConfiguration config = plugin.getConfig();
        boolean enabled = config.getBoolean("CustomTAB.enabled");
        if (!enabled) return;
        int interval = config.getInt("CustomTAB.interval");
        List<String> headerlist = config.getStringList("CustomTAB.header");
        List<String> footerList = config.getStringList("CustomTAB.footer");
        String playerFormat = config.getString("CustomTAB.playerFormat");
        for (Player player : Bukkit.getOnlinePlayers()) {
            Iterator<String> headeri = headerlist.iterator();
            Iterator<String> footeri = footerList.iterator();
            StringBuilder headersb = new StringBuilder();
            StringBuilder footersb = new StringBuilder();

            while (headeri.hasNext()) {
                headersb.append(Functions.colorParse(Placeholder.setPlaceholders(player, headeri.next())));
                if (headeri.hasNext()) headersb.append("\n");
            }
            String header = headersb.toString();

            while (footeri.hasNext()) {
                footersb.append(Functions.colorParse(Placeholder.setPlaceholders(player, footeri.next())));
                if (footeri.hasNext()) footersb.append("\n");
            }
            String footer = footersb.toString();


            player.setPlayerListHeader(header);
            player.setPlayerListFooter(footer);

            player.setPlayerListName(Placeholder.setPlaceholders(player,playerFormat));

        }
    }
}
