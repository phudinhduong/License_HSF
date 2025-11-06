package hsf302.he187383.phudd.licensev3.service;


import hsf302.he187383.phudd.licensev3.enums.*;
import hsf302.he187383.phudd.licensev3.model.*;
import hsf302.he187383.phudd.licensev3.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepo;
    private final WalletTxnRepository txnRepo;
    private final TopupRepository topupRepo;
    private final UserRepository userRepository;

    @Transactional
    public void addCredits(UUID userId, long credits) {
        Wallet wallet = walletRepo.findByUserId(userId)
                .orElseGet(() -> {
                    Wallet newWallet = new Wallet();
                    newWallet.setUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
                    newWallet.setBalance(0L); // mặc định 0
                    newWallet.setStatus(WalletStatus.ACTIVE);
                    return newWallet;
                });

        wallet.setBalance(wallet.getBalance() + credits);
        walletRepo.save(wallet);
    }

    public Wallet getWalletByUserId(UUID userId) {
        return walletRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
    }

    /** Mua hàng: tạo WalletTxn PURCHASE/OUT + trừ ví, idempotent theo idempotencyKey */
    public WalletTxn purchase(UUID userId, long amount, String idempotencyKey, String refType, UUID refId) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be > 0");

        // Idempotency: trả về txn cũ nếu đã tạo
        var existed = txnRepo.findByIdempotencyKey(idempotencyKey);
        if (existed.isPresent()) return existed.get();

        var wallet = walletRepo.findByUserId(userId)
                .orElseGet(() -> {
                    var user = userRepository.findById(userId)
                            .orElseThrow(() -> new IllegalArgumentException("User not found"));
                    return walletRepo.save(Wallet.builder().user(user).balance(0L).status(WalletStatus.ACTIVE).build());
                });

        if (wallet.getBalance() < amount) {
            throw new IllegalStateException("Not enough credits");
        }

        // 1) Ghi txn PENDING
        var txn = WalletTxn.builder()
                .wallet(wallet)
 //               .type(WalletTxnType.PURCHASE)

                .direction(WalletTxnDirection.OUT)
                .amount(amount)
                .balanceAfter(wallet.getBalance() - amount) // tạm tính
                .status(WalletTxnStatus.valueOf("PENDING"))
//                .refType(Topup_Ref_Type.valueOf(refType))
                .refType(Ref_Type.valueOf(refType))

                .refId(refId)
                .idempotencyKey(idempotencyKey)
                .build();
        txn = txnRepo.save(txn);

        // 2) Trừ ví
        wallet.setBalance(wallet.getBalance() - amount);
        walletRepo.save(wallet);

        // 3) Hoàn tất giao dịch
        txn.setBalanceAfter(wallet.getBalance());
        txn.setStatus(WalletTxnStatus.valueOf("COMPLETED"));
        return txnRepo.save(txn);
    }

    public Page<Wallet> searchWallets(UUID userId, WalletStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("updatedAt").descending());
        return walletRepo.searchWallets(userId, status, pageable);
    }
}


