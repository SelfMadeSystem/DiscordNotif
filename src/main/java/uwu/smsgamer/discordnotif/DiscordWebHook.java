package uwu.smsgamer.discordnotif;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;

import java.io.IOException;

public class DiscordWebHook implements Runnable {
    private final String webHookUrl;
    private final String message;

    public DiscordWebHook(String webHookUrl, String message) {
        this.webHookUrl = webHookUrl;
        this.message = message;
    }

    @Override
    public void run() {
        HttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpPost thePost = new HttpPost(webHookUrl);
            StringEntity params = new StringEntity("{\"content\": \"" + JSONObject.escape(message) + "\"}");
            thePost.addHeader("content-type", "application/json");
            thePost.addHeader("User-Agent", "Mozilla/5.0 (X11; U; Linux i686) Gecko/20071127 Firefox/2.0.0.11");
            thePost.setEntity(params);
            httpClient.execute(thePost);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}