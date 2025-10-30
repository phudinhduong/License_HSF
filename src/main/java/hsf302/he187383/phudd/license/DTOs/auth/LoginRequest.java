package hsf302.he187383.phudd.license.DTOs.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LoginRequest {

    private UUID orgId;
    private String orgCode;

    @Email @NotBlank private String email;
    @NotBlank private String password;
}
