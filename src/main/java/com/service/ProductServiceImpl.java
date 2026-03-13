package com.service;

import com.domain.Product;
import com.google.protobuf.BoolValue;
import com.google.protobuf.Empty;
import com.google.protobuf.Int64Value;
import com.mapper.ProductMapper;
import com.proto.service.*;
import com.repository.ProductRepository;
import io.quarkus.grpc.GrpcService;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@GrpcService
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    public ProductServiceImpl(ProductRepository repository, ProductMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @WithTransaction
    public Uni<com.proto.service.Product> create(com.proto.service.Product request) {
        log.info("Creating a new product.");
        Product entity = mapper.productToEntity(request);
        entity.setId(null);
        return repository.persistAndFlush(entity).map(mapper::entityToProduct);
    }

    @Override
    @WithTransaction
    public Uni<com.proto.service.Product> update(com.proto.service.Product request) {
        log.info("Updating product. id: " + request.getId());
        Product entity = mapper.productToEntity(request);
        return repository.findById(request.getId())
                .onItem().ifNull().fail()
                .onItem().ifNotNull().transformToUni(saved ->
                {
                    saved.setName(request.getName());
                    saved.setPrice(request.getPrice());
                    return repository.persistAndFlush(saved).onItem().transform(mapper::entityToProduct);
                });
    }

    @Override
    @WithTransaction
    public Uni<com.proto.service.Product> findById(Int64Value request) {
        log.info("Finding product. id: " + request.getValue());
        Uni<Product> entity = repository.findById(request.getValue());
        return entity.onItem().ifNull().fail().map(mapper::entityToProduct);
    }

    @Override
    @WithTransaction
    public Uni<ProductList> list(Empty request) {
        log.info("Listing all products.");
        Uni<List<Product>> entityList = repository.listAll();
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
