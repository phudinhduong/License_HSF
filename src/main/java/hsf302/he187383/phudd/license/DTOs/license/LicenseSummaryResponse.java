package hsf302.he187383.phudd.license.DTOs.license;

import hsf302.he187383.phudd.license.enums.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LicenseSummaryResponse {
    private UUID id;
    private String licenseKey;
    private LicenseStatus status;
    private Instant endAt;
    private Integer activeDevices;
}
