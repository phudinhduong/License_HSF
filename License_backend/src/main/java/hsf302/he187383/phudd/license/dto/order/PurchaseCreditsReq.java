package hsf302.he187383.phudd.license.dto.order;

import jakarta.validation.constraints.*;
import lombok.*;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PurchaseCreditsReq {
    @NotNull
    private UUID planId;

    // để chống double charge (unique ở WalletTxn.idempotencyKey)
    @NotBlank @Size(max = 128)
    private String idempotencyKey;
}
