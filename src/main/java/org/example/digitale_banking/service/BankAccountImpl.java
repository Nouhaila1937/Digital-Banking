package org.example.digitale_banking.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.digitale_banking.Dtos.*;
import org.example.digitale_banking.Enum.OperationType;
import org.example.digitale_banking.Repositories.BankAccountRepo;
import org.example.digitale_banking.Repositories.CustomerRepo;
import org.example.digitale_banking.Repositories.OperationsRepo;
import org.example.digitale_banking.entities.*;
import org.example.digitale_banking.exceptions.BankAccountException;
import org.example.digitale_banking.exceptions.CustomerNotFoundException;
import org.example.digitale_banking.mappers.BankAccountMapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.digitale_banking.exceptions.BalanceNotSufficientException;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j // au lieu de faire Logger log = LoggerFactory.getLogger(this.getClass().getName());
public class BankAccountImpl implements BankAccountService {
    //Logger log = LoggerFactory.getLogger(this.getClass().getName());
//    @Autowired
    private CustomerRepo customerRepo;
//    @Autowired
    private BankAccountRepo bankAccountRepo;
//    @Autowired
    private OperationsRepo operationsRepo;
    private BankAccountMapperImpl bankAccountMapper;


//    @Override
//    public Customer saveCustomer(Customer customer) {
//        Customer savedCustomer = customerRepo.save(customer);
//        return savedCustomer;
//    }

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        Customer customer=bankAccountMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepo.save(customer);
        return bankAccountMapper.fromCustomer(savedCustomer);
    }


    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        Customer customer=bankAccountMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepo.save(customer);
        return bankAccountMapper.fromCustomer(savedCustomer);
    }

    @Override
    public void deleteCustomer(Long customerId){
        customerRepo.deleteById(customerId);
    }
//    @Override
//    public CurrentAccount saveCurrentAccount(double initialBalance, Long customerId, double overdraft) throws CustomerNotFoundException{
//        Customer customer = customerRepo.findById(customerId).orElse(null);
//        if(customer == null) {
//            throw new CustomerNotFoundException("Customer not found");
//        }
//        CurrentAccount currentAccount=new CurrentAccount();
//        currentAccount.setBalance(initialBalance);
//        currentAccount.setCustomer(customer);
//        currentAccount.setOverdraft(overdraft);
//        currentAccount.setDate(new Date());
//        currentAccount.setId(UUID.randomUUID().toString());
//        CurrentAccount savedCurrentAccount = bankAccountRepo.save(currentAccount);
//        return savedCurrentAccount;
//    }

    @Override
    public CurrentBankAccountDTO saveCurrentAccount(double initialBalance, Long customerId, double overdraft) throws CustomerNotFoundException{
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
        return  bankAccountMapper.fromCurrentBankAccount(savedCurrentAccount);
    }


    @Override
    public SavingAccountDTO savingAccount(double interestRate, double initialBalance, Long customerId) throws CustomerNotFoundException {
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
        return bankAccountMapper.fromSavingBankAccount(savedSavingAccount);
    }

//    @Override
//    public List<Customer> listCustomers() {
//        return customerRepo.findAll();
//    }

        @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers = customerRepo.findAll();
        // première méthode avec programmation impératif
//        List<CustomerDTO> customerDTOS = new ArrayList<>();
//        for (Customer customer : customers) { // pour chaque customer de customers
//            CustomerDTO customerDTO = bankAccountMapper.fromCustomer(customer);
//            customerDTOS.add(customerDTO);
//        }
//
//        return customerDTOS ;
            // méthode 2

            List <CustomerDTO> customerDTOS=customers.stream()
                    .map(customer->bankAccountMapper.fromCustomer(customer))
                    .collect(Collectors.toList());
            return customerDTOS;
    }

