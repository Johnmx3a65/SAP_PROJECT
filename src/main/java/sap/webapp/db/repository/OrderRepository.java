package sap.webapp.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sap.webapp.db.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
