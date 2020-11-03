package exchange.velox.commonutils;

import java.util.Date;

/**
 * Alias of DateTimeUtils.
 */
public class DTU extends DateTimeUtils {

    public static long diff(Date from, Date to) {
        return daysDiff(from, to);
    }

    public static boolean before(Date a, Date b) {
        return dateBefore(a, b);
    }

    public static boolean onOrBefore(Date a, Date b) {
        return dateBeforeOrSame(a, b);
    }

    public static boolean after(Date a, Date b) {
        return dateAfter(a, b);
    }

    public static boolean onOrAfter(Date a, Date b) {
        return dateAfterOrSame(a, b);
    }

    public static boolean same(Date a, Date b) {
        return dateSame(a, b);
    }
}
