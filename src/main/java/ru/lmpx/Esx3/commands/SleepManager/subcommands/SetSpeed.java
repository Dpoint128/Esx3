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

public class SetSpeed extends SubCommand implements LCommand {

    private final Main plugin;

    public SetSpeed(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onCommand(CommandSender sender, @NotNull String[] args) {

        if (!(sender.hasPermission(Functions.permAll()) || sender.hasPermission(Functions.permRoot() + getPermission()))) {
            Functions.noPermission(sender);
            return;
        }

        File configFile = new File(plugin.getDataFolder() + File.separator + "config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        if (args.length == 0) {
            Functions.invalidArgument(sender);
            return;
        }

        if (Functions.isNumber(args[0])) {
            int argspeed = Integer.parseInt(args[0]);
            int min = 20;
            int max = 200;
            if (argspeed >= min && argspeed <= max) {
                config.set("skipNight.skipSpeed", argspeed);
            } else {
                Functions.lmpxMessage(sender, ChatColor.RED + Functions.getMessage("invalidSkipSpeed")
                        .replaceAll("\\{min}", String.valueOf(min))
                        .replaceAll("\\{max}", String.valueOf(max)));
                return;
            }

            try {
                config.save(configFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            plugin.reloadConfig();
            Functions.lmpxMessage(sender, ChatColor.GREEN + Functions.getMessage("skipSpeedChanged"));
        }
    }

    @Override
    public String getPermission() {
        return "sleepManagerSetSpeed";
    }

    @Override
    public String name() {
        return "speed";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
