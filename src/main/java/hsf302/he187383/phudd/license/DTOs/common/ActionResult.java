package hsf302.he187383.phudd.license.DTOs.common;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ActionResult {
    private String message; // "ok", "revoked", ...
}