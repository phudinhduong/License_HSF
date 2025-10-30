package hsf302.he187383.phudd.license.DTOs.org;

import hsf302.he187383.phudd.license.enums.*;
import lombok.*;

@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserUpdateRequest {
    private String fullName;
    private UserRole role;
    private UserStatus status;
    private String newPassword;
}
