package cdu.diploma.mediamonitoring.controller;

import cdu.diploma.mediamonitoring.dto.AllDataDto;
import cdu.diploma.mediamonitoring.dto.MentionsDto;
import cdu.diploma.mediamonitoring.dto.SentimentDataDto;
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

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.List;

@Controller
public class DashboardController {
    private final ProjectRepo projectRepo;
    private final RedditDataRepo redditDataRepo;
    private final TwitterDataRepo twitterDataRepo;
    private final YTDataRepo ytDataRepo;
    private final AnalysingService analysingService;

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

        analysingService.doParallelSentimentAnalysis(model, socialMediaPlatform, source);

        model.addAttribute("project", project);

        ArrayList<SentimentDataDto> sentimentData = new ArrayList<>();
        HashSet<String> dates = new HashSet<>();
        ArrayList<AllDataDto> allData = analysingService.getAllDataDtos(socialMediaPlatform, dates, source);

        analysingService.sentimentDataChart(model, sentimentData, dates, allData);

        analysingService.sentimentPieGraph(model, allData);

        analysingService.totalMentionsCountChart(model, dates, allData);

        analysingService.wordCloudGeneration(model, allData);

        analysingService.reachAnalysis(model, dates, allData);

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
