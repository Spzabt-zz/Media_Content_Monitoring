package cdu.diploma.mediamonitoring.data.processing.sentiment_analysis;

import cdu.diploma.mediamonitoring.domain.model.*;
import cdu.diploma.mediamonitoring.domain.repo.AnalyseDataRepo;
import cdu.diploma.mediamonitoring.domain.repo.RedditDataRepo;
import cdu.diploma.mediamonitoring.domain.repo.TwitterDataRepo;
import cdu.diploma.mediamonitoring.domain.repo.YTDataRepo;
import cdu.diploma.mediamonitoring.domain.dto.AllDataDto;
import cdu.diploma.mediamonitoring.domain.dto.SentimentDataDto;
import cdu.diploma.mediamonitoring.domain.dto.SentimentPieDto;
import cdu.diploma.mediamonitoring.nlp.pipeline.Pipeline;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.springframework.ui.Model;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

public class SentimentAnalysis {
    private final RedditDataRepo redditDataRepo;
    private final TwitterDataRepo twitterDataRepo;
    private final YTDataRepo ytDataRepo;
    private final AnalyseDataRepo analyseDataRepo;

    public SentimentAnalysis(RedditDataRepo redditDataRepo, TwitterDataRepo twitterDataRepo, YTDataRepo ytDataRepo, AnalyseDataRepo analyseDataRepo) {
        this.redditDataRepo = redditDataRepo;
        this.twitterDataRepo = twitterDataRepo;
        this.ytDataRepo = ytDataRepo;
        this.analyseDataRepo = analyseDataRepo;
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

    public void analyseSentiment(Model model, SocialMediaPlatform socialMediaPlatform, String source) {
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

        if (Objects.equals(source, PlatformName.TWITTER.name())) {
            twitterSentimentAnalyser(model, socialMediaPlatform, isAnalysedTwitter, updatedTwitterData, futures, twitterExecutor);
        } else if (Objects.equals(source, PlatformName.REDDIT.name())) {
            redditSentimentAnalyser(model, socialMediaPlatform, isAnalysedReddit, updatedRedditData, futures, redditExecutor);
        } else if (Objects.equals(source, PlatformName.YOU_TUBE.name())) {
            youTubeSentimentAnalyser(model, socialMediaPlatform, isAnalysedYouTube, updatedYTData, futures, youtubeExecutor);
        } else {
            twitterSentimentAnalyser(model, socialMediaPlatform, isAnalysedTwitter, updatedTwitterData, futures, twitterExecutor);

            redditSentimentAnalyser(model, socialMediaPlatform, isAnalysedReddit, updatedRedditData, futures, redditExecutor);

            youTubeSentimentAnalyser(model, socialMediaPlatform, isAnalysedYouTube, updatedYTData, futures, youtubeExecutor);
        }

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

    private void youTubeSentimentAnalyser(Model model, SocialMediaPlatform socialMediaPlatform, AtomicBoolean isAnalysedYouTube, List<YTData> updatedYTData, List<Future<?>> futures, ExecutorService youtubeExecutor) {
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
    }

    private void redditSentimentAnalyser(Model model, SocialMediaPlatform socialMediaPlatform, AtomicBoolean isAnalysedReddit, List<RedditData> updatedRedditData, List<Future<?>> futures, ExecutorService redditExecutor) {
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
    }

    private void twitterSentimentAnalyser(Model model, SocialMediaPlatform socialMediaPlatform, AtomicBoolean isAnalysedTwitter, List<TwitterData> updatedTwitterData, List<Future<?>> futures, ExecutorService twitterExecutor) {
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
    }

    public void sentimentDataChart(Model model, ArrayList<SentimentDataDto> sentimentData, HashSet<String> dates, ArrayList<AllDataDto> allData, AnalyseData analyseData) {
        int posCount = 0;
        int negCount = 0;
        for (String date : dates) {
            for (AllDataDto allDatum : allData) {
                if (date.equals(allDatum.getDate())) {
                    if (Objects.equals(allDatum.getSentiment(), "Positive") || Objects.equals(allDatum.getSentiment(), "Very positive")) {
                        posCount++;
                    } else if (Objects.equals(allDatum.getSentiment(), "Negative") || Objects.equals(allDatum.getSentiment(), "Very negative")) {
                        negCount++;
                    }
                }
            }
            sentimentData.add(new SentimentDataDto(date, posCount, negCount));
            posCount = 0;
            negCount = 0;
        }

        sentimentData.sort(new Comparator<SentimentDataDto>() {
            public int compare(SentimentDataDto s1, SentimentDataDto s2) {
                return s1.getDate().compareTo(s2.getDate());
            }
        });

        ObjectMapper mapper = new ObjectMapper();

        try {
            String json = mapper.writeValueAsString(sentimentData);
            model.addAttribute("sentimentChartData", json);

            analyseData.setSentimentDataChart(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void sentimentPieGraph(Model model, ArrayList<AllDataDto> allData, AnalyseData analyseData) {
        ObjectMapper mapper;
        HashSet<String> sentimentPieces = new HashSet<>();
        ArrayList<SentimentPieDto> sentimentPieDtos = new ArrayList<>();

        for (AllDataDto allDatum : allData) {
            sentimentPieces.add(allDatum.getSentiment());
        }

        int count = 0;
        int totalSentimentPieces = allData.size();
        for (String sentiment : sentimentPieces) {
            for (AllDataDto allDatum : allData) {
                if (sentiment.equals(allDatum.getSentiment())) {
                    count++;
                }
            }
            double percentage = (double) count / totalSentimentPieces * 100.0;
            sentimentPieDtos.add(new SentimentPieDto(sentiment, count, Math.round(percentage)));
            count = 0;
        }

        sentimentPieDtos.sort(new Comparator<SentimentPieDto>() {
            public int compare(SentimentPieDto s1, SentimentPieDto s2) {
                return s1.getSentiment().compareTo(s2.getSentiment());
            }
        });

        mapper = new ObjectMapper();

        try {
            String json = mapper.writeValueAsString(sentimentPieDtos);
            model.addAttribute("sentimentPieData", json);
            analyseData.setSentimentPieGraph(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
