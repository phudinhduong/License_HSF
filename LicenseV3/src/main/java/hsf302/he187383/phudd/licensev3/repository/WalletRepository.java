package hsf302.he187383.phudd.licensev3.repository;

import hsf302.he187383.phudd.licensev3.enums.WalletStatus;
import hsf302.he187383.phudd.licensev3.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {
    Optional<Wallet> findByUserId(UUID userId);

    @Query("""
            SELECT w FROM Wallet w
            JOIN FETCH w.user u
            WHERE (:userId IS NULL OR u.id = :userId)
            AND (:status IS NULL OR w.status = :status)
            """)
    Page<Wallet> searchWallets(@Param("userId") UUID userId,
                               @Param("status") WalletStatus status,
                               Pageable pageable);

}
