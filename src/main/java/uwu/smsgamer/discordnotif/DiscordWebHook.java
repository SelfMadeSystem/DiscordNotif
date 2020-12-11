package uwu.smsgamer.discordnotif;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class DiscordWebHook implements Runnable {
    private final String webHookUrl;
    private final String message;

    public DiscordWebHook(String webHookUrl, String message) {
        this.webHookUrl = webHookUrl;
        this.message = message;
    }

    @Override
    public void run() {
        try {
            URL url = new URL(webHookUrl);

            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

            con.setRequestMethod("POST");

            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux i686) Gecko/20071127 Firefox/2.0.0.11");
            con.setDoOutput(true);

            String param = "{\"content\": \"" + StringHelper.escape(message) + "\"}";

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = param.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            con.getResponseMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}