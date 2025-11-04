package hsf302.he187383.phudd.license.dto.accountsession;

import hsf302.he187383.phudd.license.enums.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SessionResp {
    private UUID id;
    private UUID accountId;
    private UUID licenseId;
    private String jti;
    private String tokenHash;
    private String deviceId;
    private String ip;
    private String userAgent;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant createdAtSession;
    private Instant lastSeenAt;
    private Instant expiresAt;
    private SessionStatus status;
}
