package hsf302.he187383.phudd.license.dto.topup;

import hsf302.he187383.phudd.license.enums.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TopupResp {
    private UUID id;
    private UUID userId;
    private PaymentProvider provider;
    private BigDecimal moneyAmount;
    private String currency;
    private Long creditsGranted;
    private TopupStatus status;
    private String paymentRef;
    private Instant createdAt;
    private Instant updatedAt;
}
