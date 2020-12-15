package sap.webapp.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sap.webapp.db.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
}
