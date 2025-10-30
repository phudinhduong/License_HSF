package hsf302.he187383.phudd.license.DTOs.webhook;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class WebhookUpdateRequest {
    private String url;
    private String secret;
    private String eventsJson;
    private Boolean isActive;
}
