package cdu.diploma.mediamonitoring.repo;

import cdu.diploma.mediamonitoring.model.TwitterData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TwitterDataRepo extends JpaRepository<TwitterData, Long> {
}
