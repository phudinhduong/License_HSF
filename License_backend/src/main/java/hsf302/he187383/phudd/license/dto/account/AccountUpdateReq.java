package hsf302.he187383.phudd.license.dto.account;

import hsf302.he187383.phudd.license.enums.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AccountUpdateReq {
    @NotNull
    private UUID id;

    @NotBlank @Size(max = 64)
    private String username;

    @Size(min = 8, max = 255)
    private String newPassword; // optional

    @NotNull
    private AccountStatus status;
}
