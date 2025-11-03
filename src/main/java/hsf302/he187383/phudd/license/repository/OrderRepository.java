package hsf302.he187383.phudd.license.repository;

import hsf302.he187383.phudd.license.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {}
