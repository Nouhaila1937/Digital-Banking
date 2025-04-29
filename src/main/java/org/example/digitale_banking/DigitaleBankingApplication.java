package org.example.digitale_banking;

import org.example.digitale_banking.Enum.AccountStatus;
import org.example.digitale_banking.Enum.OperationType;
import org.example.digitale_banking.Repositories.BankAccountRepo;
import org.example.digitale_banking.Repositories.CustomerRepo;
import org.example.digitale_banking.Repositories.OperationsRepo;
import org.example.digitale_banking.entities.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class DigitaleBankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(DigitaleBankingApplication.class, args);
    }

    @Bean
    CommandLineRunner start(CustomerRepo customerRepository,
                            BankAccountRepo bankAccountRepo ,
                            OperationsRepo accountOperation
                            ) {
        return args -> {
            Stream.of("Nouhaila", "Adam", "Malak", "Yaakoub").forEach(name -> {
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name + "gmail.com");
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
}