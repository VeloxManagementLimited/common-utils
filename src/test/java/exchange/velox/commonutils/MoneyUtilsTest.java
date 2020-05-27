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
}
