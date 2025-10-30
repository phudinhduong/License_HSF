package hsf302.he187383.phudd.license.model;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="key_pairs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class KeyPairEntity extends BaseEntity {

    @Column(length = 64, nullable = false, unique = true)
    private String kid;

    @Column(length = 16, nullable = false)
    private String algorithm = "RS256";

    @Lob @Column(columnDefinition = "nvarchar(max)", nullable = false)
    private String publicPem;

    @Lob @Column(columnDefinition = "nvarchar(max)", nullable = false)
    private String privatePem; // Prod: để vault, trường này có thể không map

    @Column(nullable = false)
    private java.time.Instant activeFrom;

    private java.time.Instant activeTo;
}
