package com.github.blvxme.itrumtesttask.core.wallet;

import com.github.blvxme.itrumtesttask.core.wallet.exception.InvalidBalanceException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "wallets")
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {
    @Id
    @Column(name = "id")
    @Getter
    private UUID id;

    @Column(name = "balance")
    @Getter
    private BigDecimal balance;

    public void updateBalance(BigDecimal amount) throws InvalidBalanceException {
        BigDecimal newBalance = balance.add(amount);
        if (isBalanceInvalid(newBalance)) {
            throw new InvalidBalanceException(balance, amount, newBalance);
        }

        balance = newBalance;
    }

    private boolean isBalanceInvalid(BigDecimal balance) {
        return balance.compareTo(BigDecimal.ZERO) < 0;
    }
}
