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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
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
            @RequestParam("brand") String projName) {
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




        return "redirect:/dashboard";
    }

    @PostMapping("/delete-project/{projectId}")
    public String deleteProject(
            @AuthenticationPrincipal User user,
            @PathVariable Long projectId) {
        Project project = projectRepo.findProjectById(projectId);
        projectRepo.delete(project);

        return "redirect:/panel";
    }

    //todo: finish project editing
    @GetMapping("/edit-project/{projectId}")
    public String getEditProject(
            @AuthenticationPrincipal User user,
            @PathVariable Long projectId,
            @RequestParam("tags") String keywords,
            @RequestParam("brand") String projName) {


        return "projectSettings";
    }

    @PostMapping("/edit-project/{projectId}")
    public String editProject(
            @AuthenticationPrincipal User user,
            @PathVariable Long projectId,
            @RequestParam("tags") String keywords,
            @RequestParam("brand") String projName) {
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




        return "redirect:/panel";
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
