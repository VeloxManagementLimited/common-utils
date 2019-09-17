package exchange.velox.commonutils;

import javax.money.Monetary;
import javax.money.UnknownCurrencyException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MoneyUtils {

    public static final BigDecimal D_2 = BigDecimal.valueOf(2);
    public static final BigDecimal D_7 = BigDecimal.valueOf(7);
    public static final BigDecimal D_100 = BigDecimal.valueOf(100);
    public static final BigDecimal D_365 = BigDecimal.valueOf(365);
    public static final BigDecimal D_360 = BigDecimal.valueOf(360);
    public static final BigDecimal D_1000 = BigDecimal.valueOf(1_000);

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0.00");

    public static BigDecimal add(BigDecimal amount1, BigDecimal amount2) {
        if (amount1 == null) {
            amount1 = BigDecimal.ZERO;
        }
        if (amount2 == null) {
            amount2 = BigDecimal.ZERO;
        }
        return amount1.add(amount2);
    }

    public static BigDecimal getPercent(BigDecimal budget, BigDecimal percent) {
        if (budget == null || percent == null) {
            return BigDecimal.ZERO;
        }
        return cutTo2Decimal(budget.multiply(MoneyUtils.divide(percent, D_100)));
    }

    public static BigDecimal getPercent(BigDecimal budget, Integer percent) {
        return getPercent(budget, toBigDecimal(percent));
    }

    public static String toLog(BigDecimal amount) {
        if (amount == null) {
            return "nil";
        }
        return print(amount);
    }

    public static String print(BigDecimal amount) {
        if (amount == null) {
            return "";
        }
        return DECIMAL_FORMAT.format(cutTo2Decimal(amount));
    }

    public static boolean isZero(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) == 0;
    }

    public static boolean isNullOrZero(BigDecimal amount) {
        return amount == null || amount.compareTo(BigDecimal.ZERO) == 0;
    }

    public static boolean isNotNullAndNotZero(BigDecimal amount) {
        return amount != null && amount.compareTo(BigDecimal.ZERO) != 0;
    }

    public static BigDecimal toBigDecimal(Integer i) {
        return i == null ? null : new BigDecimal(i);
    }

    public static BigDecimal toBigDecimal(Long l) {
        return l == null ? null : BigDecimal.valueOf(l);
    }

    public static boolean isGreaterThanZero(BigDecimal amount) {
        return amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public static boolean isNotGreaterThanZero(BigDecimal amount) {
        return !isGreaterThanZero(amount);
    }

    public static BigDecimal zeroIfNegative(BigDecimal amount) {
        return BigDecimal.ZERO.max(amount);
    }

    public static BigDecimal zeroIfNull(BigDecimal amount) {
        return amount == null ? BigDecimal.ZERO : amount;
    }

    public static BigDecimal max(BigDecimal amount1, BigDecimal amount2) {
        return amount1.max(amount2);
    }

    public static BigDecimal min(BigDecimal amount1, BigDecimal amount2) {
        return amount1.min(amount2);
    }

    /**
     * Modulus.
     *
     * Example: 100.00 % 22.00 = 12.00
     *
     * @param amount
     * @param divisor
     * @return amount % divisor, result can be negative
     */
    public static BigDecimal mod(BigDecimal amount, BigDecimal divisor) {
        return amount.remainder(divisor);
    }

    public static BigDecimal cutTo2Decimal(BigDecimal amount) {
        if (amount == null) {
            return null;
        }
        return amount.setScale(2, RoundingMode.DOWN);
    }

    public static BigDecimal halfUp(BigDecimal amount) {
        if (amount == null) {
            return null;
        }
        return amount.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * To make sure we dont get ArithmeticException when the result is infinite float number
     * @param amount
     * @param divisor
     * @return
     */
    public static BigDecimal divide(BigDecimal amount, BigDecimal divisor) {
        if (amount == null) {
            return null;
        }
        return amount.divide(divisor, 9, RoundingMode.DOWN);
    }

    public static BigDecimal floor(BigDecimal amount) {
        if (amount == null) {
            return null;
        }
        return amount.setScale(0, RoundingMode.DOWN)
                     .setScale(2, RoundingMode.DOWN);
    }

    public static BigDecimal ceil(BigDecimal amount) {
        if (amount == null) {
            return null;
        }
        return amount.setScale(0, RoundingMode.UP)
                     .setScale(2, RoundingMode.DOWN);
    }

    public static boolean isValidCurrency(String currency) {
        try {
            Monetary.getCurrency(currency);
        } catch (UnknownCurrencyException e) {
            return false;
        }
        return true;
    }

    public static BigDecimal getBigDecimal(Object number) {
        BigDecimal value = null;
        if (number != null) {
            if (number instanceof BigDecimal) {
                value = (BigDecimal) number;
            } else if (number instanceof String) {
                value = new BigDecimal((String) number);
            } else if (number instanceof BigInteger) {
                value = new BigDecimal((BigInteger) number);
            } else if (number instanceof Number) {
                value = new BigDecimal(((Number) number).doubleValue());
            }
        }
        return value;
    }
}
