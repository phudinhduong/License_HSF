package hsf302.he187383.phudd.license.DTOs.webhook;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class WebhookDeliveryResponse {
    private UUID id;
    private UUID webhookId;
    private String eventType;
    private Integer statusCode;
    private Instant deliveredAt;
    private Integer retryCount;
    private String lastError;
}
