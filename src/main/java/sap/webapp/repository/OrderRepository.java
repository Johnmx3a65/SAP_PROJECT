package sap.webapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sap.webapp.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
