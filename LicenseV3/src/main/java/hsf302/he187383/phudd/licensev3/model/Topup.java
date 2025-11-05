package hsf302.he187383.phudd.licensev3.model;

import hsf302.he187383.phudd.licensev3.enums.*;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "topups")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(of = "id")
public class Topup extends BaseEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_topup_user"))
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private PaymentProvider provider;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal moneyAmount;

    @Column(nullable = false, length = 8)
    private String currency;

    @Column(nullable = false)
    private Long creditsGranted;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private TopupStatus status;

    @Column(length = 128)
    private String paymentRef;
}
