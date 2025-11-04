package hsf302.he187383.phudd.license.dto.wallet;

import hsf302.he187383.phudd.license.enums.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class WalletResp {
    private UUID id;
    private UUID userId;
    private Long balance;
    private WalletStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}
