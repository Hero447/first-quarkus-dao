package com.mapper;


import com.domain.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;


import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA)
public interface CustomerMapper {
    @Mapping(target = "id", expression = "java(null)")
    Customer customerToEntity(com.proto.service.Customer customer);

    com.proto.service.Customer entityToCustomer(Customer entity);

    List<com.proto.service.Customer> entityListToCustomerList(List<Customer> entityList);

    void updateEntity(com.proto.service.Customer source, @MappingTarget Customer target);
}
