package com.service;

import com.domain.Product;
import com.google.protobuf.BoolValue;
import com.google.protobuf.Int64Value;
import com.mapper.ProductMapper;
import com.proto.service.*;
import com.repository.ProductRepository;
import io.quarkus.grpc.GrpcService;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@GrpcService
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    @Override
    @WithTransaction
    public Uni<com.proto.service.Product> create(com.proto.service.Product request) {
        log.info("Creating a new product.");
        return repository.persistAndFlush(mapper.productToEntity(request)).map(mapper::entityToProduct);
    }

    @Override
    @WithTransaction
    public Uni<com.proto.service.Product> update(com.proto.service.Product request) {
        log.info("Updating product. id: " + request.getId());
        return repository.findById(request.getId())
                .onItem().ifNull()
                .failWith(()-> new EntityNotFoundException("Product with ID " + request.getId() + " not found"))
                .onItem().ifNotNull().transformToUni(saved ->
                {
                    mapper.updateEntity(request, saved);
                    return repository.persistAndFlush(saved).onItem().transform(mapper::entityToProduct);
                });
    }

    @Override
    @WithTransaction
    public Uni<com.proto.service.Product> findById(Int64Value request) {
        log.info("Finding product. id: " + request.getValue());
        Uni<Product> entity = repository.findById(request.getValue());
        return entity.onItem().ifNull()
                .failWith(()-> new EntityNotFoundException("Product with ID " + request + " not found"))
                .map(mapper::entityToProduct);
    }

    @Override
    @WithTransaction
    public Uni<ProductList> list(ProductFilter productFilter) {
        log.info("Listing all products.");
        StringBuilder query = new StringBuilder("1=1");
        Map<String, Object> params = new HashMap<>();

        if (productFilter.hasMinPrice()) {
            query.append(" and price >= :minPrice");
            params.put("minPrice", productFilter.getMinPrice());
        }

        if (productFilter.hasMaxPrice()) {
            query.append(" and price <= :maxPrice");
            params.put("maxPrice", productFilter.getMaxPrice());
        }

        Uni<List<Product>> entityList = repository.list(query.toString(), params);
        return entityList.onItem().transform(list -> ProductList.newBuilder()
                .addAllResultList(mapper.entityListToProductList(list))
                .setResultCount(Int64Value.of(list.size()))
                .build());
    }

    @Override
    @WithTransaction
    public Uni<BoolValue> delete(Int64Value request) {
        log.info("Deleting product by id. id: " + request.getValue());
        return repository.deleteById(request.getValue())
                .map(item -> BoolValue.newBuilder().setValue(item).build());
    }
}
