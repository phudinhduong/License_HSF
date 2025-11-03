package hsf302.he187383.phudd.license.dto.order;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderPayReq {
    @NotNull
    private UUID orderId;

    @NotBlank @Size(max = 128)
    private String idempotencyKey;

    @NotBlank @Size(max = 128)
    private String paymentRef;
}