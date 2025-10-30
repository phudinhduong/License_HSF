package hsf302.he187383.phudd.license.DTOs.org;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
class OrganizationCreateRequest {
    @NotBlank private String code;
    @NotBlank private String name;
}
