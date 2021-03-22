package ru.lmpx.Esx3.commands.SleepManager;

import com.iridium.iridiumcolorapi.IridiumColorAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import ru.lmpx.Esx3.Functions;
import ru.lmpx.Esx3.Main;
import ru.lmpx.Esx3.commands.LCommand;
import ru.lmpx.Esx3.commands.SleepManager.subcommands.Enable;
import ru.lmpx.Esx3.commands.SleepManager.subcommands.SetSpeed;
import ru.lmpx.Esx3.commands.SubCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class SleepManager implements LCommand, CommandExecutor {
    private final Main plugin;
    private final List<SubCommand> scs;

    public SleepManager(Main plugin) {
        this.plugin = plugin;
        scs = new ArrayList<>();

        scs.add(new SetSpeed(plugin));
        scs.add(new Enable(plugin));

        plugin.getLogger().info(Functions.subcmdregistered(name()));
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {

        if (!(sender.hasPermission(Functions.permAll()) || sender.hasPermission(Functions.permRoot() + getPermission()))) {
            Functions.noPermission(sender);
            return true;
        }

        if (args.length == 0) {
            String help = "&1/sleepManager &2[ &3speed &2]";
            help = help
                    .replaceAll("&1", "<SOLID:87fff5>")
                    .replaceAll("&2", "<SOLID:59ff9e>")
                    .replaceAll("&3", "<SOLID:fff759>")
                    .replaceAll("&4", "<SOLID:777777>");
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
        plugin.getCommand(name()).setExecutor(this);
        plugin.getCommand(name()).setTabCompleter(new SleepManagerTabCompleter(plugin,scs));
        plugin.getLogger().info(Functions.cmdregistered(name()));
    }

    @Override
    public String getPermission() {
        return "sleepManager";
    }

    @Override
    public String name() {
        return "sleepManager";
    }
}
