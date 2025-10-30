package hsf302.he187383.phudd.license.DTOs.license;

import hsf302.he187383.phudd.license.enums.LicenseType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LicenseCreateRequest {
    @NotNull
    private UUID orgId;
    @NotNull private UUID productId;
    @NotNull private UUID planId;
    @NotBlank private String licenseKey;
    @NotNull private LicenseType type;
    private Instant startAt;       // optional
    private Instant endAt;         // null if perpetual
    @Min(0) private Integer maxDevices = 1;
    @Min(0) private Integer maxConcurrent = 1;
    private String buildMin;
    private String buildMax;
    private String signedPayloadJson; // optional
    private String metadataJson = "{}";
}
