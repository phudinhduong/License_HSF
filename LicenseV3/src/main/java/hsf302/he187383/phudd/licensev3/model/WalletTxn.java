package hsf302.he187383.phudd.licensev3.model;

import hsf302.he187383.phudd.licensev3.enums.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "wallet_txns",
        uniqueConstraints = @UniqueConstraint(name = "uk_wallet_txns_idem", columnNames = "idempotency_key"))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class WalletTxn extends BaseEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_txn_wallet"))
    private Wallet wallet;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private WalletTxnType type; // TOPUP / PURCHASE / REFUND / WITHDRAW

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 8)
    private WalletTxnDirection direction; // IN  / OUT

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    private Long balanceAfter;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private WalletTxnStatus status; // PENDING / COMPLETED / FAILED / REVERSED

    @Column(name = "ref_type", length = 64)
    private Topup_Ref_Type refType; // Loại liên kết: "TOPUP", "ORDER", "REFUND"...

    @Column(name = "ref_id", columnDefinition = "uniqueidentifier")
    private UUID refId; // ID của bản ghi tham chiếu (VD: Topup ID, Order ID)

    @Column(name = "idempotency_key", length = 128, nullable = false)
    private String idempotencyKey; // Đảm bảo không trùng lặp giao dịch

}
