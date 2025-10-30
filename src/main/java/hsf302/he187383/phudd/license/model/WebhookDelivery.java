package hsf302.he187383.phudd.license.model;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="webhook_deliveries")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class WebhookDelivery extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="webhook_id", nullable=false)
    private Webhook webhook;

    @Column(length = 64, nullable = false)
    private String eventType;

    @Lob @Column(columnDefinition = "nvarchar(max) not null")
    private String payload; // JSON

    private Integer statusCode;
    private java.time.Instant deliveredAt;
    @Column(nullable = false) private Integer retryCount = 0;

    @Lob @Column(columnDefinition = "nvarchar(max)")
    private String lastError;
}
