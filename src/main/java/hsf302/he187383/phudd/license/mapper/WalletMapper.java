package hsf302.he187383.phudd.license.mapper;

import hsf302.he187383.phudd.license.dto.wallet.WalletResp;
import hsf302.he187383.phudd.license.model.Wallet;
import org.mapstruct.*;

@Mapper(config = MapStructCentralConfig.class)
public interface WalletMapper {
    @Mappings({
            @Mapping(target = "userId", source = "user.id")
    })
    WalletResp toResp(Wallet e);
}
