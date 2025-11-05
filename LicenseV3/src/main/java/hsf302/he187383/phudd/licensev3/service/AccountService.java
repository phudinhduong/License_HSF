package hsf302.he187383.phudd.licensev3.service;

import hsf302.he187383.phudd.licensev3.enums.*;
import hsf302.he187383.phudd.licensev3.model.*;
import hsf302.he187383.phudd.licensev3.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {

    private final AccountRepository accountRepo;
    private final LicenseRepository licenseRepo;

    @Transactional(readOnly = true)
    public List<Account> findByLicense(UUID licenseId) {
        return accountRepo.findByLicenseId(licenseId);
    }

    public Account create(UUID licenseId, String username, String passwordHash) {
        var license = licenseRepo.findById(licenseId)
                .orElseThrow(() -> new IllegalArgumentException("License not found"));
        var acc = Account.builder()
                .license(license)
                .username(username)
                .passwordHash(passwordHash)
                .status(AccountStatus.ACTIVE)
                .build();
        return accountRepo.save(acc);
    }

    public void updatePassword(UUID accountId, String newPasswordHash) {
        var acc = accountRepo.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        acc.setPasswordHash(newPasswordHash);
        accountRepo.save(acc);
    }

    public void disable(UUID accountId) {
        var acc = accountRepo.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        acc.setStatus(AccountStatus.DISABLED);
        accountRepo.save(acc);
    }


}

