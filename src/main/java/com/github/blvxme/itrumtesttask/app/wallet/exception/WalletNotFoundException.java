package com.github.blvxme.itrumtesttask.app.wallet.exception;

import com.github.blvxme.itrumtesttask.core.wallet.exception.WalletException;

import java.util.UUID;

public class WalletNotFoundException extends WalletException {
    public WalletNotFoundException(UUID id) {
        super(String.format("Wallet not found (id: %s)", id.toString()));
    }
}
