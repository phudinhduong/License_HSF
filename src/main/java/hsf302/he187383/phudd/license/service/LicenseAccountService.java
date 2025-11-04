package hsf302.he187383.phudd.license.service;

import hsf302.he187383.phudd.license.dto.account.AccountResp;
import hsf302.he187383.phudd.license.dto.account.*;
import hsf302.he187383.phudd.license.enums.*;
import hsf302.he187383.phudd.license.mapper.AccountMapper;
import hsf302.he187383.phudd.license.model.*;
import hsf302.he187383.phudd.license.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LicenseAccountService {

    private final LicenseRepository licenseRepo;
    private final AccountRepository accountRepo;
    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AccountResp createChildAccount(UUID currentUserId, UUID licenseId, ChildAccountCreateReq req) {
        // Khoá bản ghi license theo user (tránh race tăng seatsUsed)
        License lic = licenseRepo.lockByIdAndUserId(licenseId, currentUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "License not found"));

        // 1) Kiểm tra tình trạng license
        if (lic.getStatus() != LicenseStatus.ACTIVE) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "License is not active");
        }
        if (lic.getExpiresAt() != null && Instant.now().isAfter(lic.getExpiresAt())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "License expired");
        }

        // 2) Kiểm tra seats còn trống
        Integer total = lic.getSeatsTotal();
        Integer used  = lic.getSeatsUsed() == null ? 0 : lic.getSeatsUsed();
        boolean limited = (total != null && total > 0);
        if (limited && used >= total) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No remaining seats");
        }

        // 3) Kiểm tra username global unique
        if (accountRepo.existsByUsernameIgnoreCase(req.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }

        // 4) Tạo account + tăng seatsUsed
        Account acc = Account.builder()
                .license(lic)
                .username(req.getUsername().trim())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .status(AccountStatus.ACTIVE)
                .build();
        acc = accountRepo.save(acc);

        lic.setSeatsUsed(used + 1); // tăng ghế đã dùng
        // Hibernate sẽ flush update cho lic khi transaction commit

        return accountMapper.toResp(acc);
    }

    @Transactional(readOnly = true)
    public Page<AccountResp> listAccounts(UUID currentUserId, UUID licenseId, Pageable pageable) {
        // xác thực quyền sở hữu (đọc nhẹ, không cần lock)
        licenseRepo.lockByIdAndUserId(licenseId, currentUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "License not found"));
        return accountRepo.findByLicenseId(licenseId, pageable).map(accountMapper::toResp);
    }
}
