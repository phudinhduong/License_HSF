package hsf302.he187383.phudd.license.mapper;

import hsf302.he187383.phudd.license.dto.plan.PlanCreateReq;
import hsf302.he187383.phudd.license.dto.plan.PlanResp;
import hsf302.he187383.phudd.license.dto.plan.PlanUpdateReq;
import hsf302.he187383.phudd.license.model.Plan;
import hsf302.he187383.phudd.license.model.Product;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-04T12:50:44+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Microsoft)"
)
@Component
public class PlanMapperImpl implements PlanMapper {

    @Override
    public Plan toEntity(PlanCreateReq req, JpaRefResolver ref) {
        if ( req == null ) {
            return null;
        }

        Plan.PlanBuilder plan = Plan.builder();

        plan.code( req.getCode() );
        plan.name( req.getName() );
        plan.billingType( req.getBillingType() );
        plan.priceCredits( req.getPriceCredits() );
        plan.currency( req.getCurrency() );
        plan.durationDays( req.getDurationDays() );
        plan.seats( req.getSeats() );
        plan.concurrentLimitPerAccount( req.getConcurrentLimitPerAccount() );
        plan.deviceLimitPerAccount( req.getDeviceLimitPerAccount() );

        plan.product( ref.toProduct(req.getProductId()) );

        return plan.build();
    }

    @Override
    public void update(Plan entity, PlanUpdateReq req, JpaRefResolver ref) {
        if ( req == null ) {
            return;
        }

        if ( req.getCode() != null ) {
            entity.setCode( req.getCode() );
        }
        if ( req.getName() != null ) {
            entity.setName( req.getName() );
        }
        if ( req.getBillingType() != null ) {
            entity.setBillingType( req.getBillingType() );
        }
        if ( req.getPriceCredits() != null ) {
            entity.setPriceCredits( req.getPriceCredits() );
        }
        if ( req.getCurrency() != null ) {
            entity.setCurrency( req.getCurrency() );
        }
        if ( req.getDurationDays() != null ) {
            entity.setDurationDays( req.getDurationDays() );
        }
        if ( req.getSeats() != null ) {
            entity.setSeats( req.getSeats() );
        }
        if ( req.getConcurrentLimitPerAccount() != null ) {
            entity.setConcurrentLimitPerAccount( req.getConcurrentLimitPerAccount() );
        }
        if ( req.getDeviceLimitPerAccount() != null ) {
            entity.setDeviceLimitPerAccount( req.getDeviceLimitPerAccount() );
        }

        entity.setProduct( ref.toProduct(req.getProductId()) );
    }

    @Override
    public PlanResp toResp(Plan e) {
        if ( e == null ) {
            return null;
        }

        PlanResp.PlanRespBuilder planResp = PlanResp.builder();

        planResp.productId( eProductId( e ) );
        planResp.id( e.getId() );
        planResp.code( e.getCode() );
        planResp.name( e.getName() );
        planResp.billingType( e.getBillingType() );
        planResp.priceCredits( e.getPriceCredits() );
        planResp.currency( e.getCurrency() );
        planResp.durationDays( e.getDurationDays() );
        planResp.seats( e.getSeats() );
        planResp.concurrentLimitPerAccount( e.getConcurrentLimitPerAccount() );
        planResp.deviceLimitPerAccount( e.getDeviceLimitPerAccount() );
        planResp.createdAt( e.getCreatedAt() );
        planResp.updatedAt( e.getUpdatedAt() );

        return planResp.build();
    }

    private UUID eProductId(Plan plan) {
        if ( plan == null ) {
            return null;
        }
        Product product = plan.getProduct();
        if ( product == null ) {
            return null;
        }
        UUID id = product.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
