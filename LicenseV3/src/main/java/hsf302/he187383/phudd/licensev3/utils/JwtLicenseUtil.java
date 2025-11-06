package hsf302.he187383.phudd.licensev3.utils;

import hsf302.he187383.phudd.licensev3.enums.LicenseStatus;
import hsf302.he187383.phudd.licensev3.model.License;
import hsf302.he187383.phudd.licensev3.repository.LicenseRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JwtLicenseUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtLicenseUtil.class);
    private static final String SECRET_KEY = "my-super-secret-license-key-should-be-at-least-64-characters-long-!!!!!";

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    @Autowired
    private LicenseRepository licenseRepo;

    public String generateAccessToken(License license) {
        ZoneId zone = ZoneId.of("Asia/Ho_Chi_Minh");
        ZonedDateTime expiryZoned;

        if (license.getExpiresAt() != null) {
            expiryZoned = license.getExpiresAt().atZone(zone);
        } else {
            expiryZoned = ZonedDateTime.now(zone).plusDays(30);
            license.setExpiresAt(expiryZoned.toInstant());
            licenseRepo.save(license); // lưu vào DB
        }

        Date expiryDate = Date.from(expiryZoned.toInstant()); // dùng cho JWT


        return Jwts.builder()
                .setSubject(license.getLicenseKey())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .claim("userId", license.getUser().getId())
                .claim("planId", license.getPlan().getName())
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }


    public Jws<Claims> validateLicenseToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            String appId = claims.getBody().get("appId", String.class);
            String licenseKey = claims.getBody().getSubject();

            License license = licenseRepo.findByLicenseKey(licenseKey)
                    .orElseThrow(() -> new RuntimeException("License not found in DB"));

            if (!license.getPlan().getProduct().getId().toString().equals(appId)) {
                throw new RuntimeException("License invalid: appId mismatch");
            }

            if (license.getStatus() != LicenseStatus.ACTIVE) {
                throw new RuntimeException("License invalid: not active");
            }

            return claims;
        } catch (JwtException e) {
            throw new RuntimeException("Invalid or expired token: " + e.getMessage());
        }
    }

    public Instant getExpiration(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration().toInstant();
    }
}


