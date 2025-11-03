package hsf302.he187383.phudd.license.dto.account;

import hsf302.he187383.phudd.license.enums.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AccountCreateReq {
    @NotNull
    private UUID licenseId;

    @NotBlank @Size(max = 64)
    private String username;

    @NotBlank @Size(min = 8, max = 255)
    private String password; // nhận thô, hash ở service

    @NotNull
    private AccountStatus status;
}
