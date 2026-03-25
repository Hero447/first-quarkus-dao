package com.service.db;


import com.domain.Customer;
import io.quarkus.scheduler.Scheduled;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.hibernate.reactive.mutiny.Mutiny;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
@Slf4j
public class CustomerInactivityManager {
    @Inject
    Mutiny.SessionFactory sessionFactory;

    @ConfigProperty(name = "scheduler.customer-inactive.max-period")
    Duration maxInactivity;

    @Scheduled(every = "${scheduler.customer-inspection.interval}")
    Uni<Void> checkActiveCustomers() {
        LocalDateTime baseTime = LocalDateTime.now();

        return sessionFactory.withTransaction((session, tx) ->
                        listOfActiveCustomers(session)
                                .onItem().transform(list -> list.stream()
                                        .filter(c -> c.getLastActivity().plus(maxInactivity).isBefore(baseTime))
                                        .toList()
                                )
                                .onItem().transformToUni(inactiveCustomers -> {
                                    if (inactiveCustomers.isEmpty()) {
                                        log.debug("Activity check: no inactive customers found.");
                                        return Uni.createFrom().voidItem();
                                    }

                                    log.info("Found {} inactive customers:", inactiveCustomers.size());
                                    inactiveCustomers.forEach(c -> log.info("Inactive customer: {}", c));

                                    return updateCustomersActiveStatusToFalse(session, inactiveCustomers)
                                            .replaceWithVoid();
                                })
                )
                .onItem().invoke(() -> log.info("Customer activity check completed successfully."))
                .onFailure().invoke(failure -> log.error("Error occurred during customer activity check: ", failure));
    }

    private Uni<List<Customer>> listOfActiveCustomers(Mutiny.Session session) {
        CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Customer> query = cb.createQuery(Customer.class);
        Root<Customer> root = query.from(Customer.class);
        query.select(root).where(cb.isTrue(root.get("active")));

        return session.createQuery(query).getResultList();
    }

    private Uni<List<Customer>> updateCustomersActiveStatusToFalse(Mutiny.Session session, List<Customer> customers) {
        List<Uni<Customer>> updateTasks = customers.stream()
                .map(c -> {
                    c.setActive(false);
                    return session.merge(c);
                })
                .toList();

        return Uni.combine().all().unis(updateTasks)
                .with(results -> (List<Customer>) (List<?>) results);
    }
}
