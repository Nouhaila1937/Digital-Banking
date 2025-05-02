package org.example.digitale_banking;

import org.example.digitale_banking.Dtos.BankAccountDTO;
import org.example.digitale_banking.Dtos.CurrentBankAccountDTO;
import org.example.digitale_banking.Dtos.CustomerDTO;
import org.example.digitale_banking.Dtos.SavingAccountDTO;
import org.example.digitale_banking.Enum.AccountStatus;
import org.example.digitale_banking.Enum.OperationType;
import org.example.digitale_banking.Repositories.BankAccountRepo;
import org.example.digitale_banking.Repositories.CustomerRepo;
import org.example.digitale_banking.Repositories.OperationsRepo;
import org.example.digitale_banking.entities.*;
import org.example.digitale_banking.exceptions.BalanceNotSufficientException;
import org.example.digitale_banking.exceptions.BankAccountException;
import org.example.digitale_banking.exceptions.CustomerNotFoundException;
import org.example.digitale_banking.service.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class DigitaleBankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(DigitaleBankingApplication.class, args);
    }



    //@Bean
    CommandLineRunner commandLineRunner(CustomerRepo customerRepository,
                            BankAccountRepo bankAccountRepo ,
                            OperationsRepo accountOperation
                            ) {
        return args -> {
            Stream.of("Nouhaila", "Adam", "Malak", "Yaakoub").forEach(name -> {
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name + "@gmail.com");
                customerRepository.save(customer);
            });
            customerRepository.findAll().forEach(customer -> {
                // Current account
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random()*10000);
                currentAccount.setDate(new Date());
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(customer);
                currentAccount.setOverdraft(9000);
                bankAccountRepo.save(currentAccount);
                // Savings account
                SavingAccount savingsAccount = new SavingAccount();
                savingsAccount.setId(UUID.randomUUID().toString());
                savingsAccount.setBalance(Math.random()*10000);
                savingsAccount.setDate(new Date());
                savingsAccount.setStatus(AccountStatus.CREATED);
                savingsAccount.setCustomer(customer);
                savingsAccount.setInterestRate(1.2);
                bankAccountRepo.save(savingsAccount);
            });

            // création des opérations pour chaque user
            bankAccountRepo.findAll().forEach(account->{
                for(int i = 0; i<1; i++){
                    AccountOperation operation = new AccountOperation();
                    operation.setDate(new Date());
                    operation.setAmount(Math.random()*10000);
                    operation.setBankAccount(account);
                    operation.setOperation(Math.random()>0.5? OperationType.DEBIT : OperationType.CREDIT);
                    accountOperation.save(operation);
                }
            });
        };
    }


    @Bean
    @Transactional
    CommandLineRunner start (BankAccountService bankAccountService) {
        return args -> {
            Stream.of("Nouhaila", "Adam","Manal").forEach(name -> {
                //Customer customer = new Customer();
                CustomerDTO customerdto = new CustomerDTO();
                customerdto.setName(name);
                customerdto.setEmail(name + "@gmail.com");
                //bankAccountService.saveCustomer(customer);
                bankAccountService.saveCustomer(customerdto);
            });

            bankAccountService.listCustomers().forEach(customer -> {
                try {
                     bankAccountService.saveCurrentAccount(Math.random() * 10000, customer.getId(), 9000);
                     bankAccountService.savingAccount(5.5, Math.random() * 13000, customer.getId());


//                    List<BankAccount> bankAccounts = bankAccountService.bankAccountList();
                      List<BankAccountDTO> bankAccounts = bankAccountService.bankAccountList();

                    for (BankAccountDTO bankAccount : bankAccounts) {
                        for (int i = 0; i < 10; i++) {
                            String acountId;
                            if (bankAccount instanceof SavingAccountDTO) {
                                acountId = ((SavingAccountDTO) bankAccount).getId();
                            } else {
                                acountId = ((CurrentBankAccountDTO) bankAccount).getId();
                            }
                            bankAccountService.creditBankAccount(acountId, 10 + Math.random() * 100, "Crédit Bank");
                            bankAccountService.debitBankAccount(acountId, 10 + Math.random() * 100, "Débit Bank");

                        }
                    }
                } catch (CustomerNotFoundException | BankAccountException | BalanceNotSufficientException e) {
                    e.printStackTrace();
                }
            });

        };
    }
}