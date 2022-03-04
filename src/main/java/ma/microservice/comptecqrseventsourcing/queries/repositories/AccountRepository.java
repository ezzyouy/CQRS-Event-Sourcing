package ma.microservice.comptecqrseventsourcing.queries.repositories;

import ma.microservice.comptecqrseventsourcing.queries.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,String> {
}
