package hsf302.he187383.phudd.license.mapper;

import hsf302.he187383.phudd.license.dto.wallet.*;
import hsf302.he187383.phudd.license.model.WalletTxn;
import org.mapstruct.*;

@Mapper(config = MapStructCentralConfig.class)
public interface WalletTxnMapper {
    @Mappings({
            @Mapping(target = "walletId", source = "wallet.id")
    })
    WalletTxnResp toResp(WalletTxn e);
}