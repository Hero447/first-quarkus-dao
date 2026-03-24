package com.service.db;

import com.google.protobuf.BoolValue;
import com.google.protobuf.Int64Value;
import com.mapper.CustomerMapper;
import com.proto.service.CustomerList;
import com.repository.CustomerRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;

@ApplicationScoped
@AllArgsConstructor
public class CustomerDbService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    public Uni<com.proto.service.Customer> create(com.proto.service.Customer request) {
        return repository.persist(mapper.customerToEntityNoId(request)).map(mapper::entityToCustomer);
    }

    public Uni<com.proto.service.Customer> update(com.proto.service.Customer request) {
        return repository.update(mapper.customerToEntity(request)).map(mapper::entityToCustomer);
    }

    public Uni<com.proto.service.Customer> findById(Int64Value request) {
        return repository.findById(request.getValue()).map(mapper::entityToCustomer);
    }

    public Uni<CustomerList> list() {
        return repository.listAll().onItem()
                .transform(list -> CustomerList.newBuilder()
                    .addAllResultList(mapper.entityListToCustomerList(list))
                    .setResultCount(Int64Value.of(list.size()))
                    .build());
    }

    public Uni<BoolValue> delete(Int64Value request) {
        return repository.deleteById(request.getValue())
                .map(item -> BoolValue.newBuilder().setValue(item).build());
    }
}
