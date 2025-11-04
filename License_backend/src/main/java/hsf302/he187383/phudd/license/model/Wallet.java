package hsf302.he187383.phudd.license.model;

import hsf302.he187383.phudd.license.enums.WalletStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "wallets",
        uniqueConstraints = @UniqueConstraint(name = "uk_wallets_user", columnNames = "user_id"))
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(of = "id")
public class Wallet extends BaseEntity {

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_wallet_user"))
    private User user;

    @Column(nullable = false)
    private Long balance = 0L;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private WalletStatus status;
}
