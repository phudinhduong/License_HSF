package hsf302.he187383.phudd.license.mapper;

import hsf302.he187383.phudd.license.dto.accountsession.SessionCreateReq;
import hsf302.he187383.phudd.license.dto.accountsession.SessionHeartbeatReq;
import hsf302.he187383.phudd.license.dto.accountsession.SessionResp;
import hsf302.he187383.phudd.license.model.Account;
import hsf302.he187383.phudd.license.model.AccountSession;
import hsf302.he187383.phudd.license.model.License;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-04T12:50:44+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Microsoft)"
)
@Component
public class AccountSessionMapperImpl implements AccountSessionMapper {

    @Override
    public AccountSession toEntity(SessionCreateReq req, JpaRefResolver ref) {
        if ( req == null ) {
            return null;
        }

        AccountSession.AccountSessionBuilder accountSession = AccountSession.builder();

        accountSession.jti( req.getJti() );
        accountSession.tokenHash( req.getTokenHash() );
        accountSession.deviceId( req.getDeviceId() );
        accountSession.ip( req.getIp() );
        accountSession.userAgent( req.getUserAgent() );
        accountSession.expiresAt( req.getExpiresAt() );
        accountSession.status( req.getStatus() );

        accountSession.account( ref.toAccount(req.getAccountId()) );
        accountSession.license( ref.toLicense(req.getLicenseId()) );

        return accountSession.build();
    }

    @Override
    public void heartbeat(AccountSession entity, SessionHeartbeatReq req) {
        if ( req == null ) {
            return;
        }

        if ( req.getLastSeenAt() != null ) {
            entity.setLastSeenAt( req.getLastSeenAt() );
        }
    }

    @Override
    public SessionResp toResp(AccountSession e) {
        if ( e == null ) {
            return null;
        }

        SessionResp.SessionRespBuilder sessionResp = SessionResp.builder();

        sessionResp.accountId( eAccountId( e ) );
        sessionResp.licenseId( eLicenseId( e ) );
        sessionResp.id( e.getId() );
        sessionResp.jti( e.getJti() );
        sessionResp.tokenHash( e.getTokenHash() );
        sessionResp.deviceId( e.getDeviceId() );
        sessionResp.ip( e.getIp() );
        sessionResp.userAgent( e.getUserAgent() );
        sessionResp.createdAt( e.getCreatedAt() );
        sessionResp.updatedAt( e.getUpdatedAt() );
        sessionResp.createdAtSession( e.getCreatedAtSession() );
        sessionResp.lastSeenAt( e.getLastSeenAt() );
        sessionResp.expiresAt( e.getExpiresAt() );
        sessionResp.status( e.getStatus() );

        return sessionResp.build();
    }

    private UUID eAccountId(AccountSession accountSession) {
        if ( accountSession == null ) {
            return null;
        }
        Account account = accountSession.getAccount();
        if ( account == null ) {
            return null;
        }
        UUID id = account.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private UUID eLicenseId(AccountSession accountSession) {
        if ( accountSession == null ) {
            return null;
        }
        License license = accountSession.getLicense();
        if ( license == null ) {
            return null;
        }
        UUID id = license.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
