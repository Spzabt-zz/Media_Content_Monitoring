package cdu.diploma.mediamonitoring.controller;

import cdu.diploma.mediamonitoring.model.Project;
import cdu.diploma.mediamonitoring.model.User;
import cdu.diploma.mediamonitoring.repo.ProjectRepo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;

@Controller
public class DashboardController {
    private final ProjectRepo projectRepo;

    public DashboardController(ProjectRepo projectRepo) {
        this.projectRepo = projectRepo;
    }

    @GetMapping("/")
    public String greeting(@AuthenticationPrincipal User user, Model model) {
        if (user != null)
            model.addAttribute("username", user.getUsername());
        return "greeting";
    }

    @GetMapping("/panel/results/{projectId}")
    public String mentions(
            @PathVariable Long projectId,
            Model model) {
        Project project = projectRepo.findProjectById(projectId);
        model.addAttribute("project", project);

        return "mainDashboard";
    }

    @GetMapping("/panel")
    public String panel(@AuthenticationPrincipal User user, Model model) {
        ArrayList<Project> projects = (ArrayList<Project>) projectRepo.findAllByUser(user);

        model.addAttribute("projects", projects);

        return "panel";
    }
}
