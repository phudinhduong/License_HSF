package hsf302.he187383.phudd.license.model;

import hsf302.he187383.phudd.license.enums.PlanType;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="plans",
        uniqueConstraints = @UniqueConstraint(name="uq_plan_code", columnNames = {"product_id","code"}),
        indexes = @Index(name="idx_plans_product", columnList="product_id"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Plan extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="product_id", nullable=false)
    private Product product;

    @Column(length = 64, nullable = false)
    private String code;

    @Column(length = 255, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(length = 32, nullable = false)
    private PlanType type;

    private Integer durationDays; // null if perpetual

    @Column(nullable = false)
    private Integer maxDevices = 1;

    @Column(nullable = false)
    private Integer maxConcurrent = 1;

    @Lob @Column(columnDefinition = "nvarchar(max) not null")
    private String features = "{}"; // JSON

    @Column(nullable = false)
    private Boolean isActive = true;
}
