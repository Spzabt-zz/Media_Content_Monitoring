package cdu.diploma.mediamonitoring.domain.dto;

public class MentionsDto {
    private String date;
    private int mentionsCount;

    public MentionsDto(String date, int mentionsCount) {
        this.date = date;
        this.mentionsCount = mentionsCount;
    }

    public int getMentionsCount() {
        return mentionsCount;
    }

    public void setMentionsCount(int mentionsCount) {
        this.mentionsCount = mentionsCount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
