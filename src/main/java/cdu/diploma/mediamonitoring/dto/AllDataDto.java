package cdu.diploma.mediamonitoring.dto;

public class AllDataDto {
    private String date;
    private String sentiment;

    public AllDataDto(String date, String sentiment) {
        this.date = date;
        this.sentiment = sentiment;
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
}
