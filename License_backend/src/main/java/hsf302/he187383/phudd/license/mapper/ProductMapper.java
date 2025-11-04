package hsf302.he187383.phudd.license.mapper;

import hsf302.he187383.phudd.license.dto.product.*;
import hsf302.he187383.phudd.license.model.Product;
import org.mapstruct.*;

@Mapper(config = MapStructCentralConfig.class)
public interface ProductMapper {
    Product toEntity(ProductCreateReq req);

    @BeanMapping(ignoreByDefault = true)
    @Mappings({
            @Mapping(target = "code", source = "code"),
            @Mapping(target = "name", source = "name"),
            @Mapping(target = "description", source = "description")
    })
    void update(@MappingTarget Product entity, ProductUpdateReq req);

    ProductResp toResp(Product e);
}
