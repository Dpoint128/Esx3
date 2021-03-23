package ru.lmpx.Esx3.commands.Esx3Command;

import com.iridium.iridiumcolorapi.IridiumColorAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import ru.lmpx.Esx3.Functions;
import ru.lmpx.Esx3.Main;
import ru.lmpx.Esx3.commands.LCommand;
import ru.lmpx.Esx3.commands.LTabCompleter;
import ru.lmpx.Esx3.commands.Esx3Command.subcommands.HelpCommand;
import ru.lmpx.Esx3.commands.Esx3Command.subcommands.ReloadCommand;
import ru.lmpx.Esx3.commands.SubCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Esx3Command implements CommandExecutor, LCommand {

    private final Main plugin;
    private final List<SubCommand> scs;

    public Esx3Command(Main plugin) {
        this.plugin = plugin;
        scs = new ArrayList<>();

        scs.add(new ReloadCommand(plugin));
        scs.add(new HelpCommand(plugin));

        plugin.getLogger().info(Functions.subcmdregistered(name()));
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {

        if (!(sender.hasPermission(Functions.permAll()) || sender.hasPermission(Functions.permRoot() + getPermission()))) {
            Functions.noPermission(sender);
            return true;
        }

        if (args.length == 0) {
            String help = "&1/lmpx &2[ &3reload &2]";
            help = help
                    .replaceAll("&1", "<SOLID:87fff5>")
                    .replaceAll("&2", "<SOLID:59ff9e>")
                    .replaceAll("&3", "<SOLID:fff759>");
            sender.sendMessage(IridiumColorAPI.process(help));
            return true;
        } else {

            List<String> newargs = new ArrayList<>();
            newargs.addAll(Arrays.asList(args));
            newargs.remove(0);

            Iterator<SubCommand> sci = scs.iterator();

            boolean executed = false;

            while (sci.hasNext() && !executed) {
                SubCommand sc = sci.next();

                String[] aliases = sc.aliases();
                boolean isAlias = false;
                for (int i = 0; i < aliases.length; i++) {
                    String alias = aliases[i];
                    if (args[0].equalsIgnoreCase(alias)) {
                        isAlias = true;
                    }
                }
                if (args[0].equalsIgnoreCase(sc.name()) || isAlias) {
                    sc.onCommand(sender, newargs.toArray(new String[newargs.size()]));
                    executed = true;
                }
            }
            if (!executed) {
                Functions.invalidSubcommand(sender);
            }
        }

        return true;
    }

    public void register() {
//        plugin.getCommand(name()).setExecutor(this);
//        plugin.getCommand(name()).setTabCompleter(new LTabCompleter(scs));
//        plugin.getLogger().info(Functions.cmdregistered(name()));
    }

    @Override
    public String getPermission() {
        return "esx3plugin";
    }

    @Override
    public String name() {
        return "Esx3";
    }
}
