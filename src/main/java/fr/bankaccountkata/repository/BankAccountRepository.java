package fr.bankaccountkata.repository;

import fr.bankaccountkata.domain.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository  extends JpaRepository<BankAccount,Long>{
}
