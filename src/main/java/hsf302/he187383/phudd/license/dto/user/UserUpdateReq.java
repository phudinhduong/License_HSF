package hsf302.he187383.phudd.license.dto.user;

import hsf302.he187383.phudd.license.enums.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserUpdateReq {
    @NotNull
    private UUID id;

    @Email @NotBlank @Size(max = 320)
    private String email;

    @Size(min = 8, max = 255)
    private String newPassword; // optional

    @NotNull
    private UserRole role;

    @NotNull
    private UserStatus status;
}
