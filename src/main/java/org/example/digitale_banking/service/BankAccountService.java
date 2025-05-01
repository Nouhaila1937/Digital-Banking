package org.example.digitale_banking.service;

import org.example.digitale_banking.Dtos.CustomerDTO;
import org.example.digitale_banking.entities.BankAccount;
import org.example.digitale_banking.entities.CurrentAccount;
import org.example.digitale_banking.entities.Customer;
import org.example.digitale_banking.entities.SavingAccount;
import org.example.digitale_banking.exceptions.BankAccountException;
import org.example.digitale_banking.exceptions.CustomerNotFoundException;
import org.example.digitale_banking.exceptions.BalanceNotSufficientException;

import java.util.List;

public interface BankAccountService {
//  Customer saveCustomer(Customer customer);
    CustomerDTO saveCustomer(CustomerDTO customerDTO);

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long customerDTO);

    CurrentAccount saveCurrentAccount(double initialBalance, Long customerId, double overdraft) throws CustomerNotFoundException;
    SavingAccount savingAccount(double  interestRate, double initialBalance, Long customerId)throws CustomerNotFoundException;
//  List<Customer> listCustomers();
    List<CustomerDTO> listCustomers();
    BankAccount getBankAccount(String id) throws BankAccountException;
    void debitBankAccount(String id, double amount,String description)throws BankAccountException,BalanceNotSufficientException;
    void creditBankAccount(String id, double amount,String description) throws BankAccountException;
    void transferBankAccount(String fromCustomerId, String toCustomerId, double amount) throws BankAccountException, BalanceNotSufficientException;
    List<BankAccount> bankAccountList();
    CustomerDTO getCustomer(Long id) throws CustomerNotFoundException;
}
