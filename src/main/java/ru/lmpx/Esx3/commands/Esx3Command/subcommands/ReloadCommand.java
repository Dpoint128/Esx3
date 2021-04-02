package ru.lmpx.Esx3.commands.Esx3Command.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import ru.lmpx.Esx3.Functions;
import ru.lmpx.Esx3.Main;
import ru.lmpx.Esx3.commands.LCommand;
import ru.lmpx.Esx3.commands.SubCommand;

public class ReloadCommand extends SubCommand implements LCommand {

    private final Main plugin =  Main.getPlugin(Main.class);

    @Override
    public void onCommand(CommandSender sender, @NotNull String[] args) {
        plugin.reloadConfig();
        Functions.pluginMessage(sender, ChatColor.GREEN + Functions.getMessage("configReloaded"));
    }

    @Override
    public String name() {
        return "reload";
    }

    @Override
    public String[] aliases() {
        return new String[]{"rl"};
    }

    @Override
    public String getPermission() {
        return "reloadPlugin";
    }
}
