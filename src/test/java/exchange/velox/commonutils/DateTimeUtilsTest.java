package exchange.velox.commonutils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtilsTest {


    @Test
    public void testNowAtHK() {
        System.out.println(DateTimeUtils.nowAtHK());
    }

    @Test
    public void testSwitchZoneWithoutChangingValue() {
        Date now = new Date();
        Date switched = DateTimeUtils.switchToHKTWithoutChangingValue(now);
        System.out.println(now);
        System.out.println(switched);

        Assert.assertEquals(now, switched);
    }

    @Test
    public void testIsTodayInHK() {
        Date now = new Date();

        Assert.assertTrue(DateTimeUtils.isTodayInHK(now));

        Assert.assertFalse(DateTimeUtils.isTodayInHK(DateTimeUtils.plusDays(now, 1)));
        Assert.assertFalse(DateTimeUtils.isTodayInHK(DateTimeUtils.plusDays(now, -1)));
    }

    @Test
    public void testDaysDiffAbs() {
        Date from = null, to = null;

        from = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 18, 23,
                24, 30), DateTimeUtils.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        to = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 19, 0,
                35, 00), DateTimeUtils.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        Assert.assertEquals(1, DateTimeUtils.daysDiffAbs(from, to));

        from = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 18, 13,
                24, 30), DateTimeUtils.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        to = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 31, 8,
                35, 00), DateTimeUtils.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        Assert.assertEquals(13, DateTimeUtils.daysDiffAbs(from, to));

        from = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 18, 13,
                24, 30), DateTimeUtils.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        to = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 31, 23,
                24, 30), DateTimeUtils.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        Assert.assertEquals(13, DateTimeUtils.daysDiffAbs(from, to));
        Assert.assertEquals(13, DateTimeUtils.daysDiffAbs(to, from));
    }

    @Test
    public void testDaysDiff() {
        Date from = null, to = null;

        from = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 18, 13,
                                                           24, 30), DateTimeUtils.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        to = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 19, 8,
                                                         24, 20), DateTimeUtils.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        Assert.assertEquals(1, DateTimeUtils.daysDiff(from, to));
        Assert.assertEquals(-1, DateTimeUtils.daysDiff(to, from));

        from = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 18, 13,
                                                           24, 30), DateTimeUtils.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        to = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 19, 14,
                                                         24, 20), DateTimeUtils.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        Assert.assertEquals(1, DateTimeUtils.daysDiff(from, to));
        Assert.assertEquals(-1, DateTimeUtils.daysDiff(to, from));

        from = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 18, 1,
                                                           24, 30), DateTimeUtils.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        to = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 18, 20,
                                                         24, 20), DateTimeUtils.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        Assert.assertEquals(0, DateTimeUtils.daysDiff(from, to));
        Assert.assertEquals(0, DateTimeUtils.daysDiff(to, from));

        from = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 18, 1,
                                                           24, 30), DateTimeUtils.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        to = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 22, 20,
                                                         24, 20), DateTimeUtils.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        Assert.assertEquals(4, DateTimeUtils.daysDiff(from, to));
        Assert.assertEquals(-4, DateTimeUtils.daysDiff(to, from));
    }

    @Test
    public void testDaysBeforeAfterSame() {
        Date a = null, b = null;

        a = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 18, 23,
                                                           59, 59), DateTimeUtils.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        b = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 19, 0,
                                                         0, 0), DateTimeUtils.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        Assert.assertTrue(DateTimeUtils.dateBefore(a, b));
        Assert.assertTrue(DateTimeUtils.dateAfter(b, a));


        a = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 19, 23,
                                                        59, 59), DateTimeUtils.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        b = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 19, 0,
                                                        0, 0), DateTimeUtils.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        Assert.assertTrue(DateTimeUtils.dateSame(a, b));


    }


    @Test
    public void testMinutesDiff() {
        Date from = null, to = null;

        from = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 18,
                23, 24, 30), DateTimeUtils.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        to = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 19,
                0, 35, 00), DateTimeUtils.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        Assert.assertEquals(70, DateTimeUtils.minutesDiff(from, to));


        from = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 18,
                20, 25, 30), DateTimeUtils.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        to = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 18,
                20, 30, 00), DateTimeUtils.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        Assert.assertEquals(4, DateTimeUtils.minutesDiff(from, to));

        from = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 18,
                20, 25, 30), DateTimeUtils.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        to = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 18,
                20, 30, 30), DateTimeUtils.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        Assert.assertEquals(5, DateTimeUtils.minutesDiff(from, to));

    }


    @Test
    public void testParseIsoDate() {

        Date date = DateTimeUtils.parseIsoDate("2018-12-25");
        Calendar cal = Calendar.getInstance(DateTimeUtils.DEFAULT_TIMEZONE);
        cal.setTime(date);
        Assert.assertEquals(2018, cal.get(Calendar.YEAR));
        Assert.assertEquals(11, cal.get(Calendar.MONTH));
        Assert.assertEquals(25, cal.get(Calendar.DAY_OF_MONTH));

        Date date1 = DateTimeUtils.parseIsoDate("2018-06-01");
        Date date2 = DateTimeUtils.parseIsoDate("2018-06-1");
        Date date3 = DateTimeUtils.parseIsoDate("2018-6-01");
        Date date4 = DateTimeUtils.parseIsoDate("2018-6-1");

        Assert.assertEquals(date1, date2);
        Assert.assertEquals(date2, date3);
        Assert.assertEquals(date3, date4);

    }

    @Test
    public void testPrintDate() {
        Assert.assertEquals("25/12/2018", DateTimeUtils.printDate(DateTimeUtils.parseIsoDate("2018-12-25")));
        Assert.assertEquals("01/06/2018", DateTimeUtils.printDate(DateTimeUtils.parseIsoDate("2018-06-1")));
    }

    @Test
    public void testPrintTime() {
        LocalDateTime ldt = LocalDateTime.of(2018, 5, 20, 20, 35, 59, 999);
        Assert.assertEquals("20:35", DateTimeUtils.printTime(Date.from(ldt.atZone(DateTimeUtils.DEFAULT_TIMEZONE.toZoneId()).toInstant())));
    }

    @Test
    public void testPrintDateTime() {
        LocalDateTime ldt = LocalDateTime.of(2018, 5, 20, 20, 35, 59, 999);
        Assert.assertEquals("20/05/2018 20:35", DateTimeUtils.printDateTime(Date.from(ldt.atZone(DateTimeUtils.DEFAULT_TIMEZONE.toZoneId()).toInstant())));
    }


    @Test
    public void testPlusOneDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");
        sdf.setTimeZone(DateTimeUtils.DEFAULT_TIMEZONE);

        Date actual = DateTimeUtils.plusOneDay(DateTimeUtils.parseIsoDate("2018-12-25"));
        Assert.assertEquals("2018-12-26 00:00:00.000 +0800", sdf.format(actual));

        actual = DateTimeUtils.plusOneDay(DateTimeUtils.parseIsoDate("2018-02-28"));
        Assert.assertEquals("2018-03-01 00:00:00.000 +0800", sdf.format(actual));
    }

    @Test
    public void testFormatDate() {
        LocalDateTime ldt = LocalDateTime.of(2018, 5, 20, 20, 35, 59, 999);
        Date date = Date.from(ldt.atZone(DateTimeUtils.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        String output = DateTimeUtils.formatDate(date, "dd MMM yyyy HH:mm", Locale.ENGLISH, DateTimeUtils.DEFAULT_TIMEZONE);
        Assert.assertEquals("20 May 2018 20:35", output);
    }

    @Test
    public void tesIsBusinessDate() {
        Assert.assertTrue(DateTimeUtils.isBusinessDate(DateTimeUtils.parseIsoDate("2019-06-14")));
        Assert.assertTrue(DateTimeUtils.isBusinessDate(DateTimeUtils.parseIsoDate("2019-06-17")));

        Assert.assertFalse(DateTimeUtils.isBusinessDate(DateTimeUtils.parseIsoDate("2019-06-15")));
        Assert.assertFalse(DateTimeUtils.isBusinessDate(DateTimeUtils.parseIsoDate("2019-06-16")));
        Assert.assertFalse(DateTimeUtils.isBusinessDate(DateTimeUtils.parseIsoDate("2019-07-01")));
        Assert.assertFalse(DateTimeUtils.isBusinessDate(DateTimeUtils.parseIsoDate("2020-01-01")));

        Assert.assertFalse(DateTimeUtils.isBusinessDate(DateTimeUtils.parseIsoDate("2019-07-04")));
        Assert.assertFalse(DateTimeUtils.isBusinessDate(DateTimeUtils.parseIsoDate("2020-07-04")));
    }

    @Test
    public void testToNextBusinessDateIfNot() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");
        sdf.setTimeZone(DateTimeUtils.DEFAULT_TIMEZONE);

        Date actual;

        actual = DateTimeUtils.toNextBusinessDateIfNot(DateTimeUtils.parseIsoDate("2019-06-14"));
        Assert.assertEquals("2019-06-14 00:00:00.000 +0800", sdf.format(actual));

        actual = DateTimeUtils.toNextBusinessDateIfNot(DateTimeUtils.parseIsoDate("2019-06-15"));
        Assert.assertEquals("2019-06-17 00:00:00.000 +0800", sdf.format(actual));
    }

    @Test
    public void testNextBusinessDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");
        sdf.setTimeZone(DateTimeUtils.DEFAULT_TIMEZONE);

        Date actual;

        actual = DateTimeUtils.nextBusinessDate(DateTimeUtils.parseIsoDate("2019-06-14"));
        Assert.assertEquals("2019-06-17 00:00:00.000 +0800", sdf.format(actual));

        actual = DateTimeUtils.nextBusinessDate(DateTimeUtils.parseIsoDate("2019-06-15"));
        Assert.assertEquals("2019-06-17 00:00:00.000 +0800", sdf.format(actual));

        actual = DateTimeUtils.nextBusinessDate(DateTimeUtils.parseIsoDate("2019-06-17"));
        Assert.assertEquals("2019-06-18 00:00:00.000 +0800", sdf.format(actual));
    }

    @Test
    public void testNext2BusinessDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");
        sdf.setTimeZone(DateTimeUtils.DEFAULT_TIMEZONE);

        Date actual;

        actual = DateTimeUtils.next2BusinessDate(DateTimeUtils.parseIsoDate("2019-06-14"));
        Assert.assertEquals("2019-06-18 00:00:00.000 +0800", sdf.format(actual));

        actual = DateTimeUtils.next2BusinessDate(DateTimeUtils.parseIsoDate("2019-06-15"));
        Assert.assertEquals("2019-06-18 00:00:00.000 +0800", sdf.format(actual));

        actual = DateTimeUtils.next2BusinessDate(DateTimeUtils.parseIsoDate("2019-06-13"));
        Assert.assertEquals("2019-06-17 00:00:00.000 +0800", sdf.format(actual));
    }

    @Test
    public void testIsBeforeVelotradeCutOffTime() {
        DateTime d = new DateTime(DateTimeUtils.nowAtHK()).withTime(15, 01, 59, 0).withZoneRetainFields(
                DateTimeZone.forTimeZone(DateTimeUtils.DEFAULT_TIMEZONE));
        Assert.assertTrue(DateTimeUtils.isBeforeVelotradeCutOffTime(d.toDate()));

        d = new DateTime(DateTimeUtils.nowAtHK()).withTime(15, 02, 0, 0).withZoneRetainFields(
                DateTimeZone.forTimeZone(DateTimeUtils.DEFAULT_TIMEZONE));
        Assert.assertFalse(DateTimeUtils.isBeforeVelotradeCutOffTime(d.toDate()));
    }

    @Test
    public void testIsBeforeDBSCutOffTime() {
        DateTime d = new DateTime(DateTimeUtils.nowAtHK()).withTime(17, 24, 59, 0).withZoneRetainFields(
                DateTimeZone.forTimeZone(DateTimeUtils.DEFAULT_TIMEZONE));
        Assert.assertTrue(DateTimeUtils.isBeforeDBSCutOffTime(d.toDate()));

        d = new DateTime(DateTimeUtils.nowAtHK()).withTime(17, 25, 0, 0).withZoneRetainFields(
                DateTimeZone.forTimeZone(DateTimeUtils.DEFAULT_TIMEZONE));
        Assert.assertFalse(DateTimeUtils.isBeforeDBSCutOffTime(d.toDate()));
    }
}
