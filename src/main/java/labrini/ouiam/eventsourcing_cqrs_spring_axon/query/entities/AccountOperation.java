package labrini.ouiam.eventsourcing_cqrs_spring_axon.query.entities;

import jakarta.persistence.*;
import labrini.ouiam.eventsourcing_cqrs_spring_axon.enums.OperationType;
import lombok.*;

import java.time.Instant;

@Entity @AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder
public class AccountOperation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Instant date;
    private double amount;
    @Enumerated(EnumType.STRING)
    private OperationType type;
    private String currency;
    @ManyToOne
    private Account account;
}
