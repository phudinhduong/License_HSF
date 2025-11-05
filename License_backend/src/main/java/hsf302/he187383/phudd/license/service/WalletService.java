package hsf302.he187383.phudd.license.service;



import hsf302.he187383.phudd.license.enums.*;
import hsf302.he187383.phudd.license.model.*;
import hsf302.he187383.phudd.license.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
}

