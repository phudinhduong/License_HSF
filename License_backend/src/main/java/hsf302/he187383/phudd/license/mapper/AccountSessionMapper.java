package hsf302.he187383.phudd.license.mapper;

import hsf302.he187383.phudd.license.dto.accountsession.*;
import hsf302.he187383.phudd.license.model.AccountSession;
import org.mapstruct.*;

@Mapper(config = MapStructCentralConfig.class)
public interface AccountSessionMapper {

    @Mappings({
            @Mapping(target = "account", expression = "java(ref.toAccount(req.getAccountId()))"),
            @Mapping(target = "license", expression = "java(ref.toLicense(req.getLicenseId()))"),
            @Mapping(target = "jti", source = "jti"),
            @Mapping(target = "tokenHash", source = "tokenHash"),
            @Mapping(target = "deviceId", source = "deviceId"),
            @Mapping(target = "ip", source = "ip"),
            @Mapping(target = "userAgent", source = "userAgent"),
            @Mapping(target = "expiresAt", source = "expiresAt"),
            @Mapping(target = "status", source = "status")
    })
    AccountSession toEntity(SessionCreateReq req, @Context JpaRefResolver ref);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "lastSeenAt", source = "lastSeenAt")
    void heartbeat(@MappingTarget AccountSession entity, SessionHeartbeatReq req);

    @Mappings({
            @Mapping(target = "accountId", source = "account.id"),
            @Mapping(target = "licenseId", source = "license.id")
    })
    SessionResp toResp(AccountSession e);
}
