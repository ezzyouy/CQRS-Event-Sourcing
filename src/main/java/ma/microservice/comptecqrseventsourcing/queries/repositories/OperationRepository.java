package ma.microservice.comptecqrseventsourcing.queries.repositories;

import ma.microservice.comptecqrseventsourcing.queries.entities.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepository extends JpaRepository<Operation,Long> {
}
