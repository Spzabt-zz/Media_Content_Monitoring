package cdu.diploma.mediamonitoring.repo;

import cdu.diploma.mediamonitoring.model.AnalyseData;
import cdu.diploma.mediamonitoring.model.ApiCredentials;
import cdu.diploma.mediamonitoring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiCredentialsRepo  extends JpaRepository<ApiCredentials, Long> {
    ApiCredentials findApiCredentialsByUser(User user);
}
