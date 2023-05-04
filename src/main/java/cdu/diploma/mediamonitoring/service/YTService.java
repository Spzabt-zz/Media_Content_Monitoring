package cdu.diploma.mediamonitoring.service;

import cdu.diploma.mediamonitoring.external.api.YTApi;
import cdu.diploma.mediamonitoring.model.PlatformName;
import cdu.diploma.mediamonitoring.model.SocialMediaPlatform;
import cdu.diploma.mediamonitoring.model.YTData;
import cdu.diploma.mediamonitoring.repo.YTDataRepo;
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

@Service
public class YTService {
    private final YTApi ytApi;
    private final YTDataRepo ytDataRepo;
    private String nextPage_token;
    //private final SocialMediaPlatform socialMediaPlatform;

    @Autowired
    public YTService(YTDataRepo ytDataRepo) {
        this.ytDataRepo = ytDataRepo;
        ytApi = new YTApi();
        //socialMediaPlatform = new SocialMediaPlatform(2L);
    }

    public void getVideoData(String[] keys, SocialMediaPlatform socialMediaPlatform) throws IOException {
        System.out.println("IN VID DATA");

        for (String key : keys) {
            System.out.println("KEY - " + key);
            YouTube.Search.List search = ytApi.getYoutubeService().search().list("id,snippet");
            search.setKey(YTApi.API_KEY);
            search.setType("video");
            search.setMaxResults((long) 5);
            //search.setFields("items(id(videoId),snippet(title,channelId,publishedAt))");
            //search.setQ(String.join("|", key));
            search.setQ(key);
            //search.setPageToken(this.nextPageToken);
            SearchListResponse searchResponse = search.execute();
            List<SearchResult> searchResultList = searchResponse.getItems();

            if (searchResultList == null || searchResultList.size() == 0) {
                continue;
            }

            for (SearchResult searchResult : searchResultList) {
                try {
                    //if (!this.presentIdlist.containsKey(result.getId())) {
                    String videoId = searchResult.getId().getVideoId();
                    System.out.println("vidId " + videoId);
                    String channelId = searchResult.getSnippet().getChannelId();
                    System.out.println("channelId " + channelId);
                    DateTime publishedAt = searchResult.getSnippet().getPublishedAt();
                    System.out.println("publishedAt " + publishedAt);
                    String title = searchResult.getSnippet().getTitle();
                    System.out.println("title " + title);

                    DateTime myDateTime = new DateTime("2023-04-01T00:00:00.000+03:00");
                    long value = publishedAt.getValue();
                    long value1 = myDateTime.getValue();
                    if (value < value1) {
                        continue;
                    }

                    VideoListResponse videoResponse = ytApi.getYoutubeService().videos()
                            .list("snippet,contentDetails")
                            .setId(videoId)
                            .setKey(YTApi.API_KEY)
                            .execute();

                    Video video = videoResponse.getItems().get(0);
                    String duration = video.getContentDetails().getDuration();
                    System.out.println("duration " + duration);
                    String categoryId = video.getSnippet().getCategoryId();
                    System.out.println("categoryId " + categoryId);


                    int hours = 0, minutes = 0, seconds = 0;

                    // Check if the duration contains hours
                    int hoursIndex = duration.indexOf('H');
                    if (hoursIndex > 0) {
                        hours = Integer.parseInt(duration.substring(2, hoursIndex));
                    }

                    // Check if the duration contains minutes
                    int minutesIndex = duration.indexOf('M');
                    if (minutesIndex > 0) {
                        if (hoursIndex < 0) {
                            minutes = Integer.parseInt(duration.substring(2, minutesIndex));
                        } else {
                            minutes = Integer.parseInt(duration.substring(hoursIndex + 1, minutesIndex));
                        }
                    }

                    // Check if the duration contains seconds
                    int secondsIndex = duration.indexOf('S');
                    if (secondsIndex > 0) {
                        if (minutesIndex < 0) {
                            seconds = Integer.parseInt(duration.substring(2, secondsIndex));
                        } else {
                            seconds = Integer.parseInt(duration.substring(minutesIndex + 1, secondsIndex));
                        }
                    }

                    System.out.println("Hours: " + hours);
                    System.out.println("Minutes: " + minutes);
                    System.out.println("Seconds: " + seconds);

                    ChannelListResponse channelListResponse = ytApi.getYoutubeService().channels()
                            .list("statistics")
                            .setId(channelId)
                            .setKey(YTApi.API_KEY)
                            .execute();

                    Channel channel = channelListResponse.getItems().get(0);
                    Boolean hiddenSubscriberCount = channel.getStatistics().getHiddenSubscriberCount();
                    System.out.println("hiddenSubscriberCount " + hiddenSubscriberCount);
                    BigInteger viewCount = channel.getStatistics().getViewCount();
                    System.out.println("viewCount " + viewCount);
                    BigInteger subscriberCount = channel.getStatistics().getSubscriberCount();
                    System.out.println("subscriberCount " + subscriberCount);

//                    String publishedAt = videoInfo.getPublishedAt();
//                        LocalDateTime publishedAtDateTime = LocalDateTime.parse(publishedAt, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
//                        LocalDateTime myDateTime = LocalDateTime.of(2023, 3, 22, 0, 0, 0);
//                        if (publishedAtDateTime.isBefore(myDateTime)) {
//                            continue;
//                        }

                    getComData(videoId, title, categoryId, viewCount, subscriberCount, hours, minutes, seconds, socialMediaPlatform);
                } catch (Exception error) {
                    System.out.println("Something went wrong " + error);
                }
            }
        }
    }

