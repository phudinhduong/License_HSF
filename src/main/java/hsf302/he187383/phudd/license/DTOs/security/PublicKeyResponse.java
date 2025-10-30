package hsf302.he187383.phudd.license.DTOs.security;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PublicKeyResponse {
    private String kid;
    private String algorithm; // "RS256"
    private String publicPem;
    private Instant activeFrom;
    private Instant activeTo;
}