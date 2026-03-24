package com.mapper;

import com.domain.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA)
public interface ProductMapper {
    @Mapping(target = "id", expression = "java(null)")
    Product productToEntityNoId(com.proto.service.Product product);

    Product productToEntity(com.proto.service.Product product);

    com.proto.service.Product entityToProduct(Product entity);

    List<com.proto.service.Product> entityListToProductList(List<Product> entityList);
}
