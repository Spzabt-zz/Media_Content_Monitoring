package cdu.diploma.mediamonitoring.domain.service;

import cdu.diploma.mediamonitoring.domain.model.*;
import cdu.diploma.mediamonitoring.domain.repo.ApiCredentialsRepo;
import cdu.diploma.mediamonitoring.domain.repo.RedditDataRepo;
import cdu.diploma.mediamonitoring.external.api.RedditApi;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
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
    private final ApiCredentialsRepo apiCredentialsRepo;

    public RedditService(RedditDataRepo redditDataRepo, ApiCredentialsRepo apiCredentialsRepo, ApiCredentialsRepo apiCredentialsRepo1) throws JsonProcessingException {
        this.redditDataRepo = redditDataRepo;
        redditApi = new RedditApi(apiCredentialsRepo);
        this.apiCredentialsRepo = apiCredentialsRepo1;
    }

    public boolean checkRedditApiConnection(User user) {
        try {
            String accessToken = redditApi.getAccessToken(user);

            HttpClient client = HttpClient.newBuilder().build();

            ApiCredentials apiCredentials = apiCredentialsRepo.findApiCredentialsByUserId(user.getId());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://oauth.reddit.com/api/v1/me"))
                    .header("User-Agent", apiCredentials.getRedditUserAgent())
                    .header("Authorization", "bearer " + accessToken)
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();

            return statusCode == 200;
        } catch (Exception e) {
            return false;
        }
    }


    //todo: get rid of duplicates when retrieving data
    public void searchReddit(String[] keywords, SocialMediaPlatform socialMediaPlatform, User user) throws Exception {
        String accessToken = redditApi.getAccessToken(user);

        HttpClient client = HttpClient.newBuilder().build();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2023);
        cal.set(Calendar.MONTH, Calendar.MAY);
        cal.set(Calendar.DAY_OF_MONTH, 25);
        Date myDate = cal.getTime();

        ApiCredentials apiCredentials = apiCredentialsRepo.findApiCredentialsByUserId(user.getId());

        List<String> permalinkList = new ArrayList<>();
        for (String keyword : keywords) {
            String encodedKeyword = URLEncoder.encode(keyword, "UTF-8");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://oauth.reddit.com/r/all/search.json?q=" + encodedKeyword))
                    .header("User-Agent", apiCredentials.getRedditUserAgent())
                    .header("Authorization", "bearer " + accessToken)
                    .build();


            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String json = response.body();
            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
            JsonArray postsArray = jsonObject.getAsJsonObject("data").getAsJsonArray("children");

            int counter = 0;
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
                String overEighteen = post.get("over_18").getAsString();
                boolean isOverEighteen = Boolean.parseBoolean(overEighteen);
                if (isOverEighteen) {
                    continue;
                }
                if (subTitle.length() > 300) {
                    continue;
                }
                if (subBody.length() > 300) {
                    continue;
                }
                if (counter > 50) {
                    continue;
                }

                String siteSubRedditName = post.get("subreddit").getAsString();
                String subredditSubscribers = post.get("subreddit_subscribers").getAsString();
                BigInteger subSubscribers = new BigInteger(subredditSubscribers);
                String ups = post.get("ups").getAsString();
                Long longUps = Long.valueOf(ups);

                RedditData redditData = new RedditData();
                redditData.setSubId(subId);
                redditData.setSubTitle(subTitle);
                redditData.setSubBody(subBody);
                redditData.setSubDate(subDate);
                redditData.setSubUrl(permalink);
                redditData.setSite(siteSubRedditName);
                redditData.setSubSubscribers(subSubscribers);
                redditData.setUps(longUps);

                socialMediaPlatform.setPlatformName(PlatformName.REDDIT.name());
                redditData.setSocialMediaPlatform(socialMediaPlatform);

                redditDataRepo.save(redditData);

                permalinkList.add(permalink);
                counter++;
            }
        }

        permalinkList.stream().distinct().collect(Collectors.toList());
    }
}
