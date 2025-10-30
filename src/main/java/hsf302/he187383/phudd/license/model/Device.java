package hsf302.he187383.phudd.license.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity @Table(name="devices",
        uniqueConstraints = @UniqueConstraint(name="uq_device_fp", columnNames = {"org_id","fingerprint"}),
        indexes = @Index(name="idx_devices_org", columnList="org_id"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Device extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="org_id", nullable=false)
    private Organization organization;

    @Column(length = 255)
    private String name;

    @Column(length = 128, nullable = false)
    private String fingerprint;

    @Column(length = 64)
    private String os;

    @Column(nullable = false)
    private Instant firstSeen = Instant.now();

    private Instant lastSeen;

    @Column(nullable = false)
    private Boolean blocked = false;

    @Lob @Column(columnDefinition = "nvarchar(max) not null")
    private String metadata = "{}"; // JSON
}
