package hsf302.he187383.phudd.licensev3.service;

import hsf302.he187383.phudd.licensev3.enums.*;
import hsf302.he187383.phudd.licensev3.model.Wallet;
import hsf302.he187383.phudd.licensev3.model.WalletTxn;
import hsf302.he187383.phudd.licensev3.repository.WalletTxnRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletTxnService {
    private final WalletTxnRepository txnRepo;

    @Transactional
    public WalletTxn createTopupTxn(
            Wallet wallet,
            WalletTxnDirection direction,
            Long amount,
            Ref_Type refType,
            UUID refId,
            String idempotencyKey
    ) {
        // 1️⃣ Kiểm tra ví hợp lệ
        if (wallet == null) {
            throw new IllegalArgumentException("Wallet must not be null.");
        }
        if (wallet.getStatus() != WalletStatus.ACTIVE) {
            throw new IllegalStateException("Wallet is not active.");
        }

        // 2️⃣ Kiểm tra idempotency (tránh tạo trùng)
        Optional<WalletTxn> existing = txnRepo.findByIdempotencyKey(idempotencyKey);
        if (existing.isPresent()) {
            return existing.get(); // đã có rồi thì trả lại
        }

        // 3️⃣ Kiểm tra số tiền
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0.");
        }

        // 4️⃣ Tạo mới giao dịch
        WalletTxn txn = WalletTxn.builder()
                .wallet(wallet)
                .direction(direction)
                .amount(amount)
                .balanceAfter(wallet.getBalance()) // cập nhật lại sau khi xử lý
                .status(WalletTxnStatus.COMPLETED)
                .refType(refType)
                .refId(refId)
                .idempotencyKey(idempotencyKey)
                .build();

        return txnRepo.save(txn);
    }

    public Page<WalletTxn> adminSearchTxns(UUID walletId, WalletTxnDirection direction, Ref_Type refType,
                                      LocalDateTime from, LocalDateTime to, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Instant fromInstant = (from != null) ? from.atZone(ZoneId.systemDefault()).toInstant() : null;
        Instant toInstant = (to != null) ? to.atZone(ZoneId.systemDefault()).toInstant() : null;

        return txnRepo.searchTxns(walletId, direction, refType, fromInstant, toInstant, pageable);
    }

    public Page<WalletTxn> searchTxns(UUID walletId,
                                      WalletTxnDirection direction,
                                      Ref_Type refType,
                                      Instant from,
                                      Instant to,
                                      int page,
                                      int size) {
        Pageable pageable = PageRequest.of(page, size);
        return txnRepo.searchTxns(walletId, direction, refType, from, to, pageable);
    }

}
