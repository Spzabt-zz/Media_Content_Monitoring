package cdu.diploma.mediamonitoring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Table(name = "yt_data")
@Data
@EqualsAndHashCode(of = {"id"})
public class YTData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String comId;
    private String videoId;
    private String comment;
    private Long likes;
    private String vidTitle;
    private String publicationTime;
    private Integer categoryId;
    private BigInteger viewCount;
    private BigInteger subCount;
    private Integer hours;
    private Integer minutes;
    private Integer seconds;
    private String sentiment;
    private BigInteger viewCountOfVideo;

    @ManyToOne
    @JoinColumn(name = "social_media_platform_id")
    private SocialMediaPlatform socialMediaPlatform;

    public YTData(String comId, String videoId, String comment, Long likes, String vidTitle, String publicationTime, Integer categoryId, BigInteger viewCount, BigInteger subCount, Integer hours, Integer minutes, Integer seconds, SocialMediaPlatform socialMediaPlatform) {
        this.comId = comId;
        this.videoId = videoId;
        this.comment = comment;
        this.likes = likes;
        this.vidTitle = vidTitle;
        this.publicationTime = publicationTime;
        this.categoryId = categoryId;
        this.viewCount = viewCount;
        this.subCount = subCount;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.socialMediaPlatform = socialMediaPlatform;
    }

    public YTData() {

    }
}