//    @Override
//    public BankAccount getBankAccount(String id) throws BankAccountException{
//        BankAccount bankAccount = bankAccountRepo.findById(id)
//                .orElseThrow(()->new BankAccountException("Bank account not found"));
//        return bankAccountRepo.findById(id).orElse(null);
//    }

    @Override
    public BankAccountDTO getBankAccount(String id) throws BankAccountException{
        BankAccount bankAccount = bankAccountRepo.findById(id)
                .orElseThrow(()->new BankAccountException("Bank account not found"));
        if(bankAccount instanceof CurrentAccount) {
            CurrentAccount currentAccount=(CurrentAccount) bankAccount;
            return bankAccountMapper.fromCurrentBankAccount(currentAccount);
        }
        else {
            SavingAccount savingAccount=(SavingAccount) bankAccount;
            return bankAccountMapper.fromSavingBankAccount(savingAccount);
        }
    }

    @Override
    public void debitBankAccount(String accountid, double amount, String description) throws BankAccountException,BalanceNotSufficientException{
//        BankAccount bankAccount =getBankAccount(accountid);
        BankAccount bankAccount = bankAccountRepo.findById(accountid)
                .orElseThrow(()->new BankAccountException("Bank account not found"));
        if(bankAccount.getBalance() < amount) {
            throw new BalanceNotSufficientException("Insufficient balance");
        }
        AccountOperation operation = new AccountOperation();
        operation.setOperation(OperationType.DEBIT);
        operation.setAmount(amount);
        operation.setDescription(description);
        operation.setDate(new Date());
        operation.setBankAccount(bankAccount);
        operationsRepo.save(operation);
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepo.save(bankAccount);

    }

    @Override
    public void creditBankAccount(String accountid, double amount, String description) throws BankAccountException {
//        BankAccount bankAccount =getBankAccount(accountid);
        BankAccount bankAccount = bankAccountRepo.findById(accountid)
                .orElseThrow(()->new BankAccountException("Bank account not found"));
        AccountOperation operation = new AccountOperation();
        operation.setOperation(OperationType.CREDIT);
        operation.setAmount(amount);
        operation.setDescription(description);
        operation.setDate(new Date());
        operation.setBankAccount(bankAccount);
        operationsRepo.save(operation);
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepo.save(bankAccount);
    }

    @Override
    public void transferBankAccount(String fromCustomerId, String toCustomerId, double amount) throws BankAccountException, BalanceNotSufficientException {
        debitBankAccount(fromCustomerId,amount,"Transfer to "+toCustomerId);
        creditBankAccount(toCustomerId,amount,"Transfer tfrom "+fromCustomerId);

    }

//    @Override
//    public List<BankAccount> bankAccountList(){
//        return bankAccountRepo.findAll();
//    }

    @Override
    public List<BankAccountDTO> bankAccountList(){
        List<BankAccount> bankAccounts=bankAccountRepo.findAll();
        List <BankAccountDTO> bankAccountDTOS=
        bankAccounts.stream().map(bankAccount -> {
            if(bankAccount instanceof CurrentAccount) {
                CurrentAccount currentAccount=(CurrentAccount) bankAccount;
                return bankAccountMapper.fromCurrentBankAccount(currentAccount);
            }
            else {
                SavingAccount savingAccount=(SavingAccount) bankAccount;
                return bankAccountMapper.fromSavingBankAccount(savingAccount);
            }
        }).collect(Collectors.toList());
        return bankAccountDTOS;
    }

    @Override
    public CustomerDTO getCustomer(Long id) throws CustomerNotFoundException{
        Customer customer = customerRepo.findById(id).orElseThrow(()->new CustomerNotFoundException("Customer with this id not found"));
        return  bankAccountMapper.fromCustomer(customer);
    }

    @Override
    public List<OperationsDTO> accounthistory(String accountid){
        List<AccountOperation> accountOperations=operationsRepo.findByBankAccountId(accountid);
        return accountOperations.stream().map(op->bankAccountMapper.fromAccountOperation(op)).collect(Collectors.toList());
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountException {
        BankAccount bankAccount=bankAccountRepo.findById(accountId).orElse(null);
        if(bankAccount==null) throw new BankAccountException("Account not Found");
        Page<AccountOperation> accountOperations = operationsRepo.findByBankAccountId(accountId, PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO=new AccountHistoryDTO();
        List<OperationsDTO> accountOperationDTOS = accountOperations.getContent().stream().map(op -> bankAccountMapper.fromAccountOperation(op)).collect(Collectors.toList());
        accountHistoryDTO.setAccountOperationDTOS(accountOperationDTOS);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
        return accountHistoryDTO;
    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {
        List<Customer> customers=customerRepo.searchCustomer(keyword);
        List<CustomerDTO> customerDTOS = customers.stream().map(cust -> bankAccountMapper.fromCustomer(cust)).collect(Collectors.toList());
        return customerDTOS;
    }


}
