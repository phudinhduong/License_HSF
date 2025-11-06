package hsf302.he187383.phudd.licensev3.model;

import hsf302.he187383.phudd.licensev3.enums.*;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "users",
        uniqueConstraints = @UniqueConstraint(name = "uk_users_email", columnNames = "email"))
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(of = "id")
public class User extends BaseEntity {

    @Column(nullable = false, length = 320)
    private String email;

    @Column(nullable = false, length = 255)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private UserStatus status;



    @Column(length = 200)
    private String fullName;

    @Column(length = 20)
    private String phone;

    @Column(length = 255)
    private String address;

    @Column(length = 255)
    private String description;
}
