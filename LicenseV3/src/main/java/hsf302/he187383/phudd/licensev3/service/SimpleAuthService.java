package hsf302.he187383.phudd.licensev3.service;

import hsf302.he187383.phudd.licensev3.config.AuthProperties;
import hsf302.he187383.phudd.licensev3.enums.*;
import hsf302.he187383.phudd.licensev3.model.*;
import hsf302.he187383.phudd.licensev3.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class SimpleAuthService {

    private final AccountRepository accountRepo;
    private final AccountSessionRepository sessionRepo;
    private final AuthProperties props;
    private final PasswordEncoder passwordEncoder;

    public record LoginResult(
            //id của session
            String jti,
            String deviceId,
            Account account,
            License license,
            Plan plan,
            Product product,
            Instant sessionExpiresAt
    ) {}

    public LoginResult login(String username,
                             String password,
                             String deviceId,
                             String deviceName,
                             boolean preempt,
                             String ip,
                             String userAgent) {

        var acc = accountRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("No user"));

        if (acc.getStatus() != AccountStatus.ACTIVE)
            throw new IllegalArgumentException("Disabled");

//        if (!passwordEncoder.matches(password, acc.getPasswordHash()))
//            throw new IllegalArgumentException("invalid_grant");

        var lic = acc.getLicense();
        if (lic.getStatus() != LicenseStatus.ACTIVE)
            throw new IllegalArgumentException("License Dead");

        var now = Instant.now();
        if (lic.getExpiresAt() != null && !lic.getExpiresAt().isAfter(now))
            throw new IllegalArgumentException("License Expired");





        var plan = lic.getPlan();
        var product = plan.getProduct();

        // Giới hạn concurrent/device
        var since = now.minusSeconds(props.getConcurrentWindowSeconds());
        long activeSessions = sessionRepo.countActiveSessions(acc.getId(), since, now);
        Integer ccLimit = plan.getConcurrentLimitPerAccount();

        //nếu concurrent > limit
        //Revoke thằng đầu tiên
        if (ccLimit != null && ccLimit > 0 && activeSessions >= ccLimit) {
            if (preempt) {
                var victims = sessionRepo.findActiveForPreempt(acc.getId(), since, now);
                var victim = victims.stream().findFirst().orElse(null);
                if (victim != null) {
                    victim.setStatus(SessionStatus.REVOKED);
                    victim.setExpiresAt(now);
                    sessionRepo.save(victim);
                }
            } else {
                throw new IllegalArgumentException("concurrent_limit");
            }
        }

        //sinh random device id
        String finalDeviceId = (deviceId == null || deviceId.isBlank())
                ? ("DEV-" + UUID.randomUUID()) : deviceId;

        if (plan.getDeviceLimitPerAccount() != null){
            if (sessionRepo.countActivedDevices(acc.getId(), since, now) >= plan.getDeviceLimitPerAccount()) {
                throw new IllegalArgumentException("device per account limit");
            }
        }

//        if (plan.getDeviceLimitPerAccount() != null && plan.getDeviceLimitPerAccount() > 0) {
//
//            long activeDevices = sessionRepo.countActivedDevices(acc.getId(), since, now);
//            System.out.println(activeDevices+"=========" + plan.getDeviceLimitPerAccount());
//            //quá giới hạn thiết bị đã kích hoạt thì say no
//            if (activeDevices+1 >= plan.getDeviceLimitPerAccount()) {
//                if (!preempt) throw new IllegalArgumentException("device_limit");
//                var victims = sessionRepo.findActiveForPreempt(acc.getId(), since, now);
//                var victim = victims.stream().findFirst().orElse(null);
//                if (victim != null) {
//                    victim.setStatus(SessionStatus.REVOKED);
//                    victim.setExpiresAt(now);
//                    sessionRepo.save(victim);
//                }
//            }
//        }


        String jti = UUID.randomUUID().toString();
        var session = AccountSession.builder()
                .account(acc)
                .license(lic)
                .jti(jti)
                .tokenHash("")               // không dùng, giữ trống
                .deviceId(finalDeviceId)
                .ip(ip)
                .userAgent(userAgent)
                .createdAtSession(now)
                .lastSeenAt(now)
                .expiresAt(now.plusSeconds(props.getSessionTtlSeconds()))
                .status(SessionStatus.ACTIVE)
                .build();
        sessionRepo.save(session);

        return new LoginResult(jti, finalDeviceId, acc, lic, plan, product, session.getExpiresAt());
    }


    public void heartbeat(String jti, String deviceId) {
        var s = sessionRepo.findByJti(jti)
                .orElseThrow(() -> new IllegalArgumentException("token_revoked"));

        var now = Instant.now();
        if (s.getStatus() != SessionStatus.ACTIVE)
            throw new IllegalArgumentException("token_revoked");

        if (s.getExpiresAt() != null && !s.getExpiresAt().isAfter(now)) {
            s.setStatus(SessionStatus.EXPIRED);
            sessionRepo.save(s);
            throw new IllegalArgumentException("token_expired");
        }
        if (deviceId != null && s.getDeviceId() != null && !Objects.equals(deviceId, s.getDeviceId()))
            throw new IllegalArgumentException("device_mismatch");

        s.setLastSeenAt(now);
        s.setExpiresAt(now.plusSeconds(props.getSessionTtlSeconds())); // sliding
        sessionRepo.save(s);
    }

    public void logout(String jti) {
        var s = sessionRepo.findByJti(jti)
                .orElseThrow(() -> new IllegalArgumentException("token_revoked"));
        s.setStatus(SessionStatus.REVOKED);
        s.setExpiresAt(Instant.now());
        sessionRepo.save(s);
    }
}
