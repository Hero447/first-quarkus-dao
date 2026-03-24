package com.service.db;

import com.google.protobuf.BoolValue;
import com.google.protobuf.Int64Value;
import com.mapper.ProductMapper;
import com.proto.service.ProductFilter;
import com.proto.service.ProductList;
import com.repository.ProductRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;

@ApplicationScoped
@AllArgsConstructor
public class ProductDbService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    public Uni<com.proto.service.Product> create(com.proto.service.Product request) {
        return repository.persist(mapper.productToEntityNoId(request)).map(mapper::entityToProduct);
    }

    public Uni<com.proto.service.Product> update(com.proto.service.Product  request) {
        return repository.update(mapper.productToEntity(request)).map(mapper::entityToProduct);
    }

    public Uni<com.proto.service.Product> findById(Int64Value request) {
        return repository.findById(request.getValue()).map(mapper::entityToProduct);
    }

    public Uni<ProductList> list(ProductFilter productFilter) {
        return repository.listAll(productFilter).onItem()
                .transform(list -> ProductList.newBuilder()
                    .addAllResultList(mapper.entityListToProductList(list))
                    .setResultCount(Int64Value.of(list.size()))
                    .build());
    }

    public Uni<BoolValue> delete(Int64Value request) {
        return repository.deleteById(request.getValue())
                .map(item -> BoolValue.newBuilder().setValue(item).build());
    }
}
