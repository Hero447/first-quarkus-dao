package com.mapper;


import com.domain.Customer;
import org.mapstruct.Mapper;


import java.util.List;

@Mapper(componentModel = "jakarta")
public interface CustomerMapper {
    Customer customerToEntity(com.proto.service.Customer customer);

    com.proto.service.Customer entityToCustomer(Customer entity);

    List<com.proto.service.Customer> entityListToCustomerList(List<Customer> entityList);

}
