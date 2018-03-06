package fr.bankaccountkata.controller;


import fr.bankaccountkata.domain.Operation;
import fr.bankaccountkata.domain.dto.AccountDto;
import fr.bankaccountkata.domain.dto.OperationCommand;
import fr.bankaccountkata.service.BankAccountService;
import fr.bankaccountkata.service.OperationService;
import fr.bankaccountkata.utils.NoSuchAccountException;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts/")
public class BankAccountResources {

    private final BankAccountService bankAccountService;
    private final OperationService operationService;

    public BankAccountResources(BankAccountService bankAccountService, OperationService operationService) {
        this.bankAccountService = bankAccountService;
        this.operationService = operationService;
    }

    @ApiOperation(value = "printAccountState",notes = "return given account state and recent operations")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = AccountDto.class),
            @ApiResponse(code = 404, message = "Bad request"),
    })
    @GetMapping("{accountId}")
    public AccountDto printAccountState(@PathVariable long accountId) throws NoSuchAccountException {
        return bankAccountService.printStatement(accountId);
    }

    @ApiOperation(value = "showOperationsList",notes = "lists all given account operations")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success",responseContainer = "list",response = Operation.class),
            @ApiResponse(code = 404, message = "Bad request"),
    })
    @GetMapping("{accountId}/history")
    public List<Operation> showOperationsList(@PathVariable long accountId) throws NoSuchAccountException {
        return bankAccountService.listAllOperations(accountId);
    }


    @ApiOperation(value = "deposit",notes = "perfom a deposit on the given account")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success",response = AccountDto.class),
            @ApiResponse(code = 404, message = "Bad request"),
    })
    @PutMapping(value = "{accountId}/deposit")
    public AccountDto deposit(@PathVariable long accountId,
                        @RequestBody OperationCommand operationCommand) throws NoSuchAccountException {
       return operationService.doDeposit(accountId,operationCommand.getAmount());
    }


    @ApiOperation(value = "withdrawall",notes = "perfom a withdrawal on the given account")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success",response = AccountDto.class),
            @ApiResponse(code = 404, message = "Bad request"),
    })
    @PutMapping(value = "{accountId}/withdrawal")
    public AccountDto withdrawal(@PathVariable long accountId,
                           @RequestBody OperationCommand operationCommand) throws NoSuchAccountException {
        return operationService.doWithdrawal(accountId,operationCommand.getAmount());
    }
}
