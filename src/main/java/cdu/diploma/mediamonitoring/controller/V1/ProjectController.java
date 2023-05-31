package cdu.diploma.mediamonitoring.controller.V1;

import cdu.diploma.mediamonitoring.domain.model.*;
import cdu.diploma.mediamonitoring.domain.repo.*;
import cdu.diploma.mediamonitoring.domain.service.RedditService;
import cdu.diploma.mediamonitoring.domain.service.SearchingService;
import cdu.diploma.mediamonitoring.domain.service.TwitterService;
import cdu.diploma.mediamonitoring.domain.service.YTService;
import cdu.diploma.mediamonitoring.util.KeywordsUtil;
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
    private final SearchingService searchMentions;

    @Autowired
    public ProjectController(SocialMediaPlatformRepo socialMediaPlatformRepo, RedditService redditService, TwitterService twitterService, YTService ytService, ProjectRepo projectRepo, RedditDataRepo redditDataRepo, TwitterDataRepo twitterDataRepo, YTDataRepo ytDataRepo, ApiCredentialsRepo apiCredentialsRepo, UserRepo userRepo, SearchingService searchMentions) {
        this.socialMediaPlatformRepo = socialMediaPlatformRepo;
        this.redditService = redditService;
        this.twitterService = twitterService;
        this.ytService = ytService;
        this.projectRepo = projectRepo;
        this.redditDataRepo = redditDataRepo;
        this.twitterDataRepo = twitterDataRepo;
        this.ytDataRepo = ytDataRepo;
        this.searchMentions = searchMentions;
    }

    @GetMapping("/create-project")
    public String projectCreation() {
        return "createNewProject";
    }

    @PostMapping("/create-project")
    public String createProject(
            @AuthenticationPrincipal User user,
            @RequestParam("tags") String keywords,
            @RequestParam("brand") String projName, Model model) throws Exception {
        if (keywords.isEmpty() || projName.isEmpty()) {
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Fields can't be blank.");
            return  "createNewProject";
        }

        StringBuilder keys = KeywordsUtil.getKeywordsStringFromJson(keywords);

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

        if (!redditService.checkRedditApiConnection(user)) {
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Connection to reddit API failed");
            return  "createNewProject";
        }

        searchMentions.searchMentions(user, keys, project);

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

        String[] keys = KeywordsUtil.separateKeywords(keywords);

        model.addAttribute("project", project);
        model.addAttribute("keywords", keys);

        return "projectSettings";
    }

    @PostMapping("/edit-project/{projectId}")
    public String editProject(
            @AuthenticationPrincipal User user,
            @PathVariable String projectId,
            @RequestParam("tags") String keywords,
            @RequestParam("brand") String projName, Model model) throws Exception {
        projectId = projectId.replace(",", "");
        Long longProjId = Long.valueOf(projectId);

        Project project = projectRepo.findProjectById(longProjId);

        model.addAttribute("project", project);
        if (keywords.isEmpty() || projName.isEmpty()) {
            String keywords1 = project.getKeywords();

            String[] keysForError = KeywordsUtil.separateKeywords(keywords1);

            model.addAttribute("keywords", keysForError);
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Fields can't be blank.");
            return  "projectSettings";
        }

        StringBuilder keys = KeywordsUtil.getKeywordsStringFromJson(keywords);

        if (Objects.equals(project.getKeywords(), keys.toString())) {
            String keywords1 = project.getKeywords();

            String[] keysForError = KeywordsUtil.separateKeywords(keywords1);

            model.addAttribute("keywords", keysForError);
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Nothing changed.");
            return  "projectSettings";
        }

        boolean isStartSearch = !Objects.equals(project.getKeywords(), keys.toString());

        project.setName(projName);
        project.setCreatedAt(new Date());
        project.setKeywords(keys.toString());

        projectRepo.save(project);

        String[] brandKeywords = KeywordsUtil.separateKeywords(keys.toString());
        SocialMediaPlatform socialMediaPlatform = socialMediaPlatformRepo.findByProjectId(project.getId());

        if (isStartSearch) {
            ArrayList<TwitterData> twitterData = twitterDataRepo.findAllBySocialMediaPlatform(socialMediaPlatform);
            ArrayList<RedditData> redditData = redditDataRepo.findAllBySocialMediaPlatform(socialMediaPlatform);
            ArrayList<YTData> ytData = ytDataRepo.findAllBySocialMediaPlatform(socialMediaPlatform);

            twitterDataRepo.deleteAll(twitterData);
            redditDataRepo.deleteAll(redditData);
            ytDataRepo.deleteAll(ytData);

            redditService.searchReddit(brandKeywords, socialMediaPlatform, user);
            twitterService.collectDataForModel(brandKeywords, socialMediaPlatform, user);
            ytService.getVideoData(brandKeywords, socialMediaPlatform, user);
        }

        return "redirect:/panel/results/" + project.getId();
    }
}
