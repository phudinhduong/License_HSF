package hsf302.he187383.phudd.license.DTOs.product;

import hsf302.he187383.phudd.license.enums.PlanType;
import lombok.*;

@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PlanUpdateRequest {
    private String name;
    private PlanType type;
    private Integer durationDays;
    private Integer maxDevices;
    private Integer maxConcurrent;
    private String featuresJson;
    private Boolean isActive;
}
