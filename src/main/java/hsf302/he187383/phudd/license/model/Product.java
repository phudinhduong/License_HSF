package hsf302.he187383.phudd.license.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_products_code", columnNames = "code"))
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(of = "id")
public class Product extends BaseEntity {

    @Column(nullable = false, length = 64)
    private String code;

    @Column(nullable = false, length = 255)
    private String name;

    @Lob
    private String description;
}
