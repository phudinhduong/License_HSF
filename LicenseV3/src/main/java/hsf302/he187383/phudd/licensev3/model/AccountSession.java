package hsf302.he187383.phudd.licensev3.model;


import hsf302.he187383.phudd.licensev3.enums.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "account_sessions",
        uniqueConstraints = @UniqueConstraint(name = "uk_sessions_jti", columnNames = "jti"))
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(of = "id")
public class AccountSession extends BaseEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_session_account"))
    private Account account;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "license_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_session_license"))
    private License license;

    @Column(nullable = false, length = 128)
    private String jti;

    @Column(nullable = false, length = 256)
    private String tokenHash;

    @Column(length = 64)
    private String deviceId;

    @Column(length = 45)
    private String ip;

    @Column(length = 255)
    private String userAgent;

    @Column(nullable = false)
    private Instant createdAtSession = Instant.now();

    @Column
    private Instant lastSeenAt;

    @Column
    private Instant expiresAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private SessionStatus status;
}
