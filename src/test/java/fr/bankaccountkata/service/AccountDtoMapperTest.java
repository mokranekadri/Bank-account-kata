package fr.bankaccountkata.service;

import fr.bankaccountkata.domain.BankAccount;
import fr.bankaccountkata.domain.Operation;
import fr.bankaccountkata.domain.OperationType;
import fr.bankaccountkata.domain.dto.AccountDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.Comparator;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
public class AccountDtoMapperTest {

    private AccountDtoMapper dtoMapper;

    private BankAccount account;

    @Before
    public void setUp(){
        dtoMapper = new AccountDtoMapper();
        account = new BankAccount();
        account.setId(12L);
        account.setBalance(50000);
        for(int i =0;i<10;i++) {
            Operation operation = new Operation(
                    Instant.now().minusSeconds(i), (i % 2 == 0) ? OperationType.DEPOSIT : OperationType.WITHDRAWAL, 10000, account);

            account.getOperations().add(operation);
        }
    }

    @Test
    public void mapEntityToDto_should_return_account_overview(){
        AccountDto accountDto = dtoMapper.mapEntityToDto(account);
        assertThat(accountDto.getBalance()).isEqualTo(account.getBalance());
        assertThat(accountDto.getLatestOperations()).hasSize(5);
        assertThat(accountDto.getLatestOperations().size()).isLessThanOrEqualTo(account.getOperations().size());
        assertThat(accountDto.getLatestOperations()).isSortedAccordingTo(Comparator.comparing(Operation::getDate).reversed());
    }

}

