package cdu.diploma.mediamonitoring.repo;

import cdu.diploma.mediamonitoring.model.Project;
import cdu.diploma.mediamonitoring.model.SocialMediaPlatform;
import cdu.diploma.mediamonitoring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepo extends JpaRepository<Project, Long> {
    List<Project> findAllByUser(User user);
    Project findProjectById(Long id);
    Project findByUser(User user);
    Project findByName(String name);
}
