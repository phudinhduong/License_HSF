package hsf302.he187383.phudd.license.DTOs.license;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LicenseRenewRequest {
    @NotNull
    @Min(1) private Integer addDays;
}