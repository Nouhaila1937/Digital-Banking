package org.example.digitale_banking.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.digitale_banking.Enum.OperationType;
import org.example.digitale_banking.Repositories.BankAccountRepo;
import org.example.digitale_banking.Repositories.CustomerRepo;
import org.example.digitale_banking.Repositories.OperationsRepo;
import org.example.digitale_banking.entities.*;
import org.example.digitale_banking.exceptions.BankAccountException;
import org.example.digitale_banking.exceptions.CustomerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.digitale_banking.exceptions.BalanceNotSufficientException;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
@Slf4j // au lieu de faire Logger log = LoggerFactory.getLogger(this.getClass().getName());
public class BankAccountImpl implements BankAccountService {
    //Logger log = LoggerFactory.getLogger(this.getClass().getName());
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private BankAccountRepo bankAccountRepo;
    @Autowired
    private OperationsRepo operationsRepo;
    @Override
    public Customer saveCustomer(Customer customer) {
        Customer savedCustomer = customerRepo.save(customer);
        return savedCustomer;
    }

    @Override
    public CurrentAccount saveCurrentAccount(double initialBalance, Long customerId, double overdraft) throws CustomerNotFoundException{
        Customer customer = customerRepo.findById(customerId).orElse(null);
        if(customer == null) {
            throw new CustomerNotFoundException("Customer not found");
        }
        CurrentAccount currentAccount=new CurrentAccount();
        currentAccount.setBalance(initialBalance);
        currentAccount.setCustomer(customer);
        currentAccount.setOverdraft(overdraft);
        currentAccount.setDate(new Date());
        currentAccount.setId(UUID.randomUUID().toString());
        CurrentAccount savedCurrentAccount = bankAccountRepo.save(currentAccount);
        return savedCurrentAccount;
    }

    @Override
    public SavingAccount savingAccount(double interestRate, double initialBalance, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepo.findById(customerId).orElse(null);
        if(customer == null) {
            throw new CustomerNotFoundException("Customer not found");
        }
        SavingAccount savingAccount=new SavingAccount();
        savingAccount.setBalance(initialBalance);
        savingAccount.setDate(new Date());
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCustomer(customer);
        savingAccount.setInterestRate(interestRate);
        SavingAccount savedSavingAccount = bankAccountRepo.save(savingAccount);
        return savedSavingAccount;
    }

    @Override
    public List<Customer> listCustomers() {
        return customerRepo.findAll();
    }

    @Override
    public BankAccount getBankAccount(String id) throws BankAccountException{
        BankAccount bankAccount = bankAccountRepo.findById(id)
                .orElseThrow(()->new BankAccountException("Bank account not found"));
        return bankAccountRepo.findById(id).orElse(null);
    }

    @Override
    public void debitBankAccount(String accountid, double amount, String description) throws BankAccountException,BalanceNotSufficientException{
        BankAccount bankAccount =getBankAccount(accountid);
        if(bankAccount.getBalance() <amount) {
            throw new BalanceNotSufficientException("Insufficient balance");
        }
        AccountOperation operation = new AccountOperation();
        operation.setOperation(OperationType.DEBIT);
        operation.setAmount(amount);
        operation.setDescription(description);
        operation.setDate(new Date());
        operation.setBankAccount(bankAccount);
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepo.save(bankAccount);

    }

    @Override
    public void creditBankAccount(String accountid, double amount, String description) throws BankAccountException {
        BankAccount bankAccount =getBankAccount(accountid);
        AccountOperation operation = new AccountOperation();
        operation.setOperation(OperationType.CREDIT);
        operation.setAmount(amount);
        operation.setDescription(description);
        operation.setDate(new Date());
        operation.setBankAccount(bankAccount);
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepo.save(bankAccount);
    }

    @Override
    public void transferBankAccount(String fromCustomerId, String toCustomerId, double amount) throws BankAccountException, BalanceNotSufficientException {
        debitBankAccount(fromCustomerId,amount,"Transfer to "+toCustomerId);
        creditBankAccount(toCustomerId,amount,"Transfer tfrom "+fromCustomerId);

    }
}
