package hsf302.he187383.phudd.license.dto.order;

import hsf302.he187383.phudd.license.enums.OrderStatus;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderResp {
    private UUID id;
    private UUID userId;
    private UUID planId;
    private Long priceCredits;
    private OrderStatus status;
    private String paymentRef;
    private UUID walletTxnId;
    private Instant createdAt;
    private Instant updatedAt;
}