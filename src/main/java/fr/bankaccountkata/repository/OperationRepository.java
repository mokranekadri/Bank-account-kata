package fr.bankaccountkata.repository;

import fr.bankaccountkata.domain.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepository extends JpaRepository<Operation,Long> {
}
