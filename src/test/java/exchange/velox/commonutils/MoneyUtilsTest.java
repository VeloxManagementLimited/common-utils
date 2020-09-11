package exchange.velox.commonutils;//package exchange.velox.platform.util;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

public class MoneyUtilsTest {

    @Test
    public void testCutTo2Decimal() {
        Assert.assertEquals(new BigDecimal("100.51"), MoneyUtils.cutTo2Decimal(new BigDecimal("100.512")));
        Assert.assertEquals(new BigDecimal("100.49"), MoneyUtils.cutTo2Decimal(new BigDecimal("100.499")));
        Assert.assertNull(MoneyUtils.cutTo2Decimal(null));
    }

    @Test
    public void testCutTo4Double() {
        Assert.assertEquals(Double.valueOf(12345.1234), MoneyUtils.cutTo4Double(12345.12345));
        Assert.assertEquals(Double.valueOf(100.1234), MoneyUtils.cutTo4Double(100.1234567));
        Assert.assertEquals(Double.valueOf(1.0123), MoneyUtils.cutTo4Double(1.01234));
        Assert.assertNull(MoneyUtils.cutTo4Double(null));
    }

    @Test
    public void testHalfUp() {
        Assert.assertEquals(new BigDecimal("100.56"), MoneyUtils.halfUp(new BigDecimal("100.559")));
        Assert.assertEquals(new BigDecimal("100.45"), MoneyUtils.halfUp(new BigDecimal("100.454")));
        Assert.assertNull(MoneyUtils.halfUp(null));
    }

    @Test
    public void testFloor() {
        Assert.assertEquals(new BigDecimal("100.00"), MoneyUtils.floor(new BigDecimal("100.29")));
        Assert.assertNull(MoneyUtils.cutTo2Decimal(null));
    }

    @Test
    public void testGetBigDecimal() {
        Assert.assertEquals(new BigDecimal("1234.56"), MoneyUtils.getBigDecimal("1234.56"));
        Assert.assertEquals(new BigDecimal("1234.56"), MoneyUtils.getBigDecimal("1,234.56"));

        Assert.assertEquals(new BigDecimal("1234.56"), MoneyUtils.getBigDecimal(new BigDecimal("1234.56")));
        Assert.assertTrue(new BigDecimal("1234.00").compareTo(MoneyUtils.getBigDecimal(BigInteger.valueOf(1_234))) == 0);


        Assert.assertNotEquals(new BigDecimal("1234.56"), MoneyUtils.getBigDecimal("1,134.56"));

        Assert.assertNull(MoneyUtils.getBigDecimal(""));
        Assert.assertNull(MoneyUtils.getBigDecimal(" "));
        Assert.assertNull(MoneyUtils.getBigDecimal(", "));
        Assert.assertNull(MoneyUtils.getBigDecimal(null));
    }

    @Test
    public void testRoundToThoundsandIfLarger(){
        Assert.assertEquals(new BigDecimal("11000"), MoneyUtils.roundToThousandIfLarger(new BigDecimal("11051.12")));
        Assert.assertEquals(new BigDecimal("955.12"), MoneyUtils.roundToThousandIfLarger(new BigDecimal("955.12")));
        Assert.assertEquals(new BigDecimal("3.123"), MoneyUtils.roundToThousandIfLarger(new BigDecimal("3.123")));
        Assert.assertNull(MoneyUtils.roundToThousandIfLarger(null));
    }

    @Test
    public void testDivide() {
        Assert.assertEquals(new BigDecimal("33.333333333"), MoneyUtils.divide(new BigDecimal("100.00"), new BigDecimal("3")));
        Assert.assertEquals(new BigDecimal("50.000000000"), MoneyUtils.divide(new BigDecimal("100.00"), new BigDecimal("2")));
        Assert.assertEquals(new BigDecimal("1.299000000"), MoneyUtils.divide(new BigDecimal("2.598"), new BigDecimal("2")));
        Assert.assertNull(MoneyUtils.divide(null, BigDecimal.ONE));
    }

    @Test(expected = ArithmeticException.class)
    public void testDivideByZero() {
        MoneyUtils.divide(new BigDecimal("1"), BigDecimal.ZERO);
    }

