package hsf302.he187383.phudd.license.model;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="webhooks",
        indexes = @Index(name="idx_webhooks_org", columnList="org_id"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Webhook extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="org_id")
    private Organization organization;

    @Column(length = 1024, nullable = false)
    private String url;

    @Column(length = 255, nullable = false)
    private String secret;

    @Lob @Column(columnDefinition = "nvarchar(max) not null")
    private String events = "[]"; // JSON array or CSV

    @Column(nullable = false)
    private Boolean isActive = true;
}
