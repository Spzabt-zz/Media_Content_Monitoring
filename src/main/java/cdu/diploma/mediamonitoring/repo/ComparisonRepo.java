package cdu.diploma.mediamonitoring.repo;

import cdu.diploma.mediamonitoring.model.Comparison;
import cdu.diploma.mediamonitoring.model.Project;
import cdu.diploma.mediamonitoring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComparisonRepo extends JpaRepository<Comparison, Long> {
    List<Comparison> findAllByUser(User user);
    List<Comparison> findAllByProject(Project project);
    Comparison findTop1ComparisonByProjectAndUser(Project product, User user);
}
