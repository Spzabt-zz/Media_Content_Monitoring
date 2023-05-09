package cdu.diploma.mediamonitoring.controller;

import cdu.diploma.mediamonitoring.data.processing.SentimentAnalysis;
import cdu.diploma.mediamonitoring.dto.AllDataDto;
import cdu.diploma.mediamonitoring.dto.MentionsDto;
import cdu.diploma.mediamonitoring.dto.SentimentDataDto;
import cdu.diploma.mediamonitoring.dto.SentimentPieDto;
import cdu.diploma.mediamonitoring.model.*;
import cdu.diploma.mediamonitoring.repo.ProjectRepo;
import cdu.diploma.mediamonitoring.repo.RedditDataRepo;
import cdu.diploma.mediamonitoring.repo.TwitterDataRepo;
import cdu.diploma.mediamonitoring.repo.YTDataRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

@Controller
public class DashboardController {
    private final ProjectRepo projectRepo;
    private final RedditDataRepo redditDataRepo;
    private final TwitterDataRepo twitterDataRepo;
    private final YTDataRepo ytDataRepo;
    private final SentimentAnalysis sentimentAnalysis;

    public DashboardController(ProjectRepo projectRepo, RedditDataRepo redditDataRepo, TwitterDataRepo twitterDataRepo, YTDataRepo ytDataRepo) {
        this.projectRepo = projectRepo;
        this.redditDataRepo = redditDataRepo;
        this.twitterDataRepo = twitterDataRepo;
        this.ytDataRepo = ytDataRepo;
        this.sentimentAnalysis = new SentimentAnalysis();
    }

    @GetMapping("/")
    public String greeting(@AuthenticationPrincipal User user, Model model) {
        if (user != null)
            model.addAttribute("username", user.getUsername());
        return "greeting";
    }

    @GetMapping("/panel/results/{projectId}")
    public String mentions(@PathVariable String projectId, Model model) {
        Instant start = Instant.now();

        AtomicBoolean isAnalysedTwitter = new AtomicBoolean(false);
        AtomicBoolean isAnalysedReddit = new AtomicBoolean(false);
        AtomicBoolean isAnalysedYouTube = new AtomicBoolean(false);

        projectId = projectId.replace(",", "");
        Long longProjId = Long.valueOf(projectId);

        Project project = projectRepo.findProjectById(longProjId);
        SocialMediaPlatform socialMediaPlatform = project.getSocialMediaPlatform();

        List<TwitterData> updatedTwitterData = new ArrayList<>();
        List<RedditData> updatedRedditData = new ArrayList<>();
        List<YTData> updatedYTData = new ArrayList<>();

        List<Future<?>> futures = new ArrayList<>();

        ExecutorService twitterExecutor = Executors.newFixedThreadPool(5);
        ExecutorService redditExecutor = Executors.newFixedThreadPool(5);
        ExecutorService youtubeExecutor = Executors.newFixedThreadPool(5);

        futures.add(twitterExecutor.submit(() -> {
            List<TwitterData> allTwitterData = twitterDataRepo.findAllBySocialMediaPlatformOrderByTweetedAtDesc(socialMediaPlatform);
            for (TwitterData twitterData : allTwitterData) {
                if (twitterData.getSentiment() != null) {
                    isAnalysedTwitter.set(true);
                    continue;
                }
                String sentiment = sentimentAnalysis.doSentimentAnalysis(twitterData.getTweet());
                System.out.println("twitter: " + sentiment);
                twitterData.setSentiment(sentiment);
                updatedTwitterData.add(twitterData);
            }
            if (isAnalysedTwitter.get())
                model.addAttribute("allTwitterData", allTwitterData);
            else
                model.addAttribute("allTwitterData", updatedTwitterData);
        }));

        futures.add(redditExecutor.submit(() -> {
            List<RedditData> allRedditData = redditDataRepo.findAllBySocialMediaPlatformOrderBySubDateDesc(socialMediaPlatform);
            for (RedditData redditData : allRedditData) {
                if (redditData.getSentiment() != null) {
                    isAnalysedReddit.set(true);
                    continue;
                }
                if (!Objects.equals(redditData.getSubBody(), "")) {
                    String sentiment = sentimentAnalysis.doSentimentAnalysis(redditData.getSubBody());
                    System.out.println("reddit: " + sentiment);
                    redditData.setSentiment(sentiment);
                } else {
                    String sentiment = sentimentAnalysis.doSentimentAnalysis(redditData.getSubTitle());
                    System.out.println("reddit: " + sentiment);
                    redditData.setSentiment(sentiment);
                }
                updatedRedditData.add(redditData);
            }
            if (isAnalysedReddit.get())
                model.addAttribute("allRedditData", allRedditData);
            else
                model.addAttribute("allRedditData", updatedRedditData);
        }));

        futures.add(youtubeExecutor.submit(() -> {
            List<YTData> allYTData = ytDataRepo.findAllBySocialMediaPlatformOrderByPublicationTimeDesc(socialMediaPlatform);
            for (YTData ytData : allYTData) {
                if (ytData.getSentiment() != null) {
                    isAnalysedYouTube.set(true);
                    continue;
                }
                String sentiment = sentimentAnalysis.doSentimentAnalysis(ytData.getComment());
                System.out.println("youtube: " + sentiment);
                ytData.setSentiment(sentiment);
                updatedYTData.add(ytData);
            }
            if (isAnalysedYouTube.get())
                model.addAttribute("allYTData", allYTData);
            else
                model.addAttribute("allYTData", updatedYTData);
        }));

        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        twitterExecutor.shutdown();
        redditExecutor.shutdown();
        youtubeExecutor.shutdown();

        twitterDataRepo.saveAll(updatedTwitterData);
        redditDataRepo.saveAll(updatedRedditData);
        ytDataRepo.saveAll(updatedYTData);

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
            allData.add(new AllDataDto(formattedDate, twitterData.getSentiment()));
        }

        for (RedditData redditData : allRedditData) {
            String formattedDate = sdf.format(redditData.getSubDate());
            allData.add(new AllDataDto(formattedDate, redditData.getSentiment()));
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
            allData.add(new AllDataDto(date, ytData.getSentiment()));
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

        //todo: sentiment pie diagram +
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

        //todo: reach count chart

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
}