    private void getComData(String videoId, String videoTitle, String categoryId, BigInteger viewCount, BigInteger subscriberCount, Integer hours, Integer minutes, Integer seconds, SocialMediaPlatform socialMediaPlatform) {
        System.out.println("from comment data");

        do {
            try {
                CommentThreadListResponse com_thread = ytApi.getYoutubeService().commentThreads().list("snippet")
                        .setVideoId(videoId)
                        .setPageToken(nextPage_token)
                        .setOrder("relevance")
                        .setTextFormat("plainText")
                        .setMaxResults(50L)
                        .setKey(YTApi.API_KEY)
                        .execute();

                nextPage_token = com_thread.getNextPageToken();

                for (CommentThread commentThread : com_thread.getItems()) {

                    String textDisplay = commentThread.getSnippet().getTopLevelComment().getSnippet().getTextDisplay();

                    String comId = commentThread.getSnippet().getTopLevelComment().getId();

                    Long likeCount = commentThread.getSnippet().getTopLevelComment().getSnippet().getLikeCount();

                    DateTime publishedAt = commentThread.getSnippet().getTopLevelComment().getSnippet().getPublishedAt();


                    //YTData ytData = new YTData(
                    // comId,
                    // videoId,
                    // textDisplay,
                    // likeCount,
                    // videoTitle,
                    // publishedAt.toString(),
                    // Integer.valueOf(categoryId),
                    // viewCount,
                    // subscriberCount,
                    // hours,
                    // minutes,
                    // seconds,
                    // socialMediaPlatform);

                    YTData ytData = new YTData();
                    ytData.setComId(comId);
                    ytData.setVideoId(videoId);
                    ytData.setComment(textDisplay);
                    ytData.setLikes(likeCount);
                    ytData.setVidTitle(videoTitle);
                    ytData.setPublicationTime(publishedAt.toString());
                    ytData.setCategoryId(Integer.valueOf(categoryId));
                    ytData.setViewCount(viewCount);
                    ytData.setSubCount(subscriberCount);
                    ytData.setHours(hours);
                    ytData.setMinutes(minutes);
                    ytData.setSeconds(seconds);

                    socialMediaPlatform.setPlatformName(PlatformName.YOU_TUBE.name());
                    ytData.setSocialMediaPlatform(socialMediaPlatform);

                    ytDataRepo.save(ytData);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } while (this.nextPage_token != null);
    }
}
