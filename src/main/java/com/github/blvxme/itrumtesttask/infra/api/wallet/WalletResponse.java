package com.github.blvxme.itrumtesttask.infra.api.wallet;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletResponse(
        UUID walletId,
        BigDecimal balance
) { }
