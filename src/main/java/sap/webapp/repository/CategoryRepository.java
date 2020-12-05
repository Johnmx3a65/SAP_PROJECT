package sap.webapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sap.webapp.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
