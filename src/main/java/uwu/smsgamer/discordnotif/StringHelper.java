package uwu.smsgamer.discordnotif;

import org.bukkit.*;
import uwu.smsgamer.senapi.SenAPI;
import uwu.smsgamer.senapi.utils.StringUtils;

public class StringHelper {
    private static StringUtils utils;

    public static void setup(SenAPI api) {
        utils = api.getStringUtils();
    }

    public static String stringifyNoC(final OfflinePlayer player, final String string, final String[] args) {
        return utils.replaceArgsPlaceholders(utils.replacePlaceholders(player, string), args);
    }

    public static String stringify(final OfflinePlayer player, final String string, final String[] args) {
        return utils.replaceArgsPlaceholders(utils.colorize(utils.replacePlaceholders(player, string)), args);
    }

    /*public static String papi(final OfflinePlayer player, final String string) {
        if (DiscordNotif.papiEnabled) return PlaceholderAPI.setPlaceholders(player, string);
        else return string.replace("%player_name%", player.getName());
    }

    public static String colorize(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    private static String replaceArgs(final String string, final String[] args) {
        StringBuilder sb = new StringBuilder();
        int at = 0;
        while (string.length() > at) {
            int current = at;
            boolean append = true;
            if (string.charAt(at) == '%') {
                int i = 0;
                while (string.charAt(++at) >= '0' && string.charAt(at) <= '9') i += string.charAt(at) - '0';
                switch (string.charAt(at)) {
                    case '-':
                        int j = 0;
                        while (Character.isDigit(string.charAt(++at))) j += string.charAt(at) - '0';
                        j = Math.min(j, args.length);
                        if (string.charAt(at) == '%') {
                            append = false;
                            for (; i < j; i++) sb.append(args[i]).append(i == j - 1 ? "" : " ");
                        }
                        break;
                    case '+':
                        if (string.charAt(++at) == '%') {
                            append = false;
                            for (; i < args.length; i++) sb.append(args[i]).append(i == args.length - 1 ? "" : " ");
                        }
                        break;
                    case '%':
                        append = false;
                        if (i < args.length) sb.append(args[i]);
                }
            }
            if (append) sb.append(string, current, at + 1);
            at++;
        }
        return sb.toString();
    }*/
}
