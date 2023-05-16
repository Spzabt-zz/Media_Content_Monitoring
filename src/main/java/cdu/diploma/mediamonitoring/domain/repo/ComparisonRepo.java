package cdu.diploma.mediamonitoring.domain.repo;

import cdu.diploma.mediamonitoring.domain.model.Comparison;
import cdu.diploma.mediamonitoring.domain.model.Project;
import cdu.diploma.mediamonitoring.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComparisonRepo extends JpaRepository<Comparison, Long> {
    List<Comparison> findAllByUser(User user);
    List<Comparison> findAllByProject(Project project);
    Comparison findTop1ComparisonByProjectAndUser(Project product, User user);
}
