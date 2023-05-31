package cdu.diploma.mediamonitoring.external.api;

import cdu.diploma.mediamonitoring.domain.model.ApiCredentials;
import cdu.diploma.mediamonitoring.domain.model.User;
import cdu.diploma.mediamonitoring.domain.repo.ApiCredentialsRepo;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;

public class YTApi {
    private final YouTube youtubeService;
    private final ApiCredentialsRepo apiCredentialsRepo;

    public YTApi(ApiCredentialsRepo apiCredentialsRepo) {
        this.apiCredentialsRepo = apiCredentialsRepo;
        youtubeService = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), null)
                .setApplicationName("YT-Monitoring")
                .build();
    }

    public YouTube getYoutubeService() {
        return youtubeService;
    }

    public ApiCredentials getCredentials(User user) {
        return apiCredentialsRepo.findApiCredentialsByUserId(user.getId());
    }
}
