package hsf302.he187383.phudd.license.DTOs.device;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DeviceBlockRequest {
    @NotNull
    private Boolean blocked;
    private String reason;
}
