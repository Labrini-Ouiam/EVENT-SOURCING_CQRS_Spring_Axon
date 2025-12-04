package labrini.ouiam.eventsourcing_cqrs_spring_axon.query.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import labrini.ouiam.eventsourcing_cqrs_spring_axon.enums.AccountStatus;
import lombok.*;

import java.time.Instant;

@Entity @AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder
public class Account {
    @Id
    private String id;
    private double balance;
    private String currency;
    private Instant createdAt;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
}
