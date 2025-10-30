package hsf302.he187383.phudd.license.model;

import hsf302.he187383.phudd.license.enums.*;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity @Table(name = "users",
        uniqueConstraints = @UniqueConstraint
                (name="uq_user_email", columnNames = {"org_id","email"}),
        indexes = @Index(name="idx_users_org", columnList="org_id"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="org_id")
    private Organization organization;

    @Column(length = 255, nullable = false)
    private String email;

    @Column(length = 255, nullable = false)
    private String passwordHash;

    @Column(length = 255)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private UserStatus status = UserStatus.ACTIVE;

    private Instant lastLoginAt;
}
