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
        //public final static String CONSUMER_KEY = "jgEsvUgMHBUwRMW2V13YUol1P";
        //public final static String CONSUMER_SECRET = "DmsoXAi54OLUzeoNHeejFbiqd7CgHVyXwLvw7wpwyfVXFP6k4p";
        //public final static String ACCESS_TOKEN = "762015787934679041-4fXGdkjodML8RvSsKgOtWzo3Y77QuJD";
        //public final static String ACCESS_TOKEN_SECRET = "4XJgdnBvchC7Jniia0WiaQI0RSKYH26k4tGMYNHJKY6sv";
        return twitterBuilder
                .oAuthConsumer(getCredentials(user).getTwitterConsumerKey(), getCredentials(user).getTwitterConsumerSecret())
                .oAuthAccessToken(getCredentials(user).getTwitterAccessToken(), getCredentials(user).getTwitterAccessTokenSecret())
                .build();
    }

    private ApiCredentials getCredentials(User user) {
        return apiCredentialsRepo.findApiCredentialsByUserId(user.getId());
    }
}
