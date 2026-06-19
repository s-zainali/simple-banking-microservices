package org.acme.Entities;

import java.math.BigDecimal;
import java.security.Timestamp;
import java.util.UUID;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Id;

@Entity
@Table(name = "account_entities")
public class AccountEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "account_id")
    private UUID accountId;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "customer_id")
    private UUID customerId;

    @Column(name = "account_type")
    private String accountType;

    private String currency;

    private BigDecimal balance;

    private String status;

    @Column(name = "opened_at")
    private Timestamp openedAt;

}
