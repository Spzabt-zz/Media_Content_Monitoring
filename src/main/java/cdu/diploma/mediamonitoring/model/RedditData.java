package cdu.diploma.mediamonitoring.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "reddit_data")
@Data
@EqualsAndHashCode(of = {"id"})
public class RedditData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String subId;
    private String subTitle;
    private String subBody;
    @Temporal(TemporalType.TIMESTAMP)
    private Date subDate;
    private String subUrl;
    //private String userPostId;
    private String site;
    private String sentiment;


    @ManyToOne
    @JoinColumn(name = "social_media_platform_id")
    private SocialMediaPlatform socialMediaPlatform;

    public RedditData(String subId, String subTitle, String subBody, Date subDate, String subUrl, String site, SocialMediaPlatform socialMediaPlatform) {
        this.subId = subId;
        this.subTitle = subTitle;
        this.subBody = subBody;
        this.subDate = subDate;
        this.subUrl = subUrl;
        this.site = site;
        this.socialMediaPlatform = socialMediaPlatform;
    }

    public RedditData() {

    }
}
