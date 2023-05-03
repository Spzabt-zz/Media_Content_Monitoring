package cdu.diploma.mediamonitoring.controller;

import cdu.diploma.mediamonitoring.model.Project;
import cdu.diploma.mediamonitoring.model.SocialMediaPlatform;
import cdu.diploma.mediamonitoring.model.User;
import cdu.diploma.mediamonitoring.repo.ProjectRepo;
import cdu.diploma.mediamonitoring.repo.SocialMediaPlatformRepo;
import com.google.api.client.util.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
public class ProjectController {
    private final SocialMediaPlatformRepo socialMediaPlatformRepo;
    private final ProjectRepo projectRepo;

    @Autowired
    public ProjectController(SocialMediaPlatformRepo socialMediaPlatformRepo, ProjectRepo projectRepo) {
        this.socialMediaPlatformRepo = socialMediaPlatformRepo;
        this.projectRepo = projectRepo;
    }

    @GetMapping("/create-project")
    public String projectCreation() {
        return "createNewProject";
    }

    @PostMapping("/create-project")
    public String createProject(
            @AuthenticationPrincipal User user,
            @RequestParam String keywords,
            @RequestParam String projName) {

        SocialMediaPlatform smp = new SocialMediaPlatform();

        Project project = new Project();
        project.setKeywords(keywords);
        project.setName(projName);
        project.setUser(user);
        project.setCreatedAt(new Date());

        smp.setProject(project);
        project.setSocialMediaPlatform(smp);

        socialMediaPlatformRepo.save(smp);
        projectRepo.save(project);

        return "registration";
    }
}
