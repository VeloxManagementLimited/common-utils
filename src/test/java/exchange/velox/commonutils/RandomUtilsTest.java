package exchange.velox.commonutils;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;

public class RandomUtilsTest {

    @Test
    public void testRandom() {

        StopWatch sw = new StopWatch();
        sw.start();

        String cur = "", last = "", secondLast = "";
        int nDoubles = 0;
        int nTriples = 0;

        for (int i = 0; i < 1_000_000; i++) {
            cur = RandomUtils.random(100);
            if (cur.equals(last)) {
                nDoubles++;
                if (cur.equals(secondLast)) {
                    nTriples++;
                }
            }
            secondLast = last;
            last = cur;
        }
        sw.stop();

        System.out.println(sw.toString());
        System.out.println("Doubles: " + nDoubles);
        System.out.println("Triples: " + nTriples);
    }


    @Test
    public void testRandomBytes() {

        StopWatch sw = new StopWatch();
        sw.start();

        for (int i = 0; i < 1_000_000; i++) {
            RandomUtils.randomBytes(100);
        }
        sw.stop();

        System.out.println(sw.toString());
    }
}
