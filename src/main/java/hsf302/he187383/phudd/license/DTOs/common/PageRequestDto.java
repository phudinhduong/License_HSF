package hsf302.he187383.phudd.license.DTOs.common;

import jakarta.validation.constraints.Min;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PageRequestDto {
    @Min(0) private int page = 0;
    @Min(1) private int size = 20;
    private String sort; // ví dụ: "createdAt,desc"
}