package org.acme.Entities;

import java.math.BigDecimal;
import java.util.UUID;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import java.security.Timestamp;

@Entity
@Table(name="transaction_entities")
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID);
    @Column(name = "transation_id")
    private UUID transactionId;

    @Column(name = "account_id")
    private UUID accountId;

    private String type;

    private BigDecimal ammount;

    @Column(name = "balance_after")
    private BigDecimal balanceAfter;

    private String description;

    private String reference;

    @Column(name = "created_at")
    private Timestamp createdAt;


}
 