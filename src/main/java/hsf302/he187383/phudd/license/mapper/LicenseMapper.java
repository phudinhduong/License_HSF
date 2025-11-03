package hsf302.he187383.phudd.license.mapper;

import hsf302.he187383.phudd.license.dto.license.*;
import hsf302.he187383.phudd.license.model.License;
import org.mapstruct.*;

@Mapper(config = MapStructCentralConfig.class)
public interface LicenseMapper {

    @Mappings({
            @Mapping(target = "order", expression = "java(ref.toOrder(req.getOrderId()))"),
            @Mapping(target = "user", expression = "java(ref.toUser(req.getUserId()))"),
            @Mapping(target = "plan", expression = "java(ref.toPlan(req.getPlanId()))"),
            @Mapping(target = "licenseKey", source = "licenseKey"),
            @Mapping(target = "status", source = "status"),
            @Mapping(target = "issuedAt", source = "issuedAt"),
            @Mapping(target = "expiresAt", source = "expiresAt"),
            @Mapping(target = "seatsTotal", source = "seatsTotal"),
            @Mapping(target = "seatsUsed", source = "seatsUsed")
    })
    License toEntity(LicenseCreateReq req, @Context JpaRefResolver ref);

    @BeanMapping(ignoreByDefault = true)
    @Mappings({
            @Mapping(target = "status", source = "status"),
            @Mapping(target = "expiresAt", source = "expiresAt"),
            @Mapping(target = "seatsTotal", source = "seatsTotal"),
            @Mapping(target = "seatsUsed", source = "seatsUsed")
    })
    void update(@MappingTarget License entity, LicenseUpdateReq req);

    @Mappings({
            @Mapping(target = "orderId", source = "order.id"),
            @Mapping(target = "userId", source = "user.id"),
            @Mapping(target = "planId", source = "plan.id")
    })
    LicenseResp toResp(License e);
}
