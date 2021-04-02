package ru.lmpx.Esx3;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class SleepCheckThread extends BukkitRunnable {

    private final Main plugin =  Main.getPlugin(Main.class);


    @Override
    public void run() {
        if (plugin.getConfig().getBoolean("skipNight.enable")) {
            World world = Bukkit.getWorld("world");
            if (world.getTime() >= 13000 || world.hasStorm()) {
                List<Player> worldPlayers = world.getPlayers();
                int online = worldPlayers.size();
                int sleeping = 0;
                for (Player player : worldPlayers) {
                    if (player.isSleeping()) sleeping++;
                }
                double threshold = plugin.getConfig().getDouble("skipNight.sleepingPlayersThreshold");
                if (sleeping >= Math.ceil((double) world.getPlayers().size() * threshold)) {
                    if (plugin.getConfig().getBoolean("skipNight.instantSkip")) {
                        world.setTime(0);
                    } else {
                        if (world.hasStorm() && world.getTime() < 13000) {
                            world.setStorm(false);
                        }
                        if (world.hasStorm() && world.getTime() >= 13000) {
                            world.setStorm(false);
                            world.setTime(world.getTime() + plugin.getConfig().getInt("skipNight.skipSpeed"));
                        }
                        if (world.getTime() >= 13000) {
                            world.setTime(world.getTime() + plugin.getConfig().getInt("skipNight.skipSpeed"));
                        }
                    }
                }
            }
        }
    }
}
