package ru.lmpx.Esx3;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import ru.lmpx.Esx3.commands.Esx3Command.Esx3Command;
import ru.lmpx.Esx3.handlers.ChatHandler;

import java.io.File;
import java.io.IOException;

public class Main extends JavaPlugin {

    private TablistThread tbth;
    private SleepCheckThread sleepCheckThread;

    @Override
    public void onEnable() {

        getLogger().info(ChatColor.AQUA + "\n\n" +
                "███████╗░██████╗██╗░░██╗██████╗░     ╔════════════════════════╗\n" +
                "██╔════╝██╔════╝╚██╗██╔╝╚════██╗     ║         Es3x           ║\n" +
                "█████╗░░╚█████╗░░╚███╔╝░░█████╔╝     ║    Version: 1.0.0      ║\n" +
                "██╔══╝░░░╚═══██╗░██╔██╗░░╚═══██╗     ║    Author: LIMPIX31    ║\n" +
                "███████╗██████╔╝██╔╝╚██╗██████╔╝     ║    DS: LIMPIX31#6791   ║\n" +
                "╚══════╝╚═════╝░╚═╝░░╚═╝╚═════╝░     ╚════════════════════════╝\n\n");

        File config = new File(getDataFolder() + File.separator + "config.yml");
        if (!config.exists()) {
            getLogger().info(ChatColor.BLUE + "Creating default config file");
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();
        }


        tbth = new TablistThread(this);
        tbth.start();
        getLogger().info(ChatColor.GREEN + "Tablist updating thread started");

        sleepCheckThread = new SleepCheckThread(this);
        sleepCheckThread.runTaskTimer(this, 0L, 1);


        Bukkit.getPluginManager().registerEvents(new ChatHandler(this), this);


        try {
            Functions.createMessagesFile(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {

        } else {
            getLogger().severe(ChatColor.RED + "Required PlaceholderAPI");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        //command handlers
        Esx3Command esx3Command = new Esx3Command(this);
        esx3Command.register();

    }

    @Override
    public void onDisable() {
        tbth.stop();
        getLogger().info(ChatColor.GREEN + "Tablist updating thread stopped");
    }


}