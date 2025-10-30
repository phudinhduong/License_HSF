package hsf302.he187383.phudd.license.DTOs.activation;

import lombok.*;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ActivationResponse {
    private UUID activationId;
    private String activationToken; // JWT
    private long expiresIn;
    private Map<String, Object> features; // parsed từ plan.features nếu muốn
}
