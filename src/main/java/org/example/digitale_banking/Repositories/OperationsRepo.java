package org.example.digitale_banking.Repositories;

import org.example.digitale_banking.entities.AccountOperation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationsRepo extends JpaRepository<AccountOperation, Long> {
}
