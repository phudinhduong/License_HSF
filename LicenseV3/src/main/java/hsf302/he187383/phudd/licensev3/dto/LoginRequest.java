package hsf302.he187383.phudd.licensev3.dto;


import lombok.*;

@Getter @Setter
public class LoginRequest {
    private String username;
    private String password;
    private String deviceId;
    private String deviceName;
    private Boolean preempt;
}
