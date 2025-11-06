package hsf302.he187383.phudd.licensev3.repository;

import hsf302.he187383.phudd.licensev3.enums.Ref_Type;
import hsf302.he187383.phudd.licensev3.enums.WalletTxnDirection;
import hsf302.he187383.phudd.licensev3.enums.WalletTxnStatus;
import hsf302.he187383.phudd.licensev3.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WalletTxnRepository extends JpaRepository<WalletTxn, UUID> {
    Optional<WalletTxn> findByIdempotencyKey(String idempotencyKey);

    List<WalletTxn> findByWalletIdOrderByCreatedAtDesc(UUID walletId);

    boolean existsByIdempotencyKey(String idempotencyKey);

    @Query("""
    SELECT t FROM WalletTxn t
    WHERE (:walletId IS NULL OR t.wallet.id = :walletId)
      AND (:direction IS NULL OR t.direction = :direction)
      AND (:refType IS NULL OR t.refType = :refType)
      AND (:from IS NULL OR t.createdAt >= :from)
      AND (:to IS NULL OR t.createdAt <= :to)
    ORDER BY t.createdAt DESC
""")
    Page<WalletTxn> searchTxns(@Param("walletId") UUID walletId,
                               @Param("direction") WalletTxnDirection direction,
                               @Param("refType") Ref_Type refType,
                               @Param("from") java.time.Instant from,
                               @Param("to") java.time.Instant to,
                               Pageable pageable);

}
