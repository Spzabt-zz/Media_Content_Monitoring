package cdu.diploma.mediamonitoring.repo;

import cdu.diploma.mediamonitoring.model.AnalyseData;
import cdu.diploma.mediamonitoring.model.Comparison;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnalyseDataRepo extends JpaRepository<AnalyseData, Long> {

}
