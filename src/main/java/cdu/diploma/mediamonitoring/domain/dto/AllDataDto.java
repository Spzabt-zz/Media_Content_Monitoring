package cdu.diploma.mediamonitoring.domain.dto;

import cdu.diploma.mediamonitoring.domain.model.PlatformName;

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
    private Long redditUps;
    private Long retweetCount;
    private Long favoriteCount;
    private Long ytLikesCount;
    private BigInteger viewCountOfYTVideo;
    private PlatformName platformName;
    private String ytVideoId;
    private String subUrl;
    private String twLink;

    public AllDataDto(String date, String sentiment, String text, BigInteger redditSubSubscribers, Integer twitterListedCount, BigInteger twitterFollowerCount, Integer twitterFriendCount, BigInteger youTubeChannelSubscriberCount, Long redditUps, Long retweetCount, Long favoriteCount, Long ytLikesCount, BigInteger viewCountOfYTVideo, PlatformName platformName, String ytVideoId, String subUrl, String twLink) {
        this.date = date;
        this.sentiment = sentiment;
        this.text = text;
        this.redditSubSubscribers = redditSubSubscribers;
        this.twitterListedCount = twitterListedCount;
        this.twitterFollowerCount = twitterFollowerCount;
        this.twitterFriendCount = twitterFriendCount;
        this.youTubeChannelSubscriberCount = youTubeChannelSubscriberCount;
        this.redditUps = redditUps;
        this.retweetCount = retweetCount;
        this.favoriteCount = favoriteCount;
        this.ytLikesCount = ytLikesCount;
        this.viewCountOfYTVideo = viewCountOfYTVideo;
        this.platformName = platformName;
        this.ytVideoId = ytVideoId;
        this.subUrl = subUrl;
        this.twLink = twLink;
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

    public Long getRedditUps() {
        return redditUps;
    }

    public void setRedditUps(Long redditUps) {
        this.redditUps = redditUps;
    }

    public Long getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(Long retweetCount) {
        this.retweetCount = retweetCount;
    }

    public Long getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(Long favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public Long getYtLikesCount() {
        return ytLikesCount;
    }

    public void setYtLikesCount(Long ytLikesCount) {
        this.ytLikesCount = ytLikesCount;
    }

    public BigInteger getViewCountOfYTVideo() {
        return viewCountOfYTVideo;
    }

    public void setViewCountOfYTVideo(BigInteger viewCountOfYTVideo) {
        this.viewCountOfYTVideo = viewCountOfYTVideo;
    }

    public PlatformName getPlatformName() {
        return platformName;
    }

    public void setPlatformName(PlatformName platformName) {
        this.platformName = platformName;
    }

    public String getYtVideoId() {
        return ytVideoId;
    }

    public void setYtVideoId(String ytVideoId) {
        this.ytVideoId = ytVideoId;
    }

    public String getSubUrl() {
        return subUrl;
    }

    public void setSubUrl(String subUrl) {
        this.subUrl = subUrl;
    }

    public String getTwLink() {
        return twLink;
    }

    public void setTwLink(String twLink) {
        this.twLink = twLink;
    }
}
