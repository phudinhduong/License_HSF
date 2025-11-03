package hsf302.he187383.phudd.license.mapper;

import hsf302.he187383.phudd.license.dto.user.*;
import hsf302.he187383.phudd.license.model.*;
import org.mapstruct.*;

@Mapper(config = MapStructCentralConfig.class)
public interface UserMapper {

    // Create: password sẽ được hash ở service → mapper bỏ qua
    @Mapping(target = "passwordHash", ignore = true)
    User toEntity(UserCreateReq req);

    // Update: không đụng password ở mapper (service xử lý nếu có newPassword)
    @BeanMapping(ignoreByDefault = true)
    @Mappings({
            @Mapping(target = "email", source = "email"),
            @Mapping(target = "role", source = "role"),
            @Mapping(target = "status", source = "status")
    })
    void update(@MappingTarget User entity, UserUpdateReq req);

    // Read
    UserResp toResp(User entity);
}
