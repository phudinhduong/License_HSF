package hsf302.he187383.phudd.license.model;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="heartbeats",
        indexes = @Index(name="idx_heartbeats_activation", columnList="activation_id"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Heartbeat extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="activation_id", nullable=false)
    private LicenseActivation activation;

    @Column(nullable = false)
    private java.time.Instant receivedAt = java.time.Instant.now();

    @Column(length = 45)
    private String ip;

    @Column(length = 64)
    private String appVersion;

    @Lob @Column(columnDefinition = "nvarchar(max) not null")
    private String meta = "{}"; // JSON
}
