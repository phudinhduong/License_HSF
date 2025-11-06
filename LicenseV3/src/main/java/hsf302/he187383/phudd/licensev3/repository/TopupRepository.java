package hsf302.he187383.phudd.licensev3.repository;

import hsf302.he187383.phudd.licensev3.enums.TopupStatus;
import hsf302.he187383.phudd.licensev3.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TopupRepository extends JpaRepository<Topup, UUID> {
    List<Topup> findByUserIdOrderByCreatedAtDesc(UUID userId);

    Optional<Topup> findByPaymentRef(String paymentRef);

    @Query("""
    SELECT t FROM Topup t
    WHERE t.user = :user
      AND (:status IS NULL OR t.status = :status)
      AND (:from IS NULL OR t.createdAt >= :from)
      AND (:to IS NULL OR t.createdAt <= :to)
    ORDER BY t.createdAt DESC
""")
    Page<Topup> findByUserAndFilter(
            @Param("user") User user,
            @Param("status") TopupStatus status,
            @Param("from") Instant from,
            @Param("to") Instant to,
            Pageable pageable);

    @Query("""
    SELECT t FROM Topup t
    JOIN FETCH t.user
    WHERE (:userId IS NULL OR t.user.id = :userId)
      AND (:status IS NULL OR t.status = :status)
      AND (:from IS NULL OR t.createdAt >= :from)
      AND (:to IS NULL OR t.createdAt <= :to)
    ORDER BY t.createdAt DESC
""")
    Page<Topup> findAllWithFilters(
            @Param("userId") UUID userId,
            @Param("status") TopupStatus status,
            @Param("from") Instant from,
            @Param("to") Instant to,
            Pageable pageable);

    @Query("SELECT t FROM Topup t WHERE t.status = 'PENDING' AND t.createdAt < :deadline")
    List<Topup> findPendingTopupsBefore(@Param("deadline") Instant deadline);

}
