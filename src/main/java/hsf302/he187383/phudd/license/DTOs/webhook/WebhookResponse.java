package hsf302.he187383.phudd.license.DTOs.webhook;

import lombok.*;

import java.time.Instant;
import java.util.*;

@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class WebhookResponse {
    private UUID id;
    private UUID orgId;
    private String url;
    private String eventsJson;
    private Boolean isActive;
    private Instant createdAt;
    private Instant updatedAt;
}
