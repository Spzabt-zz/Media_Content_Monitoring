package cdu.diploma.mediamonitoring.external.api;

import cdu.diploma.mediamonitoring.domain.model.ApiCredentials;
import cdu.diploma.mediamonitoring.domain.model.User;
import cdu.diploma.mediamonitoring.domain.repo.ApiCredentialsRepo;
import twitter4j.Twitter;
import twitter4j.Twitter.TwitterBuilder;

public class TwitterApi {
    private final ApiCredentialsRepo apiCredentialsRepo;


    public TwitterApi(ApiCredentialsRepo apiCredentialsRepo) {
        this.apiCredentialsRepo = apiCredentialsRepo;
    }

    public Twitter getTwitterBuilder(User user) {
        TwitterBuilder twitterBuilder = Twitter.newBuilder();
        return twitterBuilder
                .oAuthConsumer(getCredentials(user).getTwitterConsumerKey(), getCredentials(user).getTwitterConsumerSecret())
                .oAuthAccessToken(getCredentials(user).getTwitterAccessToken(), getCredentials(user).getTwitterAccessTokenSecret())
                .build();
    }

    private ApiCredentials getCredentials(User user) {
        return apiCredentialsRepo.findApiCredentialsByUserId(user.getId());
    }
}
