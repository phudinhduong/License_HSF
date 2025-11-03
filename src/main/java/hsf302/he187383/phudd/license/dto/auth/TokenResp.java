package hsf302.he187383.phudd.license.dto.auth;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TokenResp {
    private String accessToken;
    private String refreshToken;
    private long   expiresIn; // seconds for access token
    private String tokenType; // "Bearer"
}
