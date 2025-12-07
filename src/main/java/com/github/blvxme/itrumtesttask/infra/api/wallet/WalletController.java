package com.github.blvxme.itrumtesttask.infra.api.wallet;

import com.github.blvxme.itrumtesttask.app.wallet.WalletService;
import com.github.blvxme.itrumtesttask.core.wallet.Money;
import com.github.blvxme.itrumtesttask.core.wallet.exception.InvalidBalanceException;
import com.github.blvxme.itrumtesttask.app.wallet.exception.WalletNotFoundException;
import com.github.blvxme.itrumtesttask.infra.api.error.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
public class WalletController {
    private static final Logger log = LoggerFactory.getLogger(WalletController.class);

    private final WalletService walletService;

    @GetMapping("/{walletId}")
    public ResponseEntity<?> getWalletBalance(@PathVariable UUID walletId, HttpServletRequest request) {
        try {
            BigDecimal balance = walletService.getWalletBalance(walletId).toBigDecimal();
            WalletResponse response = new WalletResponse(walletId, balance);
            return ResponseEntity.ok(response);
        } catch (WalletNotFoundException e) {
            ErrorResponse error = new ErrorResponse(Instant.now(), HttpStatus.NOT_FOUND.value(), e.getMessage(), request.getRequestURI());
            log.error(error.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @PostMapping
    public ResponseEntity<?> updateWalletBalance(@RequestBody @Valid WalletRequest walletRequest, HttpServletRequest request) {
        UUID walletId = walletRequest.walletId();
        OperationType operationType = walletRequest.operationType();
        BigDecimal amount = operationType == OperationType.DEPOSIT ? walletRequest.amount() : walletRequest.amount().negate();

        try {
            BigDecimal newBalance = walletService.updateWalletBalance(walletId, new Money(amount)).toBigDecimal();
            WalletResponse response = new WalletResponse(walletId, newBalance);
            return ResponseEntity.ok(response);
        } catch (WalletNotFoundException e) {
            ErrorResponse error = new ErrorResponse(Instant.now(), HttpStatus.NOT_FOUND.value(), e.getMessage(), request.getRequestURI());
            log.error(error.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (InvalidBalanceException e) {
            ErrorResponse error = new ErrorResponse(Instant.now(), HttpStatus.BAD_REQUEST.value(), e.getMessage(), request.getRequestURI());
            log.error(error.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}
