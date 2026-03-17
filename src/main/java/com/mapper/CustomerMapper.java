package com.mapper;


import com.domain.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;


import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA)
public interface CustomerMapper {
    Customer customerToEntity(com.proto.service.Customer customer);

    com.proto.service.Customer entityToCustomer(Customer entity);

    List<com.proto.service.Customer> entityListToCustomerList(List<Customer> entityList);

}
