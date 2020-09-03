package exchange.velox.commonutils;

import org.junit.Assert;
import org.junit.Test;

public class JSONUtilsTest {

    @Test
    public void testStringToObject() {
        String json = "{\"p1\":\"v1\", \"p3\":\"v3\"}";
        TestDTO obj = (TestDTO) JSONUtils.stringToObject(json, TestDTO.class);

        Assert.assertEquals("v1", obj.getP1());
        Assert.assertNull(obj.getP2());
    }
}
