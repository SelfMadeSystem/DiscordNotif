package uwu.smsgamer.discordnotif;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.*;

public class StringHelper {
    public static String stringifyNoC(final OfflinePlayer player, final String string, final String[] args) {
        return replaceArgsPlaceholders(replacePlaceholders(player, string), args);
    }

    public static String stringify(final OfflinePlayer player, final String string, final String[] args) {
        return replaceArgsPlaceholders(colorize(replacePlaceholders(player, string)), args);
    }

    public static String escape(String s) {
        assert s != null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            switch (ch) {
                case '"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '/':
                    sb.append("\\/");
                    break;
                default:
                    //Reference: http://www.unicode.org/versions/Unicode5.1.0/
                    if (ch <= '\u001F' || ch >= '\u007F' && ch <= '\u009F' || ch >= '\u2000' && ch <= '\u20FF') {
                        String ss = Integer.toHexString(ch);
                        sb.append("\\u");
                        for (int k = 0; k < 4 - ss.length(); k++) {
                            sb.append('0');
                        }
                        sb.append(ss.toUpperCase());
                    } else {
                        sb.append(ch);
                    }
            }
        }
        return sb.toString();
    }

    public static String replacePlaceholders(final OfflinePlayer player, final String string) {
        if (DiscordNotif.papiEnabled) return PlaceholderAPI.setPlaceholders(player, string);
        else return string.replace("%player_name%", player.getName());
    }

    public static String colorize(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    private static String replaceArgsPlaceholders(final String string, final String[] args) {
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
    }
}
