package cdu.diploma.mediamonitoring.service;

import cdu.diploma.mediamonitoring.external.api.RedditApi;
import cdu.diploma.mediamonitoring.model.RedditData;
import cdu.diploma.mediamonitoring.model.SocialMediaPlatform;
import cdu.diploma.mediamonitoring.repo.RedditDataRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.api.client.util.DateTime;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RedditService {
    private final RedditApi redditApi;
    private final RedditDataRepo redditDataRepo;
    private static final String USER_AGENT = "Praw1 by u/Spzabt_zz";

    public RedditService(RedditDataRepo redditDataRepo) throws JsonProcessingException {
        this.redditDataRepo = redditDataRepo;
        redditApi = new RedditApi();
    }

    public List<String> searchReddit(String[] keywords) throws Exception {
        String accessToken = redditApi.getAccessToken();

        HttpClient client = HttpClient.newBuilder().build();
        SocialMediaPlatform socialMediaPlatform = new SocialMediaPlatform(1L);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2023);
        cal.set(Calendar.MONTH, Calendar.APRIL);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date myDate = cal.getTime();

        List<String> permalinkList = new ArrayList<>();
        for (String keyword : keywords) {
            String encodedKeyword = URLEncoder.encode(keyword, "UTF-8");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://oauth.reddit.com/r/all/search.json?q=" + encodedKeyword))
                    .header("User-Agent", USER_AGENT)
                    .header("Authorization", "bearer " + accessToken)
                    .build();


            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String json = response.body();
            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
            JsonArray postsArray = jsonObject.getAsJsonObject("data").getAsJsonArray("children");

            for (JsonElement postElement : postsArray) {
                JsonObject post = postElement.getAsJsonObject().getAsJsonObject("data");
                String permalink = "https://www.reddit.com" + post.get("permalink").getAsString();
                String subId = post.get("name").getAsString();
                String subTitle = post.get("title").getAsString();
                String subBody = post.get("selftext").getAsString();

                long timestamp = Long.parseLong(post.get("created").getAsString().split("\\.")[0]);
                Date subDate = new Date(timestamp * 1000L);

                if (subDate.before(myDate)) {
                    continue;
                }

                String siteSubRedditName = post.get("subreddit").getAsString();

                RedditData redditData = new RedditData(subId, subTitle, subBody, subDate, permalink, siteSubRedditName, socialMediaPlatform);

                redditDataRepo.save(redditData);

                permalinkList.add(permalink);
            }
        }

        return permalinkList.stream().distinct().collect(Collectors.toList());
    }
}
