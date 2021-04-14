package ru.lmpx.Esx3.placeholders;

import me.clip.placeholderapi.PlaceholderAPI;
import net.minecraft.server.v1_16_R2.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import ru.lmpx.Esx3.Functions;
import ru.lmpx.Esx3.Main;

public class Placeholder {


    private static String PH_PING = "\\{ping}";
    private static String PH_TPS = "\\{tps}";
    private static String PH_ONLINE = "\\{online}";
    private static String PH_MOTD = "\\{motd}";
    private static String PH_PLAYER = "\\{player}";
    private static String PH_SLEEPING = "\\{sleeping}";

    public static String setPlaceholders(Player player, String s) {
        Main plugin = Main.getPlugin(Main.class);
        FileConfiguration config = plugin.getConfig();

        int ping = ((CraftPlayer) player).getHandle().ping;
        double tps = MinecraftServer.getServer().recentTps[0];

        return PlaceholderAPI.setPlaceholders(player, s.replaceAll(PH_PING, "<SOLID:" + Functions.intToHexColorInvert(ping, 20, 1000) + ">" + ping)
                .replaceAll(PH_TPS, "<SOLID:" + Functions.intToHexColor(tps, 5, 20) + ">" + String.valueOf(Math.round(tps * 100) / 100))
                .replaceAll(PH_ONLINE, String.valueOf(Bukkit.getServer().getOnlinePlayers().size() - 1))
                .replaceAll(PH_MOTD, PlaceholderAPI.setPlaceholders(player, Functions.colorParse(Main.MOTD)))
                .replaceAll(PH_PLAYER, player.getName())
                .replaceAll(PH_SLEEPING, player.isSleeping() ? Functions.colorParse(config.getString("CustomTAB.sleepStatus")) : ""));
    }

}
