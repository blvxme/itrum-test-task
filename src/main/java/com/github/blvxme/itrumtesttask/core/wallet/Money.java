package com.github.blvxme.itrumtesttask.core.wallet;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Money {
    public static final Money ZERO = new Money(BigDecimal.ZERO);

    private final BigDecimal value;

    public Money(BigDecimal value) {
        this.value = value.setScale(2, RoundingMode.HALF_EVEN);
    }

    public Money(String value) {
        this.value = new BigDecimal(value).setScale(2, RoundingMode.HALF_EVEN);
    }

    @Override
    public String toString() {
        return value.toString();
    }

    public BigDecimal toBigDecimal() {
        return value;
    }

    public Money add(Money another) {
        return new Money(value.add(another.value));
    }

    public Money subtract(Money another) {
        return new Money(value.subtract(another.value));
    }

    public Money negate() {
        return new Money(value.negate());
    }

    @Override
    public final boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Money another)) {
            return false;
        }

        return value.stripTrailingZeros().equals(another.value.stripTrailingZeros());
    }

    public boolean notEquals(Object object) {
        return !equals(object);
    }

    public boolean greaterThan(Money another) {
        return value.compareTo(another.value) > 0;
    }

    public boolean greaterThanOrEquals(Money another) {
        return value.compareTo(another.value) >= 0;
    }

    public boolean lessThan(Money another) {
        return value.compareTo(another.value) < 0;
    }

    public boolean lessThanOrEquals(Money another) {
        return value.compareTo(another.value) <= 0;
    }

    @Override
    public int hashCode() {
        return value.stripTrailingZeros().hashCode();
    }
}
