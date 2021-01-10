package sap.webapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sap.webapp.entity.Role;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}
