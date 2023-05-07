package cdu.diploma.mediamonitoring.repo;

import cdu.diploma.mediamonitoring.model.RedditData;
import cdu.diploma.mediamonitoring.model.SocialMediaPlatform;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface RedditDataRepo extends JpaRepository<RedditData, Long> {
    ArrayList<RedditData> findAllBySocialMediaPlatform(SocialMediaPlatform socialMediaPlatform);
    ArrayList<RedditData> findAllBySocialMediaPlatformOrderBySubDate(SocialMediaPlatform socialMediaPlatform);
}
