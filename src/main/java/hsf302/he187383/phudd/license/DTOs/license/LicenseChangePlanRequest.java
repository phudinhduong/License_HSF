package hsf302.he187383.phudd.license.DTOs.license;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LicenseChangePlanRequest {
    @NotNull
    private UUID newPlanId;
    private Boolean keepLimits; // giữ maxDevices/maxConcurrent hiện tại
}
