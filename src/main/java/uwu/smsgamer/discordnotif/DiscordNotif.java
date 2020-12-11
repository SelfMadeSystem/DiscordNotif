package uwu.smsgamer.discordnotif;

import me.godead.lilliputian.*;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.*;
import org.bukkit.plugin.java.JavaPlugin;
import uwu.smsgamer.senapi.utils.PlayerUtils;

import java.io.File;
import java.util.List;

// Yes shit code idc
public final class DiscordNotif extends JavaPlugin implements CommandExecutor {

    public static FileConfiguration config;
    public static String noPermission;
    public static String noPermissionNotif;
    public static String basicUsage;
    public static String errorMessage;
    public static String notifTypeNotFound;

    @Override
    public void onEnable() {
        final Lilliputian lil = new Lilliputian(this);
        lil.getDependencyBuilder().addDependency(new Dependency(
          Repository.JITPACK,
          "com.github.True-cc",
          "SenAPI",
          "0.2"
        )).addDependency(new Dependency(
          Repository.MAVENCENTRAL,
          "org.apache.httpcomponents",
          "httpclient",
          "4.5.13"
        )).addDependency(new Dependency(
          Repository.MAVENCENTRAL,
          "com.googlecode.json-simple",
          "json-simple",
          "1.1.1"
        )).loadDependencies();

        getCommand("discordnotif").setExecutor(this);
        loadConfig();
        noPermission = config.getString("messages.no-permission");
        noPermissionNotif = config.getString("messages.no-permission-notif");
        basicUsage = config.getString("messages.basic-usage");
        errorMessage = config.getString("messages.error-message");
        notifTypeNotFound = config.getString("messages.notif-type-not-found");

    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        OfflinePlayer player = PlayerUtils.getPlayer(sender);
        try {
            if (!sender.hasPermission("discordnotif.command")) {
                sender.sendMessage(StringHelper.stringify(player, noPermission, args));
                return true;
            }
            if (args.length == 0) {
                sender.sendMessage(StringHelper.stringify(player, basicUsage, args));
                return true;
            }
            ConfigurationSection section = getSection(args[0]);
            if (section == null) {
                sender.sendMessage(StringHelper.stringify(player, notifTypeNotFound, args));
                return true;
            }
            String sectionName = section.getName();
            if (sender.hasPermission("discordnotif.notif." + sectionName)) {
                if (args.length - 1 >= section.getInt("min-args")) {
                    new DiscordWebHook(
                      section.getString("url"),
                      StringHelper.stringifyNoC(player, section.getString("discord-message"), args)).run();
                    sender.sendMessage(StringHelper.stringify(player, section.getString("success-message"), args));
                } else {
                    sender.sendMessage(StringHelper.stringify(player, section.getString("usage-message"), args));
                }
            } else {
                sender.sendMessage(StringHelper.stringify(player, noPermissionNotif, args));
            }
            return true;
        } catch (Throwable t) {
            t.printStackTrace();
            sender.sendMessage(StringHelper.stringify(player, errorMessage, args));
            return true;
        }
    }

    private ConfigurationSection getSection(String name) {
        String lowerName = name.toLowerCase();
        for (String key : config.getKeys(false)) {
            List<String> stringList = config.getStringList(key + ".aliases");
            if (stringList != null && stringList.stream().anyMatch(s -> s.toLowerCase().equals(lowerName)))
                return config.getConfigurationSection(key);
        }
        return null;
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
