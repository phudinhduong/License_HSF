package hsf302.he187383.phudd.license.dto.accountsession;

import hsf302.he187383.phudd.license.enums.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SessionHeartbeatReq {
    @NotNull
    private UUID id;

    @NotNull
    private Instant lastSeenAt;
}
