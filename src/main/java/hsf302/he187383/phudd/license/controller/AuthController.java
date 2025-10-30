package hsf302.he187383.phudd.license.controller;

import hsf302.he187383.phudd.license.DTOs.auth.*;
import hsf302.he187383.phudd.license.DTOs.auth.TokenResponse;
import hsf302.he187383.phudd.license.model.*;
import hsf302.he187383.phudd.license.repository.UserRepository;
import hsf302.he187383.phudd.license.service.*;
import io.jsonwebtoken.*;
import jakarta.validation.*;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final AppUserDetailsService uds;
    private final UserRepository userRepo;
    private final TokenService tokenService;

    public AuthController(AuthenticationManager authManager, AppUserDetailsService uds,
                          UserRepository userRepo, TokenService tokenService) {
        this.authManager = authManager; this.uds = uds;
        this.userRepo = userRepo; this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest req) {
        // 1) Tìm user theo ưu tiên orgId -> orgCode -> email
        User u = req.getOrgId() != null
                ? userRepo.findByOrganizationIdAndEmail(req.getOrgId(), req.getEmail())
                .orElseThrow(() -> new ExceptionInInitializerError("User not found (orgId/email)"))
                : (req.getOrgCode() != null && !req.getOrgCode().isBlank())
                ? userRepo.findByOrgCodeAndEmail(req.getOrgCode(), req.getEmail())
                .orElseThrow(() -> new ExceptionInInitializerError("User not found (orgCode/email)"))
                : userRepo.findByEmail(req.getEmail())
                .orElseThrow(() -> new ExceptionInInitializerError("User not found (email)"));

        // 2) Xây username composite cho AuthManager
        String composite = (u.getOrganization() != null ? u.getOrganization().getId() : null)
                + "::"
                + (u.getOrganization() != null ? u.getOrganization().getCode() : null)
                + "::"
                + u.getEmail();

        // 3) Authenticate (BCrypt check)
        var authToken = new UsernamePasswordAuthenticationToken(composite, req.getPassword());
        authManager.authenticate(authToken); // throw nếu sai pass

        // 4) Issue tokens
        var tokens = tokenService.generateTokens(u);

        // 5) Trả thêm role để FE điều hướng (user vs org_admin vs owner)
        return ResponseEntity.ok(TokenResponse.builder()
                .accessToken(tokens.accessToken())
                .refreshToken(tokens.refreshToken())
                .expiresIn(tokens.accessExpiresIn())
                .tokenType("Bearer")
                .build());
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody RefreshRequest req) {
        var jws = tokenService.parseAndValidate(req.getRefreshToken());
        if (!tokenService.isRefreshToken(jws)) return ResponseEntity.badRequest().build();

        UUID uid = UUID.fromString(jws.getBody().get("uid", String.class));
        User u = userRepo.findById(uid).orElseThrow(() -> new ExceptionInInitializerError("User not found"));
        var tokens = tokenService.generateTokens(u);

        return ResponseEntity.ok(TokenResponse.builder()
                .accessToken(tokens.accessToken())
                .refreshToken(tokens.refreshToken())
                .expiresIn(tokens.accessExpiresIn())
                .tokenType("Bearer")
                .build());
    }
}
