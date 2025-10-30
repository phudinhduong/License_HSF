package hsf302.he187383.phudd.license.DTOs.auth;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RefreshRequest {
    @NotBlank private String refreshToken;
}
