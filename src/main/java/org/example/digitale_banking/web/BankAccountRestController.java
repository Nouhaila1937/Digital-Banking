package org.example.digitale_banking.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.digitale_banking.Dtos.BankAccountDTO;
import org.example.digitale_banking.exceptions.BankAccountException;
import org.example.digitale_banking.service.BankAccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@Slf4j
public class BankAccountRestController {
    BankAccountService bankAccountService;

    // injection des dépendances
    public BankAccountRestController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @GetMapping("/accounts/{id}")
    public BankAccountDTO getBankAccount(@PathVariable String id) throws BankAccountException {
        BankAccountDTO bankAccountDTO = bankAccountService.getBankAccount(id);
        return bankAccountDTO;
    }

    @GetMapping("/accounts")
    public List<BankAccountDTO> getBankAccounts() throws BankAccountException {
        return bankAccountService.bankAccountList();
    }















}
