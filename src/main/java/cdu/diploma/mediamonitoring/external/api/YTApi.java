package cdu.diploma.mediamonitoring.external.api;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;

public class YTApi {
    public final static String API_KEY = "AIzaSyB8Cx1Rqka876aO1QtkiOcMmjP9cBFFygI";

    private final YouTube youtubeService;

    public YTApi() {
        youtubeService = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), null)
                .setApplicationName("YT-Monitoring")
                .build();
    }

    public YouTube getYoutubeService() {
        return youtubeService;
    }
}
