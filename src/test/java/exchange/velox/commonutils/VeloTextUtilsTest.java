package exchange.velox.commonutils;

import org.junit.Assert;
import org.junit.Test;

public class VeloTextUtilsTest {

    @Test
    public void testHowSimilar() {
        double threshold = 0.9d;
        Assert.assertTrue(VeloTextUtils.howSimilar("Mr Dung Tuan BUI", "Bui Tuan Dung") >= threshold);
        Assert.assertTrue(VeloTextUtils.howSimilar("abc", "def") == 0);
        Assert.assertTrue(VeloTextUtils.howSimilar("中国", "中国中") < threshold);
    }

    @Test
    public void testSlugify() {

        Assert.assertEquals("-AbC-X-yZ-abc.PdF",
                            VeloTextUtils.slugify(" AbC X-yZ#$!~*&^-+.abc.PdF"));

        Assert.assertEquals("aao",
                            VeloTextUtils.slugify("aáô"));

        Assert.assertEquals("", VeloTextUtils.slugify(null));
        Assert.assertEquals("", VeloTextUtils.slugify("    "));
    }
}