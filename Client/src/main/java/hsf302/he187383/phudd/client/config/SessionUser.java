package hsf302.he187383.phudd.client.config;

import lombok.*;

import java.time.Instant;

@Getter @Setter @Builder
public class SessionUser {
    private String jti;
    private String deviceId;
    private String accountId;
    private String username;

    // License summary fields
    private String licenseId;
    private String licenseStatus;
    private String productCode;
    private String productName;
    private String planCode;
    private String planName;
    private String billingType;
    private String expiresAt;

    private Instant lastHeartbeatAt;
}
