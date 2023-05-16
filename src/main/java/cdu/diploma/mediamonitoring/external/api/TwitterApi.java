package cdu.diploma.mediamonitoring.external.api;

import cdu.diploma.mediamonitoring.domain.model.ApiCredentials;
import cdu.diploma.mediamonitoring.domain.model.User;
import cdu.diploma.mediamonitoring.domain.repo.ApiCredentialsRepo;
import twitter4j.Twitter;
import twitter4j.Twitter.TwitterBuilder;

public class TwitterApi {
    private User user;
    private final ApiCredentialsRepo apiCredentialsRepo;
    public final static String CONSUMER_KEY = "jgEsvUgMHBUwRMW2V13YUol1P";
    public final static String CONSUMER_SECRET = "DmsoXAi54OLUzeoNHeejFbiqd7CgHVyXwLvw7wpwyfVXFP6k4p";
    public final static String ACCESS_TOKEN = "762015787934679041-4fXGdkjodML8RvSsKgOtWzo3Y77QuJD";
    public final static String ACCESS_TOKEN_SECRET = "4XJgdnBvchC7Jniia0WiaQI0RSKYH26k4tGMYNHJKY6sv";
    private final Twitter twitter;


    public TwitterApi(User user, ApiCredentialsRepo apiCredentialsRepo) {
        this.user = user;
        this.apiCredentialsRepo = apiCredentialsRepo;
        TwitterBuilder twitterBuilder = Twitter.newBuilder();
//        twitter = twitterBuilder
//                .oAuthConsumer(TwitterApi.CONSUMER_KEY, TwitterApi.CONSUMER_SECRET)
//                .oAuthAccessToken(TwitterApi.ACCESS_TOKEN, TwitterApi.ACCESS_TOKEN_SECRET)
//                .build();
        twitter = twitterBuilder
                .oAuthConsumer(getCredentials().getTwitterConsumerKey(), getCredentials().getTwitterConsumerSecret())
                .oAuthAccessToken(getCredentials().getTwitterAccessToken(), getCredentials().getTwitterAccessTokenSecret())
                .build();
    }

    public Twitter getTwitterBuilder() {
        return twitter;
    }

    private ApiCredentials getCredentials() {

        return apiCredentialsRepo.findApiCredentialsByUser(user);
    }
}
