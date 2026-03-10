package com.mapper;


import com.domain.Customer;
import jakarta.enterprise.context.ApplicationScoped;


import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class CustomerMapper {
    public Customer customerToEntity(com.proto.service.Customer customer) {
        if ( customer == null ) {
            return null;
        }

        Customer customerEntity = new Customer();
        customerEntity.setId(customer.getId());
        customerEntity.setName(customer.getName());
        customerEntity.setEmail(customer.getEmail());

        return customerEntity;
    }

    public com.proto.service.Customer entityToCustomer(Customer entity) {
        if ( entity == null ) {
            return null;
        }

        com.proto.service.Customer.Builder customer = com.proto.service.Customer.newBuilder();

        return customer.setId(entity.getId()).setName(entity.getName()).setEmail(entity.getEmail()).build();
    }

    public List<com.proto.service.Customer> entityListToCustomerList(List<Customer> entityList) {
        if ( entityList == null ) {
            return null;
        }

        List<com.proto.service.Customer> list = new ArrayList<com.proto.service.Customer>( entityList.size() );
        for ( Customer customer : entityList ) {
            list.add( entityToCustomer(customer) );
        }

        return list;
    }

}
