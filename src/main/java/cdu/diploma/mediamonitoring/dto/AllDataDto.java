package cdu.diploma.mediamonitoring.dto;

public class AllDataDto {
    private String date;
    private String sentiment;
    private String text;

    public AllDataDto(String date, String sentiment, String text) {
        this.date = date;
        this.sentiment = sentiment;
        this.text = text;
    }

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
