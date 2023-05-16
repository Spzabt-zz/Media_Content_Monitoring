package cdu.diploma.mediamonitoring.domain.repo;

import cdu.diploma.mediamonitoring.domain.model.RedditData;
import cdu.diploma.mediamonitoring.domain.model.SocialMediaPlatform;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface RedditDataRepo extends JpaRepository<RedditData, Long> {
    ArrayList<RedditData> findAllBySocialMediaPlatform(SocialMediaPlatform socialMediaPlatform);
    ArrayList<RedditData> findAllBySocialMediaPlatformOrderBySubDate(SocialMediaPlatform socialMediaPlatform);
    ArrayList<RedditData> findAllBySocialMediaPlatformOrderBySubDateDesc(SocialMediaPlatform socialMediaPlatform);
    RedditData findRedditDataById(Long id);
}
