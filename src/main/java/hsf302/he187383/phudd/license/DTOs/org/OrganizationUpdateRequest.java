package hsf302.he187383.phudd.license.DTOs.org;


import hsf302.he187383.phudd.license.enums.OrgStatus;
import lombok.*;

@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @Builder
class OrganizationUpdateRequest {
    private String name;
    private OrgStatus status;
}