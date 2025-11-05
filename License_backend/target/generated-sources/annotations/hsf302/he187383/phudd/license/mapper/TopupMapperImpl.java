package hsf302.he187383.phudd.license.mapper;

import hsf302.he187383.phudd.license.dto.topup.TopupCreateReq;
import hsf302.he187383.phudd.license.dto.topup.TopupResp;
import hsf302.he187383.phudd.license.model.Topup;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-04T12:50:44+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Microsoft)"
)
@Component
public class TopupMapperImpl implements TopupMapper {

    @Override
    public Topup toEntity(TopupCreateReq req) {
        if ( req == null ) {
            return null;
        }

        Topup.TopupBuilder topup = Topup.builder();

        topup.provider( req.getProvider() );
        topup.moneyAmount( req.getMoneyAmount() );
        topup.currency( req.getCurrency() );
        topup.creditsGranted( req.getCreditsGranted() );
        topup.paymentRef( req.getPaymentRef() );

        return topup.build();
    }

    @Override
    public TopupResp toResp(Topup e) {
        if ( e == null ) {
            return null;
        }

        TopupResp.TopupRespBuilder topupResp = TopupResp.builder();

        topupResp.id( e.getId() );
        topupResp.provider( e.getProvider() );
        topupResp.moneyAmount( e.getMoneyAmount() );
        topupResp.currency( e.getCurrency() );
        topupResp.creditsGranted( e.getCreditsGranted() );
        topupResp.status( e.getStatus() );
        topupResp.paymentRef( e.getPaymentRef() );
        topupResp.createdAt( e.getCreatedAt() );
        topupResp.updatedAt( e.getUpdatedAt() );

        return topupResp.build();
    }
}
