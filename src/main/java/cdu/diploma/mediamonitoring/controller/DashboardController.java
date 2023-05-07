package cdu.diploma.mediamonitoring.controller;

import cdu.diploma.mediamonitoring.data.processing.SentimentAnalysis;
import cdu.diploma.mediamonitoring.dto.AllData;
import cdu.diploma.mediamonitoring.dto.SentimentData;
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
            List<TwitterData> allTwitterData = twitterDataRepo.findAllBySocialMediaPlatform(socialMediaPlatform);
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
            List<RedditData> allRedditData = redditDataRepo.findAllBySocialMediaPlatform(socialMediaPlatform);
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
            List<YTData> allYTData = ytDataRepo.findAllBySocialMediaPlatform(socialMediaPlatform);
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
        ArrayList<SentimentData> sentimentData = new ArrayList<>();
        HashSet<String> dates = new HashSet<>();
        List<TwitterData> allTwitterData = twitterDataRepo.findAllBySocialMediaPlatformOrderByTweetedAt(socialMediaPlatform);
        List<RedditData> allRedditData = redditDataRepo.findAllBySocialMediaPlatformOrderBySubDate(socialMediaPlatform);
        List<YTData> allYTData = ytDataRepo.findAllBySocialMediaPlatformOrderByPublicationTime(socialMediaPlatform);
        ArrayList<AllData> allData = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (TwitterData twitterData : allTwitterData) {
            String formattedDate = sdf.format(twitterData.getTweetedAt());
            allData.add(new AllData(formattedDate, twitterData.getSentiment()));
        }

        for (RedditData redditData : allRedditData) {
            String formattedDate = sdf.format(redditData.getSubDate());
            allData.add(new AllData(formattedDate, redditData.getSentiment()));
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
            allData.add(new AllData(date, ytData.getSentiment()));
        }

        allData.sort(new Comparator<AllData>() {
            public int compare(AllData s1, AllData s2) {
                return s1.getDate().compareTo(s2.getDate());
            }
        });

        for (AllData allDatum : allData) {
            dates.add(allDatum.getDate());
        }

        int posCount = 0;
        int negCount = 0;
        for (String date : dates) {
            for (AllData allDatum : allData) {
                if (date.equals(allDatum.getDate())) {
                    if (Objects.equals(allDatum.getSentiment(), "Positive")) {
                        posCount++;
                    } else if (Objects.equals(allDatum.getSentiment(), "Negative")) {
                        negCount++;
                    }
                }
            }
            sentimentData.add(new SentimentData(date, posCount, negCount));
            posCount = 0;
            negCount = 0;
        }

        sentimentData.sort(new Comparator<SentimentData>() {
            public int compare(SentimentData s1, SentimentData s2) {
                return s1.getDate().compareTo(s2.getDate());
            }
        });

        ObjectMapper mapper = new ObjectMapper();

        try {
            String json = mapper.writeValueAsString(sentimentData);
            model.addAttribute("charData", json);
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
}
