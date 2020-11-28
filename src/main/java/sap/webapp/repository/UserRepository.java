package sap.webapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sap.webapp.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
}
