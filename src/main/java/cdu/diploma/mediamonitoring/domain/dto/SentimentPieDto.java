package cdu.diploma.mediamonitoring.domain.dto;

public class SentimentPieDto {
    private String sentiment;
    private int count;
    private double percentage;

    public SentimentPieDto(String sentiment, int count, double percentage) {
        this.sentiment = sentiment;
        this.count = count;
        this.percentage = percentage;
    }

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}
