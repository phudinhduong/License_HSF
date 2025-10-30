package hsf302.he187383.phudd.license.repository;

import hsf302.he187383.phudd.license.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlanRepository extends JpaRepository<Plan, UUID> {
    List<Plan> findByProductId(UUID productId);
    Optional<Plan> findByProductIdAndCode(UUID productId, String code);
}
