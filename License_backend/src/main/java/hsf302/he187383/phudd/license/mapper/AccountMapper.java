package hsf302.he187383.phudd.license.mapper;

import hsf302.he187383.phudd.license.dto.account.*;
import hsf302.he187383.phudd.license.model.Account;
import org.mapstruct.*;

@Mapper(config = MapStructCentralConfig.class)
public interface AccountMapper {

    @Mappings({
            @Mapping(target = "license", expression = "java(ref.toLicense(req.getLicenseId()))"),
            @Mapping(target = "username", source = "username"),
            @Mapping(target = "passwordHash", ignore = true), // hash ở service
            @Mapping(target = "status", source = "status")
    })
    Account toEntity(AccountCreateReq req, @Context JpaRefResolver ref);

    @BeanMapping(ignoreByDefault = true)
    @Mappings({
            @Mapping(target = "username", source = "username"),
            @Mapping(target = "status", source = "status")
    })
    void update(@MappingTarget Account entity, AccountUpdateReq req);

    @Mappings({
            @Mapping(target = "licenseId", source = "license.id")
    })
    AccountResp toResp(Account e);
}
