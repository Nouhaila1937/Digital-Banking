package org.example.digitale_banking.mappers;

import org.example.digitale_banking.Dtos.CurrentBankAccountDTO;
import org.example.digitale_banking.Dtos.CustomerDTO;
import org.example.digitale_banking.Dtos.OperationsDTO;
import org.example.digitale_banking.Dtos.SavingAccountDTO;
import org.example.digitale_banking.entities.AccountOperation;
import org.example.digitale_banking.entities.CurrentAccount;
import org.example.digitale_banking.entities.Customer;
import org.example.digitale_banking.entities.SavingAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class BankAccountMapperImpl {

    public CustomerDTO fromCustomer(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO); //source customer destination customerDTO on aura pas besoin de faire des set et get pour tout les champs
// copier les proprietes de l'objet customer vers l'objet customerDTO
//        customerDTO.setId(customer.getId());
//        customerDTO.setName(customer.getName());
//        customerDTO.setEmail(customer.getEmail());
        return customerDTO;
    }

    public Customer fromCustomerDTO(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        return customer;
    }

    public SavingAccountDTO fromSavingBankAccount(SavingAccount savingAccount) {
        SavingAccountDTO savingAccountDTO = new SavingAccountDTO();
        BeanUtils.copyProperties(savingAccount, savingAccountDTO);
        // parce que la propriètè customerdto qui existe dans savingaccountdto ne sera pas transfèrer du coup on a besoin de faire ceci
        savingAccountDTO.setCustomerDTO(fromCustomer(savingAccount.getCustomer()));
        savingAccountDTO.setType(savingAccount.getClass().getSimpleName());
        return savingAccountDTO;

    }

    public SavingAccount fromSavingBankAccountDTO(SavingAccountDTO savingAccountDTO) {
        SavingAccount savingAccount = new SavingAccount ();
        BeanUtils.copyProperties(savingAccountDTO, savingAccount );
        savingAccount.setCustomer(fromCustomerDTO(savingAccountDTO.getCustomerDTO()));
        return savingAccount ;

    }

    public CurrentBankAccountDTO fromCurrentBankAccount(CurrentAccount  currentAccount) {
        CurrentBankAccountDTO currentAccountDTO = new CurrentBankAccountDTO();
        BeanUtils.copyProperties( currentAccount, currentAccountDTO );
        currentAccountDTO.setCustomerDTO(fromCustomer(currentAccount.getCustomer()));
        currentAccountDTO.setType(currentAccount.getClass().getSimpleName());
        return currentAccountDTO ;

    }

    public CurrentAccount fromCurrentBankAccountDTO(CurrentBankAccountDTO  currentAccountDTO) {
        CurrentAccount currentAccount = new CurrentAccount();
        BeanUtils.copyProperties( currentAccountDTO,currentAccount);
        currentAccount.setCustomer(fromCustomerDTO(currentAccountDTO.getCustomerDTO()));
        return currentAccount ;

    }

    public OperationsDTO fromAccountOperation(AccountOperation accountOperation) {
        OperationsDTO operationsDTO = new OperationsDTO();
        BeanUtils.copyProperties(accountOperation, operationsDTO);
        return operationsDTO;
    }

    public AccountOperation fromAccountOperationDTO(AccountOperation accountOperationDTO) {
        AccountOperation accountOperation = new AccountOperation();
        BeanUtils.copyProperties(accountOperationDTO, accountOperation);
        return accountOperation;
    }



}
