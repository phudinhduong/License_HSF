package hsf302.he187383.phudd.license.dto.license;

import hsf302.he187383.phudd.license.enums.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LicenseResp {
    private UUID id;
    private UUID orderId;
    private UUID userId;
    private UUID planId;
    private String licenseKey;
    private LicenseStatus status;
    private Instant issuedAt;
    private Instant expiresAt;
    private Integer seatsTotal;
    private Integer seatsUsed;
    private Instant createdAt;
    private Instant updatedAt;
}
