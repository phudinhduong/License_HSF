package hsf302.he187383.phudd.licensev3.service;


import hsf302.he187383.phudd.licensev3.enums.*;
import hsf302.he187383.phudd.licensev3.model.*;
import hsf302.he187383.phudd.licensev3.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class WalletService {

    private final WalletRepository walletRepo;
    private final WalletTxnRepository txnRepo;
    private final UserRepository userRepo;

    public Wallet getOrCreateByUser(UUID userId) {
        return walletRepo.findByUserId(userId)
                .orElseGet(() -> {
                    var user = userRepo.findById(userId)
                            .orElseThrow(() -> new IllegalArgumentException("User not found"));
                    var w = Wallet.builder()
                            .user(user)
                            .balance(0L)
                            .status(WalletStatus.ACTIVE)
                            .build();
                    return walletRepo.save(w);
                });
    }

    public void changeBalance(UUID userId, long amount, String refType, UUID refId) {
        var wallet = getOrCreateByUser(userId);
        long newBalance = wallet.getBalance() + amount;
        if (newBalance < 0) {
            throw new IllegalStateException("Not enough credits");
        }
        wallet.setBalance(newBalance);
        walletRepo.save(wallet);

        var txn = WalletTxn.builder()
                .wallet(wallet)
                .type(amount >= 0 ? WalletTxnType.ADJUSTMENT : WalletTxnType.PURCHASE)
                .direction(amount >= 0 ? WalletTxnDirection.IN : WalletTxnDirection.OUT)
                .amount(Math.abs(amount))
                .balanceAfter(newBalance)
                .status("COMPLETED")
                .refType(refType)
                .refId(refId)
                .idempotencyKey(UUID.randomUUID().toString())
                .build();
        txnRepo.save(txn);
    }

    @Transactional(readOnly = true)
    public List<WalletTxn> listTxns(UUID userId) {
        var wallet = walletRepo.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found"));
        return txnRepo.findByWalletIdOrderByCreatedAtDesc(wallet.getId());
    }


    /** Mua hàng: tạo WalletTxn PURCHASE/OUT + trừ ví, idempotent theo idempotencyKey */
    public WalletTxn purchase(UUID userId, long amount, String idempotencyKey, String refType, UUID refId) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be > 0");

        // Idempotency: trả về txn cũ nếu đã tạo
        var existed = txnRepo.findByIdempotencyKey(idempotencyKey);
        if (existed.isPresent()) return existed.get();

        var wallet = walletRepo.findByUserId(userId)
                .orElseGet(() -> {
                    var user = userRepo.findById(userId)
                            .orElseThrow(() -> new IllegalArgumentException("User not found"));
                    return walletRepo.save(Wallet.builder().user(user).balance(0L).status(WalletStatus.ACTIVE).build());
                });

        if (wallet.getBalance() < amount) {
            throw new IllegalStateException("Not enough credits");
        }

        // 1) Ghi txn PENDING
        var txn = WalletTxn.builder()
                .wallet(wallet)
                .type(WalletTxnType.PURCHASE)
                .direction(WalletTxnDirection.OUT)
                .amount(amount)
                .balanceAfter(wallet.getBalance() - amount) // tạm tính
                .status("PENDING")
                .refType(refType)
                .refId(refId)
                .idempotencyKey(idempotencyKey)
                .build();
        txn = txnRepo.save(txn);

        // 2) Trừ ví
        wallet.setBalance(wallet.getBalance() - amount);
        walletRepo.save(wallet);

        // 3) Hoàn tất giao dịch
        txn.setBalanceAfter(wallet.getBalance());
        txn.setStatus("COMPLETED");
        return txnRepo.save(txn);
    }
}

