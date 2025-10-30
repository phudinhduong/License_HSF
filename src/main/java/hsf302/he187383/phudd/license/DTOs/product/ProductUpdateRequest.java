package hsf302.he187383.phudd.license.DTOs.product;

import lombok.*;

@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductUpdateRequest {
    private String name;
    private String currentVersion;
}
