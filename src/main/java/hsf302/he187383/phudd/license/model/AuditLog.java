package hsf302.he187383.phudd.license.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity @Table(name="audit_logs",
        indexes = @Index(name="idx_audit_org_time", columnList="orgId, createdAt"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuditLog extends BaseEntity {

    @Column(columnDefinition = "uniqueidentifier")
    private UUID actorId;     // user id (nullable)

    @Column(columnDefinition = "uniqueidentifier")
    private UUID orgId;       // org id (nullable)

    @Column(length = 64, nullable = false)
    private String action;    // e.g., license.created

    @Column(length = 64, nullable = false)
    private String entityType; // LICENSE/DEVICE/USER...

    @Column(columnDefinition = "uniqueidentifier")
    private UUID entityId;

    @Lob @Column(columnDefinition = "nvarchar(max) not null")
    private String details = "{}"; // JSON
}
