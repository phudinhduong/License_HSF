package hsf302.he187383.phudd.licensev3.model;

import hsf302.he187383.phudd.licensev3.enums.BillingType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "plans",
        uniqueConstraints = @UniqueConstraint(name = "uk_plans_product_code", columnNames = {"product_id", "code"}))
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(of = "id")
public class Plan extends BaseEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_plan_product"))
    private Product product;

    @Column(nullable = false, length = 64)
    private String code;

    @Column(nullable = false, length = 255)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private BillingType billingType;

    @Column(nullable = false)
    private Long priceCredits;

    @Column(length = 8)
    private String currency = "USD";

    @Column
    private Integer durationDays; // null/0 nếu perpetual

    @Column
    private Integer seats;

    @Column
    private Integer concurrentLimitPerAccount;

    @Column
    private Integer deviceLimitPerAccount;
}
