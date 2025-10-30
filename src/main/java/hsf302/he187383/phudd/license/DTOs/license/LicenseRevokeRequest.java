package hsf302.he187383.phudd.license.DTOs.license;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LicenseRevokeRequest {
    @NotBlank
    private String reason;
}