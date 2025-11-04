package hsf302.he187383.phudd.license.dto.user;

import hsf302.he187383.phudd.license.enums.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserCreateReq {
    @Email @NotBlank @Size(max = 320)
    private String email;

    @NotBlank @Size(min = 8, max = 255)
    private String password; // nhận password thô, hash ở service

    @NotNull
    private UserRole role;

    @NotNull
    private UserStatus status;
}
