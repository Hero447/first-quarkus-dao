package com.repository;

import com.domain.Product;
import com.proto.service.ProductFilter;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ProductRepository {

    @Inject
    Mutiny.SessionFactory sessionFactory;

    public Uni<Product> persist(Product product) {
        return sessionFactory.withTransaction((session, tx) ->
                session.persist(product)
                        .chain(session::flush)
                        .replaceWith(product)
        );
    }

    public Uni<Product> update(Product product) {
        return sessionFactory.withTransaction((session, tx) ->
                session.merge(product)
        );
    }

    public Uni<Product> findById(Long id) {
        return sessionFactory.withSession(session ->
                session.find(Product.class, id)
        );
    }

    public Uni<List<Product>> listAll(ProductFilter productFilter) {
        return sessionFactory.withSession(session -> {
            CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
            CriteriaQuery<Product> query = cb.createQuery(Product.class);
            Root<Product> root = query.from(Product.class);

            List<Predicate> predicates = new ArrayList<>();

            if (productFilter.hasMinPrice()) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), productFilter.getMinPrice()));
            }

            if (productFilter.hasMaxPrice()) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), productFilter.getMaxPrice()));
            }

            query.select(root).where(cb.and(predicates.toArray(new Predicate[0])));

            return session.createQuery(query).getResultList();
        });
    }

    public Uni<Boolean> deleteById(Long id) {
        return sessionFactory.withTransaction(session ->
                session.find(Product.class, id)
                        .chain(product -> {
                            if (product != null) {
                                return session.remove(product).map(v -> true);
                            }
                            return Uni.createFrom().item(false);
                        })
        );
    }
}
