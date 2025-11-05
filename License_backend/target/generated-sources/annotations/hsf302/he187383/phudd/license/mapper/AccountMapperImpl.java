package hsf302.he187383.phudd.license.mapper;

import hsf302.he187383.phudd.license.dto.account.AccountCreateReq;
import hsf302.he187383.phudd.license.dto.account.AccountResp;
import hsf302.he187383.phudd.license.dto.account.AccountUpdateReq;
import hsf302.he187383.phudd.license.model.Account;
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
public class AccountMapperImpl implements AccountMapper {

    @Override
    public Account toEntity(AccountCreateReq req, JpaRefResolver ref) {
        if ( req == null ) {
            return null;
        }

        Account.AccountBuilder account = Account.builder();

        account.username( req.getUsername() );
        account.status( req.getStatus() );

        account.license( ref.toLicense(req.getLicenseId()) );

        return account.build();
    }

    @Override
    public void update(Account entity, AccountUpdateReq req) {
        if ( req == null ) {
            return;
        }

        if ( req.getUsername() != null ) {
            entity.setUsername( req.getUsername() );
        }
        if ( req.getStatus() != null ) {
            entity.setStatus( req.getStatus() );
        }
    }

    @Override
    public AccountResp toResp(Account e) {
        if ( e == null ) {
            return null;
        }

        AccountResp.AccountRespBuilder accountResp = AccountResp.builder();

        accountResp.licenseId( eLicenseId( e ) );
        accountResp.id( e.getId() );
        accountResp.username( e.getUsername() );
        accountResp.status( e.getStatus() );
        accountResp.createdAt( e.getCreatedAt() );
        accountResp.updatedAt( e.getUpdatedAt() );

        return accountResp.build();
    }

    private UUID eLicenseId(Account account) {
        if ( account == null ) {
            return null;
        }
        License license = account.getLicense();
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
