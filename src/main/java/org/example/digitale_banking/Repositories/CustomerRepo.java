package org.example.digitale_banking.Repositories;

import org.example.digitale_banking.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepo extends JpaRepository<Customer, Long> {
}
