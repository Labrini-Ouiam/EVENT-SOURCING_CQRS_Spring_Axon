package labrini.ouiam.eventsourcing_cqrs_spring_axon.query.queries;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class WatchEventQuery {
    private String AccountId;
}
