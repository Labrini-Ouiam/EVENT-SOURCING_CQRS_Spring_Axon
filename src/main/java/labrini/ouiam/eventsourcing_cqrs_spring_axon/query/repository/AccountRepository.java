package labrini.ouiam.eventsourcing_cqrs_spring_axon.query.repository;

import labrini.ouiam.eventsourcing_cqrs_spring_axon.query.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
}
