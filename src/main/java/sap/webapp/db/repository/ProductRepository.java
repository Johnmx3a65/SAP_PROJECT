package sap.webapp.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sap.webapp.db.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

}
