package cdu.diploma.mediamonitoring.controller;

import cdu.diploma.mediamonitoring.data.processing.SentimentAnalysis;
import cdu.diploma.mediamonitoring.model.*;
import cdu.diploma.mediamonitoring.repo.ProjectRepo;
import cdu.diploma.mediamonitoring.repo.RedditDataRepo;
import cdu.diploma.mediamonitoring.repo.TwitterDataRepo;
import cdu.diploma.mediamonitoring.repo.YTDataRepo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
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
