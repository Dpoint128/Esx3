package ru.lmpx.Esx3.commands.SleepManager.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import ru.lmpx.Esx3.Functions;
import ru.lmpx.Esx3.Main;
import ru.lmpx.Esx3.commands.LCommand;
import ru.lmpx.Esx3.commands.SubCommand;

import java.io.File;
import java.io.IOException;

public class Enable extends SubCommand implements LCommand {

    private final Main plugin;

    public Enable(Main plugin) {
        this.plugin = plugin;
    }


    @Override
    public String getPermission() {
        return "sleepManagerEnable";
    }

    @Override
    public void onCommand(CommandSender sender, @NotNull String[] args) {

        if (!(sender.hasPermission(Functions.permAll()) || sender.hasPermission(Functions.permRoot() + getPermission()))) {
            Functions.noPermission(sender);
            return;
        }

        File configFile = new File(plugin.getDataFolder() + File.separator + "config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        boolean enable = false;

        if (args.length == 0) {
            Functions.invalidArgument(sender);
            return;
        }

        if (args[0].equalsIgnoreCase("true")) enable = true;
        else if (args[0].equalsIgnoreCase("false")) enable = false;
        else {
            Functions.invalidArgument(sender);
            return;
        }
        config.set("skipNight.enable", enable);
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        plugin.reloadConfig();
        if (enable) Functions.lmpxMessage(sender, ChatColor.GREEN + Functions.getMessage("NightSkipEnabled"));
        if (!enable) Functions.lmpxMessage(sender, ChatColor.GREEN + Functions.getMessage("NightSkipDisabled"));

    }

    @Override
    public String name() {
        return "enable";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
