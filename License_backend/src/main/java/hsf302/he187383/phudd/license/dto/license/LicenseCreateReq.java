package hsf302.he187383.phudd.license.dto.license;

import hsf302.he187383.phudd.license.enums.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LicenseCreateReq {
    @NotNull
    private UUID orderId;

    @NotNull
    private UUID userId;

    @NotNull
    private UUID planId;

    @NotBlank @Size(max = 128)
    private String licenseKey;

    @NotNull
    private LicenseStatus status;

    @NotNull
    private Instant issuedAt;

    private Instant expiresAt; // optional nếu perpetual

    @PositiveOrZero
    private Integer seatsTotal;

    @PositiveOrZero
    private Integer seatsUsed; // thường = 0 khi phát hành
}
