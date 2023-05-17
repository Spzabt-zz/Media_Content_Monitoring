package cdu.diploma.mediamonitoring.domain.service;

import cdu.diploma.mediamonitoring.domain.model.Project;
import cdu.diploma.mediamonitoring.domain.model.SocialMediaPlatform;
import cdu.diploma.mediamonitoring.domain.model.User;
import cdu.diploma.mediamonitoring.util.KeywordsUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SearchingService {
    private final RedditService redditService;
    private final TwitterService twitterService;
    private final YTService ytService;

    public SearchingService(RedditService redditService, TwitterService twitterService, YTService ytService) {
        this.redditService = redditService;
        this.twitterService = twitterService;
        this.ytService = ytService;
    }

    public void searchMentions(User user, StringBuilder keys, Project project) {
        Thread redditThread = new Thread(() -> {
            SocialMediaPlatform socialMediaPlatform = project.getSocialMediaPlatform();
            String[] brandKeywords = KeywordsUtil.separateKeywords(keys.toString());
            try {
                redditService.searchReddit(brandKeywords, socialMediaPlatform, user);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Thread twitterThread = new Thread(() -> {
            SocialMediaPlatform socialMediaPlatform = project.getSocialMediaPlatform();
            String[] brandKeywords = KeywordsUtil.separateKeywords(keys.toString());
            twitterService.collectDataForModel(brandKeywords, socialMediaPlatform, user);
        });

        Thread ytThread = new Thread(() -> {
            SocialMediaPlatform socialMediaPlatform = project.getSocialMediaPlatform();
            String[] brandKeywords = KeywordsUtil.separateKeywords(keys.toString());
            try {
                ytService.getVideoData(brandKeywords, socialMediaPlatform, user);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        redditThread.start();
        twitterThread.start();
        ytThread.start();

        try {
            redditThread.join();
            twitterThread.join();
            ytThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
