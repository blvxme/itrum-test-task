package com.github.blvxme.itrumtesttask.infra.api.wallet;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletRequest(
        @NotNull
        UUID walletId,

        @NotNull
        OperationType operationType,

        @NotNull
        BigDecimal amount
) { }
