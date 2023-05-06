package cdu.diploma.mediamonitoring.repo;

import cdu.diploma.mediamonitoring.model.SocialMediaPlatform;
import cdu.diploma.mediamonitoring.model.YTData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface YTDataRepo extends JpaRepository<YTData, Long> {
    ArrayList<YTData> findAllBySocialMediaPlatform(SocialMediaPlatform socialMediaPlatform);
}
