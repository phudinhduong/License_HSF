package hsf302.he187383.phudd.licensev3.model;

import hsf302.he187383.phudd.licensev3.enums.LicenseStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "licenses",
        uniqueConstraints = @UniqueConstraint(name = "uk_licenses_license_key", columnNames = "license_key"))
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(of = "id")
public class License extends BaseEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_license_order"))
    private Order order;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_license_user"))
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_license_plan"))
    private Plan plan;

    @Column(name = "license_key", nullable = false, length = 128)
    private String licenseKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private LicenseStatus status;

    @Column(nullable = false)
    private Instant issuedAt;

    @Column
    private Instant expiresAt; // null nếu perpetual

    @Column
    private Integer seatsTotal;

    @Column(nullable = false)
    private Integer seatsUsed = 0;
}
