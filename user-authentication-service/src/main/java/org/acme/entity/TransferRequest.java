package org.acme.entity;

import java.math.BigDecimal;

public record TransferRequest(
    String sourceAccountNumber,
    String targetAccountNumber,
    BigDecimal amount
) {}