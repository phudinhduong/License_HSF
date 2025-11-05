package hsf302.he187383.phudd.license.mapper;

import hsf302.he187383.phudd.license.dto.wallet.WalletResp;
import hsf302.he187383.phudd.license.model.User;
import hsf302.he187383.phudd.license.model.Wallet;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-04T12:50:44+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Microsoft)"
)
@Component
public class WalletMapperImpl implements WalletMapper {

    @Override
    public WalletResp toResp(Wallet e) {
        if ( e == null ) {
            return null;
        }

        WalletResp.WalletRespBuilder walletResp = WalletResp.builder();

        walletResp.userId( eUserId( e ) );
        walletResp.id( e.getId() );
        walletResp.balance( e.getBalance() );
        walletResp.status( e.getStatus() );
        walletResp.createdAt( e.getCreatedAt() );
        walletResp.updatedAt( e.getUpdatedAt() );

        return walletResp.build();
    }

    private UUID eUserId(Wallet wallet) {
        if ( wallet == null ) {
            return null;
        }
        User user = wallet.getUser();
        if ( user == null ) {
            return null;
        }
        UUID id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
