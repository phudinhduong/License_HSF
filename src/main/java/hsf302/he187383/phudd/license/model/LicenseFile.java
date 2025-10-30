package hsf302.he187383.phudd.license.model;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="license_files")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LicenseFile extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="license_id", nullable=false)
    private License license;

    @Lob
    @Column(columnDefinition = "nvarchar(max)", nullable = false)
    private String jwsCompact; // JWS token string

    @Column(nullable = false)
    private Integer version = 1;
}
