package cdu.diploma.mediamonitoring.domain.repo;

import cdu.diploma.mediamonitoring.domain.model.SocialMediaPlatform;
import cdu.diploma.mediamonitoring.domain.model.TwitterData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface TwitterDataRepo extends JpaRepository<TwitterData, Long> {
    ArrayList<TwitterData> findAllBySocialMediaPlatform(SocialMediaPlatform socialMediaPlatform);
    ArrayList<TwitterData> findAllBySocialMediaPlatformOrderByTweetedAt(SocialMediaPlatform socialMediaPlatform);
    ArrayList<TwitterData> findAllBySocialMediaPlatformOrderByTweetedAtDesc(SocialMediaPlatform socialMediaPlatform);
    TwitterData findTwitterDataById(Long id);
}
