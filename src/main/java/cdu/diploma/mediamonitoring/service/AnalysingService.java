package cdu.diploma.mediamonitoring.service;

import cdu.diploma.mediamonitoring.data.processing.SentimentAnalysis;
import cdu.diploma.mediamonitoring.model.SocialMediaPlatform;
import cdu.diploma.mediamonitoring.repo.RedditDataRepo;
import cdu.diploma.mediamonitoring.repo.TwitterDataRepo;
import cdu.diploma.mediamonitoring.repo.YTDataRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class AnalysingService {
    private final SentimentAnalysis sentimentAnalysis;

    @Autowired
    public AnalysingService(RedditDataRepo redditDataRepo, TwitterDataRepo twitterDataRepo, YTDataRepo ytDataRepo) {
        sentimentAnalysis = new SentimentAnalysis(redditDataRepo, twitterDataRepo, ytDataRepo);
    }

    public void doParallelSentimentAnalysis(Model model, SocialMediaPlatform socialMediaPlatform) {
        sentimentAnalysis.analyseSentiment(model, socialMediaPlatform);
    }
}
