package hsf302.he187383.phudd.license.dto.plan;

import hsf302.he187383.phudd.license.enums.BillingType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PlanResp {
    private UUID id;
    private UUID productId;
    private String code;
    private String name;
    private BillingType billingType;
    private Long priceCredits;
    private String currency;
    private Integer durationDays;
    private Integer seats;
    private Integer concurrentLimitPerAccount;
    private Integer deviceLimitPerAccount;
    private Instant createdAt;
    private Instant updatedAt;
}
