package hsf302.he187383.phudd.license.service;

import hsf302.he187383.phudd.license.enums.*;
import hsf302.he187383.phudd.license.model.*;
import hsf302.he187383.phudd.license.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;


@Service
@RequiredArgsConstructor
@Transactional
public class LicenseService {

    private final LicenseRepository licenseRepo;
    private final UserRepository userRepo;
    private final PlanRepository planRepo;
    private final OrderRepository orderRepo;

    @Transactional(readOnly = true)
    public List<License> findByUser(UUID userId) {
        return licenseRepo.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public License findByKey(String key) {
        return licenseRepo.findByLicenseKey(key)
                .orElseThrow(() -> new IllegalArgumentException("License not found"));
    }

    // nếu bé muốn tạo license từ order
    public License issueLicense(UUID orderId, String licenseKey) {
        var order = orderRepo.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        var user = order.getUser();
        var plan = order.getPlan();

        var license = License.builder()
                .order(order)
                .user(user)
                .plan(plan)
                .licenseKey(licenseKey)
                .status(LicenseStatus.ACTIVE)
                .issuedAt(Instant.now())
                .expiresAt(plan.getBillingType() == BillingType.SUBSCRIPTION
                        ? Instant.now().plusSeconds(plan.getDurationDays() * 24L * 3600L)
                        : null)
                .seatsTotal(plan.getSeats())
                .seatsUsed(0)
                .build();
        return licenseRepo.save(license);
    }
}

