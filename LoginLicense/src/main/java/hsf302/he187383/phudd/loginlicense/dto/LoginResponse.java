package hsf302.he187383.phudd.loginlicense.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private boolean ok;
    private String  jti;
    private String  deviceId;
    private String  accountId;
    private String  username;
    private LicenseSummary license;

    @Getter @Setter @AllArgsConstructor @NoArgsConstructor
    public static class LicenseSummary {
        private String id;
        private String status;
        private String issuedAt;
        private String expiresAt;
        private Integer seatsUsed;
        private Integer seatsTotal;
        private ProductSummary product;
        private PlanSummary plan;
        private String sessionExpiresAt; // để B hiển thị/đếm ngược
    }

    @Getter @Setter @AllArgsConstructor @NoArgsConstructor
    public static class ProductSummary {
        private String code; private String name;
    }

    @Getter @Setter @AllArgsConstructor @NoArgsConstructor
    public static class PlanSummary {
        private String code; private String name; private String billingType;
    }
}
