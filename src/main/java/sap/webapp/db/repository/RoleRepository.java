package sap.webapp.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sap.webapp.db.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}
