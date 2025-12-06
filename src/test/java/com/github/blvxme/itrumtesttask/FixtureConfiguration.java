package com.github.blvxme.itrumtesttask;

import com.github.blvxme.itrumtesttask.core.wallet.Wallet;
import com.github.blvxme.itrumtesttask.core.wallet.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.UUID;

@TestConfiguration
@RequiredArgsConstructor
public class FixtureConfiguration {
    private final WalletRepository walletRepository;

    @Bean
    public Fixture fixture() {
        Wallet fakeWallet = createFakeWallet();
        return new Fixture(fakeWallet);
    }

    private Wallet createFakeWallet() {
        return walletRepository.save(new Wallet(UUID.randomUUID(), BigDecimal.ZERO));
    }
}
