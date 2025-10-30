package hsf302.he187383.phudd.license.DTOs.auth;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private long expiresIn; // seconds
    private String tokenType; // "Bearer"
}
