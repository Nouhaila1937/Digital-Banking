package org.example.digitale_banking.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.digitale_banking.Dtos.AccountHistoryDTO;
import org.example.digitale_banking.Dtos.BankAccountDTO;
import org.example.digitale_banking.Dtos.OperationsDTO;
import org.example.digitale_banking.exceptions.BankAccountException;
import org.example.digitale_banking.service.BankAccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@Slf4j
@AllArgsConstructor
public class BankAccountRestController {
    BankAccountService bankAccountService;

    @GetMapping("/accounts/{id}")
    public BankAccountDTO getBankAccount(@PathVariable String id) throws BankAccountException {
        BankAccountDTO bankAccountDTO = bankAccountService.getBankAccount(id);
        return bankAccountDTO;
    }

    @GetMapping("/accounts")
    public List<BankAccountDTO> getBankAccounts() throws BankAccountException {
        return bankAccountService.bankAccountList();
    }

    @GetMapping("/accounts/{id}/operations")
    public List<OperationsDTO> getHistory(@PathVariable (name="id") String accountid) {
        return bankAccountService.accounthistory(accountid);
    }

    @GetMapping("/accounts/{accountId}/pageOperations")
    public AccountHistoryDTO getAccountHistory(
            @PathVariable String accountId,
            @RequestParam(name="page",defaultValue = "0") int page,
            @RequestParam(name="size",defaultValue = "5")int size) throws BankAccountException {
        return bankAccountService.getAccountHistory(accountId,page,size);
    }













}
