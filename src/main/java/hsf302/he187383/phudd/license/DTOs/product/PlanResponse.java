package hsf302.he187383.phudd.license.DTOs.product;

import hsf302.he187383.phudd.license.enums.PlanType;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PlanResponse {
    private UUID id;
    private UUID productId;
    private String code;
    private String name;
    private PlanType type;
    private Integer durationDays;
    private Integer maxDevices;
    private Integer maxConcurrent;
    private String featuresJson;
    private Boolean isActive;
    private Instant createdAt;
    private Instant updatedAt;
}
