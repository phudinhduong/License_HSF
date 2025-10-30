package hsf302.he187383.phudd.license.DTOs.security;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RotateKeyRequest {
    @NotBlank private String newPublicPem;
    @NotBlank private String newPrivatePem; // prod: không expose qua API công khai
    private Instant activeFrom;             // nếu null -> ngay lập tức
}