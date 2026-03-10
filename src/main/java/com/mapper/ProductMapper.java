package com.mapper;

import com.domain.Product;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ProductMapper {
    public Product productToEntity(com.proto.service.Product product) {
        if ( product == null ) {
            return null;
        }

        Product productEntity = new Product();
        productEntity.setId(product.getId());
        productEntity.setName(product.getName());
        productEntity.setPrice(product.getPrice());

        return productEntity;
    }

    public com.proto.service.Product entityToProduct(Product entity) {
        if ( entity == null ) {
            return null;
        }

        com.proto.service.Product.Builder product = com.proto.service.Product.newBuilder();

        return product.setId(entity.getId()).setName(entity.getName()).setPrice(entity.getPrice()).build();
    }


    public List<com.proto.service.Product> entityListToProductList(List<Product> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<com.proto.service.Product> list = new ArrayList<com.proto.service.Product>( entityList.size() );
        for ( Product product : entityList ) {
            list.add( entityToProduct(product) );
        }

        return list;
    }
}
