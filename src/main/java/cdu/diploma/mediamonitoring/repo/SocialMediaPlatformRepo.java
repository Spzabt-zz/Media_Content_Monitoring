package cdu.diploma.mediamonitoring.repo;

import cdu.diploma.mediamonitoring.model.SocialMediaPlatform;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialMediaPlatformRepo extends JpaRepository<SocialMediaPlatform, Long> {
    SocialMediaPlatform findByProjectId(Long project_id);
}
