package com.repository;

import com.domain.Customer;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Blocking
public class CustomerRepository implements PanacheRepository<Customer> {
}
