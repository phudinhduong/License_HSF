package hsf302.he187383.phudd.licensev3.controller;

import hsf302.he187383.phudd.licensev3.dto.*;
import hsf302.he187383.phudd.licensev3.service.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(produces = APPLICATION_JSON_VALUE)
public class SimpleAuthController {

    private final SimpleAuthService authService;

    @PostMapping(path = "/auth/login", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody LoginRequest req, HttpServletRequest http) {
        try {
            boolean preempt = req.getPreempt() != null && req.getPreempt();
            var res = authService.login(
                    req.getUsername(),
                    req.getPassword(),
                    req.getDeviceId(),
                    req.getDeviceName(),
                    preempt,
                    clientIp(http),
                    http.getHeader("User-Agent")
            );

            var lic = res.license();
            var plan = res.plan();
            var prod = res.product();

            var body = new LoginResponse(
                    true,
                    res.jti(),
                    res.deviceId(),
                    res.account().getId().toString(),
                    res.account().getUsername(),
                    new LoginResponse.LicenseSummary(
                            lic.getId().toString(),
                            lic.getStatus().name(),
                            lic.getIssuedAt() != null ? lic.getIssuedAt().toString() : null,
                            lic.getExpiresAt() != null ? lic.getExpiresAt().toString() : null,
                            lic.getSeatsUsed(), lic.getSeatsTotal(),
                            new LoginResponse.ProductSummary(prod.getCode(), prod.getName()),
                            new LoginResponse.PlanSummary(plan.getCode(), plan.getName(), plan.getBillingType().name()),
                            res.sessionExpiresAt() != null ? res.sessionExpiresAt().toString() : null
                    )
            );
            return ResponseEntity.ok(body);

        } catch (IllegalArgumentException e) {
            String code = e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorPayload(code, humanize(code)));
        }
    }

    @PostMapping(path = "/auth/heartbeat", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> heartbeat(@RequestBody HeartbeatRequest req) {
        try {
            authService.heartbeat(req.getJti(), req.getDeviceId());
            return ResponseEntity.ok().body("{\"ok\":true}");
        } catch (IllegalArgumentException e) {
            String code = e.getMessage();
            HttpStatus st = switch (code) {
                case "token_expired" -> HttpStatus.UNAUTHORIZED;
                case "token_revoked", "device_mismatch" -> HttpStatus.FORBIDDEN;
                default -> HttpStatus.BAD_REQUEST;
            };
            return ResponseEntity.status(st).body(new ErrorPayload(code, humanize(code)));
        }
    }

    @PostMapping(path = "/auth/logout", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> logout(@RequestBody LogoutRequest req) {
        try {
            authService.logout(req.getJti());
            return ResponseEntity.ok().body("{\"ok\":true}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorPayload("token_revoked","Already logged out"));
        }
    }

    private static String clientIp(HttpServletRequest req) {
        String xff = req.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) return xff.split(",")[0].trim();
        return req.getRemoteAddr();
    }

    private static String humanize(String code) {
        return switch (code) {
            case "invalid_grant"    -> "Sai tài khoản hoặc mật khẩu.";
            case "account_disabled" -> "Tài khoản đã bị vô hiệu hóa.";
            case "license_expired"  -> "License đã hết hạn.";
            case "license_revoked"  -> "License đã bị thu hồi.";
            case "concurrent_limit" -> "Đã đạt số phiên đồng thời tối đa.";
            case "device_limit"     -> "Đã vượt số thiết bị cho phép.";
            case "token_revoked"    -> "Phiên đã bị hủy.";
            case "token_expired"    -> "Phiên đã hết hạn.";
            case "device_mismatch"  -> "Thiết bị không khớp với phiên.";
            default -> code;
        };
    }
}
