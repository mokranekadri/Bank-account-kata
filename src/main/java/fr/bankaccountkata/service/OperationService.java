package fr.bankaccountkata.service;

import com.google.common.annotations.VisibleForTesting;
import fr.bankaccountkata.domain.BankAccount;
import fr.bankaccountkata.domain.Operation;
import fr.bankaccountkata.domain.OperationType;
import fr.bankaccountkata.domain.dto.AccountDto;
import fr.bankaccountkata.repository.BankAccountRepository;
import fr.bankaccountkata.repository.OperationRepository;
import fr.bankaccountkata.utils.NoSuchAccountException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;

@Service
@Transactional
public class OperationService {

    private final OperationRepository operationRepository;
    private final BankAccountRepository bankAccountRepository;
    private AccountDtoMapper dtoMapper;

    public OperationService(OperationRepository operationRepository, BankAccountRepository bankAccountRepository, AccountDtoMapper dtoMapper) {
        this.operationRepository = operationRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.dtoMapper = dtoMapper;
    }

    /**
     * debits the specified amount on the specified account
     * @param accountId the account identifier
     * @param amount the amount of the transaction
     * @throws NoSuchAccountException
     */
    public AccountDto doWithdrawal(long accountId, long amount) throws NoSuchAccountException {
        Operation operation = createAndPerformOperation(accountId,amount,OperationType.WITHDRAWAL);
        BankAccount bankAccount = bankAccountRepository.findById(accountId).get();
        bankAccount.getOperations().add(operation);
        return dtoMapper.mapEntityToDto(bankAccount);
    }


    /**
     * deposit the specified amount into the specified account
     * @param accountId the account identifier
     * @param amount the amount of the transaction
     * @throws NoSuchAccountException
     */
    public AccountDto doDeposit(long accountId, long amount) throws NoSuchAccountException {
        Operation operation = createAndPerformOperation(accountId,amount,OperationType.DEPOSIT);
        BankAccount bankAccount = bankAccountRepository.findById(accountId).get();
        bankAccount.getOperations().add(operation);
        return dtoMapper.mapEntityToDto(bankAccount);
    }


    /**
     * create and perform the specified operation on the given account
     * @param accountId the account identifier
     * @param amount the amount of the transaction
     * @param operationType the transaction type(debit or credit)
     * @return newly created operation
     * @throws NoSuchAccountException
     */
      @VisibleForTesting
      Operation createAndPerformOperation(long accountId, long amount, OperationType operationType) throws NoSuchAccountException {
        Optional<BankAccount> optionalBankAccount = bankAccountRepository.findById(accountId);
        if(!optionalBankAccount.isPresent()){
            throw new NoSuchAccountException(": "+accountId);
        }
        BankAccount account = optionalBankAccount.get();
        int opType = operationType.equals(OperationType.WITHDRAWAL) ? -1 : 1;
        Operation operation = new Operation();
        operation.setAmount(opType*amount);
        operation.setDate(Instant.now());
        operation.setAccount(account);
        operation.setType(operationType);
        account.balance+=opType*amount;
        operationRepository.save(operation);
        return operation;
    }
}
