package ru.lmpx.Esx3;

import com.iridium.iridiumcolorapi.IridiumColorAPI;
import me.clip.placeholderapi.PlaceholderAPI;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import ru.lmpx.Esx3.placeholders.Placeholder;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public abstract class Functions {

    public static double frtr(double value, double From1, double From2, double To1, double To2) {
        return (value - From1) / (From2 - From1) * (To2 - To1) + To1;
    }

    public static int roundTo(int x, int y) {
        return (int) Math.floor(((x + y) / y)) * y;
    }

    private static String getPluginMessageFinalString(String msg) {
        return ChatColor.BLUE + "[ESX3] " + ChatColor.WHITE + msg;
    }

    public static void pluginMessage(CommandSender sender, String msg) {
        sender.sendMessage(getPluginMessageFinalString(msg));
    }

    public static void pluginMessage(Player player, String msg) {
        player.sendMessage(getPluginMessageFinalString(msg));
    }

    public static void invalidSubcommand(CommandSender sender) {
        pluginMessage(sender, ChatColor.RED + getMessage((Player) sender, "invalidSubcommand"));
    }

    public static void noPermission(CommandSender sender) {
        pluginMessage(sender, ChatColor.RED + getMessage((Player) sender, "noPermission"));
    }

    public static String permRoot() {
        return "esx3.";
    }

    public static String permAll() {
        return "esx3.*";
    }

    public static String cmdregistered(String cmd) {
        return ChatColor.LIGHT_PURPLE + "[Command] " + ChatColor.GREEN + "Registered " + cmd + " command";
    }

    public static String subcmdregistered(String cmd) {
        return ChatColor.LIGHT_PURPLE + "[SubCommand] " + ChatColor.GREEN + "Registered " + cmd + " subcommands";
    }

    public static boolean isNumber(String s) throws NumberFormatException {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void invalidArgument(CommandSender sender) {
        pluginMessage(sender, ChatColor.RED + getMessage((Player) sender, "invalidArgument"));
    }

    public static void createMessagesFile() throws IOException {
        Main plugin = Main.getPlugin(Main.class);
        String lang = plugin.getConfig().getString("messagesLang");

        if (!(lang.equalsIgnoreCase("ru") ||
                lang.equalsIgnoreCase("en"))) {
            plugin.getLogger().severe(ChatColor.DARK_RED + "Incorrect or unsupported language (" + lang + "). Disabling plugin");
            Bukkit.getPluginManager().disablePlugin(plugin);
            return;
        }

        File messages = new File(plugin.getDataFolder() + File.separator + ("messages_" + lang + ".yml"));
        if (!messages.exists()) {
            try {
                messages.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            InputStream is = plugin.getClass().getClassLoader()
                    .getResourceAsStream("messages_" + lang + ".yml");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            StringBuilder defaultmessages = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                defaultmessages.append(line + "\n");
                line = br.readLine();
            }
            FileWriter messagesFile = new FileWriter(messages.getPath(), false);
            messagesFile.write(defaultmessages.toString());
            messagesFile.flush();

            plugin.getLogger().info(ChatColor.GREEN + "Created new \"" + lang + "\" messages file");
        }
    }

    public static String getMessage(Player player, String path) {
        Main plugin = Main.getPlugin(Main.class);
        String lang = plugin.getConfig().getString("messagesLang");

        if (!(lang.equalsIgnoreCase("ru") ||
                lang.equalsIgnoreCase("en"))) {
            plugin.getLogger().severe(ChatColor.DARK_RED + "Incorrect or unsupported language (" + lang + "). Disabling plugin");
            Bukkit.getPluginManager().disablePlugin(plugin);
            return "";
        }

        File messagesFile = new File(plugin.getDataFolder() + File.separator + ("messages_" + lang + ".yml"));
        if (!messagesFile.exists()) {
            try {
                createMessagesFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        YamlConfiguration messages = YamlConfiguration.loadConfiguration(messagesFile);
        return PlaceholderAPI.setPlaceholders(player, Placeholder.setPlaceholders(player, colorParse(messages.getString(path))));

    }

    public static String colorParse(String s) {
        return IridiumColorAPI.process(ChatColor.translateAlternateColorCodes('&', s));
    }

    public static String intToHexColor(double x, double min, double max) {
        int maxColor = (int) Math.round(Functions.frtr(x, min, max, 0, 510));

        if (x < min) return "ff0000";
        if (x > max) return "00ff00";

        int red = 255;
        int green = 0;

        for (int i = 0; i < maxColor; i++) {
            if (green < 255) {
                green++;
            } else {
                red--;
            }
        }

        return StringUtils.leftPad(Integer.toHexString(red), 2, "0") + StringUtils.leftPad(Integer.toHexString(green), 2, "0") + "00";
    }

    public static String intToHexColorInvert(double x, double min, double max) {
        int maxColor = (int) Math.round(Functions.frtr(x, min, max, 0, 510));

        if (x < min) return "00ff00";
        if (x > max) return "ff0000";

        int red = 0;
        int green = 255;

        for (int i = 0; i < maxColor; i++) {
            if (red < 255) {
                red++;
            } else {
                green--;
            }
        }

        return StringUtils.leftPad(Integer.toHexString(red), 2, "0") + StringUtils.leftPad(Integer.toHexString(green), 2, "0") + "00";
    }

    public static String getMOTD() {
        Main plugin = Main.getPlugin(Main.class);
        FileConfiguration config = plugin.getConfig();

        String url = config.getString("CustomTAB.MOTDUrl");

        URL obj = null;
        try {
            obj = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) obj.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            return "";
        }
        String inputLine = "";
        StringBuffer response = new StringBuffer();

        while (true) {
            try {
                if ((inputLine = in.readLine()) == null) break;
            } catch (IOException e) {
                return "";
            }
            response.append(inputLine);
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (response == null) return "";
        return response.toString();

    }

}
