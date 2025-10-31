package hsf302.he187383.phudd.license.mappers;

import hsf302.he187383.phudd.license.DTOs.product.*;
import hsf302.he187383.phudd.license.model.*;
import org.mapstruct.*;

import java.util.UUID;

@Mapper(config = MapStructConfig.class)
public interface PlanMapper {

    @Mapping(target = "product",
            expression = "java( productRef(req.getProductId()) )")
    @Mapping(target = "features", source = "featuresJson")
    Plan toEntity(PlanCreateRequest req);

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "features", source = "featuresJson")
    void update(@MappingTarget Plan entity, PlanUpdateRequest req);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "featuresJson", source = "features")
    PlanResponse toDto(Plan e);

    default Product productRef(UUID id){
        if (id == null) return null;
        Product p = new Product();
        p.setId(id);
        return p;
    }
}
