package com.github.blvxme.itrumtesttask.core.wallet;

import com.github.blvxme.itrumtesttask.core.wallet.exception.InvalidBalanceException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @SuppressWarnings("JpaAttributeTypeInspection")
    @Column(name = "balance")
    @Getter
    private Money balance;

    public void updateBalance(Money amount) throws InvalidBalanceException {
        Money newBalance = balance.add(amount);
        if (newBalance.lessThan(Money.ZERO)) {
            throw new InvalidBalanceException(balance, amount, newBalance);
        }

        balance = newBalance;
    }
}
