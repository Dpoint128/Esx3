package ru.lmpx.Esx3;

import com.iridium.iridiumcolorapi.IridiumColorAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TablistThread implements Runnable {

    private boolean running = true;

    private final Main plugin =  Main.getPlugin(Main.class);

    public void start() {
        new Thread(this).start();
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        boolean sleepTabStatus = plugin.getConfig().getBoolean("sleepTabStatus");
        while (running) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.setPlayerListName(player.getName() + " " + (sleepTabStatus && player.isSleeping() ? IridiumColorAPI.process("<SOLID:faf58e>Zz") : ""));
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
