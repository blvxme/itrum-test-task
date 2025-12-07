package com.github.blvxme.itrumtesttask.core.wallet.exception;

import com.github.blvxme.itrumtesttask.core.wallet.Money;

public class InvalidBalanceException extends WalletException {
    public InvalidBalanceException(Money currentBalance, Money amount, Money newBalance) {
        super(String.format(
                "Invalid balance (current balance: %s, amount: %s, new balance: %s)",
                currentBalance.toString(),
                amount.toString(),
                newBalance.toString()
        ));
    }
}
