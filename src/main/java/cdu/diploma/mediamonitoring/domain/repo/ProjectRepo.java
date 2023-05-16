package cdu.diploma.mediamonitoring.domain.repo;

import cdu.diploma.mediamonitoring.domain.model.Project;
import cdu.diploma.mediamonitoring.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepo extends JpaRepository<Project, Long> {
    List<Project> findAllByUser(User user);
    Project findProjectById(Long id);
    Project findByUser(User user);
    Project findByName(String name);
}
