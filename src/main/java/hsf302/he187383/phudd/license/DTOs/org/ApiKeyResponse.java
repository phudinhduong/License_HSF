package hsf302.he187383.phudd.license.DTOs.org;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ApiKeyResponse {
    private UUID id;
    private UUID orgId;
    private String name;
    private String maskedKey; // chỉ hiển thị **** tail
    private String scopesJson;
    private Instant createdAt;
    private Instant revokedAt;
}
