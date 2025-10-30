package hsf302.he187383.phudd.license.DTOs.product;

import hsf302.he187383.phudd.license.enums.PlanType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PlanCreateRequest {
    @NotNull
    private UUID productId;
    @NotBlank private String code;
    @NotBlank private String name;
    @NotNull private PlanType type;
    private Integer durationDays; // null if PERPETUAL
    @Min(0) private Integer maxDevices = 1;
    @Min(0) private Integer maxConcurrent = 1;
    @NotBlank private String featuresJson; // JSON
    private Boolean isActive = true;
}
