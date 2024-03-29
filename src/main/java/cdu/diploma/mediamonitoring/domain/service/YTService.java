package cdu.diploma.mediamonitoring.domain.service;

import cdu.diploma.mediamonitoring.domain.model.User;
import cdu.diploma.mediamonitoring.domain.model.YTData;
import cdu.diploma.mediamonitoring.domain.repo.ApiCredentialsRepo;
import cdu.diploma.mediamonitoring.external.api.YTApi;
import cdu.diploma.mediamonitoring.domain.model.PlatformName;
import cdu.diploma.mediamonitoring.domain.model.SocialMediaPlatform;
import cdu.diploma.mediamonitoring.domain.repo.YTDataRepo;
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class YTService {
    private final YTApi ytApi;
    private final YTDataRepo ytDataRepo;
    private String nextPage_token;

    @Autowired
    public YTService(YTDataRepo ytDataRepo, ApiCredentialsRepo apiCredentialsRepo) {
        this.ytDataRepo = ytDataRepo;
        ytApi = new YTApi(apiCredentialsRepo);
    }

    public void getVideoData(String[] keys, SocialMediaPlatform socialMediaPlatform, User user) throws IOException {
        System.out.println("IN VID DATA");

        for (String key : keys) {
            System.out.println("KEY - " + key);
            YouTube.Search.List search = ytApi.getYoutubeService().search().list("id,snippet");
            search.setKey(ytApi.getCredentials(user).getYtApiKey());
            search.setType("video");
            search.setMaxResults((long) 10);
            search.setQ(key);

            LocalDateTime currentDateTime = LocalDateTime.now();

            LocalDateTime oneMonthAgo = currentDateTime.minusMonths(1);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
            String oneMonthAgoString = oneMonthAgo.format(formatter);
            String currentDateTimeString = currentDateTime.format(formatter);

            search.setPublishedAfter(new DateTime(oneMonthAgoString));
            search.setPublishedBefore(new DateTime(currentDateTimeString));

            search.setRelevanceLanguage("en");
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

//                    DateTime myDateTime = new DateTime("2023-04-01T00:00:00.000+03:00");
//                    long value = publishedAt.getValue();
//                    long value1 = myDateTime.getValue();
//                    if (value < value1) {
//                        continue;
//                    }

                    VideoListResponse videoResponse = ytApi.getYoutubeService().videos()
                            .list("snippet,contentDetails")
                            .setId(videoId)
                            .setKey(ytApi.getCredentials(user).getYtApiKey())
                            .execute();

                    Video video = videoResponse.getItems().get(0);
                    String duration = video.getContentDetails().getDuration();
                    System.out.println("duration " + duration);
                    String categoryId = video.getSnippet().getCategoryId();
                    System.out.println("categoryId " + categoryId);
                    //BigInteger viewCountOfVideo = video.getStatistics().getViewCount();
                    BigInteger viewCountOfVideo = new BigInteger("0");

                    int hours = 0, minutes = 0, seconds = 0;

                    int hoursIndex = duration.indexOf('H');
                    if (hoursIndex > 0) {
                        hours = Integer.parseInt(duration.substring(2, hoursIndex));
                    }

                    int minutesIndex = duration.indexOf('M');
                    if (minutesIndex > 0) {
                        if (hoursIndex < 0) {
                            minutes = Integer.parseInt(duration.substring(2, minutesIndex));
                        } else {
                            minutes = Integer.parseInt(duration.substring(hoursIndex + 1, minutesIndex));
                        }
                    }

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
                            .setKey(ytApi.getCredentials(user).getYtApiKey())
                            .execute();

                    Channel channel = channelListResponse.getItems().get(0);
                    Boolean hiddenSubscriberCount = channel.getStatistics().getHiddenSubscriberCount();
                    System.out.println("hiddenSubscriberCount " + hiddenSubscriberCount);
                    BigInteger viewCount = channel.getStatistics().getViewCount();
                    System.out.println("viewCount " + viewCount);
                    BigInteger subscriberCount = channel.getStatistics().getSubscriberCount();
                    System.out.println("subscriberCount " + subscriberCount);

                    getComData(videoId, title, categoryId, viewCount, subscriberCount, hours, minutes, seconds, socialMediaPlatform, viewCountOfVideo, user);
                } catch (Exception error) {
                    System.out.println("Something went wrong " + error);
                }
            }
        }
    }

    //todo: get rid of duplicates when retrieving data
    private void getComData(String videoId, String videoTitle, String categoryId, BigInteger viewCount, BigInteger subscriberCount, Integer hours, Integer minutes, Integer seconds, SocialMediaPlatform socialMediaPlatform, BigInteger vieCountOfVideo, User user) {
        System.out.println("from comment data");

        int counter = 0;

        //do {
            try {
                CommentThreadListResponse com_thread = ytApi.getYoutubeService().commentThreads().list("snippet")
                        .setVideoId(videoId)
                        //.setPageToken(nextPage_token)
                        .setOrder("relevance")
                        .setTextFormat("plainText")
                        .setMaxResults(5L)
                        .setKey(ytApi.getCredentials(user).getYtApiKey())
                        .execute();

                //nextPage_token = com_thread.getNextPageToken();

                for (CommentThread commentThread : com_thread.getItems()) {

                    String textDisplay = commentThread.getSnippet().getTopLevelComment().getSnippet().getTextDisplay();

                    if (textDisplay.isEmpty() || textDisplay.length() > 300) {
                        continue;
                    }
//                    if (counter >= 3) {
//                        continue;
//                    }

                    String comId = commentThread.getSnippet().getTopLevelComment().getId();

                    Long likeCount = commentThread.getSnippet().getTopLevelComment().getSnippet().getLikeCount();

                    DateTime publishedAt = commentThread.getSnippet().getTopLevelComment().getSnippet().getPublishedAt();

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
                    ytData.setViewCountOfVideo(vieCountOfVideo);

                    socialMediaPlatform.setPlatformName(PlatformName.YOU_TUBE.name());
                    ytData.setSocialMediaPlatform(socialMediaPlatform);

                    ytDataRepo.save(ytData);

                    counter++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        //} while (nextPage_token != null);
    }
}
