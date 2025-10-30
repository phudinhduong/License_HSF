package hsf302.he187383.phudd.license.DTOs.activation;

import lombok.*;

@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class HeartbeatResponse {
    private String status; // "ok" | "expired" | "suspended" | "update_required"
    private String instruction; // message cho client
}
