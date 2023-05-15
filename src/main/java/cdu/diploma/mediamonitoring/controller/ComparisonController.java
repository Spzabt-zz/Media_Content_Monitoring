package cdu.diploma.mediamonitoring.controller;

import cdu.diploma.mediamonitoring.dto.AllDataDto;
import cdu.diploma.mediamonitoring.dto.SentimentDataDto;
import cdu.diploma.mediamonitoring.model.*;
import cdu.diploma.mediamonitoring.repo.*;
import cdu.diploma.mediamonitoring.service.AnalysingService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Controller
public class ComparisonController {
    private final ProjectRepo projectRepo;
    private final RedditDataRepo redditDataRepo;
    private final TwitterDataRepo twitterDataRepo;
    private final YTDataRepo ytDataRepo;
    private final ComparisonRepo comparisonRepo;
    private final AnalysingService analysingService;
    private final AnalyseDataRepo analyseDataRepo;
    private final SocialMediaPlatformRepo socialMediaPlatformRepo;

    public ComparisonController(ProjectRepo projectRepo, RedditDataRepo redditDataRepo, TwitterDataRepo twitterDataRepo, YTDataRepo ytDataRepo, ComparisonRepo comparisonRepo, AnalysingService analysingService, AnalyseDataRepo analyseDataRepo, SocialMediaPlatformRepo socialMediaPlatformRepo) {
        this.projectRepo = projectRepo;
        this.redditDataRepo = redditDataRepo;
        this.twitterDataRepo = twitterDataRepo;
        this.ytDataRepo = ytDataRepo;
        this.comparisonRepo = comparisonRepo;
        this.analysingService = analysingService;
        this.analyseDataRepo = analyseDataRepo;
        this.socialMediaPlatformRepo = socialMediaPlatformRepo;
    }

    @GetMapping("/panel/compare/{projectId}")
    public String compareProjects(@PathVariable String projectId, @AuthenticationPrincipal User user, Model model) {
        projectId = projectId.replace(",", "");
        long longProjId = Long.parseLong(projectId);

        Project project = projectRepo.findProjectById(longProjId);
        List<Project> projectsByUser = projectRepo.findAllByUser(user);
        //SocialMediaPlatform socialMediaPlatform = project.getSocialMediaPlatform();

        model.addAttribute("project", project);
        List<Comparison> comparisons = comparisonRepo.findAllByUser(user);

        for (Comparison comparison : comparisons) {
            ArrayList<SentimentDataDto> sentimentData = new ArrayList<>();
            HashSet<String> dates = new HashSet<>();
            ArrayList<AllDataDto> allData = analysingService.getAllDataDtos(comparison.getProject().getSocialMediaPlatform(), dates, PlatformName.NONE.name());

            AnalyseData analyseData = new AnalyseData();
            analysingService.sentimentDataChart(model, sentimentData, dates, allData, analyseData);

            analysingService.sentimentPieGraph(model, allData, analyseData);

            analysingService.totalMentionsCountChart(model, dates, allData, analyseData);

            analysingService.wordCloudGeneration(model, allData, analyseData);

            analysingService.reachAnalysis(model, dates, allData, analyseData);

            analyseData.setSocialMediaPlatform(comparison.getProject().getSocialMediaPlatform());
            analyseDataRepo.save(analyseData);
            comparison.getProject().getSocialMediaPlatform().setAnalyseData(analyseData);
            socialMediaPlatformRepo.save(comparison.getProject().getSocialMediaPlatform());

            System.out.println(comparison.getId() + " ------------------ " + comparison.getProject().getSocialMediaPlatform().getAnalyseData().reachAnalysis);
        }

        model.addAttribute("projects", comparisons);
        model.addAttribute("projectsByUser", projectsByUser);
        model.addAttribute("index", 0);

        return "comparing";
    }

    @PostMapping("/panel/compare/{projectId}")
    public String addToComparingProjects(@PathVariable String projectId, @AuthenticationPrincipal User user, Model model) {
        projectId = projectId.replace(",", "");
        long longProjId = Long.parseLong(projectId);

        Project projectById = projectRepo.findProjectById(longProjId);
        List<Project> projectsByUser = projectRepo.findAllByUser(user);

        //List<Comparison> comparisons = comparisonRepo.findAllByUser(user);
        Comparison comparison = new Comparison();

        comparison.setProject(projectById);
        comparison.setUser(user);
        comparisonRepo.save(comparison);

        model.addAttribute("project", projectById);
        model.addAttribute("projectsByUser", projectsByUser);
        return "redirect:/panel";
    }

    @PostMapping("/comparison-delete/{projectId}")
    public String deleteProductComparison(@AuthenticationPrincipal User user, @PathVariable String projectId) {
        projectId = projectId.replace(",", "");
        long longProjId = Long.parseLong(projectId);

        Project project = projectRepo.findProjectById(longProjId);
        Comparison comparison = comparisonRepo.findTop1ComparisonByProjectAndUser(project, user);

        comparisonRepo.delete(comparison);

        return "redirect:/panel/compare/" + longProjId;
    }
}
