package com.github.blvxme.itrumtesttask.app.wallet;

import com.github.blvxme.itrumtesttask.core.wallet.exception.InvalidBalanceException;
import com.github.blvxme.itrumtesttask.app.wallet.exception.WalletNotFoundException;
import com.github.blvxme.itrumtesttask.core.wallet.Wallet;
import com.github.blvxme.itrumtesttask.core.wallet.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;

    public BigDecimal getWalletBalance(UUID id) throws WalletNotFoundException {
        Wallet wallet = getWalletOrThrow(id);
        return wallet.getBalance();
    }

    public BigDecimal updateWalletBalance(UUID id, BigDecimal amount) throws WalletNotFoundException, InvalidBalanceException {
        Wallet wallet = getWalletOrThrow(id);
        wallet.updateBalance(amount);
        return walletRepository.save(wallet).getBalance();
    }

    private Wallet getWalletOrThrow(UUID id) throws WalletNotFoundException {
        return walletRepository.findById(id).orElseThrow(() -> new WalletNotFoundException(id));
    }
}
