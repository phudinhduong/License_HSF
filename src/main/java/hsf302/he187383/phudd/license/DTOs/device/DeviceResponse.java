package hsf302.he187383.phudd.license.DTOs.device;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DeviceResponse {
    private UUID id;
    private UUID orgId;
    private String name;
    private String fingerprint;
    private String os;
    private Instant firstSeen;
    private Instant lastSeen;
    private Boolean blocked;
    private String metadataJson;
}
