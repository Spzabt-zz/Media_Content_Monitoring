package cdu.diploma.mediamonitoring.service;

import cdu.diploma.mediamonitoring.external.api.TwitterApi;
import cdu.diploma.mediamonitoring.model.SocialMediaPlatform;
import cdu.diploma.mediamonitoring.model.TwitterData;
import cdu.diploma.mediamonitoring.repo.TwitterDataRepo;
import org.springframework.stereotype.Service;
import twitter4j.Twitter;
import twitter4j.v1.*;

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

    public TwitterService(TwitterDataRepo twitterDataRepo) {
        this.twitterDataRepo = twitterDataRepo;
        twitterApi = new TwitterApi();
    }

    public void collectDataForModel(String[] keys) {
        for (String key : keys) {
            try {
                Query query = Query.of(key)
                        .resultType(Query.ResultType.mixed)
                        .lang("en")
                        .since("2023-04-01")
                        .count(100);

                Twitter twitter = twitterApi.getTwitterBuilder();

                QueryResult result = twitter.v1().search().search(query);
                for (Status status : result.getTweets()) {
                    System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText() + ":"
                            + status.getCreatedAt());

                    saveTweetsInDb(status, keys);
                }
            } catch (Exception e) {
                System.err.println("Error occurred while searching tweets: " + e.getMessage());
            }
        }
    }

    private void saveTweetsInDb(Status status, String[] keywords) {
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
                String username = status.getUser().getScreenName();
                BigInteger followerCount = BigInteger.valueOf(status.getUser().getFollowersCount());
                Integer friendCount = status.getUser().getFriendsCount();
                Integer listedCount = status.getUser().getListedCount();
                Integer verificationStatus = status.getUser().isVerified() ? 1 : 0;

                LocalDateTime tweetedAt = status.getCreatedAt();
                ZonedDateTime zonedDateTime = tweetedAt.atZone(ZoneId.systemDefault());
                Instant instant = zonedDateTime.toInstant();
                Date tweetedAtDate = Date.from(instant);

                SocialMediaPlatform socialMediaPlatform = new SocialMediaPlatform(3L);

                TwitterData twitterData = new TwitterData(
                        id,
                        link,
                        tweetText,
                        username,
                        followerCount,
                        friendCount,
                        listedCount,
                        verificationStatus,
                        tweetedAtDate,
                        socialMediaPlatform
                );
                twitterDataRepo.save(twitterData);
            }
        }
    }
}
