package hsf302.he187383.phudd.license.service;

import hsf302.he187383.phudd.license.dto.order.*;
import hsf302.he187383.phudd.license.enums.*;
import hsf302.he187383.phudd.license.mapper.LicenseMapper;
import hsf302.he187383.phudd.license.mapper.OrderMapper;
import hsf302.he187383.phudd.license.mapper.WalletMapper;
import hsf302.he187383.phudd.license.model.*;
import hsf302.he187383.phudd.license.repository.*;
import hsf302.he187383.phudd.license.utils.LicenseKeyGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PlanRepository planRepo;
    private final WalletRepository walletRepo;
    private final OrderRepository orderRepo;
    private final WalletTxnRepository walletTxnRepo;
    private final LicenseRepository licenseRepo;
    private final UserRepository userRepo;

    private final OrderMapper orderMapper;
    private final WalletMapper walletMapper;
    private final LicenseMapper licenseMapper;

    /**
     * One-shot purchase: deduct credits, ledger, create order (PAID) and license.
     */
    @Transactional
    public PurchaseCreditsResp purchaseWithCredits(UUID currentUserId, PurchaseCreditsReq req) {
        // 1) Idempotency: nếu đã có txn cùng key → trả về Order/License trước đó
        Optional<WalletTxn> existedTxn = walletTxnRepo.findByIdempotencyKey(req.getIdempotencyKey());

        if (existedTxn.isPresent()) {
            WalletTxn txn = existedTxn.get();
            if (!"ORDER".equalsIgnoreCase(txn.getRefType()) || txn.getRefId() == null) {
                throw new ResponseStatusException(CONFLICT, "Idempotency key is already used");
            }
            Order existedOrder = orderRepo.findById(txn.getRefId())
                    .orElseThrow(() -> new ResponseStatusException(CONFLICT, "Linked order not found"));
            License existedLic = licenseRepo.findAll().stream()
                    .filter(l -> l.getOrder().getId().equals(existedOrder.getId()))
                    .findFirst()
                    .orElse(null);

            return PurchaseCreditsResp.builder()
                    .order(orderMapper.toResp(existedOrder))
                    .wallet(walletMapper.toResp(txn.getWallet()))
                    .license(existedLic != null ? licenseMapper.toResp(existedLic) : null)
                    .build();
        }

        System.out.println("Finish 1");

        // 2) Validate user + plan
        User user = userRepo.findById(currentUserId)
                .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "User not found"));
        Plan plan = planRepo.findById(req.getPlanId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Plan not found"));

        long price = plan.getPriceCredits() == null ? 0L : plan.getPriceCredits();

        System.out.println("Finish 2");

        // 3) Lock wallet & check balance
        Wallet wallet = walletRepo.lockByUserId(currentUserId)
                .orElseGet(() -> walletRepo.findByUserId(currentUserId)
                        .orElseThrow(() -> new ResponseStatusException(PRECONDITION_REQUIRED, "Wallet not found")));
        if (wallet.getStatus() != WalletStatus.ACTIVE) {
            throw new ResponseStatusException(FORBIDDEN, "Wallet is locked");
        }
        if (wallet.getBalance() == null || wallet.getBalance() < price) {
            throw new ResponseStatusException(PAYMENT_REQUIRED, "Insufficient credits");
        }

        System.out.println("Finish 3");

        // 4) Create order (PENDING)
        Order order = Order.builder()
                .user(user)
                .plan(plan)
                .priceCredits(price)
                .status(OrderStatus.PENDING)
                .build();
        order = orderRepo.save(order);

        System.out.println("Finish 4");

        // 5) Deduct balance & ledger txn (idempotent by key)
        wallet.setBalance(wallet.getBalance() - price);

        WalletTxn txn = WalletTxn.builder()
                .wallet(wallet)
                .type(WalletTxnType.PURCHASE)
                .direction(WalletTxnDirection.OUT)
                .amount(price)
                .balanceAfter(wallet.getBalance())
                .status("COMPLETED")
                .refType("ORDER")
                .refId(order.getId())
                .idempotencyKey(req.getIdempotencyKey())
                .build();

        try {
            wallet = walletRepo.save(wallet);
            txn = walletTxnRepo.save(txn);
        } catch (DataIntegrityViolationException ex) {
            // Unique idempotency_key conflict (race) → trả về kết quả đã có
            WalletTxn again = walletTxnRepo.findByIdempotencyKey(req.getIdempotencyKey())
                    .orElseThrow(() -> new ResponseStatusException(CONFLICT, "Idempotency conflict"));
            Order existedOrder = orderRepo.findById(again.getRefId())
                    .orElseThrow(() -> new ResponseStatusException(CONFLICT, "Linked order not found"));
            License existedLic = licenseRepo.findAll().stream()
                    .filter(l -> l.getOrder().getId().equals(existedOrder.getId()))
                    .findFirst()
                    .orElse(null);
            return PurchaseCreditsResp.builder()
                    .order(orderMapper.toResp(existedOrder))
                    .wallet(walletMapper.toResp(again.getWallet()))
                    .license(existedLic != null ? licenseMapper.toResp(existedLic) : null)
                    .build();
        }

        System.out.println("Finish 5");

        // 6) Mark order paid & link walletTxn
        order.setWalletTxn(txn);
        order.setStatus(OrderStatus.PAID);
        order = orderRepo.save(order);

        System.out.println("Finish 6");

        // 7) Issue license
        License license = issueLicense(order, user, plan);

        System.out.println("Finish 7");

        // 8) Build response
        return PurchaseCreditsResp.builder()
                .order(orderMapper.toResp(order))
                .wallet(walletMapper.toResp(wallet))
                .license(licenseMapper.toResp(license))
                .build();
    }

    private License issueLicense(Order order, User user, Plan plan) {
        Instant now = Instant.now();
        Instant expires = null;
        if (plan.getBillingType() == BillingType.SUBSCRIPTION) {
            Integer days = plan.getDurationDays();
            if (days == null || days <= 0) {
                throw new ResponseStatusException(UNPROCESSABLE_ENTITY, "SUBSCRIPTION requires durationDays > 0");
            }
            expires = now.plus(days, ChronoUnit.DAYS);
        }
        // generate unique license key
        String key;
        int guard = 0;
        do {
            key = LicenseKeyGenerator.randomKey(plan.getCode());
            guard++;
            if (guard > 5) throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "Cannot generate license key");
        } while (licenseRepo.existsByLicenseKey(key));

        License lic = License.builder()
                .order(order)
                .user(user)
                .plan(plan)
                .licenseKey(key)
                .status(LicenseStatus.ACTIVE)
                .issuedAt(now)
                .expiresAt(expires)
                .seatsTotal(plan.getSeats())
                .seatsUsed(0)
                .build();
        return licenseRepo.save(lic);
    }
}
