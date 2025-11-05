package hsf302.he187383.phudd.licensev3.repository;

import hsf302.he187383.phudd.licensev3.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TopupRepository extends JpaRepository<Topup, UUID> {
    List<Topup> findByUserIdOrderByCreatedAtDesc(UUID userId);
}
