package hsf302.he187383.phudd.license.dto.wallet;

import hsf302.he187383.phudd.license.enums.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class WalletAdjustReq {
    @NotNull
    private UUID walletId;

    @NotNull
    private Long delta; // + nạp / - trừ

    @NotBlank
    private String reason; // ghi sổ
}