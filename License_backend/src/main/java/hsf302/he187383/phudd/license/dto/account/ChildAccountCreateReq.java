package hsf302.he187383.phudd.license.dto.account;


import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ChildAccountCreateReq {
    @NotBlank
    @Size(max = 64)
    private String username;

    @NotBlank
    @Size(min = 8, max = 255)
    private String password; // plaintext → mã hoá ở service
}
