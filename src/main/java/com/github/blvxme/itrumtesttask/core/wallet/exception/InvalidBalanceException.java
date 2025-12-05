package com.github.blvxme.itrumtesttask.core.wallet.exception;

import java.math.BigDecimal;

public class InvalidBalanceException extends WalletException {
    public InvalidBalanceException(BigDecimal currentBalance, BigDecimal amount, BigDecimal newBalance) {
        super(String.format(
                "Invalid balance (current balance: %s, amount: %s, new balance: %s)",
                currentBalance.toString(),
                amount.toString(),
                newBalance.toString()
        ));
    }
}
