package cdu.diploma.mediamonitoring.external.api;

import cdu.diploma.mediamonitoring.model.ApiCredentials;
import cdu.diploma.mediamonitoring.model.User;
import cdu.diploma.mediamonitoring.repo.ApiCredentialsRepo;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;

public class YTApi {
    public final static String API_KEY = "AIzaSyB8Cx1Rqka876aO1QtkiOcMmjP9cBFFygI";

    private final YouTube youtubeService;
    private final User user;
    private final ApiCredentialsRepo apiCredentialsRepo;

    public YTApi(User user, ApiCredentialsRepo apiCredentialsRepo) {
        this.user = user;
        this.apiCredentialsRepo = apiCredentialsRepo;
        youtubeService = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), null)
                .setApplicationName("YT-Monitoring")
                .build();
    }

    public YouTube getYoutubeService() {
        return youtubeService;
    }

    public ApiCredentials getCredentials() {

        return apiCredentialsRepo.findApiCredentialsByUser(user);
    }
}
