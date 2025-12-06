package com.github.blvxme.itrumtesttask;

import com.github.blvxme.itrumtesttask.core.wallet.Wallet;
import com.github.blvxme.itrumtesttask.core.wallet.WalletRepository;
import com.github.blvxme.itrumtesttask.infra.api.error.ErrorResponse;
import com.github.blvxme.itrumtesttask.infra.api.wallet.OperationType;
import com.github.blvxme.itrumtesttask.infra.api.wallet.WalletRequest;
import com.github.blvxme.itrumtesttask.infra.api.wallet.WalletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WalletIntegrationTest extends BasicIntegrationTest {
    @Autowired
    private WalletRepository walletRepository;

    @Test
    public void shouldReturnNotFoundWhenGettingBalanceOfNonExistingWallet() throws Exception {
        UUID walletId = UUID.randomUUID();
        String path = String.format("/api/v1/wallets/%s", walletId);

        MvcResult result = mockMvc
                .perform(get(path))
                .andExpect(status().isNotFound())
                .andReturn();

        ErrorResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ErrorResponse.class
        );

        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.error()).isEqualTo(String.format("Wallet not found (id: %s)", walletId));
        assertThat(response.path()).isEqualTo(path);
    }

    @Test
    public void shouldReturnBalanceWhenWalletExists() throws Exception {
        UUID walletId = fixture.fakeWallet().getId();
        String path = String.format("/api/v1/wallets/%s", walletId);

        MvcResult result = mockMvc
                .perform(get(path))
                .andExpect(status().isOk())
                .andReturn();

        WalletResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                WalletResponse.class
        );

        assertThat(response).isNotNull();
        assertThat(response.walletId().toString()).isEqualTo(walletId.toString());
        assertThat(response.balance()).isEqualByComparingTo(fixture.fakeWallet().getBalance());
    }

    @Test
    public void shouldReturnNotFoundWhenDepositingToNonExistingWallet() throws Exception {
        UUID walletId = UUID.randomUUID();
        WalletRequest request = new WalletRequest(walletId, OperationType.DEPOSIT, BigDecimal.ONE);
        String path = "/api/v1/wallets";

        MvcResult result = mockMvc
                .perform(
                        post(path)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isNotFound())
                .andReturn();

        ErrorResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ErrorResponse.class
        );

        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.error()).isEqualTo(String.format("Wallet not found (id: %s)", walletId));
        assertThat(response.path()).isEqualTo(path);
    }

    @Test
    @Transactional
    public void shouldIncreaseBalanceWhenDepositingToExistingWallet() throws Exception {
        UUID walletId = fixture.fakeWallet().getId();
        WalletRequest request = new WalletRequest(walletId, OperationType.DEPOSIT, BigDecimal.ONE);
        String path = "/api/v1/wallets";

        MvcResult result = mockMvc
                .perform(
                        post(path)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andReturn();

        WalletResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                WalletResponse.class
        );

        assertThat(response).isNotNull();
        assertThat(response.walletId().toString()).isEqualTo(walletId.toString());
        assertThat(response.balance()).isEqualByComparingTo(fixture.fakeWallet().getBalance().add(BigDecimal.ONE));
        assertThat(response.balance()).isEqualByComparingTo(walletRepository.findById(walletId).orElseThrow().getBalance());
    }

    @Test
    @Transactional
    public void shouldDecreaseBalanceWhenWithdrawingFormExistingWallet() throws Exception {
        UUID walletId = UUID.randomUUID();
        BigDecimal initialWalletBalance = new BigDecimal("1000.00");
        Wallet wallet = new Wallet(walletId, initialWalletBalance);
        walletRepository.save(wallet);

        BigDecimal difference = new BigDecimal("700.30");
        BigDecimal newWalletBalance = initialWalletBalance.subtract(difference);

        WalletRequest request = new WalletRequest(walletId, OperationType.WITHDRAW, difference);
        String path = "/api/v1/wallets";

        MvcResult result = mockMvc
                .perform(
                        post(path)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andReturn();

        WalletResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                WalletResponse.class
        );

        assertThat(response).isNotNull();
        assertThat(response.walletId().toString()).isEqualTo(walletId.toString());
        assertThat(response.balance()).isEqualByComparingTo(newWalletBalance);
        assertThat(response.balance()).isEqualByComparingTo(walletRepository.findById(walletId).orElseThrow().getBalance());
    }

    @Test
    public void shouldReturnBadRequestWhenWithdrawalResultsInNegativeBalance() throws Exception {
        UUID walletId = fixture.fakeWallet().getId();

        BigDecimal initialWalletBalance = fixture.fakeWallet().getBalance().setScale(2, RoundingMode.UNNECESSARY);
        BigDecimal difference = BigDecimal.TEN;
        BigDecimal newWalletBalance = initialWalletBalance.subtract(difference).setScale(2, RoundingMode.UNNECESSARY);

        WalletRequest request = new WalletRequest(walletId, OperationType.WITHDRAW, difference);
        String path = "/api/v1/wallets";

        MvcResult result = mockMvc
                .perform(
                        post(path)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        ErrorResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ErrorResponse.class
        );

        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.error()).isEqualTo(String.format(
                "Invalid balance (current balance: %s, amount: %s, new balance: %s)",
                initialWalletBalance,
                difference.negate(),
                newWalletBalance
        ));
        assertThat(response.path()).isEqualTo(path);
    }

    @Test
    public void shouldReturnBadRequestWhenOperationTypeIsInvalid() throws Exception {
        UUID walletId = fixture.fakeWallet().getId();
        String path = "/api/v1/wallets";
        String request = String.format(
                "{ 'walletId': '%s', 'operationType': '%s', 'amount': '%s' }",
                walletId.toString(),
                "SOME_NON_EXISTENT_OPERATION_TYPE",
                BigDecimal.TEN
        );

        MvcResult result = mockMvc
                .perform(
                        post(path)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        ErrorResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ErrorResponse.class
        );

        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.error()).isEqualTo("Invalid request body");
        assertThat(response.path()).isEqualTo(path);
    }
}
