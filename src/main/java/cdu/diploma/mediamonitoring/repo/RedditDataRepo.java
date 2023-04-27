package cdu.diploma.mediamonitoring.repo;

import cdu.diploma.mediamonitoring.model.RedditData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RedditDataRepo extends JpaRepository<RedditData, Long> {
}
