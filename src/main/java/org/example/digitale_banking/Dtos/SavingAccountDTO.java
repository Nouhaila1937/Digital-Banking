package org.example.digitale_banking.Dtos;

import lombok.Data;
import org.example.digitale_banking.Enum.AccountStatus;

import java.util.Date;

@Data
public class SavingAccountDTO extends BankAccountDTO{
    private String id;
    private double balance;
    private Date date;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private double interestRate;

}
