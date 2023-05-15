package cdu.diploma.mediamonitoring.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "sm_platform")
@Data
@EqualsAndHashCode(of = {"id"})
public class SocialMediaPlatform {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String platformName;

    @OneToOne(mappedBy = "socialMediaPlatform", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Project project;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "analyse_data_id")
    private AnalyseData analyseData;

    @OneToMany(mappedBy = "socialMediaPlatform", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TwitterData> twitterData;

    @OneToMany(mappedBy = "socialMediaPlatform", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<YTData> ytData;

    @OneToMany(mappedBy = "socialMediaPlatform", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RedditData> redditData;

    public SocialMediaPlatform(String platformName) {
        this.platformName = platformName;
    }

    public SocialMediaPlatform(Long id) {
        this.id = id;
    }

    public SocialMediaPlatform() {

    }
}
