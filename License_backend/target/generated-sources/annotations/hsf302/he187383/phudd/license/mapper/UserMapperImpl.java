package hsf302.he187383.phudd.license.mapper;

import hsf302.he187383.phudd.license.dto.user.UserCreateReq;
import hsf302.he187383.phudd.license.dto.user.UserResp;
import hsf302.he187383.phudd.license.dto.user.UserUpdateReq;
import hsf302.he187383.phudd.license.model.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-04T12:50:44+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Microsoft)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toEntity(UserCreateReq req) {
        if ( req == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.email( req.getEmail() );
        user.role( req.getRole() );
        user.status( req.getStatus() );

        return user.build();
    }

    @Override
    public void update(User entity, UserUpdateReq req) {
        if ( req == null ) {
            return;
        }

        if ( req.getEmail() != null ) {
            entity.setEmail( req.getEmail() );
        }
        if ( req.getRole() != null ) {
            entity.setRole( req.getRole() );
        }
        if ( req.getStatus() != null ) {
            entity.setStatus( req.getStatus() );
        }
    }

    @Override
    public UserResp toResp(User entity) {
        if ( entity == null ) {
            return null;
        }

        UserResp.UserRespBuilder userResp = UserResp.builder();

        userResp.id( entity.getId() );
        userResp.email( entity.getEmail() );
        userResp.role( entity.getRole() );
        userResp.status( entity.getStatus() );
        userResp.createdAt( entity.getCreatedAt() );
        userResp.updatedAt( entity.getUpdatedAt() );

        return userResp.build();
    }
}
