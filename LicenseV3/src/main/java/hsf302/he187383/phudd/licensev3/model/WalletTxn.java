package hsf302.he187383.phudd.licensev3.model;

import hsf302.he187383.phudd.licensev3.enums.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "wallet_txns",
        uniqueConstraints = @UniqueConstraint(name = "uk_wallet_txns_idem", columnNames = "idempotency_key"))
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(of = "id")
public class WalletTxn extends BaseEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_txn_wallet"))
    private Wallet wallet;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private WalletTxnType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 8)
    private WalletTxnDirection direction;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    private Long balanceAfter;

    @Column(nullable = false, length = 16)
    private String status; // PENDING/COMPLETED/FAILED/REVERSED

    @Column(name = "ref_type", length = 64)
    private String refType;

    @Column(name = "ref_id", columnDefinition = "uniqueidentifier")
    private UUID refId;

    @Column(name = "idempotency_key", length = 128, nullable = false)
    private String idempotencyKey;

    public void setId(UUID walletTxnId) {
        this.id = UUID.randomUUID();
    }

//    @Column(nullable = false)
//    private Instant createdAt = Instant.now();
}
