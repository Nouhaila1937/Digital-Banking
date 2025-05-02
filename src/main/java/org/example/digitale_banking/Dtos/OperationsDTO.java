package org.example.digitale_banking.Dtos;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.example.digitale_banking.Enum.OperationType;
import org.example.digitale_banking.entities.BankAccount;

import java.util.Date;
@Data
public class OperationsDTO {
    private Long id;
    private Date date;
    private double amount;
    private OperationType operation;
    private String description;
}
