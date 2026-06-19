package org.acme.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.PasswordType;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Read-only security mapping onto the SAME table the core service owns
 * (user_entities). The account number doubles as the login username.
 * No separate auth_users table anymore.
 */
@Entity
@Table(name = "user_entities")
@UserDefinition
public class UserEntity extends PanacheEntityBase {

    @Id
    @Username
    @Column(name = "account_number")
    public String accountNumber;

    @Password(PasswordType.CLEAR)
    public String password;

    @Roles
    public String role; // ADMIN | TELLER | CUSTOMER
}
