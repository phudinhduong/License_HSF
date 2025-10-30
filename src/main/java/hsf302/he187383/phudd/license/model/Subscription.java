package hsf302.he187383.phudd.license.model;

import hsf302.he187383.phudd.license.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="subscriptions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Subscription extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="org_id", nullable=false)
    private Organization organization;

    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name="license_id", unique = true)
    private License license;

    @Column(length = 32, nullable = false)
    private String provider; // STRIPE / PAYPAL / OTHER

    @Column(length = 128, nullable = false)
    private String externalId;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private SubscriptionStatus status;

    private java.time.Instant currentPeriodEnd;
}
