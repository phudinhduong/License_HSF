package hsf302.he187383.phudd.license.DTOs.license;

import hsf302.he187383.phudd.license.enums.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LicenseResponse {
    private UUID id;
    private UUID orgId;
    private UUID productId;
    private UUID planId;
    private String licenseKey;
    private LicenseType type;
    private LicenseStatus status;
    private Instant startAt;
    private Instant endAt;
    private Integer maxDevices;
    private Integer maxConcurrent;
    private String buildMin;
    private String buildMax;
    private String signedPayloadJson;
    private String metadataJson;
    private UUID createdBy;
    private Instant createdAt;
    private Instant updatedAt;
}
