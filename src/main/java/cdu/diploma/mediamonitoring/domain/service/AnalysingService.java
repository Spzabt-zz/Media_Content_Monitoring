package cdu.diploma.mediamonitoring.domain.service;

import cdu.diploma.mediamonitoring.data.processing.mention_analysis.MentionsAnalysis;
import cdu.diploma.mediamonitoring.data.processing.sentiment_analysis.SentimentAnalysis;
import cdu.diploma.mediamonitoring.data.processing.wordcloud_generation.WordCloudGenerator;
import cdu.diploma.mediamonitoring.domain.model.*;
import cdu.diploma.mediamonitoring.domain.repo.AnalyseDataRepo;
import cdu.diploma.mediamonitoring.domain.repo.RedditDataRepo;
import cdu.diploma.mediamonitoring.domain.repo.TwitterDataRepo;
import cdu.diploma.mediamonitoring.domain.repo.YTDataRepo;
import cdu.diploma.mediamonitoring.domain.dto.AllDataDto;
import cdu.diploma.mediamonitoring.domain.dto.MentionsDto;
import cdu.diploma.mediamonitoring.domain.dto.SentimentDataDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class AnalysingService {
    private final SentimentAnalysis sentimentAnalysis;
    private final RedditDataRepo redditDataRepo;
    private final TwitterDataRepo twitterDataRepo;
    private final YTDataRepo ytDataRepo;
    private final AnalyseDataRepo analyseDataRepo;
    private final MentionsAnalysis mentionsAnalysis;

    @Autowired
    public AnalysingService(RedditDataRepo redditDataRepo, TwitterDataRepo twitterDataRepo, YTDataRepo ytDataRepo, AnalyseDataRepo analyseDataRepo) {
        this.redditDataRepo = redditDataRepo;
        this.twitterDataRepo = twitterDataRepo;
        this.ytDataRepo = ytDataRepo;
        this.analyseDataRepo = analyseDataRepo;
        sentimentAnalysis = new SentimentAnalysis(redditDataRepo, twitterDataRepo, ytDataRepo, analyseDataRepo);
        mentionsAnalysis = new MentionsAnalysis(redditDataRepo, twitterDataRepo, ytDataRepo, analyseDataRepo);
    }

    public void doParallelSentimentAnalysis(Model model, SocialMediaPlatform socialMediaPlatform, String source) {
        sentimentAnalysis.analyseSentiment(model, socialMediaPlatform, source);
    }

    @NotNull
    public ArrayList<AllDataDto> getAllDataDtos(SocialMediaPlatform socialMediaPlatform, HashSet<String> dates, String source) {
        ArrayList<AllDataDto> allData = new ArrayList<>();
        List<TwitterData> allTwitterData = new ArrayList<>();
        List<RedditData> allRedditData = new ArrayList<>();
        List<YTData> allYTData = new ArrayList<>();

        if (Objects.equals(source, PlatformName.TWITTER.name())) {
            allTwitterData = twitterDataRepo.findAllBySocialMediaPlatformOrderByTweetedAt(socialMediaPlatform);
        } else if (Objects.equals(source, PlatformName.REDDIT.name())) {
            allRedditData = redditDataRepo.findAllBySocialMediaPlatformOrderBySubDate(socialMediaPlatform);
        } else if (Objects.equals(source, PlatformName.YOU_TUBE.name())) {
            allYTData = ytDataRepo.findAllBySocialMediaPlatformOrderByPublicationTime(socialMediaPlatform);
        } else {
            allTwitterData = twitterDataRepo.findAllBySocialMediaPlatformOrderByTweetedAt(socialMediaPlatform);
            allRedditData = redditDataRepo.findAllBySocialMediaPlatformOrderBySubDate(socialMediaPlatform);
            allYTData = ytDataRepo.findAllBySocialMediaPlatformOrderByPublicationTime(socialMediaPlatform);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (TwitterData twitterData : allTwitterData) {
            String formattedDate = sdf.format(twitterData.getTweetedAt());
            allData.add(new AllDataDto(formattedDate, twitterData.getSentiment(), twitterData.getTweet(), new BigInteger("0"), twitterData.getListedCount(), twitterData.getFollowerCount(), twitterData.getFriendCount(), new BigInteger("0"), 0L, twitterData.getRetweetCount(), twitterData.getFavoriteCount(), 0L, new BigInteger("0"), PlatformName.TWITTER, "", "", twitterData.getLink()));
        }

        for (RedditData redditData : allRedditData) {
            String formattedDate = sdf.format(redditData.getSubDate());

            if (Objects.equals(redditData.getSubBody(), ""))
                allData.add(new AllDataDto(formattedDate, redditData.getSentiment(), redditData.getSubTitle(), redditData.getSubSubscribers(), 0, new BigInteger("0"), 0, new BigInteger("0"), redditData.getUps(), 0L, 0L, 0L, new BigInteger("0"), PlatformName.REDDIT, "", redditData.getSubUrl(), ""));
            else
                allData.add(new AllDataDto(formattedDate, redditData.getSentiment(), redditData.getSubBody(), redditData.getSubSubscribers(), 0, new BigInteger("0"), 0, new BigInteger("0"), redditData.getUps(), 0L, 0L, 0L, new BigInteger("0"), PlatformName.REDDIT, "", redditData.getSubUrl(), ""));
        }

        for (YTData ytData : allYTData) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            LocalDateTime dateTime = LocalDateTime.parse(ytData.getPublicationTime(), formatter);
            int year = dateTime.getYear();
            int month = dateTime.getMonthValue();
            int day = dateTime.getDayOfMonth();
            String date = "";
            if (month < 10 && day < 10) {
                date = String.valueOf(year + "-0" + month + "-0" + day);
            } else if (month < 10) {
                date = String.valueOf(year + "-0" + month + "-" + day);
            } else if (day < 10) {
                date = String.valueOf(year + "-" + month + "-0" + day);
            } else {
                date = String.valueOf(year + "-" + month + "-" + day);
            }
            allData.add(new AllDataDto(date, ytData.getSentiment(), ytData.getComment(), new BigInteger("0"), 0, new BigInteger("0"), 0, ytData.getSubCount(), 0L, 0L, 0L, ytData.getLikes(), ytData.getViewCountOfVideo(), PlatformName.YOU_TUBE, ytData.getVideoId(), "", ""));
        }

        allData.sort(new Comparator<AllDataDto>() {
            public int compare(AllDataDto s1, AllDataDto s2) {
                return s1.getDate().compareTo(s2.getDate());
            }
        });

        for (AllDataDto allDatum : allData) {
            dates.add(allDatum.getDate());
        }

        return allData;
    }

    public String sentimentDataChart(Model model, ArrayList<SentimentDataDto> sentimentData, HashSet<String> dates, ArrayList<AllDataDto> allData, AnalyseData analyseData) {
        return sentimentAnalysis.sentimentDataChart(model, sentimentData, dates, allData, analyseData);
    }

    public String sentimentPieGraph(Model model, ArrayList<AllDataDto> allData, AnalyseData analyseData) {
        return sentimentAnalysis.sentimentPieGraph(model, allData, analyseData);
    }

    public String totalMentionsCountChart(Model model, HashSet<String> dates, ArrayList<AllDataDto> allData, AnalyseData analyseData) {
        return mentionsAnalysis.totalMentionsAnalysis(model, dates, allData, analyseData);
    }

    public String wordCloudGeneration(Model model, ArrayList<AllDataDto> allData, AnalyseData analyseData) {
        ObjectMapper mapper;
        List<Map<String, Object>> words = new ArrayList<>();

        for (AllDataDto allDatum : allData) {
            Map<String, Object> individual = new HashMap<>();
            individual.put("title", allDatum.getText());
            words.add(individual);
        }

        WordCloudGenerator wordCloudGenerator = new WordCloudGenerator(words);

        mapper = new ObjectMapper();

        try {
            String json = mapper.writeValueAsString(wordCloudGenerator.generateWordCloud());
            model.addAttribute("words", json);
            analyseData.setWordCloudGeneration(json);

            return json;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return "no data";
    }

    public String reachAnalysis(Model model, HashSet<String> dates, ArrayList<AllDataDto> allData, AnalyseData analyseData) {
        ObjectMapper mapper;
        final double TWITTER_WEIGHT = 1.0;
        final double REDDIT_WEIGHT = 1.0;
        final double YOUTUBE_WEIGHT = 1.0;

        ArrayList<MentionsDto> smReach = new ArrayList<>();
        for (String date : dates) {
            int reach = 0;
            reach = getReach(allData, TWITTER_WEIGHT, REDDIT_WEIGHT, YOUTUBE_WEIGHT, reach, date);
            smReach.add(new MentionsDto(date, reach));
        }

        smReach.sort(new Comparator<MentionsDto>() {
            public int compare(MentionsDto s1, MentionsDto s2) {
                return s1.getDate().compareTo(s2.getDate());
            }
        });

        mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(smReach);
            model.addAttribute("reachChartData", json);
            analyseData.setReachAnalysis(json);

            return json;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return "no data";
    }

    public int totalMentionsCount(ArrayList<AllDataDto> allData) {
        int total = 0;

        for (AllDataDto allDatum : allData) {
            total++;
        }

        return total;
    }

    public long totalReachCount(ArrayList<AllDataDto> allData, HashSet<String> dates) {
        final double TWITTER_WEIGHT = 1.0;
        final double REDDIT_WEIGHT = 1.0;
        final double YOUTUBE_WEIGHT = 1.0;

        int reach = 0;
        for (String date : dates) {
            reach = getReach(allData, TWITTER_WEIGHT, REDDIT_WEIGHT, YOUTUBE_WEIGHT, reach, date);
        }

        return reach;
    }

    public long retweetTotalCount(ArrayList<AllDataDto> allData) {
        long retweetCount = 0;

        for (AllDataDto allDatum : allData) {
            retweetCount += allDatum.getRetweetCount();
        }

        return retweetCount;
    }

    public long likeCount(ArrayList<AllDataDto> allData) {
        long likes = 0;

        for (AllDataDto allDatum : allData) {
            likes += allDatum.getYtLikesCount() + allDatum.getRedditUps() + allDatum.getYtLikesCount();
        }

        return likes;
    }

    public long positiveCount(ArrayList<AllDataDto> allData) {
        long positiveCount = 0;

        for (AllDataDto allDatum : allData) {
            if (Objects.equals(allDatum.getSentiment(), "Positive")) {
                positiveCount++;
            }
        }

        return positiveCount;
    }

    public long negativeCount(ArrayList<AllDataDto> allData) {
        long negativeCount = 0;

        for (AllDataDto allDatum : allData) {
            if (Objects.equals(allDatum.getSentiment(), "Negative")) {
                negativeCount++;
            }
        }

        return negativeCount;
    }

    public List<AllDataDto> mostPopularMentions(ArrayList<AllDataDto> allData) {

        allData.sort(new Comparator<AllDataDto>() {
            public int compare(AllDataDto a, AllDataDto b) {
                int cmp = b.getViewCountOfYTVideo().compareTo(a.getViewCountOfYTVideo());
                if (cmp == 0) {
                    cmp = b.getRedditUps().compareTo(a.getRedditUps());
                    if (cmp == 0) {
                        cmp = b.getFavoriteCount().compareTo(a.getFavoriteCount());
                    }
                }
                return cmp;
            }
        });

        return allData;
    }

    public List<AllDataDto> mostPopularMentionsForTheReport(ArrayList<AllDataDto> allData) {
        allData.sort(new Comparator<AllDataDto>() {
            public int compare(AllDataDto a, AllDataDto b) {
                int cmp = b.getViewCountOfYTVideo().compareTo(a.getViewCountOfYTVideo());
                if (cmp == 0) {
                    cmp = b.getRedditUps().compareTo(a.getRedditUps());
                    if (cmp == 0) {
                        cmp = b.getFavoriteCount().compareTo(a.getFavoriteCount());
                    }
                }
                return cmp;
            }
        });

        // Escape special characters in the mention variables
        for (AllDataDto mention : allData) {
            mention.setYtVideoId(escapeXml(mention.getYtVideoId()));
            mention.setSubUrl(escapeXml(mention.getSubUrl()));
            mention.setTwLink(escapeXml(mention.getTwLink()));
            mention.setSentiment(escapeXml(mention.getSentiment()));
            mention.setText(escapeXml(mention.getText()));
        }

        return allData;
    }

    private String escapeXml(String input) {
        return input
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }


    public void countOfMentionsBySources(ArrayList<AllDataDto> allData, Model model) {
        mentionsAnalysis.mentionsBySourcesAnalysis(allData, model);
    }

    private int getReach(ArrayList<AllDataDto> allData, double TWITTER_WEIGHT, double REDDIT_WEIGHT, double YOUTUBE_WEIGHT, int reach, String date) {
        Set<Integer> processedAccounts = new HashSet<>();
        for (AllDataDto allDatum : allData) {
            if (date.equals(allDatum.getDate())) {
                if (!processedAccounts.contains(allDatum.getTwitterFollowerCount().intValue())
                        || !processedAccounts.contains(allDatum.getTwitterFriendCount())
                        || !processedAccounts.contains(allDatum.getTwitterListedCount())) {
                    reach += allDatum.getTwitterFriendCount() * TWITTER_WEIGHT
                            + allDatum.getTwitterListedCount() * TWITTER_WEIGHT
                            + allDatum.getTwitterFollowerCount().intValue() * TWITTER_WEIGHT;
                    processedAccounts.add(allDatum.getTwitterFollowerCount().intValue());
                    processedAccounts.add(allDatum.getTwitterFriendCount());
                    processedAccounts.add(allDatum.getTwitterListedCount());
                }
                if (!processedAccounts.contains(allDatum.getRedditSubSubscribers().intValue())) {
                    reach += allDatum.getRedditSubSubscribers().intValue() * REDDIT_WEIGHT;
                    processedAccounts.add(allDatum.getRedditSubSubscribers().intValue());
                }
                if (!processedAccounts.contains(allDatum.getYouTubeChannelSubscriberCount().intValue())) {
                    reach += allDatum.getYouTubeChannelSubscriberCount().intValue() * YOUTUBE_WEIGHT;
                    processedAccounts.add(allDatum.getYouTubeChannelSubscriberCount().intValue());
                }
            }
        }
        return reach;
    }
}
