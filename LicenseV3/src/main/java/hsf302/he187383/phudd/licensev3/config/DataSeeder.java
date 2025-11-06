package hsf302.he187383.phudd.licensev3.config;

import hsf302.he187383.phudd.licensev3.enums.*;
import hsf302.he187383.phudd.licensev3.model.*;
import hsf302.he187383.phudd.licensev3.repository.*;

import hsf302.he187383.phudd.licensev3.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@Configuration
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepo;
    private final ProductRepository productRepo;
    private final PlanRepository planRepo;
    private final PasswordEncoder passwordEncoder;

        // nếu chưa dùng có thể bỏ
    private final WalletRepository walletRepo;
    private final WalletTxnRepository walletTxnRepo;


    @Override
    public void run(String... args) throws Exception {
        seedUsers();
        seedProductsAndPlans();
    }

    private void seedUsers() {
        if (userRepo.count() == 0) {
            var admin = User.builder()
                    .email("admin@gmail.com")
                    .passwordHash(passwordEncoder.encode("123123"))
                    .role(UserRole.ADMIN)
                    .status(UserStatus.ACTIVE)
                    .build();
            userRepo.save(admin);

            var user = User.builder()
                    .email("user@gmail.com")
                    .passwordHash(passwordEncoder.encode("123123"))
                    .role(UserRole.USER)
                    .status(UserStatus.ACTIVE)
                    .build();
            userRepo.save(user);

            ensureWalletWithBalance(user, 99999999L);

            System.out.println("✅ Seeded users: admin@gmail.com / user@gmail.com (pass: 123123)");
        }
    }

    /** Tạo/cập nhật ví user về số dư mong muốn và ghi một ADJUSTMENT/IN txn */
    private void ensureWalletWithBalance(User u, long balance) {
        var existing = walletRepo.findByUserId(u.getId());

        Wallet w = existing.orElseGet(() ->
                walletRepo.save(Wallet.builder()
                        .user(u)
                        .balance(0L)
                        .status(WalletStatus.ACTIVE)
                        .build())
        );

        if (!Objects.equals(w.getBalance(), balance)) {
            // điều chỉnh số dư về balance
            long delta = balance - w.getBalance();
            w.setBalance(balance);
            w.setStatus(WalletStatus.ACTIVE);
            w = walletRepo.save(w);

            // Ghi sổ giao dịch (ADJUSTMENT)
            var txn = WalletTxn.builder()
                    .wallet(w)
                    .direction(delta >= 0 ? WalletTxnDirection.IN : WalletTxnDirection.OUT)
                    .amount(Math.abs(delta))
                    .balanceAfter(w.getBalance())
                    .status(WalletTxnStatus.COMPLETED)
                    .refType(Ref_Type.TOPUP)
                    .refId(null)
                    .idempotencyKey("TOPUP-" + u.getEmail())
                    .build();
            walletTxnRepo.save(txn);
        }
    }


    private void seedProductsAndPlans() {
        // Product 1: License Manager
        var p1 = getOrCreateProduct(
                "LIC_MAIN",
                "License Manager",
                "Core product to issue and manage licenses."
        );

        getOrCreatePlan(p1, "BASIC",
                "Basic Perpetual",
                BillingType.PERPETUAL,
                10_000L, "USD",
                null,            // durationDays null → perpetual
                1,               // seats
                1,               // concurrentLimitPerAccount
                2                // deviceLimitPerAccount
        );

        getOrCreatePlan(p1, "PRO_MONTH",
                "Pro Monthly",
                BillingType.SUBSCRIPTION,
                5_000L, "USD",
                30,              // 30 days
                3,
                2,
                5
        );

        getOrCreatePlan(p1, "ENT_YEAR",
                "Enterprise Yearly",
                BillingType.SUBSCRIPTION,
                50_000L, "USD",
                365,
                20,
                10,
                20
        );

        // Product 2: Analytics Add-on
        var p2 = getOrCreateProduct(
                "ANALYTICS",
                "Analytics Add-on",
                "Advanced usage analytics and reports."
        );

        getOrCreatePlan(p2, "STARTER",
                "Starter Monthly",
                BillingType.SUBSCRIPTION,
                1_500L, "USD",
                30,
                null, null, null
        );

        getOrCreatePlan(p2, "PLUS",
                "Plus Monthly",
                BillingType.SUBSCRIPTION,
                4_500L, "USD",
                30,
                null, null, null
        );

        // Product 3: Cloud Storage
        var p3 = getOrCreateProduct(
                "STORAGE",
                "Cloud Storage",
                "Cloud storage for backups and artifacts."
        );

        getOrCreatePlan(p3, "S50",
                "Storage 50GB",
                BillingType.SUBSCRIPTION,
                800L, "USD",
                30,
                null, null, null
        );

        getOrCreatePlan(p3, "S200",
                "Storage 200GB",
                BillingType.SUBSCRIPTION,
                2_200L, "USD",
                30,
                null, null, null
        );

        getOrCreatePlan(p3, "S1TB",
                "Storage 1TB",
                BillingType.SUBSCRIPTION,
                7_500L, "USD",
                30,
                null, null, null
        );

        System.out.println("✅ Seeded products & plans (idempotent).");
    }

    // ---------- helpers ----------

    private Product getOrCreateProduct(String code, String name, String description) {
        return productRepo.findByCode(code).orElseGet(() -> {
            var p = Product.builder()
                    .code(code)
                    .name(name)
                    .description(description)
                    .build();
            return productRepo.save(p);
        });
    }

    private Plan getOrCreatePlan(Product product,
                                 String code,
                                 String name,
                                 BillingType billingType,
                                 Long priceCredits,
                                 String currency,
                                 Integer durationDays,
                                 Integer seats,
                                 Integer concurrentLimitPerAccount,
                                 Integer deviceLimitPerAccount) {

        return planRepo.findByProductIdAndCode(product.getId(), code).orElseGet(() -> {
            var plan = Plan.builder()
                    .product(product)
                    .code(code)
                    .name(name)
                    .billingType(billingType)
                    .priceCredits(priceCredits)
                    .currency(currency != null ? currency : "USD")
                    .durationDays(durationDays) // null/0 = perpetual
                    .seats(seats)
                    .concurrentLimitPerAccount(concurrentLimitPerAccount)
                    .deviceLimitPerAccount(deviceLimitPerAccount)
                    .build();
            return planRepo.save(plan);
        });
    }
}
