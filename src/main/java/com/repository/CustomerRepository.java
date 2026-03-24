package com.repository;

import com.domain.Customer;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.List;

@ApplicationScoped
public class CustomerRepository {

    @Inject
    Mutiny.SessionFactory sessionFactory;

    public Uni<Customer> persist(Customer customer) {
        return sessionFactory.withTransaction((session, tx) ->
                session.persist(customer)
                        .chain(session::flush)
                        .replaceWith(customer)
        );
    }

    public Uni<Customer> update(Customer customer) {
        return sessionFactory.withTransaction((session, tx) ->
                session.merge(customer)
        );
    }

    public Uni<Customer> findById(Long id) {
        return sessionFactory.withSession(session ->
                session.find(Customer.class, id)
        );
    }

    public Uni<List<Customer>> listAll() {
        return sessionFactory.withSession(session -> {
            CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
            CriteriaQuery<Customer> query = cb.createQuery(Customer.class);
            Root<Customer> root = query.from(Customer.class);

            query.select(root);

            return session.createQuery(query).getResultList();
        });
    }

    public Uni<Boolean> deleteById(Long id) {
        return sessionFactory.withTransaction(session ->
                session.find(Customer.class, id)
                        .chain(customer -> {
                            if (customer != null) {
                                return session.remove(customer).map(v -> true);
                            }
                            return Uni.createFrom().item(false);
                        })
        );
    }
}
