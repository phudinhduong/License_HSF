package hsf302.he187383.phudd.license.repository;

import hsf302.he187383.phudd.license.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface WalletTxnRepository extends JpaRepository<WalletTxn, UUID> {
    Optional<WalletTxn> findByIdempotencyKey(String idempotencyKey);
    List<WalletTxn> findByWalletIdOrderByCreatedAtDesc(UUID walletId);
}
