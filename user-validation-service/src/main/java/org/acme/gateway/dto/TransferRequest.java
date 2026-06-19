package org.acme.gateway.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record TransferRequest(
    @NotBlank(message = "Source account number cannot be blank")
    String sourceAccountNumber,
    @NotBlank(message = "Target account number cannot be blank")
    String targetAccountNumber,

    @NotNull(message = "Amount is required")
    @Positive(message = "Transfer amount must be greater than zero")
    BigDecimal amount
) {}