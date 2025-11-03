package hsf302.he187383.phudd.license.dto.account;

import hsf302.he187383.phudd.license.enums.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AccountResp {
    private UUID id;
    private UUID licenseId;
    private String username;
    private AccountStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}
