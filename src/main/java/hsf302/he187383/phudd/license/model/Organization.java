package hsf302.he187383.phudd.license.model;

import hsf302.he187383.phudd.license.enums.OrgStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "organizations",
        indexes = { @Index(name="idx_org_code", columnList="code", unique = true) })
@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Organization extends BaseEntity {

    @Column(length = 64, nullable = false, unique = true)
    private String code;

    @Column(length = 255, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private OrgStatus status = OrgStatus.ACTIVE;
}
