package hsf302.he187383.phudd.license.controller;

import hsf302.he187383.phudd.license.dto.account.*;
import hsf302.he187383.phudd.license.repository.UserRepository;
import hsf302.he187383.phudd.license.service.LicenseAccountService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/licenses/{licenseId}/accounts")
@RequiredArgsConstructor
public class LicenseAccountController {

    private final LicenseAccountService service;
    private final UserRepository userRepo;

    private UUID getCurrentUserId(Authentication auth) {
        String email = auth.getName();
        return userRepo.findByEmailIgnoreCase(email)
                .map(u -> u.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @PostMapping
    public AccountResp create(@PathVariable UUID licenseId,
                              @Valid @RequestBody ChildAccountCreateReq req,
                              Authentication auth) {
        return service.createChildAccount(getCurrentUserId(auth), licenseId, req);
    }

    @GetMapping
    public Page<AccountResp> list(@PathVariable UUID licenseId,
                                  Authentication auth,
                                  @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)
                                  Pageable pageable) {
        return service.listAccounts(getCurrentUserId(auth), licenseId, pageable);
    }
}
