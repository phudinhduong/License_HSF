package hsf302.he187383.phudd.license.config;

import hsf302.he187383.phudd.license.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.CommandLineRunner;

import hsf302.he187383.phudd.license.enums.*; // BillingType, UserRole, UserStatus, WalletStatus
import hsf302.he187383.phudd.license.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class SeedingData {

    private final ProductRepository productRepo;
    private final PlanRepository planRepo;
    private final UserRepository userRepo;
    private final WalletRepository walletRepo;
    private final PasswordEncoder encoder;
    private final WalletTxnRepository walletTxnRepo;
    private final OrderRepository orderRepo;

    @Bean
    CommandLineRunner seedDemo() {
        return args -> {
            // tránh seed trùng
            if (productRepo.count() > 0 || userRepo.count() > 0) return;

            // 1) Product
            Product prod = productRepo.save(Product.builder()
                    .code("DEMO")
                    .name("Demo Product")
                    .description("Seeded product")
                    .build());

            // 2) Plan: giá = 1 credit
            Plan plan = planRepo.save(Plan.builder()
                    .product(prod)
                    .code("DEMO_1CRED")
                    .name("Demo Plan 1 Credit")
                    .billingType(BillingType.PERPETUAL) // hoặc BillingType.PERPETUAL tùy enum của bạn (PERPETUAL)
                    .priceCredits(1L)
                    .durationDays(0)           // perpetual
                    .seats(5)                  // ví dụ
                    .concurrentLimitPerAccount(2)
                    .deviceLimitPerAccount(3)
                    .build());

            User admin = userRepo.save(User.builder()
                    .email("admin@gmail.com")
                    .passwordHash(encoder.encode("123123"))
                    .role(UserRole.ADMIN)
                    .status(UserStatus.ACTIVE)
                    .build());

            // 3) User + Wallet: 9,999 credits
            User user = userRepo.save(User.builder()
                    .email("user@gmail.com")
                    .passwordHash(encoder.encode("123123"))
                    .role(UserRole.USER)
                    .status(UserStatus.ACTIVE)
                    .build());

            walletRepo.save(Wallet.builder()
                    .user(user)               // nếu Wallet dùng FK userId thay vì relation, set userId tương ứng
                    .balance(9_999L)
                    .status(WalletStatus.ACTIVE)
                    .build());

//            Optional wallet1 = walletRepo.findByUserId(user.getId());
//            Wallet wallet = (Wallet) wallet1.get();

//            Order order = Order.builder()
//                    .user(user)
//                    .plan(plan)
//                    .priceCredits(1L)
//                    .status(OrderStatus.PENDING)
//                    .build();
//            order = orderRepo.save(order);
//
//            WalletTxn txn = WalletTxn.builder()
//                    .wallet(wallet)
//                    .type(WalletTxnType.PURCHASE)
//                    .direction(WalletTxnDirection.OUT)
//                    .amount(1L)
//                    .balanceAfter(wallet.getBalance())
//                    .status("COMPLETED")
//                    .refType("ORDER")
//                    .refId(order.getId())
//                    .idempotencyKey("123123132132132")
//                    .build();
//            walletTxnRepo.save(txn);

        };
    }
}
