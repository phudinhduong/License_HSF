package hsf302.he187383.phudd.licensev3.repository;

import hsf302.he187383.phudd.licensev3.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByUserIdOrderByCreatedAtDesc(UUID userId);
    Optional<Order> findByUserIdAndPlanId(UUID userId, UUID planId);

    @EntityGraph(attributePaths = {"plan", "plan.product", "walletTxn"})
    Page<Order> findByUserId(UUID userId, Pageable pageable);
}