package com.service.grpc;

import com.google.protobuf.BoolValue;
import com.google.protobuf.Int64Value;
import com.proto.service.*;
import com.service.db.ProductDbService;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@GrpcService
@AllArgsConstructor
public class ProductGrpcServiceImpl implements ProductService {

    private final ProductDbService dbService;

    @Override
    public Uni<com.proto.service.Product> create(com.proto.service.Product request) {
        log.info("Creating a new product.");
        return dbService.create(request);
    }

    @Override
    public Uni<com.proto.service.Product> update(com.proto.service.Product request) {
        log.info("Updating product. id: " + request.getId());
        return dbService.update(request);
    }

    @Override
    public Uni<com.proto.service.Product> findById(Int64Value request) {
        log.info("Finding product. id: " + request.getValue());
        return dbService.findById(request);
    }

    @Override
    public Uni<ProductList> list(ProductFilter productFilter) {
        log.info("Listing all products.");
        return dbService.list(productFilter);
    }

    @Override
    public Uni<BoolValue> delete(Int64Value request) {
        log.info("Deleting product by id. id: " + request.getValue());
        return dbService.delete(request);
    }
}
