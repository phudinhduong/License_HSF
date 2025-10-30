package hsf302.he187383.phudd.license.DTOs.webhook;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class WebhookCreateRequest {
    private UUID orgId; // optional, null = global
    @NotBlank @Size(max = 1024) private String url;
    @NotBlank private String secret;
    @NotBlank private String eventsJson; // ["license.created","license.expired"]
    private Boolean isActive = true;
}
