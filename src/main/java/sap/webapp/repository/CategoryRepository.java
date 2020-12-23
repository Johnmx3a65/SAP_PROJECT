package sap.webapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sap.webapp.entity.Category;
import sap.webapp.entity.Product;

import java.util.Set;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
