package cdu.diploma.mediamonitoring.controller;

import cdu.diploma.mediamonitoring.data.processing.WordCloudGenerator;
import cdu.diploma.mediamonitoring.dto.AllDataDto;
import cdu.diploma.mediamonitoring.dto.MentionsDto;
import cdu.diploma.mediamonitoring.dto.SentimentDataDto;
import cdu.diploma.mediamonitoring.dto.SentimentPieDto;
import cdu.diploma.mediamonitoring.model.*;
import cdu.diploma.mediamonitoring.repo.ProjectRepo;
import cdu.diploma.mediamonitoring.repo.RedditDataRepo;
import cdu.diploma.mediamonitoring.repo.TwitterDataRepo;
import cdu.diploma.mediamonitoring.repo.YTDataRepo;
import cdu.diploma.mediamonitoring.service.AnalysingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

@Controller
public class DashboardController {
    private final ProjectRepo projectRepo;
    private final RedditDataRepo redditDataRepo;
    private final TwitterDataRepo twitterDataRepo;
    private final YTDataRepo ytDataRepo;
    private AnalysingService analysingService;

    @Autowired
    public DashboardController(ProjectRepo projectRepo, RedditDataRepo redditDataRepo, TwitterDataRepo twitterDataRepo, YTDataRepo ytDataRepo, AnalysingService analysingService) {
        this.projectRepo = projectRepo;
        this.redditDataRepo = redditDataRepo;
        this.twitterDataRepo = twitterDataRepo;
        this.ytDataRepo = ytDataRepo;
        this.analysingService = analysingService;
    }

    @GetMapping("/")
    public String greeting(@AuthenticationPrincipal User user, Model model) {
        if (user != null)
            model.addAttribute("username", user.getUsername());
        return "greeting";
    }

    @GetMapping("/panel/results/{projectId}")
    public String mentions(@PathVariable String projectId, @RequestParam(value = "source", required = false) String source, Model model) {
        Instant start = Instant.now();

        projectId = projectId.replace(",", "");
        Long longProjId = Long.valueOf(projectId);

        Project project = projectRepo.findProjectById(longProjId);
        SocialMediaPlatform socialMediaPlatform = project.getSocialMediaPlatform();

        analysingService.doParallelSentimentAnalysis(model, socialMediaPlatform);

        model.addAttribute("project", project);

        //sentiment data chart here
        ArrayList<SentimentDataDto> sentimentData = new ArrayList<>();
        HashSet<String> dates = new HashSet<>();
        List<TwitterData> allTwitterData = twitterDataRepo.findAllBySocialMediaPlatformOrderByTweetedAt(socialMediaPlatform);
        List<RedditData> allRedditData = redditDataRepo.findAllBySocialMediaPlatformOrderBySubDate(socialMediaPlatform);
        List<YTData> allYTData = ytDataRepo.findAllBySocialMediaPlatformOrderByPublicationTime(socialMediaPlatform);
        ArrayList<AllDataDto> allData = new ArrayList<>();

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

        int posCount = 0;
        int negCount = 0;
        for (String date : dates) {
            for (AllDataDto allDatum : allData) {
                if (date.equals(allDatum.getDate())) {
                    if (Objects.equals(allDatum.getSentiment(), "Positive") || Objects.equals(allDatum.getSentiment(), "Very positive")) {
                        posCount++;
                    } else if (Objects.equals(allDatum.getSentiment(), "Negative") || Objects.equals(allDatum.getSentiment(), "Very negative")) {
                        negCount++;
                    }
                }
            }
            sentimentData.add(new SentimentDataDto(date, posCount, negCount));
            posCount = 0;
            negCount = 0;
        }

        sentimentData.sort(new Comparator<SentimentDataDto>() {
            public int compare(SentimentDataDto s1, SentimentDataDto s2) {
                return s1.getDate().compareTo(s2.getDate());
            }
        });

        ObjectMapper mapper = new ObjectMapper();

        try {
            String json = mapper.writeValueAsString(sentimentData);
            model.addAttribute("sentimentChartData", json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        //todo: sentiment pie diagram
        HashSet<String> sentimentPieces = new HashSet<>();
        ArrayList<SentimentPieDto> sentimentPieDtos = new ArrayList<>();

        for (AllDataDto allDatum : allData) {
            sentimentPieces.add(allDatum.getSentiment());
        }

        int count = 0;
        int totalSentimentPieces = allData.size();
        for (String sentiment : sentimentPieces) {
            for (AllDataDto allDatum : allData) {
                if (sentiment.equals(allDatum.getSentiment())) {
                    count++;
                }
            }
            double percentage = (double) count / totalSentimentPieces * 100.0;
            sentimentPieDtos.add(new SentimentPieDto(sentiment, count, Math.round(percentage)));
            count = 0;
        }

        sentimentPieDtos.sort(new Comparator<SentimentPieDto>() {
            public int compare(SentimentPieDto s1, SentimentPieDto s2) {
                return s1.getSentiment().compareTo(s2.getSentiment());
            }
        });

        mapper = new ObjectMapper();

        try {
            String json = mapper.writeValueAsString(sentimentPieDtos);
            model.addAttribute("sentimentPieData", json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        //todo: mentions count chart
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

        //todo: word cloud
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

        //todo: reach count chart
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

        Instant end = Instant.now();
        System.out.println(Duration.between(start, end));

        return "mainDashboard";
    }

    @GetMapping("/panel")
    public String panel(@AuthenticationPrincipal User user, Model model) {
        ArrayList<Project> projects = (ArrayList<Project>) projectRepo.findAllByUser(user);

        model.addAttribute("projects", projects);

        return "panel";
    }

    @GetMapping("/panel/sources/{projectId}")
    public String analyseBySource(@PathVariable String projectId, Model model) {
        projectId = projectId.replace(",", "");
        long longProjId = Long.parseLong(projectId);

        ArrayList<String> platformNames = new ArrayList<>();

        platformNames.add(PlatformName.YOU_TUBE.name());
        platformNames.add(PlatformName.REDDIT.name());
        platformNames.add(PlatformName.TWITTER.name());

        model.addAttribute("sources", platformNames);
        model.addAttribute("projectId", longProjId);

        return "sources";
    }

    @PostMapping("/panel/results/{projectId}/{mentionId}")
    public String deleteMention(@PathVariable String projectId, @PathVariable String mentionId) {
        projectId = projectId.replace(",", "");
        long longProjId = Long.parseLong(projectId);

        mentionId = mentionId.replace(",", "");
        long longMentionId = Long.parseLong(mentionId);

        Optional<TwitterData> twitterData = twitterDataRepo.findById(longMentionId);
        Optional<RedditData> redditData = redditDataRepo.findById(longMentionId);
        Optional<YTData> ytData = ytDataRepo.findById(longMentionId);

        twitterData.ifPresent(twitterDataRepo::delete);
        redditData.ifPresent(redditDataRepo::delete);
        ytData.ifPresent(ytDataRepo::delete);

        return "redirect:/panel/results/" + longProjId;
    }

    private static class WordCloudData {
        public static List<Map<String, Object>> getWords() {
            List<Map<String, Object>> words = new ArrayList<>();
            words.add(Map.of("text", "Lorem", "size", 10));
            words.add(Map.of("text", "ipsum", "size", 20));
            words.add(Map.of("text", "dolor", "size", 30));
            words.add(Map.of("text", "sit", "size", 40));
            words.add(Map.of("text", "amet", "size", 50));
            return words;
        }
    }
}
