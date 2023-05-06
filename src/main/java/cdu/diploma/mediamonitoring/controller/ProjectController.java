package cdu.diploma.mediamonitoring.controller;

import cdu.diploma.mediamonitoring.model.Project;
import cdu.diploma.mediamonitoring.model.SocialMediaPlatform;
import cdu.diploma.mediamonitoring.model.User;
import cdu.diploma.mediamonitoring.repo.ProjectRepo;
import cdu.diploma.mediamonitoring.repo.SocialMediaPlatformRepo;
import cdu.diploma.mediamonitoring.service.RedditService;
import cdu.diploma.mediamonitoring.service.TwitterService;
import cdu.diploma.mediamonitoring.service.YTService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@Controller
public class ProjectController {
    private final SocialMediaPlatformRepo socialMediaPlatformRepo;
    private final RedditService redditService;
    private final TwitterService twitterService;
    private final YTService ytService;
    private final ProjectRepo projectRepo;

    @Autowired
    public ProjectController(SocialMediaPlatformRepo socialMediaPlatformRepo, RedditService redditService, TwitterService twitterService, YTService ytService, ProjectRepo projectRepo) {
        this.socialMediaPlatformRepo = socialMediaPlatformRepo;
        this.redditService = redditService;
        this.twitterService = twitterService;
        this.ytService = ytService;
        this.projectRepo = projectRepo;
    }

    @GetMapping("/create-project")
    public String projectCreation() {
        return "createNewProject";
    }

    @PostMapping("/create-project")
    public String createProject(
            @AuthenticationPrincipal User user,
            @RequestParam("tags") String keywords,
            @RequestParam("brand") String projName) throws Exception {
        StringBuilder keys = getKeywordsStringFromJson(keywords);

        SocialMediaPlatform smp = new SocialMediaPlatform();

        Project project = new Project();
        project.setKeywords(keys.toString());
        project.setName(projName);
        project.setUser(user);
        project.setCreatedAt(new Date());

        smp.setProject(project);
        project.setSocialMediaPlatform(smp);

        socialMediaPlatformRepo.save(smp);
        projectRepo.save(project);

        Project projectByUser = projectRepo.findByUser(user);
        SocialMediaPlatform socialMediaPlatform = projectByUser.getSocialMediaPlatform();

        String[] brandKeywords = separateKeywords(keys.toString());

        redditService.searchReddit(brandKeywords, socialMediaPlatform);
        twitterService.collectDataForModel(brandKeywords, socialMediaPlatform);
        ytService.getVideoData(brandKeywords, socialMediaPlatform);

        return "redirect:/panel/results/" + projectByUser.getId();
    }

    @PostMapping("/delete-project/{projectId}")
    public String deleteProject(
            @AuthenticationPrincipal User user,
            @PathVariable Long projectId) {
        Project project = projectRepo.findProjectById(projectId);
        projectRepo.delete(project);

        return "redirect:/panel";
    }

    @GetMapping("/edit-project/{projectId}")
    public String getEditProject(
            @AuthenticationPrincipal User user,
            @PathVariable Long projectId,
            Model model) {
        Project project = projectRepo.findProjectById(projectId);

        String keywords = project.getKeywords();

        String[] keys = separateKeywords(keywords);

        model.addAttribute("project", project);
        model.addAttribute("keywords", keys);

        return "projectSettings";
    }

    @PostMapping("/edit-project/{projectId}")
    public String editProject(
            @PathVariable Long projectId,
            @RequestParam("tags") String keywords,
            @RequestParam("brand") String projName) {
        Project project = projectRepo.findProjectById(projectId);
        StringBuilder keys = getKeywordsStringFromJson(keywords);

        project.setName(projName);
        project.setCreatedAt(new Date());
        project.setKeywords(keys.toString());

        projectRepo.save(project);

        return "redirect:/panel";
    }

    @NotNull
    private StringBuilder getKeywordsStringFromJson(String keywords) {
        StringBuilder keys = new StringBuilder();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Keyword> keywordList = objectMapper.readValue(keywords, new TypeReference<List<Keyword>>(){});

            for (Keyword keyword : keywordList) {
                keys.append(keyword.getValue()).append(", ");
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return keys;
    }

    @NotNull
    private String[] separateKeywords(String keywords) {
        String[] keys = keywords.split(",");

        for (int i = 0; i < keys.length; i++) {
            keys[i] = keys[i].trim();
        }
        return keys;
    }

    private static class Keyword {
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
