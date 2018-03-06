package fr.bankaccountkata.service;


import fr.bankaccountkata.domain.BankAccount;
import fr.bankaccountkata.domain.Operation;
import fr.bankaccountkata.domain.dto.AccountDto;
import fr.bankaccountkata.repository.BankAccountRepository;
import fr.bankaccountkata.repository.OperationRepository;
import fr.bankaccountkata.utils.NoSuchAccountException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final AccountDtoMapper accountDtoMapper;

    public BankAccountService(BankAccountRepository bankAccountRepository, AccountDtoMapper accountDtoMapper) {
        this.bankAccountRepository = bankAccountRepository;
        this.accountDtoMapper = accountDtoMapper;
    }

    /**
     *
     * @param accountId account identifier
     * @return all operations on a given account
     * @throws NoSuchAccountException
     */
    public List<Operation> listAllOperations(long accountId) throws NoSuchAccountException {
        Optional<BankAccount> optionalBankAccount = bankAccountRepository.findById(accountId);
        if(!optionalBankAccount.isPresent()){
            throw new NoSuchAccountException(": "+accountId);
        }
        return optionalBankAccount.get().operations;
    }

    /**
     *
     * @param accountId account identifier
     * @return  the account state including latest operations
     * @throws NoSuchAccountException
     */
    public AccountDto printStatement(long accountId) throws NoSuchAccountException {
        Optional<BankAccount> optionalBankAccount = bankAccountRepository.findById(accountId);
        if(!optionalBankAccount.isPresent()){
            throw new NoSuchAccountException(": "+accountId);
        }
        return accountDtoMapper.mapEntityToDto(optionalBankAccount.get());
    }
}
