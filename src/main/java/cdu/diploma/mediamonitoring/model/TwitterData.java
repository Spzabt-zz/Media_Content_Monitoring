package cdu.diploma.mediamonitoring.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "twitter_data")
@Data
@EqualsAndHashCode(of = {"id"})
public class TwitterData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String twId;
    private String link;
    private String tweet;
    private String username;
    private BigInteger followerCount;
    private Integer friendCount;
    private Integer listedCount;
    private Integer verificationStatus;
    //private String tweetForUser;
    @Temporal(TemporalType.TIMESTAMP)
    private Date tweetedAt;
    private String sentiment;

    @ManyToOne
    @JoinColumn(name = "social_media_platform_id")
    private SocialMediaPlatform socialMediaPlatform;

    public TwitterData(String twId, String link, String tweet, String username, BigInteger followerCount, Integer friendCount, Integer listedCount, Integer verificationStatus, Date tweetedAt, SocialMediaPlatform socialMediaPlatform) {
        this.twId = twId;
        this.link = link;
        this.tweet = tweet;
        this.username = username;
        this.followerCount = followerCount;
        this.friendCount = friendCount;
        this.listedCount = listedCount;
        this.verificationStatus = verificationStatus;
        this.tweetedAt = tweetedAt;
        this.socialMediaPlatform = socialMediaPlatform;
    }

    public TwitterData() {

    }
}
