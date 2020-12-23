package sap.webapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sap.webapp.entity.Product;
import sap.webapp.entity.User;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findAllByAuthor(User user);
}
