package hsf302.he187383.phudd.license.mapper;

import hsf302.he187383.phudd.license.dto.wallet.WalletTxnResp;
import hsf302.he187383.phudd.license.model.Wallet;
import hsf302.he187383.phudd.license.model.WalletTxn;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-04T12:50:44+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Microsoft)"
)
@Component
public class WalletTxnMapperImpl implements WalletTxnMapper {

    @Override
    public WalletTxnResp toResp(WalletTxn e) {
        if ( e == null ) {
            return null;
        }

        WalletTxnResp.WalletTxnRespBuilder walletTxnResp = WalletTxnResp.builder();

        walletTxnResp.walletId( eWalletId( e ) );
        walletTxnResp.id( e.getId() );
        walletTxnResp.type( e.getType() );
        walletTxnResp.direction( e.getDirection() );
        walletTxnResp.amount( e.getAmount() );
        walletTxnResp.balanceAfter( e.getBalanceAfter() );
        walletTxnResp.status( e.getStatus() );
        walletTxnResp.refType( e.getRefType() );
        walletTxnResp.refId( e.getRefId() );
        walletTxnResp.idempotencyKey( e.getIdempotencyKey() );
        walletTxnResp.createdAt( e.getCreatedAt() );
        walletTxnResp.updatedAt( e.getUpdatedAt() );

        return walletTxnResp.build();
    }

    private UUID eWalletId(WalletTxn walletTxn) {
        if ( walletTxn == null ) {
            return null;
        }
        Wallet wallet = walletTxn.getWallet();
        if ( wallet == null ) {
            return null;
        }
        UUID id = wallet.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
