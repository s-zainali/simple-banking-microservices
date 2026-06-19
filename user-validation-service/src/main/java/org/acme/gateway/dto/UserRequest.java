package org.acme.gateway.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record UserRequest(
    @NotBlank(message = "Account number cannot be blank")
    @Size(min = 10, max = 10, message = "Account number must be exactly 10 characters")
    @Pattern(regexp = "^[0-9]+$", message = "Account number must contain only digits")
    String accountNumber,

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    String name,

    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "^\\+?[0-9\\s\\-\\(\\)]+$", message = "Invalid phone number format")
    String phoneNumber,

    @NotNull(message = "Initial balance is required")
    @PositiveOrZero(message = "Initial balance cannot be negative")
    BigDecimal balance,

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 64, message = "Password must be between 6 and 64 characters")
    String password,

    @NotBlank(message = "Role is required")
    @Pattern(regexp = "^(ADMIN|TELLER|CUSTOMER)$", message = "Role must be ADMIN, TELLER or CUSTOMER")
    String role
) {}
