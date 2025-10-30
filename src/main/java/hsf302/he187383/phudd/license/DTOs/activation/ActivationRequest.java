package hsf302.he187383.phudd.license.DTOs.activation;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;
import java.util.Map;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ActivationRequest {
    @NotBlank private String licenseKey;
    @NotBlank private String deviceFingerprint;
    private String deviceName;
    private String appVersion;
}
