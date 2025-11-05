package hsf302.he187383.phudd.licensev3.repository;

import hsf302.he187383.phudd.licensev3.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlanRepository extends JpaRepository<Plan, UUID> {
    List<Plan> findByProductIdOrderByCreatedAtDesc(UUID productId);
    Optional<Plan> findByProductIdAndCode(UUID productId, String code);
    void deletePlanByProduct_Id(UUID productId);
}
