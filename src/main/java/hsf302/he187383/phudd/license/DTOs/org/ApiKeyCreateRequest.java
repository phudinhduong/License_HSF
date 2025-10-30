package hsf302.he187383.phudd.license.DTOs.org;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ApiKeyCreateRequest {
    @NotNull
    private UUID orgId;
    @NotBlank private String name;
    private String scopesJson; // JSON string, optional
}
