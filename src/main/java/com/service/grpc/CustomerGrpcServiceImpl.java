package com.service.grpc;

import com.google.protobuf.BoolValue;
import com.google.protobuf.Empty;
import com.google.protobuf.Int64Value;
import com.proto.service.CustomerList;
import com.proto.service.CustomerService;
import com.service.db.CustomerDbService;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@GrpcService
@AllArgsConstructor
public class CustomerGrpcServiceImpl implements CustomerService {

    private final CustomerDbService dbService;

    @Override
    public Uni<com.proto.service.Customer> create(com.proto.service.Customer request) {
        log.info("Creating a new customer.");
        return dbService.create(request);
    }

    @Override
    public Uni<com.proto.service.Customer> update(com.proto.service.Customer request) {
        log.info("Updating the customer. id: " + request.getId());
        return dbService.update(request);
    }

    @Override
    public Uni<com.proto.service.Customer> findById(Int64Value request) {
        log.info("Finding the customer. id: " + request.getValue());
        return dbService.findById(request);
    }

    @Override
    public Uni<CustomerList> list(Empty request) {
        log.info("Listing all customers.");
        return dbService.list();
    }

    @Override
    public Uni<BoolValue> delete(Int64Value request) {
        log.info("Deleting customer by id. id: " + request.getValue());
        return dbService.delete(request);
    }
}
