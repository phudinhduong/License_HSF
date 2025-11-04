package hsf302.he187383.phudd.license.dto.accountsession;

import hsf302.he187383.phudd.license.enums.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SessionCreateReq {
    @NotNull
    private UUID accountId;

    @NotNull
    private UUID licenseId;

    @NotBlank @Size(max = 128)
    private String jti;

    @NotBlank @Size(max = 256)
    private String tokenHash;

    @Size(max = 64)
    private String deviceId;

    @Size(max = 45)
    private String ip;

    @Size(max = 255)
    private String userAgent;

    private Instant expiresAt;

    @NotNull
    private SessionStatus status; // thường = ACTIVE
}
