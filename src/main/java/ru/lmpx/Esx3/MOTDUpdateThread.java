package ru.lmpx.Esx3;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

public class MOTDUpdateThread implements Runnable {

    Main plugin = Main.getPlugin(Main.class);
    boolean running = true;

    public void start() {
        new Thread(this).start();
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        FileConfiguration config = plugin.getConfig();
        while (running){
            Main.MOTD = Functions.getMOTD();
            try {
                Thread.sleep(config.getInt("CustomTAB.MOTDUpdateInterval"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
