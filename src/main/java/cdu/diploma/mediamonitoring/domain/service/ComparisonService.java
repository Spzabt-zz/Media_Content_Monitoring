package cdu.diploma.mediamonitoring.domain.service;

import cdu.diploma.mediamonitoring.domain.model.AnalyseData;
import cdu.diploma.mediamonitoring.domain.model.Comparison;
import cdu.diploma.mediamonitoring.domain.model.PlatformName;
import cdu.diploma.mediamonitoring.domain.model.User;
import cdu.diploma.mediamonitoring.domain.repo.*;
import cdu.diploma.mediamonitoring.domain.dto.AllDataDto;
import cdu.diploma.mediamonitoring.domain.dto.SentimentDataDto;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class ComparisonService {
    private final ProjectRepo projectRepo;
    private final RedditDataRepo redditDataRepo;
    private final TwitterDataRepo twitterDataRepo;
    private final YTDataRepo ytDataRepo;
    private final ComparisonRepo comparisonRepo;
    private final AnalysingService analysingService;
    private final AnalyseDataRepo analyseDataRepo;
    private final SocialMediaPlatformRepo socialMediaPlatformRepo;

    public ComparisonService(ProjectRepo projectRepo, RedditDataRepo redditDataRepo, TwitterDataRepo twitterDataRepo, YTDataRepo ytDataRepo, ComparisonRepo comparisonRepo, AnalysingService analysingService, AnalyseDataRepo analyseDataRepo, SocialMediaPlatformRepo socialMediaPlatformRepo) {
        this.projectRepo = projectRepo;
        this.redditDataRepo = redditDataRepo;
        this.twitterDataRepo = twitterDataRepo;
        this.ytDataRepo = ytDataRepo;
        this.comparisonRepo = comparisonRepo;
        this.analysingService = analysingService;
        this.analyseDataRepo = analyseDataRepo;
        this.socialMediaPlatformRepo = socialMediaPlatformRepo;
    }

    @NotNull
    public List<Comparison> getComparisons(User user, Model model) {
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
        return comparisons;
    }
}
