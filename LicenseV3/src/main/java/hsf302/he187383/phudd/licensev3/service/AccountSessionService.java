package hsf302.he187383.phudd.licensev3.service;

import hsf302.he187383.phudd.licensev3.enums.*;
import hsf302.he187383.phudd.licensev3.model.*;
import hsf302.he187383.phudd.licensev3.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountSessionService {

    private final AccountSessionRepository sessionRepo;
    private final AccountRepository accountRepo;
    private final LicenseRepository licenseRepo;

    public AccountSession create(UUID accountId, UUID licenseId, String jti, String tokenHash,
                                 String deviceId, String ip, String userAgent, Instant expiresAt) {

        var account = accountRepo.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        var license = licenseRepo.findById(licenseId)
                .orElseThrow(() -> new IllegalArgumentException("License not found"));

        var session = AccountSession.builder()
                .account(account)
                .license(license)
                .jti(jti)
                .tokenHash(tokenHash)
                .deviceId(deviceId)
                .ip(ip)
                .userAgent(userAgent)
                .createdAtSession(Instant.now())
                .lastSeenAt(Instant.now())
                .expiresAt(expiresAt)
                .status(SessionStatus.ACTIVE)
                .build();
        return sessionRepo.save(session);
    }

    public void touch(String jti) {
        var session = sessionRepo.findByJti(jti)
                .orElseThrow(() -> new IllegalArgumentException("Session not found"));
        session.setLastSeenAt(Instant.now());
        sessionRepo.save(session);
    }

    public void revoke(String jti) {
        var session = sessionRepo.findByJti(jti)
                .orElseThrow(() -> new IllegalArgumentException("Session not found"));
        session.setStatus(SessionStatus.REVOKED);
        sessionRepo.save(session);
    }
}

