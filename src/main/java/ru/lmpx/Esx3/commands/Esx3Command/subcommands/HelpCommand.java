package ru.lmpx.Esx3.commands.Esx3Command.subcommands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import ru.lmpx.Esx3.Functions;
import ru.lmpx.Esx3.Main;
import ru.lmpx.Esx3.commands.LCommand;
import ru.lmpx.Esx3.commands.SubCommand;

public class HelpCommand extends SubCommand implements LCommand {
    private final Main plugin;

    public HelpCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onCommand(CommandSender sender, @NotNull String[] args) {
        Functions.lmpxMessage(sender, "some help string");
    }

    @Override
    public String name() {
        return "help";
    }

    @Override
    public String[] aliases() {
        return new String[]{"?", "h"};
    }

    @Override
    public String getPermission() {
        return "help";
    }
}
