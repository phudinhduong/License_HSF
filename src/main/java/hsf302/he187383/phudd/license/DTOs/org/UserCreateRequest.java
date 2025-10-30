package hsf302.he187383.phudd.license.DTOs.org;

import hsf302.he187383.phudd.license.enums.UserRole;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.*;

@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserCreateRequest {
    @NotNull
    private UUID orgId;
    @Email @NotBlank private String email;
    @NotBlank private String password;
    private String fullName;
    @NotNull private UserRole role;
}
