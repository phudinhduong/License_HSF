package hsf302.he187383.phudd.licensev3.controller;

import hsf302.he187383.phudd.licensev3.model.Account;
import hsf302.he187383.phudd.licensev3.model.License;
import hsf302.he187383.phudd.licensev3.model.Product;
import hsf302.he187383.phudd.licensev3.dto.LicenseResponse;
import hsf302.he187383.phudd.licensev3.dto.VerifyLicenseRequest;
import hsf302.he187383.phudd.licensev3.repository.AccountRepository;
import hsf302.he187383.phudd.licensev3.repository.LicenseRepository;
import hsf302.he187383.phudd.licensev3.utils.JwtLicenseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/license")
public class APIController {

    @Autowired
    private LicenseRepository licenseRepo;
    @Autowired
    private JwtLicenseUtil jwtLicenseUtil;
    @Autowired
    private AccountRepository accountRepository;


@PostMapping("/activate")
public ResponseEntity<?> activateLicense(@RequestBody VerifyLicenseRequest request) {
    // Lấy account theo username
    Account account = accountRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new RuntimeException("Account not found!"));

    License license = account.getLicense();
    ZoneId zone = ZoneId.of("Asia/Ho_Chi_Minh");

    // Lấy expiresAt từ DB, nếu null thì mặc định +30 ngày từ bây giờ
    Instant expiresAtInstant = license.getExpiresAt();
    if (expiresAtInstant == null) {
        expiresAtInstant = ZonedDateTime.now(zone).plusDays(30).toInstant();
        license.setExpiresAt(expiresAtInstant); // optional: lưu lại vào DB
    }

    // Chuyển expiresAt sang ZonedDateTime theo timezone Việt Nam
    ZonedDateTime expiryZoned = expiresAtInstant.atZone(zone);
    ZonedDateTime nowZoned = ZonedDateTime.now(zone);

    // Kiểm tra license hết hạn
    if (expiryZoned.isBefore(nowZoned)) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "status", "error",
                        "message", "License expired at " + expiryZoned
                ));
    }

    // Kiểm tra appId
    String requestAppId = request.getAppId();
    Product product = license.getPlan().getProduct();

    if (!product.getId().toString().equals(requestAppId)
            || !license.getUser().getId().equals(account.getLicense().getUser().getId())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of(
                        "status", "error",
                        "message", "License already used on another device"
                ));
    }

    // Sinh JWT token
    String token = jwtLicenseUtil.generateAccessToken(license);

    // Trả về token và expiresAt theo ZonedDateTime
    LicenseResponse response = new LicenseResponse(
            token,
            expiryZoned.toInstant()
    );

    return ResponseEntity.ok(response);
}


    @GetMapping("/verify")
    public ResponseEntity<?> verifyLicense() {
        return ResponseEntity.ok("License hợp lệ");
    }

}
