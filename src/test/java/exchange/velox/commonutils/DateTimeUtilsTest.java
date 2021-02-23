package exchange.velox.commonutils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeUtilsTest {

    @Test
    public void testNowAtHK() {
        System.out.println(DTU.nowAtHK());
    }

    @Test
    public void testSwitchZoneWithoutChangingValue() {
        Date now = new Date();
        Date switched = DTU.switchToHKTWithoutChangingValue(now);
        System.out.println(now);
        System.out.println(switched);

        Assert.assertEquals(now, switched);
    }

    @Test
    public void testIsTodayInHK() {
        Date now = new Date();

        Assert.assertTrue(DTU.isTodayInHK(now));

        Assert.assertFalse(DTU.isTodayInHK(DTU.plusDays(now, 1)));
        Assert.assertFalse(DTU.isTodayInHK(DTU.plusDays(now, -1)));
    }


    @Test
    public void testPrintCustom() throws ParseException {
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date dt  = isoFormat.parse("2020-09-15 15:00:00.0");
        String date = DTU.printCustomDate(dt);
        Assert.assertEquals(date, "2020-09-15");
        dt  = isoFormat.parse("2020-09-15 16:00:00.0");
        date = DTU.printCustomDate(dt);
        Assert.assertEquals(date, "2020-09-16");
    }

    @Test
    public void testDaysDiffAbs() {
        Date from = null, to = null;

        from = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 18, 23,
                24, 30), DTU.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        to = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 19, 0,
                35, 00), DTU.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        Assert.assertEquals(1, DTU.daysDiffAbs(from, to));

        from = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 18, 13,
                24, 30), DTU.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        to = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 31, 8,
                35, 00), DTU.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        Assert.assertEquals(13, DTU.daysDiffAbs(from, to));

        from = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 18, 13,
                24, 30), DTU.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        to = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 31, 23,
                24, 30), DTU.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        Assert.assertEquals(13, DTU.daysDiffAbs(from, to));
        Assert.assertEquals(13, DTU.daysDiffAbs(to, from));
    }

    @Test
    public void testDaysDiff() {
        Date from = null, to = null;

        from = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 18, 13,
                                                           24, 30), DTU.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        to = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 19, 8,
                                                         24, 20), DTU.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        Assert.assertEquals(1, DTU.daysDiff(from, to));
        Assert.assertEquals(-1, DTU.daysDiff(to, from));

        from = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 18, 13,
                                                           24, 30), DTU.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        to = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 19, 14,
                                                         24, 20), DTU.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        Assert.assertEquals(1, DTU.daysDiff(from, to));
        Assert.assertEquals(-1, DTU.daysDiff(to, from));

        from = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 18, 1,
                                                           24, 30), DTU.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        to = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 18, 20,
                                                         24, 20), DTU.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        Assert.assertEquals(0, DTU.daysDiff(from, to));
        Assert.assertEquals(0, DTU.daysDiff(to, from));

        from = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 18, 1,
                                                           24, 30), DTU.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        to = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 22, 20,
                                                         24, 20), DTU.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        Assert.assertEquals(4, DTU.daysDiff(from, to));
        Assert.assertEquals(-4, DTU.daysDiff(to, from));
    }

    @Test
    public void test_businessDaysDiffHK() {


        Date from = null, to = null;

        from = Date.from(ZonedDateTime.of(LocalDateTime.of(2021, Month.MARCH, 30, 13,
                24, 30), DTU.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        to = Date.from(ZonedDateTime.of(LocalDateTime.of(2021, Month.APRIL, 7, 8,
                24, 20), DTU.DEFAULT_TIMEZONE.toZoneId()).toInstant());

        Assert.assertEquals(3, DateTimeUtils.businessDaysDiffHK(from, to, false));
        Assert.assertEquals(4, DateTimeUtils.businessDaysDiffHK(from, to, true));
    }

    @Test
    public void testDaysBeforeAfterSame() {
        Date a = null, b = null;

        a = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 18, 23,
                                                           59, 59), DTU.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        b = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 19, 0,
                                                         0, 0), DTU.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        Assert.assertTrue(DTU.dateBefore(a, b));
        Assert.assertTrue(DTU.onOrBefore(a, DTU.minusDays(b, 1)));

        Assert.assertTrue(DTU.dateAfter(b, a));
        Assert.assertTrue(DTU.dateAfterOrSame(b, DTU.plusDays(a, 1)));

        Assert.assertEquals(a, DTU.min(a, b));
        Assert.assertEquals(b, DTU.max(a, b));

        // --------------
        a = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 19, 23,
                                                        59, 59), DTU.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        b = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 19, 0,
                                                        0, 0), DTU.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        Assert.assertTrue(DTU.same(a, b));


    }


    @Test
    public void testMinutesDiff() {
        Date from = null, to = null;

        from = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 18,
                23, 24, 30), DTU.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        to = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 19,
                0, 35, 00), DTU.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        Assert.assertEquals(70, DTU.minutesDiff(from, to));


        from = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 18,
                20, 25, 30), DTU.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        to = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 18,
                20, 30, 00), DTU.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        Assert.assertEquals(4, DTU.minutesDiff(from, to));

        from = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 18,
                20, 25, 30), DTU.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        to = Date.from(ZonedDateTime.of(LocalDateTime.of(2018, Month.MAY, 18,
                20, 30, 30), DTU.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        Assert.assertEquals(5, DTU.minutesDiff(from, to));

    }


    @Test
    public void testParseIsoDate() {

        Date date = DTU.parseIsoDate("2018-12-25");
        Calendar cal = Calendar.getInstance(DTU.DEFAULT_TIMEZONE);
        cal.setTime(date);
        Assert.assertEquals(2018, cal.get(Calendar.YEAR));
        Assert.assertEquals(11, cal.get(Calendar.MONTH));
        Assert.assertEquals(25, cal.get(Calendar.DAY_OF_MONTH));

        Date date1 = DTU.parseIsoDate("2018-06-01");
        Date date2 = DTU.parseIsoDate("2018-06-1");
        Date date3 = DTU.parseIsoDate("2018-6-01");
        Date date4 = DTU.parseIsoDate("2018-6-1");

        Assert.assertEquals(date1, date2);
        Assert.assertEquals(date2, date3);
        Assert.assertEquals(date3, date4);

    }

    @Test
    public void testParseIsoDateTime() {
        Date date5 = DTU.parseIsoDateTime("2018-12-25 00:00:00.0");
        Calendar cal5 = Calendar.getInstance(DTU.DEFAULT_TIMEZONE);
        cal5.setTime(date5);
        Assert.assertEquals(2018, cal5.get(Calendar.YEAR));
        Assert.assertEquals(11, cal5.get(Calendar.MONTH));
        Assert.assertEquals(25, cal5.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void testPrintDate() {
        Assert.assertEquals("25/12/2018", DTU.printDate(DTU.parseIsoDate("2018-12-25")));
        Assert.assertEquals("01/06/2018", DTU.printDate(DTU.parseIsoDate("2018-06-1")));
    }

    @Test
    public void testPrintTime() {
        LocalDateTime ldt = LocalDateTime.of(2018, 5, 20, 20, 35, 59, 999);
        Assert.assertEquals("20:35", DTU.printTime(Date.from(ldt.atZone(DTU.DEFAULT_TIMEZONE.toZoneId()).toInstant())));
    }

    @Test
    public void testPrintDateTime() {
        LocalDateTime ldt = LocalDateTime.of(2018, 5, 20, 20, 35, 59, 999);
        Assert.assertEquals("20/05/2018 20:35", DTU.printDateTime(Date.from(ldt.atZone(DTU.DEFAULT_TIMEZONE.toZoneId()).toInstant())));
    }


    @Test
    public void testPlusOneDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");
        sdf.setTimeZone(DTU.DEFAULT_TIMEZONE);

        Date actual = DTU.plusOneDay(DTU.parseIsoDate("2018-12-25"));
        Assert.assertEquals("2018-12-26 00:00:00.000 +0800", sdf.format(actual));

        actual = DTU.plusOneDay(DTU.parseIsoDate("2018-02-28"));
        Assert.assertEquals("2018-03-01 00:00:00.000 +0800", sdf.format(actual));
    }

    @Test
    public void testFormatDate() {
        LocalDateTime ldt = LocalDateTime.of(2018, 5, 20, 20, 35, 59, 999);
        Date date = Date.from(ldt.atZone(DTU.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        String output = DTU.formatDate(date, "dd MMM yyyy HH:mm", Locale.ENGLISH, DTU.DEFAULT_TIMEZONE);
        Assert.assertEquals("20 May 2018 20:35", output);
    }

    @Test
    public void testFormatDate2() {
        LocalDateTime ldt = LocalDateTime.of(2018, 5, 20, 20, 35, 59, 999);
        Date date = Date.from(ldt.atZone(DTU.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        String output = DTU.formatDate(date, "yyyy-MM-dd", Locale.ENGLISH, DTU.DEFAULT_TIMEZONE);
        Assert.assertEquals("2018-05-20", output);
    }

    @Test
    public void testFormatDate3() {
        LocalDateTime ldt = LocalDateTime.of(2018, 5, 20, 20, 35, 59, 999);
        Date date = Date.from(ldt.atZone(DTU.DEFAULT_TIMEZONE.toZoneId()).toInstant());
        String output = DTU.formatDate(date, "dd-MM-YYYY", Locale.ENGLISH, DTU.DEFAULT_TIMEZONE);
        Assert.assertEquals("20-05-2018", output);
    }

    @Test
    public void tesIsBusinessDate() {
        Assert.assertTrue(DTU.isBusinessDate(DTU.parseIsoDate("2019-06-14")));
        Assert.assertTrue(DTU.isBusinessDate(DTU.parseIsoDate("2019-06-17")));

        Assert.assertFalse(DTU.isBusinessDate(DTU.parseIsoDate("2019-06-15")));
        Assert.assertFalse(DTU.isBusinessDate(DTU.parseIsoDate("2019-06-16")));
        Assert.assertFalse(DTU.isBusinessDate(DTU.parseIsoDate("2019-07-01")));
        Assert.assertFalse(DTU.isBusinessDate(DTU.parseIsoDate("2020-01-01")));

        Assert.assertFalse(DTU.isBusinessDate(DTU.parseIsoDate("2019-07-04")));
        Assert.assertFalse(DTU.isBusinessDate(DTU.parseIsoDate("2020-07-04")));
    }

    @Test
    public void testToNextBusinessDateIfNot() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");
        sdf.setTimeZone(DTU.DEFAULT_TIMEZONE);

        Date actual;

        actual = DTU.toNextBusinessDateIfNot(DTU.parseIsoDate("2019-06-14"));
        Assert.assertEquals("2019-06-14 00:00:00.000 +0800", sdf.format(actual));

        actual = DTU.toNextBusinessDateIfNot(DTU.parseIsoDate("2019-06-15"));
        Assert.assertEquals("2019-06-17 00:00:00.000 +0800", sdf.format(actual));
    }

    @Test
    public void testNextBusinessDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");
        sdf.setTimeZone(DTU.DEFAULT_TIMEZONE);

        Date actual;

        actual = DTU.nextBusinessDate(DTU.parseIsoDate("2019-06-14"));
        Assert.assertEquals("2019-06-17 00:00:00.000 +0800", sdf.format(actual));

        actual = DTU.nextBusinessDate(DTU.parseIsoDate("2019-06-15"));
        Assert.assertEquals("2019-06-17 00:00:00.000 +0800", sdf.format(actual));

        actual = DTU.nextBusinessDate(DTU.parseIsoDate("2019-06-17"));
        Assert.assertEquals("2019-06-18 00:00:00.000 +0800", sdf.format(actual));
    }

    @Test
    public void testNext2BusinessDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");
        sdf.setTimeZone(DTU.DEFAULT_TIMEZONE);

        Date actual;

        actual = DTU.next2BusinessDate(DTU.parseIsoDate("2019-06-14"));
        Assert.assertEquals("2019-06-18 00:00:00.000 +0800", sdf.format(actual));

        actual = DTU.next2BusinessDate(DTU.parseIsoDate("2019-06-15"));
        Assert.assertEquals("2019-06-18 00:00:00.000 +0800", sdf.format(actual));

        actual = DTU.next2BusinessDate(DTU.parseIsoDate("2019-06-13"));
        Assert.assertEquals("2019-06-17 00:00:00.000 +0800", sdf.format(actual));
    }

    @Test
    public void testIsBeforeVelotradeCutOffTime() {
        DateTime d = new DateTime(DTU.nowAtHK()).withTime(11, 58, 59, 0).withZoneRetainFields(
                DateTimeZone.forTimeZone(DTU.DEFAULT_TIMEZONE));
        Assert.assertTrue(DTU.isBeforeVelotradeCutOffTime(d.toDate()));

        d = new DateTime(DTU.nowAtHK()).withTime(12, 0, 0, 0).withZoneRetainFields(
                DateTimeZone.forTimeZone(DTU.DEFAULT_TIMEZONE));
        Assert.assertFalse(DTU.isBeforeVelotradeCutOffTime(d.toDate()));
    }

    @Test
    public void testGetAllHolidays() {
        DateTime from = new DateTime(DTU.nowAtHK()).withDate(2019,9,1).withTime(11, 58, 59, 0).withZoneRetainFields(
                DateTimeZone.forTimeZone(DTU.DEFAULT_TIMEZONE));

        DateTime to = new DateTime(DTU.nowAtHK()).withDate(2019,9,13).withTime(12, 0, 0, 0).withZoneRetainFields(
                DateTimeZone.forTimeZone(DTU.DEFAULT_TIMEZONE));
        Assert.assertEquals(1, DTU.getHolidays(from.toDate(), to.toDate(), "USD").size());

        // ---
        from = new DateTime(DTU.nowAtHK()).withDate(2019,10,14).withTime(11, 58, 59, 0).withZoneRetainFields(
                DateTimeZone.forTimeZone(DTU.DEFAULT_TIMEZONE));

        to = new DateTime(DTU.nowAtHK()).withDate(2019,12,25).withTime(12, 0, 0, 0).withZoneRetainFields(
                DateTimeZone.forTimeZone(DTU.DEFAULT_TIMEZONE));
        Assert.assertEquals(4, DTU.getHolidays(from.toDate(), to.toDate(), "USD").size());

        // ---
        from = new DateTime(DTU.nowAtHK()).withDate(2019,12,25).withTime(11, 58, 59, 0).withZoneRetainFields(
                    DateTimeZone.forTimeZone(DTU.DEFAULT_TIMEZONE));

        to = new DateTime(DTU.nowAtHK()).withDate(2019,12,25).withTime(12, 0, 0, 0).withZoneRetainFields(
                    DateTimeZone.forTimeZone(DTU.DEFAULT_TIMEZONE));
        Assert.assertEquals(1, DTU.getHolidays(from.toDate(), to.toDate(), "USD").size());


        // ---
        from = new DateTime(DTU.nowAtHK()).withDate(2019,12,24).withTime(11, 58, 59, 0).withZoneRetainFields(
                    DateTimeZone.forTimeZone(DTU.DEFAULT_TIMEZONE));

        to = new DateTime(DTU.nowAtHK()).withDate(2019,12,24).withTime(12, 0, 0, 0).withZoneRetainFields(
                    DateTimeZone.forTimeZone(DTU.DEFAULT_TIMEZONE));
        Assert.assertEquals(0, DTU.getHolidays(from.toDate(), to.toDate(), "USD").size());


        // ---
        from = new DateTime(DTU.nowAtHK()).withDate(2019,12,28).withTime(11, 58, 59, 0).withZoneRetainFields(
                    DateTimeZone.forTimeZone(DTU.DEFAULT_TIMEZONE));

        to = new DateTime(DTU.nowAtHK()).withDate(2019,12,29).withTime(12, 0, 0, 0).withZoneRetainFields(
                    DateTimeZone.forTimeZone(DTU.DEFAULT_TIMEZONE));
        Assert.assertEquals(0, DTU.getHolidays(from.toDate(), to.toDate(), "USD").size());
    }

    @Test
    public void testIsBeforeDBSCutOffTime() {
        DateTime d = new DateTime(DTU.nowAtHK()).withTime(17, 24, 59, 0).withZoneRetainFields(
                DateTimeZone.forTimeZone(DTU.DEFAULT_TIMEZONE));
        Assert.assertTrue(DTU.isBeforeDBSCutOffTime(d.toDate()));

        d = new DateTime(DTU.nowAtHK()).withTime(17, 25, 0, 0).withZoneRetainFields(
                DateTimeZone.forTimeZone(DTU.DEFAULT_TIMEZONE));
        Assert.assertFalse(DTU.isBeforeDBSCutOffTime(d.toDate()));
    }

    @Test
    public void testGetYearFromDate() {
        DateTime dateTime = new DateTime(DTU.nowAtHK())
                .withDate(2019, 12, 28)
                .withTime(11, 58, 59, 0)
                .withZoneRetainFields(
                        DateTimeZone.forTimeZone(DTU.DEFAULT_TIMEZONE));
        int year = DTU.getYearFromDate(dateTime.toDate());
        Assert.assertEquals(2019, year);
    }

    @Test
    public void testDayDiff() {
        DateTime dateTime = new DateTime(DTU.nowAtHK())
                .withDate(2019, 12, 28)
                .withZoneRetainFields(DateTimeZone.forTimeZone(DTU.DEFAULT_TIMEZONE));

        long dayDiff = DTU.getDayDifference(dateTime.toDate(), 0);
        Assert.assertEquals(dayDiff, dateTime.getMillis());
    }

    @Test
    public void testStartOfDate() {
        Date date = DTU.startOfDate(1);
        Calendar calendar = Calendar.getInstance(DTU.DEFAULT_TIMEZONE);
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.AM_PM, Calendar.AM);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DATE, 1);
        Assert.assertEquals(date.getDate(), calendar.getTime().getDate());
    }

    @Test
    public void testMidnightOfDate() {
        Date date = DTU.midnightOfDate(1);
        ZonedDateTime now = ZonedDateTime.now(DTU.DEFAULT_TIMEZONE.toZoneId())
                .plusDays(1);
        Date newDate = Date.from(now.toInstant());
        Assert.assertEquals(date.getDate(), newDate.getDate());
    }

    @Test
    public void testGetInputDateTime() {
        DateTime d = new DateTime(DTU.nowAtHK()).withTime(17, 24, 59, 0).withZoneRetainFields(
                    DateTimeZone.forTimeZone(DTU.DEFAULT_TIMEZONE));
        Date fiveMinutesAgo = DTU.minusMinutes(d.toDate(), 5);
        Assert.assertEquals(19, fiveMinutesAgo.getMinutes());

        d = new DateTime(DTU.nowAtHK()).withTime(0, 4, 59, 0).withZoneRetainFields(
                    DateTimeZone.forTimeZone(DTU.DEFAULT_TIMEZONE));
        fiveMinutesAgo = DTU.minusMinutes(d.toDate(), 5);
        Assert.assertEquals(59, fiveMinutesAgo.getMinutes());


        d = new DateTime(DTU.nowAtHK()).withTime(17, 24, 59, 0).withZoneRetainFields(
                    DateTimeZone.forTimeZone(DTU.DEFAULT_TIMEZONE));
        Date zeroMinutesAgo = DTU.minusMinutes(d.toDate(), 0);
        Assert.assertEquals(d.toDate(), zeroMinutesAgo);
    }

    @Test
    public void testFirstDayOfWeek() {
        Date firstDayOfWeek = DTU.firstDayOfWeek();
        Assert.assertEquals(0, firstDayOfWeek.getDay());
        Assert.assertEquals(0, firstDayOfWeek.getMinutes());
    }

    @Test
    public void testLastDayOfWeek() {
        Date lastDayOfWeek = DTU.lastDayOfWeek();
        Assert.assertEquals(59, lastDayOfWeek.getMinutes());
        Assert.assertEquals(59, lastDayOfWeek.getSeconds());
    }
}