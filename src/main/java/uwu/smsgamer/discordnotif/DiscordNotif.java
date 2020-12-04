package uwu.smsgamer.discordnotif;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;

public final class DiscordNotif extends JavaPlugin implements CommandExecutor {

    public static FileConfiguration config;
    public static boolean papiEnabled;

    @Override
    public void onEnable() {
        papiEnabled = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
        getCommand("discordnotif").setExecutor(this);
        loadConfig();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            ConfigurationSection section = getSection(args[0]);
            String sectionName = section.getName();
            if (sender.hasPermission("discordnotif." + sectionName)) {
                OfflinePlayer player = sender instanceof OfflinePlayer ? (OfflinePlayer) sender : ConsolePlayer.getInstance();
                if (args.length - 1 >= section.getInt("min-args")) {
                    new DiscordWebHook(section.getString("url"),
                      StringHelper.stringifyNoC(player, section.getString("discord-message"), args)).run();
                    sender.sendMessage(StringHelper.stringify(player, section.getString("success-message"), args));
                } else {
                    sender.sendMessage(StringHelper.stringify(player, section.getString("usage-message"), args));
                }
            } else throw new Exception("No permission");
        } catch (Exception e) {
            if (e.getMessage() == null)
                sender.sendMessage((Arrays.toString(e.getStackTrace()) + "\nReason: " + e.getMessage()).replace("1.12", "1.0").replace("1_12", "1_0"));
            else if (e.getMessage().toLowerCase().contains("no permission"))
                sender.sendMessage(("Error. Reason: " + e.getMessage()).replace("1.12", "1.0").replace("1_12", "1_0"));
            else if (e.getMessage().toLowerCase().contains("no section"))
                sender.sendMessage(("Error. Reason: " + e.getMessage()));
            else
                sender.sendMessage((Arrays.toString(e.getStackTrace()) + "\nReason: " + e.getMessage()).replace("1.12", "1.0").replace("1_12", "1_0"));
        }
        return true;
    }

    private ConfigurationSection getSection(String name) throws Exception {
        String lowerName = name.toLowerCase();
        for (String key : config.getKeys(false))
            if (config.getStringList(key + ".aliases").stream().anyMatch(s -> s.toLowerCase().equals(lowerName)))
                return config.getConfigurationSection(key);
        throw new Exception("No section.");
    }


    /**
     * Used to load configuration file: config.yml
     */
    public void loadConfig() {
        File configFile = new File(this.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            this.saveResource("config.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }
}
