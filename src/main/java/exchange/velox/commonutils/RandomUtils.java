package exchange.velox.commonutils;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomUtils {

    public static String random(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }

    public static byte[] randomBytes(int length) {
        return org.apache.commons.lang3.RandomUtils.nextBytes(length);
    }
}
