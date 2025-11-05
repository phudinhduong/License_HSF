package hsf302.he187383.phudd.license.repository;

import hsf302.he187383.phudd.license.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface TopupRepository extends JpaRepository<Topup, UUID> {
    List<Topup> findByUserIdOrderByCreatedAtDesc(UUID userId);
}
