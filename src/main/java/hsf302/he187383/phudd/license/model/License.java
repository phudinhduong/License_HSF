package hsf302.he187383.phudd.license.model;

import hsf302.he187383.phudd.license.enums.*;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity @Table(name="licenses",
        indexes = {
                @Index(name="idx_licenses_org", columnList="org_id"),
                @Index(name="idx_licenses_product", columnList="product_id"),
                @Index(name="idx_licenses_status", columnList="status"),
                @Index(name="idx_licenses_end_at", columnList="endAt")
        })
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class License extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="org_id", nullable=false)
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="product_id", nullable=false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="plan_id", nullable=false)
    private Plan plan;

    @Column(length = 128, nullable = false, unique = true)
    private String licenseKey;

    @Enumerated(EnumType.STRING)
    @Column(length = 32, nullable = false)
    private LicenseType type;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private LicenseStatus status = LicenseStatus.ACTIVE;

    @Column(nullable = false)
    private Instant startAt = Instant.now();

    private Instant endAt; // null for perpetual

    @Column(nullable = false)
    private Integer maxDevices = 1;

    @Column(nullable = false)
    private Integer maxConcurrent = 1;

    @Column(length = 32) private String buildMin;
    @Column(length = 32) private String buildMax;

    @Lob @Column(columnDefinition = "nvarchar(max)")
    private String signedPayload; // JSON

    @Lob @Column(columnDefinition = "nvarchar(max) not null")
    private String metadata = "{}"; // JSON

    @Column(columnDefinition = "uniqueidentifier")
    private UUID createdBy; // user id (FK optional)
}
