package fr.bankaccountkata.service;


import fr.bankaccountkata.domain.BankAccount;
import fr.bankaccountkata.domain.Operation;
import fr.bankaccountkata.domain.OperationType;
import fr.bankaccountkata.domain.dto.AccountDto;
import fr.bankaccountkata.repository.BankAccountRepository;
import fr.bankaccountkata.utils.NoSuchAccountException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class BankAccountServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private AccountDtoMapper accountDtoMapper;

    @InjectMocks
    private BankAccountService bankAccountService;

    private List<Operation> operations;
    private BankAccount account ;
    @Before
    public void setUp(){
        account = new BankAccount();
        account.setBalance(5000);
        account.setId(12L);
        operations = new ArrayList<>();
        operations.add(new Operation(Instant.now(), OperationType.DEPOSIT,10000,account));
        account.setOperations(operations);
    }

    @Test(expected = NoSuchAccountException.class)
    public void listAllOperations_should_throw_exception_for_no_such_account() throws Exception {
        when(bankAccountRepository.findById(anyLong())).thenReturn(Optional.empty());
        bankAccountService.listAllOperations(12L);
        Assert.fail("should have thrown NoSuchAccountException ");
    }


    @Test
    public void listAllOperations_should_successfully_return_all_account_operations() throws NoSuchAccountException {
        when(bankAccountRepository.findById(12L)).thenReturn(Optional.of(account));
        when(accountDtoMapper.mapEntityToDto(any(BankAccount.class))).thenCallRealMethod();
        List<Operation> operations = bankAccountService.listAllOperations(12L);
        assertThat(operations).isNotEmpty();
        assertThat(operations).hasSize(1);
    }

    @Test(expected = NoSuchAccountException.class)
    public void printStatement_should_throw_exception_for_no_such_account() throws NoSuchAccountException {
        when(bankAccountRepository.findById(anyLong())).thenReturn(Optional.empty());
        bankAccountService.printStatement(12L);
        Assert.fail("should have thrown NoSuchAccountException ");
    }

    @Test
    public void printStatement_should_successfully_return_current_account_balance() throws NoSuchAccountException {
        when(bankAccountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        when(accountDtoMapper.mapEntityToDto(any(BankAccount.class))).thenCallRealMethod();
        AccountDto accountDto = bankAccountService.printStatement(12L);
        assertThat(accountDto.getBalance()).isEqualTo(account.getBalance());
        assertThat(accountDto.getLatestOperations()).isNotEmpty();
        assertThat(accountDto.getLatestOperations()).hasSameSizeAs(account.getOperations());

        Operation operation = new Operation(Instant.now().minusSeconds(10000), OperationType.DEPOSIT,10000,account);
        account.getOperations().add(operation);
        when(bankAccountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        accountDto = bankAccountService.printStatement(12L);
        assertThat(accountDto.getLatestOperations()).hasSize(2);
        assertThat(accountDto.getLatestOperations()).isSortedAccordingTo(Comparator.comparing(Operation::getDate).reversed());

    }




}
