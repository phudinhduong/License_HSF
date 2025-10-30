package hsf302.he187383.phudd.license.model;

import hsf302.he187383.phudd.license.enums.ActivationStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity @Table(name="license_activations",
        uniqueConstraints = @UniqueConstraint(name="uq_activation", columnNames = {"license_id","device_id"}),
        indexes = {
                @Index(name="idx_activations_license", columnList="license_id"),
                @Index(name="idx_activations_device", columnList="device_id")
        })
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LicenseActivation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="license_id", nullable=false)
    private License license;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="device_id", nullable=false)
    private Device device;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private ActivationStatus status = ActivationStatus.ACTIVE;

    @Column(nullable = false)
    private Instant activatedAt = Instant.now();

    private Instant deactivatedAt;

    private Instant lastHeartbeatAt;

    @Column(length = 64)
    private String appVersion;

    @Lob @Column(columnDefinition = "nvarchar(max) not null")
    private String meta = "{}"; // JSON
}
