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

    @Size(max = 64)
    private String code;

    @Size(max = 255)
    private String name;

    private BillingType billingType;

    @PositiveOrZero
    private Long priceCredits;

    @Size(max = 8)
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
