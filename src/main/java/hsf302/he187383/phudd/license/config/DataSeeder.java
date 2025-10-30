package hsf302.he187383.phudd.license.config;

import hsf302.he187383.phudd.license.enums.*;
import hsf302.he187383.phudd.license.model.*;
import hsf302.he187383.phudd.license.repository.*;
import hsf302.he187383.phudd.license.utils.KeyGenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final OrganizationRepository orgRepo;
    private final UserRepository userRepo;
    private final ProductRepository productRepo;
    private final PlanRepository planRepo;
    private final LicenseRepository licenseRepo;
    private final KeyPairRepository keyRepo;
    private final PasswordEncoder encoder; // BCrypt từ SecurityConfig

    @Override
    public void run(String... args) {
        // Tránh seed lại nếu đã có dữ liệu
        if (orgRepo.count() > 0) {
            return;
        }

        System.out.println("🌱 Seeding sample data...");

        // 1) Organization
        Organization org = Organization.builder()
                .code("ACME")
                .name("ACME Software Co.")
                .status(OrgStatus.ACTIVE)
                .build();
        orgRepo.save(org);

        // 2) Users (3 role: OWNER / ORG_ADMIN / USER)
        User owner = User.builder()
                .organization(org)
                .email("admin@gmail.com")
                .passwordHash(encoder.encode("123456"))
                .fullName("System Owner")
                .role(UserRole.ADMIN)
                .status(UserStatus.ACTIVE)
                .build();
        userRepo.save(owner);

        User orgAdmin = User.builder()
                .organization(org)
                .email("orgadmin@acme.com")
                .passwordHash(encoder.encode("123456"))
                .fullName("Org Admin")
                .role(UserRole.ORG_ADMIN)
                .status(UserStatus.ACTIVE)
                .build();
        userRepo.save(orgAdmin);

        User normalUser = User.builder()
                .organization(org)
                .email("user@acme.com")
                .passwordHash(encoder.encode("123456"))
                .fullName("Normal User")
                .role(UserRole.USER)
                .status(UserStatus.ACTIVE)
                .build();
        userRepo.save(normalUser);

        System.out.println("✅ Users:");
        System.out.println("   - owner@acme.com / 123456 (OWNER)");
        System.out.println("   - orgadmin@acme.com / 123456 (ORG_ADMIN)");
        System.out.println("   - user@acme.com / 123456 (USER)");

        // 3) Product
        Product product = Product.builder()
                .code("PRO_EDITOR")
                .name("ACME Pro Editor")
                .currentVersion("2.1.0")
                .build();
        productRepo.save(product);

        // 4) Plans
        Plan planBasic = Plan.builder()
                .product(product)
                .code("BASIC")
                .name("Basic Plan")
                .type(PlanType.SUBSCRIPTION)
                .durationDays(365)
                .maxDevices(1)
                .maxConcurrent(1)
                .features("{\"watermark\":true,\"export_pdf\":false}")
                .isActive(true)
                .build();
        Plan planPro = Plan.builder()
                .product(product)
                .code("PRO")
                .name("Pro Plan")
                .type(PlanType.SUBSCRIPTION)
                .durationDays(365)
                .maxDevices(3)
                .maxConcurrent(3)
                .features("{\"watermark\":false,\"export_pdf\":true,\"ai_assist\":true}")
                .isActive(true)
                .build();
        planRepo.save(planBasic);
        planRepo.save(planPro);

        // 5) License (Pro) cho ACME
        License license = License.builder()
                .organization(org)
                .product(product)
                .plan(planPro)
                .licenseKey("ACME-1234-PRO")
                .type(LicenseType.SUBSCRIPTION)
                .status(LicenseStatus.ACTIVE)
                .startAt(Instant.now())
                .endAt(Instant.now().plusSeconds(365L * 24 * 60 * 60)) // +1 năm
                .maxDevices(planPro.getMaxDevices())
                .maxConcurrent(planPro.getMaxConcurrent())
                .metadata("{\"note\":\"seeded license\"}")
                .build();
        licenseRepo.save(license);

        System.out.println("✅ Product/Plans seeded. License key: ACME-1234-PRO");

        // 6) KeyPair cho JWT (tự sinh PEM hợp lệ)
        if (keyRepo.count() == 0) {
            var pemPair = KeyGenUtil.generateRsaPemPair(); // RSA 2048
            KeyPairEntity key = KeyPairEntity.builder()
                    .kid("key-" + System.currentTimeMillis())
                    .algorithm("RS256")
                    .privatePem(pemPair.privatePem())
                    .publicPem(pemPair.publicPem())
                    .activeFrom(Instant.now())
                    .build();
            keyRepo.save(key);
            System.out.println("✅ Generated RSA key pair for JWT signing (kid=" + key.getKid() + ")");
        }

        System.out.println("🎉 Seeding completed.");
    }
}
