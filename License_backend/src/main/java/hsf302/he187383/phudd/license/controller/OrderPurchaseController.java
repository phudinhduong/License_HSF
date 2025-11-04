package hsf302.he187383.phudd.license.controller;

import hsf302.he187383.phudd.license.dto.order.*;
import hsf302.he187383.phudd.license.repository.UserRepository;
import hsf302.he187383.phudd.license.service.PurchaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderPurchaseController {

    private final PurchaseService purchaseService;
    private final UserRepository userRepo;

    @PostMapping("/credits")
    public PurchaseCreditsResp purchaseWithCredits(@Valid @RequestBody PurchaseCreditsReq req,
                                                   Authentication auth) {
        // Lấy userId từ email trong SecurityContext
        String email = auth.getName();

        System.out.println("Email: " + email);

        UUID userId = userRepo.findByEmailIgnoreCase(email)
                .map(u -> u.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println("UserId: " + userId);

        return purchaseService.purchaseWithCredits(userId, req);
    }
}
