package com.github.blvxme.itrumtesttask.infra.db.wallet;

import com.github.blvxme.itrumtesttask.core.wallet.Money;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.math.BigDecimal;

@Converter(autoApply = true)
public class MoneyConverter implements AttributeConverter<Money, BigDecimal> {
    @Override
    public BigDecimal convertToDatabaseColumn(Money money) {
        return money == null ? null : money.toBigDecimal();
    }

    @Override
    public Money convertToEntityAttribute(BigDecimal bigDecimal) {
        return bigDecimal == null ? null : new Money(bigDecimal);
    }
}
