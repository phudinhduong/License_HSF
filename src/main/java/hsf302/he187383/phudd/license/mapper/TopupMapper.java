package hsf302.he187383.phudd.license.mapper;

import hsf302.he187383.phudd.license.dto.topup.*;
import hsf302.he187383.phudd.license.model.Topup;
import org.mapstruct.*;

@Mapper(config = MapStructCentralConfig.class)
public interface TopupMapper {

    // user & status (PENDING/PAID...) set ở service
    @Mappings({
            @Mapping(target = "provider", source = "provider"),
            @Mapping(target = "moneyAmount", source = "moneyAmount"),
            @Mapping(target = "currency", source = "currency"),
            @Mapping(target = "creditsGranted", source = "creditsGranted"),
            @Mapping(target = "paymentRef", source = "paymentRef"),
            @Mapping(target = "user", ignore = true),
            @Mapping(target = "status", ignore = true)
    })
    Topup toEntity(TopupCreateReq req);

    TopupResp toResp(Topup e);
}
