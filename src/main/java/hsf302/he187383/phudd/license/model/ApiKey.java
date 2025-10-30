package hsf302.he187383.phudd.license.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="api_keys",
        uniqueConstraints = @UniqueConstraint(name="uq_api_key_name", columnNames = {"org_id","name"}),
        indexes = @Index(name="idx_api_keys_org", columnList="org_id"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ApiKey extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="org_id")
    private Organization organization;

    @Column(length = 128, nullable = false)
    private String name;

    @Column(length = 255, nullable = false)
    private String hashedKey;

    @Lob
    @Column(columnDefinition = "nvarchar(max)")
    private String scopes; // JSON text

    private java.time.Instant revokedAt;
}
