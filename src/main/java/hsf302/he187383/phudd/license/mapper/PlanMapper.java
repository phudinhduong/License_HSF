package hsf302.he187383.phudd.license.mapper;

import hsf302.he187383.phudd.license.dto.plan.*;
import hsf302.he187383.phudd.license.model.Plan;
import org.mapstruct.*;

@Mapper(config = MapStructCentralConfig.class)
public interface PlanMapper {

    @Mappings({
            @Mapping(target = "product", expression = "java(ref.toProduct(req.getProductId()))"),
            @Mapping(target = "code", source = "code"),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "billingType", source = "billingType"),
            @Mapping(target = "priceCredits", source = "priceCredits"),
            @Mapping(target = "currency", source = "currency"),
            @Mapping(target = "durationDays", source = "durationDays"),
            @Mapping(target = "seats", source = "seats"),
            @Mapping(target = "concurrentLimitPerAccount", source = "concurrentLimitPerAccount"),
            @Mapping(target = "deviceLimitPerAccount", source = "deviceLimitPerAccount")
    })
    Plan toEntity(PlanCreateReq req, @Context JpaRefResolver ref);

    @BeanMapping(ignoreByDefault = true)
    @Mappings({
            @Mapping(target = "product", expression = "java(ref.toProduct(req.getProductId()))"),
            @Mapping(target = "code", source = "code"),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "billingType", source = "billingType"),
            @Mapping(target = "priceCredits", source = "priceCredits"),
            @Mapping(target = "currency", source = "currency"),
            @Mapping(target = "durationDays", source = "durationDays"),
            @Mapping(target = "seats", source = "seats"),
            @Mapping(target = "concurrentLimitPerAccount", source = "concurrentLimitPerAccount"),
            @Mapping(target = "deviceLimitPerAccount", source = "deviceLimitPerAccount")
    })
    void update(@MappingTarget Plan entity, PlanUpdateReq req, @Context JpaRefResolver ref);

    @Mappings({
            @Mapping(target = "productId", source = "product.id")
    })
    PlanResp toResp(Plan e);
}
