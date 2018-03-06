package fr.bankaccountkata.controller;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fr.bankaccountkata.BankAccountApp;
import fr.bankaccountkata.domain.BankAccount;
import fr.bankaccountkata.domain.Operation;
import fr.bankaccountkata.domain.OperationType;
import fr.bankaccountkata.domain.dto.OperationCommand;
import fr.bankaccountkata.repository.BankAccountRepository;
import fr.bankaccountkata.repository.OperationRepository;
import fr.bankaccountkata.service.BankAccountService;
import fr.bankaccountkata.service.OperationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;

import static fr.bankaccountkata.helper.TestHelper.convertObjectToJsonBytes;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BankAccountApp.class)
public class BankAccountControllerTest {
    @Autowired
    private OperationService operationService;

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private GlobalErrorHandler globalErrorHandler;

    private MockMvc restMvc;


    @Before
    public void setUp() {

        BankAccountResources bankAccountResources = new BankAccountResources(bankAccountService, operationService);
        this.restMvc = MockMvcBuilders.standaloneSetup(bankAccountResources).setControllerAdvice(globalErrorHandler)
                .build();

    }

    @Test
    public void printAccountState_should_return_error_message_and_404_code_status() throws Exception {
        restMvc.perform(get("/api/accounts/155555555")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Transactional
    public void printAccountState_should_return_account_details() throws Exception {
        BankAccount account = new BankAccount();
        account.setBalance(1000);
        account.setOperations(new ArrayList<>());
        bankAccountRepository.saveAndFlush(account);
        restMvc.perform(get("/api/accounts/{id}", account.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.latestOperations").isEmpty())
                .andExpect(jsonPath("$.balance").value(account.getBalance()));
    }

    @Test
    public void deposit_should_return_error_message_and_404_code_status() throws Exception {

        restMvc.perform(put("/api/accounts/555555/deposit")
                .accept(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(new OperationCommand(2522L))))
                .andExpect(status().is4xxClientError());

    }

    @Test
    @Transactional
    public void deposit_should_perform_a_deposit_operation() throws Exception {
        BankAccount account = new BankAccount();
        account.setBalance(0);
        account.setOperations(new ArrayList<>());
        bankAccountRepository.saveAndFlush(account);
        restMvc.perform(put("/api/accounts/{accountId}/deposit", account.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(new OperationCommand(15000))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.latestOperations").isNotEmpty())
                .andExpect(jsonPath("$.balance").value(15000));

    }

    @Test
    public void withdrawal_should_return_error_message_and_404_code_status() throws Exception {

        restMvc.perform(put("/api/accounts/{accountId}/withdrawal", 575556L)
                .accept(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(new OperationCommand(2522))))
                .andExpect(status().is4xxClientError());

    }

    @Test
    @Transactional
    public void withdrawal_should_perform_a_withdrawal_operation() throws Exception {
        BankAccount account = new BankAccount();
        account.setBalance(0);
        account.setOperations(new ArrayList<>());
        bankAccountRepository.saveAndFlush(account);
        restMvc.perform(put("/api/accounts/{accountId}/withdrawal", account.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(new OperationCommand(200))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.latestOperations").isNotEmpty())
                .andExpect(jsonPath("$.balance").value(-200));
    }

    @Test
    @Transactional
    public void showOperationsList_should_list_all_previous_operations() throws Exception {
        BankAccount account = new BankAccount();
        account.setBalance(0);
        account.setOperations(new ArrayList<>());
        bankAccountRepository.saveAndFlush(account);
        Operation operation = new Operation();
        operation.setAccount(account);
        operation.setType(OperationType.WITHDRAWAL);
        operation.setAmount(2000L);
        operationRepository.saveAndFlush(operation);
        Operation operation2 = new Operation();
        operation2.setAccount(account);
        operation2.setType(OperationType.DEPOSIT);
        operation2.setAmount(2000L);
        operationRepository.saveAndFlush(operation2);
        account.getOperations().add(operation);
        account.getOperations().add(operation2);
        restMvc.perform(get("/api/accounts/{id}/history", account.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*]").isNotEmpty())
                .andExpect(jsonPath("$.[*].id").value(hasItems(operation.getId().intValue(),operation2.getId().intValue())))
                .andExpect(jsonPath("$.[*].amount").value(hasItems(operation.getAmount().intValue(),operation2.getAmount().intValue())))
                .andExpect(jsonPath("$.[*].type").value(hasItems(operation.getType().toString(),operation2.getType().toString())));
    }

    @Test
    public void showOperationsList_should_return_error_message_and_404_code_status() throws Exception {

        restMvc.perform(get("/api/accounts/{id}/history", 5858585)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}