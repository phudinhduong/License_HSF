package hsf302.he187383.phudd.clientlicense.dto;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginRequest {
    private String username;
    private String password;
    private String deviceId;
    private String deviceName;
    private Boolean preempt;
}
