package fr.bankaccountkata.service;


import fr.bankaccountkata.domain.BankAccount;
import fr.bankaccountkata.domain.Operation;
import fr.bankaccountkata.domain.OperationType;
import fr.bankaccountkata.domain.dto.AccountDto;
import fr.bankaccountkata.repository.BankAccountRepository;
import fr.bankaccountkata.repository.OperationRepository;
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
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class OperationServiceTest {


    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private OperationRepository operationRepository;

    @Mock
    private AccountDtoMapper accountDtoMapper;

    @InjectMocks
    private OperationService operationService;

    private BankAccount account ;
    private Operation operation;
    @Before
    public void setUp(){
        account = new BankAccount();
        account.setBalance(5000);
        account.setId(12L);
        operation = new Operation(Instant.now(), OperationType.DEPOSIT,10000, null);
    }

    @Test(expected = NoSuchAccountException.class)
    public void createAndPerformOperation_should_throw_NoSuchAccountException() throws NoSuchAccountException {
        when(bankAccountRepository.findById(anyLong())).thenReturn(Optional.empty());
        operationService.createAndPerformOperation(12L,0,OperationType.WITHDRAWAL);
        Assert.fail("should have thrown NoSuchAccountException ");

    }

    @Test
    public void createAndPerformOperation_should_perform_deposit() throws NoSuchAccountException {
        when(bankAccountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        long currentAccountBalance = account.getBalance();
        Operation operation = operationService.createAndPerformOperation(12L,1000,OperationType.DEPOSIT);
        assertThat(operation.getAmount()).isEqualTo(1000);
        assertThat(operation.getType()).isEqualTo(OperationType.DEPOSIT);
        assertThat(operation.getAccount()).isNotNull();
        assertThat(operation.getAccount().getBalance()).isEqualTo(currentAccountBalance+1000);
    }

    @Test
    public void createAndPerformOperation_should_perform_withdrawal() throws NoSuchAccountException {
        when(bankAccountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        long currentAccountBalance = account.getBalance();
        Operation operation = operationService.createAndPerformOperation(12L,5000,OperationType.WITHDRAWAL);
        assertThat(operation.getAmount()).isEqualTo(-5000);
        assertThat(operation.getType()).isEqualTo(OperationType.WITHDRAWAL);
        assertThat(operation.getAccount()).isNotNull();
        assertThat(operation.getAccount().getBalance()).isEqualTo(currentAccountBalance-5000);
    }

    @Test(expected = NoSuchAccountException.class)
    public void doDeposit_should_throw_NoSuchAccountException() throws NoSuchAccountException {
        when(bankAccountRepository.findById(anyLong())).thenReturn(Optional.empty());
        operationService.doDeposit(12L,1200);
        Assert.fail("should have thrown NoSuchAccountException ");
    }


    @Test
    public void doDeposit_should_perform_deposit_and_save_op() throws NoSuchAccountException {
        when(bankAccountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        when(accountDtoMapper.mapEntityToDto(any(BankAccount.class))).thenCallRealMethod();
        when(operationRepository.save(any(Operation.class))).thenReturn(operation);
        long currentAccountBalance = account.getBalance();
        AccountDto dto = operationService.doDeposit(12L,1200);
        assertThat(dto.getLatestOperations().size()).isEqualTo(1);
        assertThat(dto.getLatestOperations().get(0).getType()).isEqualTo(OperationType.DEPOSIT);
        assertThat(dto.getLatestOperations().get(0).getAmount()).isEqualTo(1200);
        assertThat(dto.getBalance()).isEqualTo(currentAccountBalance+1200);
    }

    @Test(expected = NoSuchAccountException.class)
    public void doWithdrawal_should_throw_NoSuchAccountException() throws NoSuchAccountException {
        when(bankAccountRepository.findById(anyLong())).thenReturn(Optional.empty());
        operationService.doWithdrawal(12L,1200);
        Assert.fail("should have thrown NoSuchAccountException ");
    }

    @Test
    public void doWithdrawal_should_perform_withdrawal_and_save_op() throws NoSuchAccountException {
        when(bankAccountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        when(accountDtoMapper.mapEntityToDto(any(BankAccount.class))).thenCallRealMethod();
        when(operationRepository.save(any(Operation.class))).thenReturn(operation);
        long currentAccountBalance = account.getBalance();
        AccountDto dto = operationService.doWithdrawal(12L,1200);
        assertThat(dto.getLatestOperations().size()).isEqualTo(1);
        assertThat(dto.getLatestOperations().get(0).getType()).isEqualTo(OperationType.WITHDRAWAL);
        assertThat(dto.getLatestOperations().get(0).getAmount()).isEqualTo(-1200);
        assertThat(dto.getBalance()).isEqualTo(currentAccountBalance-1200);
    }

}
