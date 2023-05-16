package cdu.diploma.mediamonitoring.controller.V1;

import cdu.diploma.mediamonitoring.domain.dto.KeywordDto;
import cdu.diploma.mediamonitoring.domain.model.*;
import cdu.diploma.mediamonitoring.domain.repo.*;
import cdu.diploma.mediamonitoring.domain.service.RedditService;
import cdu.diploma.mediamonitoring.domain.service.TwitterService;
import cdu.diploma.mediamonitoring.domain.service.YTService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
public class ProjectController {
    private final SocialMediaPlatformRepo socialMediaPlatformRepo;
    private final RedditService redditService;
    private final TwitterService twitterService;
    private final YTService ytService;
    private final ProjectRepo projectRepo;
    private final RedditDataRepo redditDataRepo;
    private final TwitterDataRepo twitterDataRepo;
    private final YTDataRepo ytDataRepo;
    private final ApiCredentialsRepo apiCredentialsRepo;
    private final UserRepo userRepo;

    @Autowired
    public ProjectController(SocialMediaPlatformRepo socialMediaPlatformRepo, RedditService redditService, TwitterService twitterService, YTService ytService, ProjectRepo projectRepo, RedditDataRepo redditDataRepo, TwitterDataRepo twitterDataRepo, YTDataRepo ytDataRepo, ApiCredentialsRepo apiCredentialsRepo, UserRepo userRepo) {
        this.socialMediaPlatformRepo = socialMediaPlatformRepo;
        this.redditService = redditService;
        this.twitterService = twitterService;
        this.ytService = ytService;
        this.projectRepo = projectRepo;
        this.redditDataRepo = redditDataRepo;
        this.twitterDataRepo = twitterDataRepo;
        this.ytDataRepo = ytDataRepo;
        this.apiCredentialsRepo = apiCredentialsRepo;
        this.userRepo = userRepo;
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

        //
//        ApiCredentials apiCredentials = new ApiCredentials();
//
//        apiCredentials.setRedditClientId(redditClient);
//        apiCredentials.setRedditClientSecret(redditClientSecret);
//
//        apiCredentials.setTwitterConsumerKey(twitterConsumerKey);
//        apiCredentials.setTwitterConsumerSecret(twitterConsumerSecret);
//        apiCredentials.setTwitterAccessToken(twitterAccessToken);
//        apiCredentials.setTwitterAccessTokenSecret(twitterAccessTokenSecret);
//
//        apiCredentials.setYtApiKey(ytApiKey);
//        apiCredentialsRepo.save(apiCredentials);
//        user.setApiCredentials(apiCredentials);
//        userRepo.save(user);
        //

        //Project projectByUser = projectRepo.findByName(projName);
        SocialMediaPlatform socialMediaPlatform = project.getSocialMediaPlatform();

        String[] brandKeywords = separateKeywords(keys.toString());

        redditService.setUser(user);
        twitterService.setUser(user);
        ytService.setUser(user);
        redditService.searchReddit(brandKeywords, socialMediaPlatform, user);
        twitterService.collectDataForModel(brandKeywords, socialMediaPlatform, user);
        ytService.getVideoData(brandKeywords, socialMediaPlatform, user);
        return "redirect:/panel/results/" + project.getId();
    }

    @PostMapping("/delete-project/{projectId}")
    public String deleteProject(
            @AuthenticationPrincipal User user,
            @PathVariable String projectId) {
        projectId = projectId.replace(",", "");
        Long longProjId = Long.valueOf(projectId);

        Project project = projectRepo.findProjectById(longProjId);
        SocialMediaPlatform socialMediaPlatform = socialMediaPlatformRepo.findByProjectId(project.getId());

        ArrayList<TwitterData> twitterData = twitterDataRepo.findAllBySocialMediaPlatform(socialMediaPlatform);
        ArrayList<RedditData> redditData = redditDataRepo.findAllBySocialMediaPlatform(socialMediaPlatform);
        ArrayList<YTData> ytData = ytDataRepo.findAllBySocialMediaPlatform(socialMediaPlatform);

        twitterDataRepo.deleteAll(twitterData);
        redditDataRepo.deleteAll(redditData);
        ytDataRepo.deleteAll(ytData);
        socialMediaPlatformRepo.delete(socialMediaPlatform);
        projectRepo.delete(project);

        return "redirect:/panel";
    }

    @GetMapping("/edit-project/{projectId}")
    public String getEditProject(
            @AuthenticationPrincipal User user,
            @PathVariable String projectId,
            Model model) {
        projectId = projectId.replace(",", "");
        Long longProjId = Long.valueOf(projectId);

        Project project = projectRepo.findProjectById(longProjId);

        String keywords = project.getKeywords();

        String[] keys = separateKeywords(keywords);

        model.addAttribute("project", project);
        model.addAttribute("keywords", keys);

        return "projectSettings";
    }

    @PostMapping("/edit-project/{projectId}")
    public String editProject(
            @PathVariable String projectId,
            @RequestParam("tags") String keywords,
            @RequestParam("brand") String projName) {
        projectId = projectId.replace(",", "");
        Long longProjId = Long.valueOf(projectId);

        Project project = projectRepo.findProjectById(longProjId);
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
            List<KeywordDto> keywordList = objectMapper.readValue(keywords, new TypeReference<List<KeywordDto>>() {
            });

            for (KeywordDto keyword : keywordList) {
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
            if (Objects.equals(keys[i], " "))
                break;
            else
                keys[i] = keys[i].trim();
        }

        String[] newKeys = new String[keys.length - 1];
        System.arraycopy(keys, 0, newKeys, 0, keys.length - 1);

        return newKeys;
    }
}
