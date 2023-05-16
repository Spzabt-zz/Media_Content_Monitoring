package cdu.diploma.mediamonitoring.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table(name = "api_credentials")
@Data
@EqualsAndHashCode(of = {"id"})
public class ApiCredentials {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String redditClientId;
    private String redditClientSecret;

    private String twitterConsumerKey;
    private String twitterConsumerSecret;
    private String twitterAccessToken;
    private String twitterAccessTokenSecret;

    private String ytApiKey;

    @OneToOne(mappedBy = "apiCredentials", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;
}
