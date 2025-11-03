package hsf302.he187383.phudd.license.dto.topup;

import hsf302.he187383.phudd.license.enums.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TopupCreateReq {
    @NotNull
    private PaymentProvider provider;

    @NotNull @Digits(integer = 19, fraction = 4)
    @Positive
    private BigDecimal moneyAmount;

    @NotBlank @Size(max = 8)
    private String currency;

    @NotNull @Positive
    private Long creditsGranted;

    @Size(max = 128)
    private String paymentRef; // nếu có từ gateway
}
