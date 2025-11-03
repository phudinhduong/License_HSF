package hsf302.he187383.phudd.license.dto.wallet;

import hsf302.he187383.phudd.license.enums.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class WalletTxnResp {
    private UUID id;
    private UUID walletId;
    private WalletTxnType type;
    private WalletTxnDirection direction;
    private Long amount;
    private Long balanceAfter;
    private String status; // PENDING/COMPLETED/FAILED/REVERSED
    private String refType;
    private UUID refId;
    private String idempotencyKey;
    private Instant createdAt;
    private Instant updatedAt;
}
