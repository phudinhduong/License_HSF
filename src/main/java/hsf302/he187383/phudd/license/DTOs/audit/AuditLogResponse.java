package hsf302.he187383.phudd.license.DTOs.audit;

import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuditLogResponse {
    private UUID id;
    private UUID actorId;
    private UUID orgId;
    private String action;
    private String entityType;
    private UUID entityId;
    private String detailsJson;
    private Instant createdAt;
}
