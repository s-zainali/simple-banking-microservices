package org.acme.gateway.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record BalanceUpdateRequest(
    @NotBlank(message = "Account number cannot be blank")
    String accountNumber,

    @NotBlank(message = "Action is required")
    @Pattern(regexp = "^(increase|decrease)$", message = "Action must be exactly 'increase' or 'decrease'")
    String action,

    @NotNull(message = "Amount is required")
    @Positive(message = "Transaction amount must be greater than zero")
    BigDecimal amount
) {}