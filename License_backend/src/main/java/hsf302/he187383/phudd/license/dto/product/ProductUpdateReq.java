package hsf302.he187383.phudd.license.dto.product;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductUpdateReq {
    @NotNull
    private UUID id;

    @NotBlank @Size(max = 64)
    private String code;

    @NotBlank @Size(max = 255)
    private String name;

    private String description;
}
