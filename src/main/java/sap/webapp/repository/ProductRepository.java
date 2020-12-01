package sap.webapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sap.webapp.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

}
