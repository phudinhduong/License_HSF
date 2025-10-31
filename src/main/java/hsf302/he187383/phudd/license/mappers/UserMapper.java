package hsf302.he187383.phudd.license.mappers;

import hsf302.he187383.phudd.license.DTOs.org.*;
import hsf302.he187383.phudd.license.model.*;
import org.mapstruct.*;

import java.util.UUID;

@Mapper(config = MapStructConfig.class, uses = {IdRefMapper.class})
public interface UserMapper extends IdRefMapper {

    @Mapping(target = "organization", expression = "java( orgRef(req.getOrgId()) )")
    @Mapping(target = "passwordHash", source = "password") // Hash ở service, mapper chỉ giữ chỗ
    User toEntity(UserCreateRequest req);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget User entity, UserUpdateRequest req);

    @Mapping(target = "orgId", source = "organization.id")
    UserResponse toDto(User entity);

    // helper
    default Organization orgRef(UUID id){
        if (id == null) return null;
        Organization o = new Organization();
        o.setId(id);
        return o;
    }
}
