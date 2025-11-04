package hsf302.he187383.phudd.license.repository;

import hsf302.he187383.phudd.license.model.Plan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface PlanRepository extends JpaRepository<Plan, UUID> {

    boolean existsByProductIdAndCodeIgnoreCase(UUID productId, String code);

    boolean existsByProductIdAndCodeIgnoreCaseAndIdNot(UUID productId, String code, UUID id);

    @Query("""
           select p from Plan p
           where (:productId is null or p.product.id = :productId)
             and (:q is null or lower(p.code) like lower(concat('%', :q, '%'))
                           or lower(p.name) like lower(concat('%', :q, '%')))
           """)
    Page<Plan> search(@Param("productId") UUID productId,
                      @Param("q") String q,
                      Pageable pageable);


}
