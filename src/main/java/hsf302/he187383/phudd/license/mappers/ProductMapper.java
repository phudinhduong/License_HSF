package hsf302.he187383.phudd.license.mappers;

import hsf302.he187383.phudd.license.DTOs.product.*;
import hsf302.he187383.phudd.license.model.*;
import org.mapstruct.*;

@Mapper(config = MapStructConfig.class)
public interface ProductMapper {

    Product toEntity(ProductCreateRequest req);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget Product entity, ProductUpdateRequest req);

    ProductResponse toDto(Product entity);
}
