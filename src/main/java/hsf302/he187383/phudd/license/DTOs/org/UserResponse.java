package hsf302.he187383.phudd.license.DTOs.org;

import hsf302.he187383.phudd.license.enums.*;
import lombok.*;

import java.time.*;
import java.util.*;

@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserResponse {
    private UUID id;
    private UUID orgId;
    private String email;
    private String fullName;
    private UserRole role;
    private UserStatus status;
    private Instant lastLoginAt;
    private Instant createdAt;
    private Instant updatedAt;
}
