package cdu.diploma.mediamonitoring.controller.V1;

import cdu.diploma.mediamonitoring.domain.model.Comparison;
import cdu.diploma.mediamonitoring.domain.model.Project;
import cdu.diploma.mediamonitoring.domain.model.User;
import cdu.diploma.mediamonitoring.domain.repo.ComparisonRepo;
import cdu.diploma.mediamonitoring.domain.repo.ProjectRepo;
import cdu.diploma.mediamonitoring.domain.service.ComparisonService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ComparisonController {
    private final ProjectRepo projectRepo;
    private final ComparisonRepo comparisonRepo;
    private final ComparisonService comparisonService;

    public ComparisonController(ProjectRepo projectRepo, ComparisonRepo comparisonRepo, ComparisonService comparisonService) {
        this.projectRepo = projectRepo;
        this.comparisonRepo = comparisonRepo;
        this.comparisonService = comparisonService;
    }

    @GetMapping("/panel/compare/{projectId}")
    public String compareProjects(@PathVariable String projectId, @AuthenticationPrincipal User user, Model model) {
        projectId = projectId.replace(",", "");
        long longProjId = Long.parseLong(projectId);

        Project project = projectRepo.findProjectById(longProjId);
        List<Project> projectsByUser = projectRepo.findAllByUser(user);

        model.addAttribute("project", project);
        List<Comparison> comparisons = comparisonService.getComparisons(user, model);

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
