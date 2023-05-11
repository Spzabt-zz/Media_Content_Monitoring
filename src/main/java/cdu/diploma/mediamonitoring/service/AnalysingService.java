package cdu.diploma.mediamonitoring.service;

import cdu.diploma.mediamonitoring.data.processing.SentimentAnalysis;
import cdu.diploma.mediamonitoring.data.processing.WordCloudGenerator;
import cdu.diploma.mediamonitoring.dto.AllDataDto;
import cdu.diploma.mediamonitoring.dto.MentionsDto;
import cdu.diploma.mediamonitoring.dto.SentimentDataDto;
import cdu.diploma.mediamonitoring.model.*;
import cdu.diploma.mediamonitoring.repo.RedditDataRepo;
import cdu.diploma.mediamonitoring.repo.TwitterDataRepo;
import cdu.diploma.mediamonitoring.repo.YTDataRepo;
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

    @Autowired
    public AnalysingService(RedditDataRepo redditDataRepo, TwitterDataRepo twitterDataRepo, YTDataRepo ytDataRepo) {
        this.redditDataRepo = redditDataRepo;
        this.twitterDataRepo = twitterDataRepo;
        this.ytDataRepo = ytDataRepo;
        sentimentAnalysis = new SentimentAnalysis(redditDataRepo, twitterDataRepo, ytDataRepo);
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
            allData.add(new AllDataDto(formattedDate, twitterData.getSentiment(), twitterData.getTweet(), new BigInteger("0"), twitterData.getListedCount(), twitterData.getFollowerCount(), twitterData.getFriendCount(), new BigInteger("0")));
        }

        for (RedditData redditData : allRedditData) {
            String formattedDate = sdf.format(redditData.getSubDate());

            if (Objects.equals(redditData.getSubBody(), ""))
                allData.add(new AllDataDto(formattedDate, redditData.getSentiment(), redditData.getSubTitle(), redditData.getSubSubscribers(), 0, new BigInteger("0"), 0, new BigInteger("0")));
            else
                allData.add(new AllDataDto(formattedDate, redditData.getSentiment(), redditData.getSubBody(), redditData.getSubSubscribers(), 0, new BigInteger("0"), 0, new BigInteger("0")));
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
            allData.add(new AllDataDto(date, ytData.getSentiment(), ytData.getComment(), new BigInteger("0"), 0, new BigInteger("0"), 0, ytData.getSubCount()));
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

    public void sentimentDataChart(Model model, ArrayList<SentimentDataDto> sentimentData, HashSet<String> dates, ArrayList<AllDataDto> allData) {
        sentimentAnalysis.sentimentDataChart(model, sentimentData, dates, allData);
    }

    public void sentimentPieGraph(Model model, ArrayList<AllDataDto> allData) {
        sentimentAnalysis.sentimentPieGraph(model, allData);
    }

    public void totalMentionsCountChart(Model model, HashSet<String> dates, ArrayList<AllDataDto> allData) {
        ObjectMapper mapper;
        ArrayList<MentionsDto> mentions = new ArrayList<>();

        int mentionsCount = 0;
        for (String date : dates) {
            for (AllDataDto allDatum : allData) {
                if (date.equals(allDatum.getDate())) {
                    mentionsCount++;
                }
            }
            mentions.add(new MentionsDto(date, mentionsCount));
            mentionsCount = 0;
        }

        mentions.sort(new Comparator<MentionsDto>() {
            public int compare(MentionsDto s1, MentionsDto s2) {
                return s1.getDate().compareTo(s2.getDate());
            }
        });

        mapper = new ObjectMapper();

        try {
            String json = mapper.writeValueAsString(mentions);
            model.addAttribute("mentionChartData", json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void wordCloudGeneration(Model model, ArrayList<AllDataDto> allData) {
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
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void reachAnalysis(Model model, HashSet<String> dates, ArrayList<AllDataDto> allData) {
        ObjectMapper mapper;
        final double TWITTER_WEIGHT = 1.0;
        final double REDDIT_WEIGHT = 1.0;
        final double YOUTUBE_WEIGHT = 1.0;

        ArrayList<MentionsDto> smReach = new ArrayList<>();
        for (String date : dates) {
            int reach = 0;
            Set<Integer> processedAccounts = new HashSet<>();
            for (AllDataDto allDatum : allData) {
                if (date.equals(allDatum.getDate())) {
                    //todo: add views parameter
                    //todo: fix reach count algo
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
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
