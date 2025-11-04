package hsf302.he187383.phudd.license.service;

import hsf302.he187383.phudd.license.config.JwtProperties;
import hsf302.he187383.phudd.license.dto.auth.*;
import hsf302.he187383.phudd.license.enums.*;
import hsf302.he187383.phudd.license.model.*;
import hsf302.he187383.phudd.license.repository.UserRepository;
import hsf302.he187383.phudd.license.repository.WalletRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authManager;
    private final WalletRepository walletRepo;
    private final JwtTokenService jwt;
    private final JwtProperties props;
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    // ===== Helpers =====
    private Map<String,Object> claims(User u, String typ, boolean includeRole) {
        Map<String,Object> c = new HashMap<>();
        c.put("sub", u.getEmail());
        c.put("uid", u.getId().toString());
        c.put("typ", typ);
        if (includeRole) c.put("role", u.getRole().name());
        return c;
    }

    private String issueAccess(User u) {
        return jwt.generate(claims(u, "access", true), props.getAccessTtlSeconds());
    }

    private String issueRefresh(User u) {
        return jwt.generate(claims(u, "refresh", false), props.getRefreshTtlSeconds());
    }

    // ===== Flows =====
    public TokenResp login(LoginReq req) {
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
            UserDetails principal = (UserDetails) auth.getPrincipal();
            User u = userRepo.findByEmailIgnoreCase(principal.getUsername())
                    .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "Invalid login"));

            return TokenResp.builder()
                    .accessToken(issueAccess(u))
                    .refreshToken(issueRefresh(u))
                    .expiresIn(props.getAccessTtlSeconds())
                    .tokenType("Bearer")
                    .build();

        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(UNAUTHORIZED, "Invalid email or password");
        } catch (DisabledException e) {
            throw new ResponseStatusException(FORBIDDEN, "User disabled");
        }
    }

    public TokenResp refresh(RefreshReq req) {
        try {
            Jws<Claims> jws = jwt.parse(req.getRefreshToken());
            Claims c = jws.getBody();
            if (!"refresh".equals(c.get("typ"))) {
                throw new ResponseStatusException(UNAUTHORIZED, "Invalid token type");
            }
            User u = userRepo.findByEmailIgnoreCase(c.getSubject())
                    .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "User not found"));

            return TokenResp.builder()
                    .accessToken(issueAccess(u))
                    .refreshToken(req.getRefreshToken()) // giữ nguyên refresh cũ
                    .expiresIn(props.getAccessTtlSeconds())
                    .tokenType("Bearer")
                    .build();

        } catch (Exception e) {
            throw new ResponseStatusException(UNAUTHORIZED, "Invalid or expired token");
        }
    }

    @Transactional
    public TokenResp register(SignupReq req) {
        String email = req.getEmail().trim().toLowerCase();
        if (userRepo.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(CONFLICT, "Email already registered");
        }

        User u = userRepo.save(User.builder()
                .email(email)
                .passwordHash(encoder.encode(req.getPassword()))
                .role(UserRole.USER)
                .status(UserStatus.ACTIVE)
                .build());

        walletRepo.save(Wallet.builder()
                .user(u).balance(0L).status(WalletStatus.ACTIVE).build());

        return TokenResp.builder()
                .accessToken(issueAccess(u))
                .refreshToken(issueRefresh(u))
                .expiresIn(props.getAccessTtlSeconds())
                .tokenType("Bearer")
                .build();
    }
}
