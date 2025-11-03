package hsf302.he187383.phudd.license.dto.license;

import hsf302.he187383.phudd.license.enums.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LicenseUpdateReq {
    @NotNull
    private UUID id;

    @NotNull
    private LicenseStatus status;

    private Instant expiresAt;

    @PositiveOrZero
    private Integer seatsTotal;

    @PositiveOrZero
    private Integer seatsUsed;
}