    @Test
    public void isValidCurrency() {
        Assert.assertFalse(MoneyUtils.isValidCurrency("AAA"));
        Assert.assertTrue(MoneyUtils.isValidCurrency("USD"));
        Assert.assertFalse(MoneyUtils.isValidCurrency("usd"));
        Assert.assertTrue(MoneyUtils.isValidCurrency("EUR"));
        Assert.assertFalse(MoneyUtils.isValidCurrency("eur"));
    }

    @Test
    public void testGetMoneyIntoWords() {
        String convertUSDNumber = MoneyUtils.getMoneyIntoWords(1234123456789L, "USD");
        String convertUSDNumberWithDecimal = MoneyUtils.getMoneyIntoWords(1234123456789.23, "USD");

        Assert.assertEquals("one trillion two hundred thirty four billion one hundred twenty three million " +
                "four hundred fifty six thousand seven hundred eighty nine US Dollars", convertUSDNumber);
        Assert.assertEquals("one trillion two hundred thirty four billion one hundred twenty three million four " +
                "hundred fifty six thousand seven hundred eighty nine US Dollars and twenty three cents", convertUSDNumberWithDecimal);

        String convertCNYNumber = MoneyUtils.getMoneyIntoWords(123456789, "CNY");
        String convertCNYWithDecimal = MoneyUtils.getMoneyIntoWords(123456789.23, "CNY");

        Assert.assertEquals("one hundred twenty three million four hundred fifty six thousand seven " +
                "hundred eighty nine Chinese Yuan", convertCNYNumber);
        Assert.assertEquals("one hundred twenty three million four hundred fifty six thousand seven " +
                "hundred eighty nine Chinese Yuan and twenty three fens", convertCNYWithDecimal);

        String convertEURNumber = MoneyUtils.getMoneyIntoWords(37565820, "EUR");
        String convertEurNumberWithDecimal = MoneyUtils.getMoneyIntoWords(37565820.29, "EUR");

        Assert.assertEquals("thirty seven million five hundred sixty five thousand eight hundred twenty Euro",
                convertEURNumber);
        Assert.assertEquals("thirty seven million five hundred sixty five thousand eight hundred twenty Euro " +
                "and twenty nine cents", convertEurNumberWithDecimal);

        String convertSGDNumber = MoneyUtils.getMoneyIntoWords(9341947, "SGD");
        String convertSGDNumberWithDecimal = MoneyUtils.getMoneyIntoWords(9341947.20, "SGD");

        Assert.assertEquals("nine million three hundred forty one thousand nine hundred forty seven " +
                "Singapore Dollars", convertSGDNumber);
        Assert.assertEquals("nine million three hundred forty one thousand nine hundred forty seven " +
                "Singapore Dollars and twenty cents", convertSGDNumberWithDecimal);

        String convertHKDNumber = MoneyUtils.getMoneyIntoWords(37000, "HKD");
        String convertHKDNumberWithDecimal = MoneyUtils.getMoneyIntoWords(37000.90, "HKD");

        Assert.assertEquals("thirty seven thousand Hong Kong Dollars", convertHKDNumber);
        Assert.assertEquals("thirty seven thousand Hong Kong Dollars and ninety cents", convertHKDNumberWithDecimal);

        String convertJPYNumber = MoneyUtils.getMoneyIntoWords(1387, "JPY");
        String convertJPYNumberWithDecimal = MoneyUtils.getMoneyIntoWords(1387.18, "JPY");

        Assert.assertEquals("one thousand three hundred eighty seven Japanese Yen", convertJPYNumber);
        Assert.assertEquals("one thousand three hundred eighty seven Japanese Yen and eighteen sens",
                convertJPYNumberWithDecimal);

        String convertGBPNumber = MoneyUtils.getMoneyIntoWords(10, "GBP");
        String convertGBPNumberWithDecimal = MoneyUtils.getMoneyIntoWords(10.23, "GBP");

        Assert.assertEquals("ten Pound Sterling", convertGBPNumber);
        Assert.assertEquals("ten Pound Sterling and twenty three pennies",
                convertGBPNumberWithDecimal);

        String convertVNDNumber = MoneyUtils.getMoneyIntoWords(10, "VND");
        String convertVNDNumberWithDecimal = MoneyUtils.getMoneyIntoWords(10.23, "VND");

        Assert.assertEquals("ten VND", convertVNDNumber);
        Assert.assertEquals("ten VND and twenty three ",
                convertVNDNumberWithDecimal);
    }
}
