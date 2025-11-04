package hsf302.he187383.phudd.license.dto.plan;

import hsf302.he187383.phudd.license.enums.BillingType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PlanCreateReq {
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
    private String currency = "USD";

    @PositiveOrZero
    private Integer durationDays; // null/0 nếu PERPETUAL

    @PositiveOrZero
    private Integer seats;

    @PositiveOrZero
    private Integer concurrentLimitPerAccount;

    @PositiveOrZero
    private Integer deviceLimitPerAccount;
}
