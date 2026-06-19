package org.acme;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_entities")
public class UserEntity extends PanacheEntityBase {

    @Id
    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    private String name;
    private BigDecimal balance;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String password;
    private String role; // "ADMIN", "TELLER", "CUSTOMER"

    // Getters
    public String getAccountNumber() {
        return this.accountNumber;
    }

    public String getName() {
        return this.name;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    @JsonIgnore
    public String getPassword() {
        return this.password;
    }

    public String getRole() {
        return this.role;
    }

    // Setters
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("password")
    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void updateBalance(String action, BigDecimal amount) {
        if ("increase".equals(action)) {
            this.balance = this.balance.add(amount);
        } else if ("decrease".equals(action)) {
            if (this.balance.compareTo(amount) < 0) {
                throw new IllegalArgumentException("Insufficient funds available.");
            }
            this.balance = this.balance.subtract(amount);
        } else {
            throw new IllegalArgumentException("Action must be 'increase' or 'decrease' only.");
        }
    }
}