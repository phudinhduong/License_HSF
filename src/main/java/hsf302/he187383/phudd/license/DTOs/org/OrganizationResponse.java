package hsf302.he187383.phudd.license.DTOs.org;

import hsf302.he187383.phudd.license.enums.OrgStatus;
import lombok.*;

import java.time.*;
import java.util.*;

@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrganizationResponse {
    private UUID id;
    private String code;
    private String name;
    private OrgStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}
