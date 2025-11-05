package hsf302.he187383.phudd.licensev3.repository;

import hsf302.he187383.phudd.licensev3.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WalletTxnRepository extends JpaRepository<WalletTxn, UUID> {
    Optional<WalletTxn> findByIdempotencyKey(String idempotencyKey);
    List<WalletTxn> findByWalletIdOrderByCreatedAtDesc(UUID walletId);
}
