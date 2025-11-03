package hsf302.he187383.phudd.license.dto.order;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderCreateReq {
    @NotNull
    private UUID planId;
}
