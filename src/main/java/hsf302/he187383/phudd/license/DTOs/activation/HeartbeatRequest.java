package hsf302.he187383.phudd.license.DTOs.activation;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class HeartbeatRequest {
    @NotBlank
    private String appVersion;
    private String metaJson; // optional
}
