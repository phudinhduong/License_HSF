package hsf302.he187383.phudd.license.service;

import hsf302.he187383.phudd.license.enums.*;
import hsf302.he187383.phudd.license.model.*;
import hsf302.he187383.phudd.license.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class ActivationService {

    private final LicenseRepository licenseRepo;
    private final DeviceRepository deviceRepo;
    private final LicenseActivationRepository actRepo;
    private final HeartbeatRepository hbRepo;
    private final TokenService tokenService;

    public ActivationService(LicenseRepository licenseRepo, DeviceRepository deviceRepo,
                             LicenseActivationRepository actRepo, HeartbeatRepository hbRepo,
                             TokenService tokenService) {
        this.licenseRepo = licenseRepo; this.deviceRepo = deviceRepo;
        this.actRepo = actRepo; this.hbRepo = hbRepo; this.tokenService = tokenService;
    }

    @Transactional
    public ActivationResult activate(String licenseKey, String fingerprint, String deviceName, String appVersion,
                                     long tokenTtlSeconds) {
        License lic = licenseRepo.findByLicenseKey(licenseKey)
                .orElseThrow(() -> new ExceptionInInitializerError("License not found"));

        if (lic.getStatus() == LicenseStatus.REVOKED) throw new ExceptionInInitializerError("License revoked");
        if (lic.getStatus() == LicenseStatus.SUSPENDED) throw new ExceptionInInitializerError("License suspended");
        if (lic.getEndAt()!=null && lic.getEndAt().isBefore(Instant.now()))
            throw new ExceptionInInitializerError("License expired");

        // device
        Device dev = deviceRepo.findByOrganizationIdAndFingerprint(lic.getOrganization().getId(), fingerprint)
                .orElseGet(() -> {
                    Device d = Device.builder()
                            .organization(lic.getOrganization())
                            .fingerprint(fingerprint)
                            .name(deviceName)
                            .os(null)
                            .firstSeen(Instant.now())
                            .build();
                    return deviceRepo.save(d);
                });
        if (Boolean.TRUE.equals(dev.getBlocked())) throw new ExceptionInInitializerError("Device blocked");

        // device limit
        long activeDevices = actRepo.countActiveByLicenseId(lic.getId());
        boolean alreadyActivatedOnThisDevice =
                actRepo.findByLicenseIdAndDeviceId(lic.getId(), dev.getId()).isPresent();

        if (!alreadyActivatedOnThisDevice && activeDevices >= lic.getMaxDevices())
            throw new ExceptionInInitializerError("Max devices reached");

        // create or reuse activation
        LicenseActivation act = actRepo.findByLicenseIdAndDeviceId(lic.getId(), dev.getId())
                .orElseGet(() -> {
                    LicenseActivation a = LicenseActivation.builder()
                            .license(lic).device(dev)
                            .status(ActivationStatus.ACTIVE)
                            .activatedAt(Instant.now())
                            .appVersion(appVersion)
                            .build();
                    return actRepo.save(a);
                });
        act.setLastHeartbeatAt(Instant.now());
        act.setAppVersion(appVersion);

        // build token claims
        Map<String,Object> claims = new HashMap<>();
        claims.put("lic_id", lic.getId().toString());
        claims.put("act_id", act.getId().toString());
        claims.put("org_id", lic.getOrganization().getId().toString());
        claims.put("features", lic.getPlan().getFeatures()); // JSON string cũng OK

//        String jwt = tokenService.signActivationToken(claims, tokenTtlSeconds);

        String jwt = "";
        return new ActivationResult(act.getId(), jwt, tokenTtlSeconds);
    }

    @Transactional
    public HeartbeatStatus heartbeat(LicenseActivation activation, String appVersion, String metaJson, String ip){
        License lic = activation.getLicense();
        if (lic.getStatus() == LicenseStatus.REVOKED) return HeartbeatStatus.REVOKED;
        if (lic.getStatus() == LicenseStatus.SUSPENDED) return HeartbeatStatus.SUSPENDED;
        if (lic.getEndAt()!=null && lic.getEndAt().isBefore(Instant.now())) return HeartbeatStatus.EXPIRED;

        activation.setLastHeartbeatAt(Instant.now());
        activation.setAppVersion(appVersion);

        Heartbeat hb = new Heartbeat();
        hb.setActivation(activation);
        hb.setAppVersion(appVersion);
        hb.setIp(ip);
        hb.setMeta(metaJson == null ? "{}" : metaJson);
        hbRepo.save(hb);

        return HeartbeatStatus.OK;
    }

    public record ActivationResult(java.util.UUID activationId, String token, long expiresIn) {}
}
