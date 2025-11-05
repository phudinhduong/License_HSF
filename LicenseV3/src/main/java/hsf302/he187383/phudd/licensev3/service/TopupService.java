package hsf302.he187383.phudd.licensev3.service;

import hsf302.he187383.phudd.licensev3.enums.*;
import hsf302.he187383.phudd.licensev3.model.*;
import hsf302.he187383.phudd.licensev3.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TopupService {

    private final TopupRepository topupRepository;
    private final WalletService walletService;
    private final WalletTxnService walletTxnService;
    private final UserRepository userRepository;

    // Ví dụ quy đổi 1.000 VND = 1 Credit
    private static final long RATE_VND_TO_CREDIT = 1000L;

    /**
     * Tạo bản ghi topup ở trạng thái PENDING, sinh mã paymentRef (vnp_TxnRef)
     */
    @Transactional
    public Topup createPendingTopup(UUID userId, BigDecimal amountVnd) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Sinh mã giao dịch paymentRef (vnp_TxnRef)
        String paymentRef = "TXN" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();

        Topup topup = Topup.builder()
                .user(user)
                .provider(PaymentProvider.VNPAY)
                .moneyAmount(amountVnd)
                .currency("VND")
                .creditsGranted(amountVnd.longValue() / RATE_VND_TO_CREDIT)
                .status(TopupStatus.PENDING)
                .paymentRef(paymentRef)
                .idempotencyKey("TOPUP_" + paymentRef)
                .build();

        return topupRepository.save(topup);
    }
    @Transactional
    public void markAsSuccess(String paymentRef, String vnpTxnNo) {
        Topup topup = topupRepository.findByPaymentRef(paymentRef)
                .orElseThrow(() -> new RuntimeException("Topup not found"));

        if (topup.getStatus() != TopupStatus.PENDING) return; // tránh cộng trùng

        topup.setStatus(TopupStatus.SUCCESS);
        topup.setPaymentRef(vnpTxnNo);
        topupRepository.save(topup);

        // Cập nhật ví
        walletService.addCredits(topup.getUser().getId(), topup.getCreditsGranted());

        Wallet wallet = walletService.getWalletByUserId(topup.getUser().getId());

        // Ghi nhận giao dịch ví
        walletTxnService.createTopupTxn(wallet, WalletTxnType.TOPUP, WalletTxnDirection.IN, topup.getCreditsGranted(), Topup_Ref_Type.TOPUP, topup.getId(), "TOPUP_" + paymentRef);
    }

    @Transactional
    public void markAsFailed(String paymentRef) {
        topupRepository.findByPaymentRef(paymentRef).ifPresent(topup -> {
            topup.setStatus(TopupStatus.FAILED);
            topupRepository.save(topup);
        });
    }

}
