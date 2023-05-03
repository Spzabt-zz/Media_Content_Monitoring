package cdu.diploma.mediamonitoring.repo;

import cdu.diploma.mediamonitoring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername (String username);
}
