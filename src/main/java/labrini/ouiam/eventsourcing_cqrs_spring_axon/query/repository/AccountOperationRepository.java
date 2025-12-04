package labrini.ouiam.eventsourcing_cqrs_spring_axon.query.repository;

import labrini.ouiam.eventsourcing_cqrs_spring_axon.query.entities.AccountOperation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {
    List<AccountOperation> findByAccountId(String Id);
}
