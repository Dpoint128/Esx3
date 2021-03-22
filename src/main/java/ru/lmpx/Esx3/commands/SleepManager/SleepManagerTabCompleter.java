package ru.lmpx.Esx3.commands.SleepManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.lmpx.Esx3.Functions;
import ru.lmpx.Esx3.Main;
import ru.lmpx.Esx3.commands.SleepManager.subcommands.Enable;
import ru.lmpx.Esx3.commands.SleepManager.subcommands.SetSpeed;
import ru.lmpx.Esx3.commands.SubCommand;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SleepManagerTabCompleter implements TabCompleter {
    private final List<SubCommand> scs;
    private final Main plugin;

    public SleepManagerTabCompleter(Main plugin,List<SubCommand> scs) {
        this.scs = scs;
        this.plugin = plugin;
    }


    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        switch (args.length) {
            case 0: {
                return null;
            }
            case 1: {
                List<String> tab = new ArrayList<>();
                Iterator<SubCommand> scsi = scs.iterator();
                while (scsi.hasNext()) {
                    tab.add(scsi.next().name());
                }
                return tab;
            }
            case 2: {

                List<String> tab = new ArrayList<>();
                Iterator<SubCommand> scsi = scs.iterator();
                boolean run = true;
                while (scsi.hasNext()&&run) {
                    String name = scsi.next().name();
                    if(args[0].equalsIgnoreCase(name)){
                        run = false;
                        if(name.equalsIgnoreCase(new SetSpeed(plugin).name())){
                            tab.add("40");
                            tab.add("60");
                            tab.add("80");
                            tab.add("100");
                            tab.add("120");
                            tab.add("150");
                            tab.add("200");
                            return tab;
                        }
                        if(name.equalsIgnoreCase(new Enable(plugin).name())){
                            tab.add("true");
                            tab.add("false");
                            return tab;
                        }
                    }
                }
            }
        }
        return null;
    }
}
