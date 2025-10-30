package hsf302.he187383.phudd.license.DTOs.product;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductResponse {
    private UUID id;
    private String code;
    private String name;
    private String currentVersion;
    private Instant createdAt;
    private Instant updatedAt;
}
