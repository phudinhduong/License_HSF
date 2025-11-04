package hsf302.he187383.phudd.license.repository;

import hsf302.he187383.phudd.license.model.WalletTxn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WalletTxnRepository extends JpaRepository<WalletTxn, UUID> {
    Optional<WalletTxn> findByIdempotencyKey(String idempotencyKey);
}