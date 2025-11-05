package hsf302.he187383.phudd.license.repository;

import hsf302.he187383.phudd.license.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface PlanRepository extends JpaRepository<Plan, UUID> {
    List<Plan> findByProductIdOrderByCreatedAtDesc(UUID productId);
    Optional<Plan> findByProductIdAndCode(UUID productId, String code);
}
