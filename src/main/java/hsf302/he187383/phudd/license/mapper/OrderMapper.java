package hsf302.he187383.phudd.license.mapper;

import hsf302.he187383.phudd.license.dto.order.*;
import hsf302.he187383.phudd.license.model.Order;
import org.mapstruct.*;

@Mapper(config = MapStructCentralConfig.class)
public interface OrderMapper {

    // Lưu ý: user sẽ lấy từ SecurityContext tại service
    @Mappings({
            @Mapping(target = "plan", expression = "java(ref.toPlan(req.getPlanId()))"),
            @Mapping(target = "priceCredits", ignore = true), // service snapshot
            @Mapping(target = "status", ignore = true),       // service set PENDING
            @Mapping(target = "paymentRef", ignore = true),
            @Mapping(target = "walletTxn", ignore = true),
            @Mapping(target = "user", ignore = true)
    })
    Order toEntity(OrderCreateReq req, @Context JpaRefResolver ref);

    @Mappings({
            @Mapping(target = "userId", source = "user.id"),
            @Mapping(target = "planId", source = "plan.id"),
            @Mapping(target = "walletTxnId", source = "walletTxn.id")
    })
    OrderResp toResp(Order e);
}
