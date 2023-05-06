package cdu.diploma.mediamonitoring.repo;

import cdu.diploma.mediamonitoring.model.SocialMediaPlatform;
import cdu.diploma.mediamonitoring.model.TwitterData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface TwitterDataRepo extends JpaRepository<TwitterData, Long> {
    ArrayList<TwitterData> findAllBySocialMediaPlatform(SocialMediaPlatform socialMediaPlatform);
}
