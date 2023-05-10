package cdu.diploma.mediamonitoring.dto;

import java.math.BigInteger;

public class AllDataDto {
    private String date;
    private String sentiment;
    private String text;
    private BigInteger redditSubSubscribers;
    private Integer twitterListedCount;
    private BigInteger twitterFollowerCount;
    private Integer twitterFriendCount;
    private BigInteger youTubeChannelSubscriberCount;

    public AllDataDto(String date, String sentiment, String text, BigInteger redditSubSubscribers, Integer twitterListedCount, BigInteger twitterFollowerCount, Integer twitterFriendCount, BigInteger youTubeChannelSubscriberCount) {
        this.date = date;
        this.sentiment = sentiment;
        this.text = text;
        this.redditSubSubscribers = redditSubSubscribers;
        this.twitterListedCount = twitterListedCount;
        this.twitterFollowerCount = twitterFollowerCount;
        this.twitterFriendCount = twitterFriendCount;
        this.youTubeChannelSubscriberCount = youTubeChannelSubscriberCount;
    }

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public BigInteger getRedditSubSubscribers() {
        return redditSubSubscribers;
    }

    public void setRedditSubSubscribers(BigInteger redditSubSubscribers) {
        this.redditSubSubscribers = redditSubSubscribers;
    }

    public Integer getTwitterListedCount() {
        return twitterListedCount;
    }

    public void setTwitterListedCount(Integer twitterListedCount) {
        this.twitterListedCount = twitterListedCount;
    }

    public BigInteger getTwitterFollowerCount() {
        return twitterFollowerCount;
    }

    public void setTwitterFollowerCount(BigInteger twitterFollowerCount) {
        this.twitterFollowerCount = twitterFollowerCount;
    }

    public Integer getTwitterFriendCount() {
        return twitterFriendCount;
    }

    public void setTwitterFriendCount(Integer twitterFriendCount) {
        this.twitterFriendCount = twitterFriendCount;
    }

    public BigInteger getYouTubeChannelSubscriberCount() {
        return youTubeChannelSubscriberCount;
    }

    public void setYouTubeChannelSubscriberCount(BigInteger youTubeChannelSubscriberCount) {
        this.youTubeChannelSubscriberCount = youTubeChannelSubscriberCount;
    }
}
