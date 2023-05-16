package cdu.diploma.mediamonitoring.domain.repo;

import cdu.diploma.mediamonitoring.domain.model.User;
import cdu.diploma.mediamonitoring.domain.model.ApiCredentials;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiCredentialsRepo  extends JpaRepository<ApiCredentials, Long> {
    ApiCredentials findApiCredentialsByUserId(Long user_id);
}
