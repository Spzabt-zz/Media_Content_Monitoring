package cdu.diploma.mediamonitoring.domain.service;

import cdu.diploma.mediamonitoring.domain.model.PlatformName;
import cdu.diploma.mediamonitoring.domain.model.SocialMediaPlatform;
import cdu.diploma.mediamonitoring.domain.model.TwitterData;
import cdu.diploma.mediamonitoring.domain.model.User;
import cdu.diploma.mediamonitoring.domain.repo.ApiCredentialsRepo;
import cdu.diploma.mediamonitoring.domain.repo.TwitterDataRepo;
import cdu.diploma.mediamonitoring.external.api.TwitterApi;
import org.springframework.stereotype.Service;
import twitter4j.Twitter;
import twitter4j.v1.Query;
import twitter4j.v1.QueryResult;
import twitter4j.v1.Status;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class TwitterService {
    private final TwitterDataRepo twitterDataRepo;
    private final TwitterApi twitterApi;

    public TwitterService(TwitterDataRepo twitterDataRepo, ApiCredentialsRepo apiCredentialsRepo) {
        this.twitterDataRepo = twitterDataRepo;
        twitterApi = new TwitterApi(apiCredentialsRepo);
    }

    public void collectDataForModel(String[] keys, SocialMediaPlatform socialMediaPlatform, User user) {
        //SocialMediaPlatform socialMediaPlatform = new SocialMediaPlatform(3L);

        for (String key : keys) {
            int counter = 0;
            try {
                Query query = Query.of(key)
                        .resultType(Query.ResultType.mixed)
                        .lang("en")
                        .since("2023-04-01")
                        .count(100);

                Twitter twitter = twitterApi.getTwitterBuilder(user);

                QueryResult result = twitter.v1().search().search(query);
                for (Status status : result.getTweets()) {
                    System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText() + ":"
                            + status.getCreatedAt());
                    if (counter > 50) {
                        return;
                    }
                    saveTweetsInDb(status, keys, socialMediaPlatform);
                    counter++;
                }
            } catch (Exception e) {
                System.err.println("Error occurred while searching tweets: " + e.getMessage());
            }
        }
    }

    //todo: get rid of duplicates when retrieving data
    private void saveTweetsInDb(Status status, String[] keywords, SocialMediaPlatform socialMediaPlatform) {
        if (status.getLang().equals("en")) {
            List<String> present = new ArrayList<>();
            System.out.println("\n\n############################################");

            for (String presentKey : keywords) {
                if (presentKey.split(" ").length > 1) {
                    String[] subP = presentKey.split(" ");
                    System.out.println("sub_p : " + Arrays.toString(subP));
                    for (String p : subP) {
                        if (status.getText().toUpperCase().contains(p.toUpperCase())) {
                            System.out.println("this word : " + p);
                            present.add(presentKey);
                        }
                    }
                }

                if (status.getText().toUpperCase().contains(presentKey.toUpperCase())) {
                    present.add(presentKey);
                }
            }

            System.out.println("text : " + status.getText());
            present = new ArrayList<>(new HashSet<>(present));
            System.out.println("PRESENT : " + present);

            if (!present.isEmpty()) {
                String id = String.valueOf(status.getId());
                String link = "https://twitter.com/twitter/statuses/" + id;
                String tweetText = status.getText();

                if (tweetText.length() > 300) {
                    return;
                }

                String username = status.getUser().getScreenName();
                BigInteger followerCount = BigInteger.valueOf(status.getUser().getFollowersCount());
                Integer friendCount = status.getUser().getFriendsCount();
                Integer listedCount = status.getUser().getListedCount();
                Integer verificationStatus = status.getUser().isVerified() ? 1 : 0;

                int retweetCount = status.getRetweetCount();
                int favoriteCount = status.getFavoriteCount();

                LocalDateTime tweetedAt = status.getCreatedAt();
                ZonedDateTime zonedDateTime = tweetedAt.atZone(ZoneId.systemDefault());
                Instant instant = zonedDateTime.toInstant();
                Date tweetedAtDate = Date.from(instant);

                TwitterData twitterData = new TwitterData();
                twitterData.setTwId(id);
                twitterData.setLink(link);
                twitterData.setTweet(tweetText);
                twitterData.setUsername(username);
                twitterData.setFollowerCount(followerCount);
                twitterData.setFriendCount(friendCount);
                twitterData.setListedCount(listedCount);
                twitterData.setVerificationStatus(verificationStatus);
                twitterData.setTweetedAt(tweetedAtDate);
                twitterData.setRetweetCount((long) retweetCount);
                twitterData.setFavoriteCount((long) favoriteCount);

                socialMediaPlatform.setPlatformName(PlatformName.TWITTER.name());
                twitterData.setSocialMediaPlatform(socialMediaPlatform);

                twitterDataRepo.save(twitterData);
            }
        }
    }
}
