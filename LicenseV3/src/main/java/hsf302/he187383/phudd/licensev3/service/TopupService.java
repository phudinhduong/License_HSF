package hsf302.he187383.phudd.licensev3.service;

import hsf302.he187383.phudd.licensev3.enums.*;
import hsf302.he187383.phudd.licensev3.model.*;
import hsf302.he187383.phudd.licensev3.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TopupService {

    private final TopupRepository topupRepo;
    private final UserRepository userRepo;

    public Topup create(UUID userId, PaymentProvider provider,
                        BigDecimal moneyAmount, String currency,
                        Long creditsGranted, String paymentRef) {
        var user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        var topup = Topup.builder()
                .user(user)
                .provider(provider)
                .moneyAmount(moneyAmount)
                .currency(currency)
                .creditsGranted(creditsGranted)
                .status(TopupStatus.PENDING)
                .paymentRef(paymentRef)
                .build();
        return topupRepo.save(topup);
    }

    @Transactional(readOnly = true)
    public List<Topup> findByUser(UUID userId) {
        return topupRepo.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public void markPaid(UUID topupId) {
        var t = topupRepo.findById(topupId)
                .orElseThrow(() -> new IllegalArgumentException("Topup not found"));
        t.setStatus(TopupStatus.PAID);
        topupRepo.save(t);
    }
}

