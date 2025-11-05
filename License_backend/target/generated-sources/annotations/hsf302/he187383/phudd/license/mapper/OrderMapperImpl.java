package hsf302.he187383.phudd.license.mapper;

import hsf302.he187383.phudd.license.dto.order.OrderCreateReq;
import hsf302.he187383.phudd.license.dto.order.OrderResp;
import hsf302.he187383.phudd.license.model.Order;
import hsf302.he187383.phudd.license.model.Plan;
import hsf302.he187383.phudd.license.model.User;
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
public class OrderMapperImpl implements OrderMapper {

    @Override
    public Order toEntity(OrderCreateReq req, JpaRefResolver ref) {
        if ( req == null ) {
            return null;
        }

        Order.OrderBuilder order = Order.builder();

        order.plan( ref.toPlan(req.getPlanId()) );

        return order.build();
    }

    @Override
    public OrderResp toResp(Order e) {
        if ( e == null ) {
            return null;
        }

        OrderResp.OrderRespBuilder orderResp = OrderResp.builder();

        orderResp.userId( eUserId( e ) );
        orderResp.planId( ePlanId( e ) );
        orderResp.walletTxnId( eWalletTxnId( e ) );
        orderResp.id( e.getId() );
        orderResp.priceCredits( e.getPriceCredits() );
        orderResp.status( e.getStatus() );
        orderResp.paymentRef( e.getPaymentRef() );
        orderResp.createdAt( e.getCreatedAt() );
        orderResp.updatedAt( e.getUpdatedAt() );

        return orderResp.build();
    }

    private UUID eUserId(Order order) {
        if ( order == null ) {
            return null;
        }
        User user = order.getUser();
        if ( user == null ) {
            return null;
        }
        UUID id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private UUID ePlanId(Order order) {
        if ( order == null ) {
            return null;
        }
        Plan plan = order.getPlan();
        if ( plan == null ) {
            return null;
        }
        UUID id = plan.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private UUID eWalletTxnId(Order order) {
        if ( order == null ) {
            return null;
        }
        WalletTxn walletTxn = order.getWalletTxn();
        if ( walletTxn == null ) {
            return null;
        }
        UUID id = walletTxn.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
