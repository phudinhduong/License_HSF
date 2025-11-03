package hsf302.he187383.phudd.license.dto.auth;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RefreshReq {
    @NotBlank
    private String refreshToken;
}
