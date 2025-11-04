package hsf302.he187383.phudd.license.dto.auth;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SignupReq {
    @Email @NotBlank @Size(max = 320)
    private String email;

    @NotBlank @Size(min = 8, max = 255)
    private String password;
}
