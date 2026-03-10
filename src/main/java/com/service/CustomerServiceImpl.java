package com.service;

import com.domain.Customer;
import com.google.protobuf.BoolValue;
import com.google.protobuf.Empty;
import com.google.protobuf.Int64Value;
import com.mapper.CustomerMapper;
import com.proto.service.CustomerList;
import com.proto.service.CustomerService;
import com.repository.CustomerRepository;
import io.quarkus.grpc.GrpcService;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@GrpcService
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    public CustomerServiceImpl(CustomerRepository repository, CustomerMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @WithTransaction
    public Uni<com.proto.service.Customer> create(com.proto.service.Customer request) {
        log.info("Creating a new customer.");
        Customer entity = mapper.customerToEntity(request);
        entity.setId(null);
        return repository.persistAndFlush(entity).map(mapper::entityToCustomer);
    }

    @Override
    @WithTransaction
    public Uni<com.proto.service.Customer> update(com.proto.service.Customer request) {
        log.info("Updating the customer. id: " + request.getId());
        Customer entity = mapper.customerToEntity(request);
        return repository.findById(request.getId())
                .onItem().ifNull().fail()
                .onItem().ifNotNull().transformToUni(saved ->
                {
                    saved.setName(request.getName());
                    saved.setEmail(request.getEmail());
                    return repository.persistAndFlush(saved).onItem().transform(mapper::entityToCustomer);
                });
    }

    @Override
    @WithTransaction
    public Uni<com.proto.service.Customer> findById(Int64Value request) {
        log.info("Finding the customer. id: " + request.getValue());
        Uni<Customer> entity = repository.findById(request.getValue());
        return entity.onItem().ifNull().fail().map(mapper::entityToCustomer);
    }

    @Override
    @WithTransaction
    public Uni<CustomerList> list(Empty request) {
        log.info("Listing all users.");
        Uni<List<Customer>> entityList = repository.listAll();
        return entityList.onItem().transform(list -> CustomerList.newBuilder()
                .addAllResultList(mapper.entityListToCustomerList(list))
                .setResultCount(Int64Value.of(list.size()))
                .build());
    }

    @Override
    @WithTransaction
    public Uni<BoolValue> delete(Int64Value request) {
        log.info("Deleting customer by id. id: " + request.getValue());
        return repository.deleteById(request.getValue()).map(item -> BoolValue.newBuilder().setValue(item).build());
    }
}
