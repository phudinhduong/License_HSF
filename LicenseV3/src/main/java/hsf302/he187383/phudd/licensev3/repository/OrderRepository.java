package hsf302.he187383.phudd.licensev3.repository;

import hsf302.he187383.phudd.licensev3.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByUserIdOrderByCreatedAtDesc(UUID userId);
    Optional<Order> findByUserIdAndPlanId(UUID userId, UUID planId);
}