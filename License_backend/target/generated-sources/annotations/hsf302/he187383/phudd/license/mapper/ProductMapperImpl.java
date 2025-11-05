package hsf302.he187383.phudd.license.mapper;

import hsf302.he187383.phudd.license.dto.product.ProductCreateReq;
import hsf302.he187383.phudd.license.dto.product.ProductResp;
import hsf302.he187383.phudd.license.dto.product.ProductUpdateReq;
import hsf302.he187383.phudd.license.model.Product;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-04T12:50:44+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Microsoft)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public Product toEntity(ProductCreateReq req) {
        if ( req == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();

        product.code( req.getCode() );
        product.name( req.getName() );
        product.description( req.getDescription() );

        return product.build();
    }

    @Override
    public void update(Product entity, ProductUpdateReq req) {
        if ( req == null ) {
            return;
        }

        if ( req.getCode() != null ) {
            entity.setCode( req.getCode() );
        }
        if ( req.getName() != null ) {
            entity.setName( req.getName() );
        }
        if ( req.getDescription() != null ) {
            entity.setDescription( req.getDescription() );
        }
    }

    @Override
    public ProductResp toResp(Product e) {
        if ( e == null ) {
            return null;
        }

        ProductResp.ProductRespBuilder productResp = ProductResp.builder();

        productResp.id( e.getId() );
        productResp.code( e.getCode() );
        productResp.name( e.getName() );
        productResp.description( e.getDescription() );
        productResp.createdAt( e.getCreatedAt() );
        productResp.updatedAt( e.getUpdatedAt() );

        return productResp.build();
    }
}
