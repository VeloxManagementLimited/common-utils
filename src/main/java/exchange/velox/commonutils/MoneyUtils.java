package exchange.velox.commonutils;

import org.apache.commons.lang3.StringUtils;

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

    private static final String INVALID_INPUT_GIVEN = "Invalid input given";

    private static final String[] units = {
            "",
            " one",
            " two",
            " three",
            " four",
            " five",
            " six",
            " seven",
            " eight",
            " nine"
    };

    private static final String[] twoDigits = {
            " ten",
            " eleven",
            " twelve",
            " thirteen",
            " fourteen",
            " fifteen",
            " sixteen",
            " seventeen",
            " eighteen",
            " nineteen"
    };

    private static final String[] tenMultiples = {
            "",
            "",
            " twenty",
            " thirty",
            " forty",
            " fifty",
            " sixty",
            " seventy",
            " eighty",
            " ninety"
    };

    private static final String[] placeValues = {
            " ",
            " thousand",
            " million",
            " billion",
            " trillion"
    };

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

    public static Double cutTo4Double(Double rate) {
        DecimalFormat decimalFormat = new DecimalFormat("#.####");
        decimalFormat.setRoundingMode(RoundingMode.DOWN);
        if (rate == null) {
            return null;
        }
        return Double.valueOf(decimalFormat.format(rate));
    }

    public static BigDecimal halfUp(BigDecimal amount) {
        if (amount == null) {
            return null;
        }
        return amount.setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal roundToThousandIfLarger(BigDecimal amount){
        return roundToNumberIfLarger(amount, 1_000);
    }

    public static BigDecimal roundToNumberIfLarger(BigDecimal amount, long number) {
        if (amount == null) {
            return null;
        }
        BigDecimal numberDecimal = BigDecimal.valueOf(number);
        if (amount.compareTo(numberDecimal) < 1) {
            return amount;
        }
        return amount.divide(numberDecimal,0, RoundingMode.DOWN).multiply(numberDecimal);
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
                String s = StringUtils.remove((String) number, ",");
                s = StringUtils.remove(s, " ");
                if (StringUtils.isNotBlank(s)) {
                    value = new BigDecimal(s);
                }
            } else if (number instanceof BigInteger) {
                value = new BigDecimal((BigInteger) number);
            } else if (number instanceof Number) {
                value = new BigDecimal(((Number) number).doubleValue());
            }
        }
        return value;
    }
    
    public static BigDecimal negate(BigDecimal value) {
        if (value == null) {
            return null;
        }
        return value.abs().negate();
    }

    public static boolean equal(BigDecimal val1, BigDecimal val2) {
        if (val1 == null && val2 == null) {
            return true;
        }
        if (val1 == null || val2 == null) {
            return false;
        }
        return val1.compareTo(val2) == 0;
    }

    public static String getMoneyIntoWords(double money, String currency) {
        long dollar = (long) money;
        long cents = Math.round((money - dollar) * 100);
        if (money == 0D) {
            return "";
        }
        if (money < 0) {
            return INVALID_INPUT_GIVEN;
        }
        String dollarPart = "";
        if (dollar > 0) {
            dollarPart = (isSpace(convert(dollar)) ? getCurrencyIntoWords(dollar, currency) : " "
                    + getCurrencyIntoWords(dollar, currency)) + convert(dollar);
        }
        String centsPart = "";
        if (cents > 0) {
            if (dollarPart.length() > 0) {
                centsPart = "and";
            }
            centsPart += convert(cents) + getMinimumMonetaryValue(cents, currency);
        }
        return dollarPart + centsPart;
    }

    private static String getCurrencyIntoWords(long dollar, String currency) {
        switch (currency) {
            case "CNY":
                return "Chinese Yuan";
            case "EUR":
                return "Euro";
            case "GBP":
                return "Pound Sterling";
            case "HKD":
                return "Hong Kong Dollar" + (dollar == 1 ? "" : "s");
            case "JPY":
                return "Japanese Yen";
            case "SGD":
                return "Singapore Dollar" + (dollar == 1 ? "" : "s");
            case "USD":
                return "US Dollar" + (dollar == 1 ? "" : "s");
            default:
                return currency;
        }
    }

    private static String getMinimumMonetaryValue(long cent, String currency) {
        switch (currency) {
            case "CNY":
                return "fen" + (cent == 1 ? "" : "s");
            case "EUR":
            case "SGD":
            case "HKD":
            case "USD":
                return "cent" + (cent == 1 ? "" : "s");
            case "GBP":
                return (cent == 1 ? "penny" : "pennies");
            case "JPY":
                return "sen" + (cent == 1 ? "" : "s");
            default:
                return "";
        }
    }

    private static String convert(long number) {
        StringBuilder word = new StringBuilder();
        int index = 0;
        do {
            int num = (int) (number % 1000);
            if (num != 0) {
                String str = conversionForUpToThreeDigits(num);
                word.insert(0, str + placeValues[index]);
            }
            index++;
            number = number / 1000;
        } while (number > 0);
        return word.toString();
    }

    private static String conversionForUpToThreeDigits(int number) {
        String word = "";
        int num = number % 100;
        if (num < 10) {
            word = word + units[num];
        } else if (num < 20) {
            word = word + twoDigits[num % 10];
        } else {
            word = tenMultiples[num / 10] + units[num % 10];
        }

        word = (number / 100 > 0) ? units[number / 100] + " hundred" + word : word;
        return word;
    }

    private static boolean isSpace(String string) {
        return Character.isWhitespace(string.charAt(string.length() - 1));
    }
}
