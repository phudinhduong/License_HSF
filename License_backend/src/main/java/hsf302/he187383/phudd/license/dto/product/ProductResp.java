package hsf302.he187383.phudd.license.dto.product;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductResp {
    private UUID id;
    private String code;
    private String name;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
}
