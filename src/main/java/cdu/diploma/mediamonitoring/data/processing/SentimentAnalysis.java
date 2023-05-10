package cdu.diploma.mediamonitoring.data.processing;

import cdu.diploma.mediamonitoring.external.api.Pipeline;
import cdu.diploma.mediamonitoring.model.RedditData;
import cdu.diploma.mediamonitoring.model.SocialMediaPlatform;
import cdu.diploma.mediamonitoring.model.TwitterData;
import cdu.diploma.mediamonitoring.model.YTData;
import cdu.diploma.mediamonitoring.repo.RedditDataRepo;
import cdu.diploma.mediamonitoring.repo.TwitterDataRepo;
import cdu.diploma.mediamonitoring.repo.YTDataRepo;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

public class SentimentAnalysis {
    private final RedditDataRepo redditDataRepo;
    private final TwitterDataRepo twitterDataRepo;
    private final YTDataRepo ytDataRepo;

    public SentimentAnalysis(RedditDataRepo redditDataRepo, TwitterDataRepo twitterDataRepo, YTDataRepo ytDataRepo) {
        this.redditDataRepo = redditDataRepo;
        this.twitterDataRepo = twitterDataRepo;
        this.ytDataRepo = ytDataRepo;
    }

    private String doSentimentAnalysis(String message) {
        StanfordCoreNLP stanfordCoreNLP = Pipeline.getPipeline();
        CoreDocument coreDocument = new CoreDocument(message);
        stanfordCoreNLP.annotate(coreDocument);
        List<CoreSentence> sentences = coreDocument.sentences();
        String sentiment = null;
        for (CoreSentence sentence : sentences) {
            sentiment = sentence.sentiment();
        }
        return sentiment;
    }

    public void analyseSentiment(Model model, SocialMediaPlatform socialMediaPlatform) {
        AtomicBoolean isAnalysedTwitter = new AtomicBoolean(false);
        AtomicBoolean isAnalysedReddit = new AtomicBoolean(false);
        AtomicBoolean isAnalysedYouTube = new AtomicBoolean(false);

        List<TwitterData> updatedTwitterData = new ArrayList<>();
        List<RedditData> updatedRedditData = new ArrayList<>();
        List<YTData> updatedYTData = new ArrayList<>();

        List<Future<?>> futures = new ArrayList<>();

        ExecutorService twitterExecutor = Executors.newFixedThreadPool(5);
        ExecutorService redditExecutor = Executors.newFixedThreadPool(5);
        ExecutorService youtubeExecutor = Executors.newFixedThreadPool(5);

        futures.add(twitterExecutor.submit(() -> {
            List<TwitterData> allTwitterData = twitterDataRepo.findAllBySocialMediaPlatformOrderByTweetedAtDesc(socialMediaPlatform);
            for (TwitterData twitterData : allTwitterData) {
                if (twitterData.getSentiment() != null) {
                    isAnalysedTwitter.set(true);
                    continue;
                }
                String sentiment = doSentimentAnalysis(twitterData.getTweet());
                System.out.println("twitter: " + sentiment);
                twitterData.setSentiment(sentiment);
                updatedTwitterData.add(twitterData);
            }
            if (isAnalysedTwitter.get())
                model.addAttribute("allTwitterData", allTwitterData);
            else
                model.addAttribute("allTwitterData", updatedTwitterData);
        }));

        futures.add(redditExecutor.submit(() -> {
            List<RedditData> allRedditData = redditDataRepo.findAllBySocialMediaPlatformOrderBySubDateDesc(socialMediaPlatform);
            for (RedditData redditData : allRedditData) {
                if (redditData.getSentiment() != null) {
                    isAnalysedReddit.set(true);
                    continue;
                }
                if (!Objects.equals(redditData.getSubBody(), "")) {
                    String sentiment = doSentimentAnalysis(redditData.getSubBody());
                    System.out.println("reddit: " + sentiment);
                    redditData.setSentiment(sentiment);
                } else {
                    String sentiment = doSentimentAnalysis(redditData.getSubTitle());
                    System.out.println("reddit: " + sentiment);
                    redditData.setSentiment(sentiment);
                }
                updatedRedditData.add(redditData);
            }
            if (isAnalysedReddit.get())
                model.addAttribute("allRedditData", allRedditData);
            else
                model.addAttribute("allRedditData", updatedRedditData);
        }));

        futures.add(youtubeExecutor.submit(() -> {
            List<YTData> allYTData = ytDataRepo.findAllBySocialMediaPlatformOrderByPublicationTimeDesc(socialMediaPlatform);
            for (YTData ytData : allYTData) {
                if (ytData.getSentiment() != null) {
                    isAnalysedYouTube.set(true);
                    continue;
                }
                String sentiment = doSentimentAnalysis(ytData.getComment());
                System.out.println("youtube: " + sentiment);
                ytData.setSentiment(sentiment);
                updatedYTData.add(ytData);
            }
            if (isAnalysedYouTube.get())
                model.addAttribute("allYTData", allYTData);
            else
                model.addAttribute("allYTData", updatedYTData);
        }));

        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        twitterExecutor.shutdown();
        redditExecutor.shutdown();
        youtubeExecutor.shutdown();

        twitterDataRepo.saveAll(updatedTwitterData);
        redditDataRepo.saveAll(updatedRedditData);
        ytDataRepo.saveAll(updatedYTData);
    }
}
