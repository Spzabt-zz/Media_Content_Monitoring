package cdu.diploma.mediamonitoring.dto;

import java.util.HashMap;
import java.util.Map;

public class SentimentDataDto {
    private String date;
    private Map<String, Integer> sentiment;

    public SentimentDataDto(String date, int positiveCount, int negativeCount) {
        this.date = date;
        this.sentiment = new HashMap<>();
        this.sentiment.put("positive", positiveCount);
        this.sentiment.put("negative", negativeCount);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Map<String, Integer> getSentiment() {
        return sentiment;
    }

    public void setSentiment(Map<String, Integer> sentiment) {
        this.sentiment = sentiment;
    }

    @Override
    public String toString() {
        return "SentimentData{" +
                "date='" + date + '\'' +
                ", sentiment=" + sentiment +
                '}';
    }
}