package cdu.diploma.mediamonitoring.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "project")
@Data
@EqualsAndHashCode(of = {"id"})
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String keywords;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "social_media_platform_id")
    private SocialMediaPlatform socialMediaPlatform;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Project(String name, String keywords, SocialMediaPlatform socialMediaPlatform, User user) {
        this.name = name;
        this.keywords = keywords;
        this.socialMediaPlatform = socialMediaPlatform;
        this.user = user;
    }

    public Project() {

    }

    /*@OneToMany(mappedBy = "project", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Keyword> keywords;*/
}
