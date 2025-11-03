package hsf302.he187383.phudd.license.dto.order;

import hsf302.he187383.phudd.license.dto.license.LicenseResp;
import hsf302.he187383.phudd.license.dto.wallet.WalletResp;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PurchaseCreditsResp {
    private OrderResp order;
    private LicenseResp license;
    private WalletResp wallet;
}
