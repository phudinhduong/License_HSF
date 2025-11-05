package hsf302.he187383.phudd.license.mapper;

import hsf302.he187383.phudd.license.dto.license.LicenseCreateReq;
import hsf302.he187383.phudd.license.dto.license.LicenseResp;
import hsf302.he187383.phudd.license.dto.license.LicenseUpdateReq;
import hsf302.he187383.phudd.license.model.License;
import hsf302.he187383.phudd.license.model.Order;
import hsf302.he187383.phudd.license.model.Plan;
import hsf302.he187383.phudd.license.model.User;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-04T12:50:44+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Microsoft)"
)
@Component
public class LicenseMapperImpl implements LicenseMapper {

    @Override
    public License toEntity(LicenseCreateReq req, JpaRefResolver ref) {
        if ( req == null ) {
            return null;
        }

        License.LicenseBuilder license = License.builder();

        license.licenseKey( req.getLicenseKey() );
        license.status( req.getStatus() );
        license.issuedAt( req.getIssuedAt() );
        license.expiresAt( req.getExpiresAt() );
        license.seatsTotal( req.getSeatsTotal() );
        license.seatsUsed( req.getSeatsUsed() );

        license.order( ref.toOrder(req.getOrderId()) );
        license.user( ref.toUser(req.getUserId()) );
        license.plan( ref.toPlan(req.getPlanId()) );

        return license.build();
    }

    @Override
    public void update(License entity, LicenseUpdateReq req) {
        if ( req == null ) {
            return;
        }

        if ( req.getStatus() != null ) {
            entity.setStatus( req.getStatus() );
        }
        if ( req.getExpiresAt() != null ) {
            entity.setExpiresAt( req.getExpiresAt() );
        }
        if ( req.getSeatsTotal() != null ) {
            entity.setSeatsTotal( req.getSeatsTotal() );
        }
        if ( req.getSeatsUsed() != null ) {
            entity.setSeatsUsed( req.getSeatsUsed() );
        }
    }

    @Override
    public LicenseResp toResp(License e) {
        if ( e == null ) {
            return null;
        }

        LicenseResp.LicenseRespBuilder licenseResp = LicenseResp.builder();

        licenseResp.orderId( eOrderId( e ) );
        licenseResp.userId( eUserId( e ) );
        licenseResp.planId( ePlanId( e ) );
        licenseResp.id( e.getId() );
        licenseResp.licenseKey( e.getLicenseKey() );
        licenseResp.status( e.getStatus() );
        licenseResp.issuedAt( e.getIssuedAt() );
        licenseResp.expiresAt( e.getExpiresAt() );
        licenseResp.seatsTotal( e.getSeatsTotal() );
        licenseResp.seatsUsed( e.getSeatsUsed() );
        licenseResp.createdAt( e.getCreatedAt() );
        licenseResp.updatedAt( e.getUpdatedAt() );

        return licenseResp.build();
    }

    private UUID eOrderId(License license) {
        if ( license == null ) {
            return null;
        }
        Order order = license.getOrder();
        if ( order == null ) {
            return null;
        }
        UUID id = order.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private UUID eUserId(License license) {
        if ( license == null ) {
            return null;
        }
        User user = license.getUser();
        if ( user == null ) {
            return null;
        }
        UUID id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private UUID ePlanId(License license) {
        if ( license == null ) {
            return null;
        }
        Plan plan = license.getPlan();
        if ( plan == null ) {
            return null;
        }
        UUID id = plan.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
