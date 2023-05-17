package cdu.diploma.mediamonitoring.data.processing.mention_analysis;

import cdu.diploma.mediamonitoring.domain.dto.AllDataDto;
import cdu.diploma.mediamonitoring.domain.dto.MentionsDto;
import cdu.diploma.mediamonitoring.domain.model.AnalyseData;
import cdu.diploma.mediamonitoring.domain.model.PlatformName;
import cdu.diploma.mediamonitoring.domain.repo.AnalyseDataRepo;
import cdu.diploma.mediamonitoring.domain.repo.RedditDataRepo;
import cdu.diploma.mediamonitoring.domain.repo.TwitterDataRepo;
import cdu.diploma.mediamonitoring.domain.repo.YTDataRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ui.Model;

import java.util.*;

public class MentionsAnalysis {

    private final RedditDataRepo redditDataRepo;
    private final TwitterDataRepo twitterDataRepo;
    private final YTDataRepo ytDataRepo;
    private final AnalyseDataRepo analyseDataRepo;

    public MentionsAnalysis(RedditDataRepo redditDataRepo, TwitterDataRepo twitterDataRepo, YTDataRepo ytDataRepo, AnalyseDataRepo analyseDataRepo) {
        this.redditDataRepo = redditDataRepo;
        this.twitterDataRepo = twitterDataRepo;
        this.ytDataRepo = ytDataRepo;
        this.analyseDataRepo = analyseDataRepo;
    }

    public String totalMentionsAnalysis(Model model, HashSet<String> dates, ArrayList<AllDataDto> allData, AnalyseData analyseData) {
        ObjectMapper mapper;
        ArrayList<MentionsDto> mentions = new ArrayList<>();

        int mentionsCount = 0;
        for (String date : dates) {
            for (AllDataDto allDatum : allData) {
                if (date.equals(allDatum.getDate())) {
                    mentionsCount++;
                }
            }
            mentions.add(new MentionsDto(date, mentionsCount));
            mentionsCount = 0;
        }

        mentions.sort(new Comparator<MentionsDto>() {
            public int compare(MentionsDto s1, MentionsDto s2) {
                return s1.getDate().compareTo(s2.getDate());
            }
        });

        mapper = new ObjectMapper();

        try {
            String json = mapper.writeValueAsString(mentions);
            model.addAttribute("mentionChartData", json);
            analyseData.setTotalMentionsCountChart(json);

            return json;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return "no data";
    }

    public void mentionsBySourcesAnalysis(ArrayList<AllDataDto> allData, Model model) {
        ObjectMapper mapper;

        int redCount = 0;
        int twCount = 0;
        int ytCount = 0;

        List<Map<String, Object>> mentionsByCount = new ArrayList<>();

        for (AllDataDto allDatum : allData) {
            if (allDatum.getPlatformName().name().equals(PlatformName.TWITTER.name())) {
                twCount++;
            }
            if (allDatum.getPlatformName().name().equals(PlatformName.REDDIT.name())) {
                redCount++;
            }
            if (allDatum.getPlatformName().name().equals(PlatformName.YOU_TUBE.name())) {
                ytCount++;
            }
        }

        int totalSentimentPieces = allData.size();

        double percentageRedCount = (double) redCount / totalSentimentPieces * 100.0;
        double percentageTwCount = (double) twCount / totalSentimentPieces * 100.0;
        double percentageYtCount = (double) ytCount / totalSentimentPieces * 100.0;

        mentionsByCount.add(Map.of("source", "Reddit", "countOfMentions", Math.floor(percentageRedCount)));
        mentionsByCount.add(Map.of("source", "Twitter", "countOfMentions", Math.floor(percentageTwCount)));
        mentionsByCount.add(Map.of("source", "YouTube", "countOfMentions", Math.floor(percentageYtCount)));

        mapper = new ObjectMapper();

        try {
            String json = mapper.writeValueAsString(mentionsByCount);
            model.addAttribute("mentionsBySourcePieData", json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
