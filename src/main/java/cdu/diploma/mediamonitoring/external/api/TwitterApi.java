package cdu.diploma.mediamonitoring.external.api;

import twitter4j.Twitter;
import twitter4j.Twitter.TwitterBuilder;
import twitter4j.TwitterObjectFactory;

public class TwitterApi {
    public final static String CONSUMER_KEY = "jgEsvUgMHBUwRMW2V13YUol1P";
    public final static String CONSUMER_SECRET = "DmsoXAi54OLUzeoNHeejFbiqd7CgHVyXwLvw7wpwyfVXFP6k4p";
    public final static String ACCESS_TOKEN = "762015787934679041-4fXGdkjodML8RvSsKgOtWzo3Y77QuJD";
    public final static String ACCESS_TOKEN_SECRET = "4XJgdnBvchC7Jniia0WiaQI0RSKYH26k4tGMYNHJKY6sv";
    public final static String BEARER_TOKEN = "AAAAAAAAAAAAAAAAAAAAAJlBmgEAAAAAoZY3XdcBVufjLOH%2BcmFmK2mjwvI%3DA3xGyO3SPMxYdoEPPhMuJpT77D98pttmqrL4hbRzPp4vDBfNs8";

    private final Twitter twitter;

    public TwitterApi() {
        TwitterBuilder twitterBuilder = Twitter.newBuilder();
        twitter = twitterBuilder
                .oAuthConsumer(TwitterApi.CONSUMER_KEY, TwitterApi.CONSUMER_SECRET)
                .oAuthAccessToken(TwitterApi.ACCESS_TOKEN, TwitterApi.ACCESS_TOKEN_SECRET)
                .build();
    }

    public Twitter getTwitterBuilder() {
        return twitter;
    }
}
