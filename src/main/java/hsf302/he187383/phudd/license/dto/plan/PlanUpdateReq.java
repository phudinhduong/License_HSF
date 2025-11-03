package hsf302.he187383.phudd.license.dto.plan;

import hsf302.he187383.phudd.license.enums.BillingType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PlanUpdateReq {
    @NotNull
    private UUID id;

    @NotNull
    private UUID productId;

    @NotBlank @Size(max = 64)
    private String code;

    @NotBlank @Size(max = 255)
    private String name;

    @NotNull
    private BillingType billingType;

    @NotNull @PositiveOrZero
    private Long priceCredits;

    @NotBlank @Size(max = 8)
    private String currency;

    @PositiveOrZero
    private Integer durationDays;

    @PositiveOrZero
    private Integer seats;

    @PositiveOrZero
    private Integer concurrentLimitPerAccount;

    @PositiveOrZero
    private Integer deviceLimitPerAccount;
}
