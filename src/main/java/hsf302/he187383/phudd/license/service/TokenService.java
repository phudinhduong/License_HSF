package hsf302.he187383.phudd.license.service;

import hsf302.he187383.phudd.license.model.*;
import hsf302.he187383.phudd.license.repository.KeyPairRepository;
import hsf302.he187383.phudd.license.utils.PemUtils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.*;
import java.time.Instant;
import java.util.*;

@Service
public class TokenService {

    private final KeyPairRepository keyRepo;

    public TokenService(KeyPairRepository keyRepo) { this.keyRepo = keyRepo; }

    // === Generate ===
    public Tokens generateTokens(User u) {
        var org = u.getOrganization();
        Map<String,Object> base = Map.of(
                "uid", u.getId().toString(),
                "org_id", org != null ? org.getId().toString() : null,
                "org_code", org != null ? org.getCode() : null,
                "email", u.getEmail(),
                "role", u.getRole().name()
        );
        String access  = sign(base, "access", 3600);
        String refresh = sign(base, "refresh", 604800);
        return new Tokens(access, refresh, 3600);
    }

    private String sign(Map<String,Object> baseClaims, String type, long ttlSeconds) {
        var now = Instant.now();
        var kp = keyRepo.findCurrent(now)
                .orElseThrow(() -> new ExceptionInInitializerError("No active signing key"));
        PrivateKey privateKey = PemUtils.readPrivateKeyFromPem(kp.getPrivatePem());

        Map<String,Object> claims = new HashMap<>(baseClaims);
        claims.put("typ", type);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject((String) baseClaims.getOrDefault("email",""))
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(ttlSeconds)))
                .setHeaderParam("kid", kp.getKid())
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    // === Verify ===
    public Jws<Claims> parseAndValidate(String jwt) {
        // Parse token đầy đủ (bao gồm header & payload)
        Jws<Claims> jws = Jwts.parserBuilder()
                .setAllowedClockSkewSeconds(60)
                .build()
                .parseClaimsJws(jwt);

        String kid = jws.getHeader().getKeyId();

        var key = keyRepo.findByKid(kid).orElseGet(() ->
                keyRepo.findCurrent(Instant.now()).orElseThrow(() ->
                        new IllegalStateException("No active key for verification")));

        PublicKey pub = PemUtils.readPublicKeyFromPem(key.getPublicPem());

        return Jwts.parserBuilder()
                .setSigningKey(pub)
                .setAllowedClockSkewSeconds(60)
                .build()
                .parseClaimsJws(jwt);
    }


    public boolean isAccessToken(Jws<Claims> jws) {
        return "access".equals(jws.getBody().get("typ", String.class));
    }
    public boolean isRefreshToken(Jws<Claims> jws) {
        return "refresh".equals(jws.getBody().get("typ", String.class));
    }

    public record Tokens(String accessToken, String refreshToken, long accessExpiresIn) {}
}
