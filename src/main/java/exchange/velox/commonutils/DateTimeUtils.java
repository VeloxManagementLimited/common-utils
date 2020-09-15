package exchange.velox.commonutils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static java.time.temporal.ChronoField.*;

public class DateTimeUtils {
    private static final LocalDate MIN = LocalDate.of(1970, 01, 01);
    private static final LocalDate MAX = LocalDate.of(2099, 12, 31);

    public static final TimeZone DEFAULT_TIMEZONE = TimeZone.getTimeZone("Asia/Hong_Kong");


    public static final SimpleDateFormat SIMPLE_DATE_FORMAT;
    public static final SimpleDateFormat CUSTOM_DATE_FORMAT;
    public static final SimpleDateFormat SIMPLE_TIME_FORMAT;
    public static final SimpleDateFormat SIMPLE_DATETIME_FORMAT;
    public static final DateTimeFormatter ISO_DATETIME_FORMAT;

    private static final List<String> HOLIDAYS_HK = Arrays.asList(
            "2019-06-07", "2019-07-01", "2019-09-14",
            "2019-10-01", "2019-10-07", "2019-12-25", "2019-12-26",
            "2020-01-01", "2020-01-25", "2020-01-26", "2020-01-27", "2020-01-28", "2020-04-04", "2020-04-10",
            "2020-04-11", "2020-04-13", "2020-04-30", "2020-05-01", "2020-06-25",
            "2020-07-01", "2020-10-01", "2020-10-02", "2020-10-25", "2020-10-26", "2020-12-25", "2020-12-26",
            "2021-01-01", "2021-02-12", "2021-02-13", "2021-02-15", "2021-04-02", "2021-04-03", "2021-04-05", "2021-04-06",
            "2021-05-01", "2021-05-19", "2021-05-01", "2021-06-14", "2021-07-01", "2021-09-22", "2021-10-01", "2021-10-14",
            "2021-12-25", "2021-12-27"
    );

    private static final List<String> HOLIDAYS_US = Arrays.asList(
                "2019-07-04", "2019-09-02", "2019-10-14","2019-11-11", "2019-11-28", "2019-12-25",
                "2020-01-01", "2020-01-20", "2020-02-17", "2020-05-25",
                "2020-07-03","2020-07-04", "2020-09-07", "2020-10-12","2020-11-11", "2020-11-26", "2020-12-25",
                "2021-01-01", "2021-01-18", "2021-05-31", "2021-07-05", "2021-09-06", "2021-11-11", "2021-11-25", "2021-12-24", "2021-12-31"
    );

    private static final List<String> HOLIDAYS_UK = Arrays.asList(
                "2019-08-26", "2019-12-25", "2019-12-26",
                "2020-01-01", "2020-04-10", "2020-04-13", "2020-05-08", "2020-05-25",
                "2020-08-31", "2020-12-25", "2020-12-26", "2020-12-28",
                "2021-01-01", "2021-04-02", "2021-04-05", "2021-05-03", "2021-05-31",
                "2021-08-30", "2021-12-27", "2021-12-28"
    );

    private static final List<String> HOLIDAYS_JP = Arrays.asList(
                "2019-07-15", "2019-08-12", "2019-09-16", "2019-09-23", "2019-10-14", "2019-10-22", "2019-11-04", "2019-11-23",
                "2020-01-01", "2020-01-13", "2020-02-11", "2020-02-24", "2020-03-20", "2020-04-29", "2020-05-03", "2020-05-04",
                "2020-05-05", "2020-05-06", "2020-07-23", "2020-07-24", "2020-08-10", "2020-09-21", "2020-09-22", "2020-11-03",
                "2020-11-23",
                "2021-01-01", "2021-01-11", "2021-02-11", "2021-02-23", "2021-03-20", "2021-04-29", "2021-05-03", "2021-05-04",
                "2021-05-05", "2021-07-22", "2021-07-23", "2021-08-08", "2021-08-09", "2021-09-20", "2021-09-23", "2021-11-03",
                "2021-11-23"
    );

    private static final List<String> HOLIDAYS_EUROPEAN_CENTRAL_BANK = Arrays.asList(
                "2019-10-03", "2019-11-01", "2019-12-24", "2019-12-25", "2019-12-26", "2019-12-31",
                "2020-01-01", "2020-04-10", "2020-04-13", "2020-05-01", "2020-05-09", "2020-05-21", "2020-06-01",
                "2020-06-11", "2020-10-03", "2020-11-01", "2020-12-24", "2020-12-25", "2020-12-26", "2020-12-31",
                "2021-01-01", "2021-04-02", "2021-04-05", "2021-05-01", "2021-05-09", "2021-05-13", "2021-05-24",
                "2021-06-03", "2021-10-03", "2021-11-01", "2021-12-24", "2021-12-25", "2021-12-26", "2021-12-31",
                "2022-01-01", "2022-04-15", "2022-04-18", "2022-05-01", "2022-05-09", "2022-05-26", "2022-06-06",
                "2022-06-16", "2022-10-03", "2022-11-01", "2022-12-24", "2022-12-25", "2022-12-26", "2022-12-31"
    );

    private static final List<String> HOLIDAYS_CN = Arrays.asList(
                "2019-09-13", "2019-10-01", "2019-10-02", "2019-10-03", "2019-10-04", "2019-10-05", "2019-10-06", "2019-10-07",
                "2020-01-01", "2020-01-24", "2020-01-25", "2020-01-26", "2020-01-27", "2020-01-28", "2020-01-29", "2020-01-30",
                "2020-04-06", "2020-05-01", "2020-06-25", "2020-06-26", "2020-10-01", "2020-10-02", "2020-10-03", "2020-10-04",
                "2020-10-05", "2020-10-06", "2020-10-07",
                "2021-02-01", "2021-02-11", "2021-02-12", "2021-02-13", "2021-02-14", "2021-02-15", "2021-02-16", "2021-02-17",
                "2021-05-01", "2021-05-02", "2021-05-03", "2021-06-14", "2021-06-15", "2021-06-16", "2021-09-21", "2021-10-01",
                "2021-10-02", "2021-10-03", "2021-10-04", "2021-10-05", "2021-10-06", "2021-10-07"
    );

    private static final Map<String, Set<String>> HOLIDAYS_BY_CURRENCY;
    static {
        HOLIDAYS_BY_CURRENCY = new HashMap<>();
        {
            Set<String> USD = new HashSet<>();
            USD.addAll(HOLIDAYS_HK);
            USD.addAll(HOLIDAYS_US);
            HOLIDAYS_BY_CURRENCY.put("USD", USD);
        }
        {
            Set<String> HKD = new HashSet<>();
            HKD.addAll(HOLIDAYS_HK);
            HOLIDAYS_BY_CURRENCY.put("HKD", HKD);
        }
        {
            Set<String> GBP = new HashSet<>();
            GBP.addAll(HOLIDAYS_HK);
            GBP.addAll(HOLIDAYS_UK);
            HOLIDAYS_BY_CURRENCY.put("GBP", GBP);
        }
        {
            Set<String> EUR = new HashSet<>();
            EUR.addAll(HOLIDAYS_HK);
            EUR.addAll(HOLIDAYS_EUROPEAN_CENTRAL_BANK);
            HOLIDAYS_BY_CURRENCY.put("EUR", EUR);
        }
        {
            Set<String> CNY = new HashSet<>();
            CNY.addAll(HOLIDAYS_HK);
            CNY.addAll(HOLIDAYS_CN);
            HOLIDAYS_BY_CURRENCY.put("CNY", CNY);
        }
        {
            Set<String> JPY = new HashSet<>();
            JPY.addAll(HOLIDAYS_HK);
            JPY.addAll(HOLIDAYS_JP);
            HOLIDAYS_BY_CURRENCY.put("JPY", JPY);
        }
    }

    private static boolean isDateInList(Collection<String> l, Date date) {
        if (CollectionUtils.isEmpty(l)) {
            return false;
        }
        Calendar calendar = Calendar.getInstance(DEFAULT_TIMEZONE);
        calendar.setTime(date);
        String s = dateToString(calendar);
        return l.contains(s);

    }

    public static boolean isHongKongHoliday(Date date) {
        return isDateInList(HOLIDAYS_HK, date);
    }

    public static boolean isUSHoliday(Date date) {
        return isDateInList(HOLIDAYS_US, date);
    }

    private static String dateToString(Calendar calendar) {
        int month = calendar.get(Calendar.MONTH) + 1;
        String m = month < 10 ? ("0" + month) : month + "";
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String d = day < 10 ? "0" + day : day + "";
        return calendar.get(Calendar.YEAR) + "-" + m + "-" + d;
    }

    static {
        SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
        SIMPLE_DATE_FORMAT.setTimeZone(DEFAULT_TIMEZONE);

        CUSTOM_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
        CUSTOM_DATE_FORMAT.setTimeZone(DEFAULT_TIMEZONE);

        SIMPLE_TIME_FORMAT = new SimpleDateFormat("HH:mm");
        SIMPLE_TIME_FORMAT.setTimeZone(DEFAULT_TIMEZONE);

        SIMPLE_DATETIME_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        SIMPLE_DATETIME_FORMAT.setTimeZone(DEFAULT_TIMEZONE);

        ISO_DATETIME_FORMAT = new DateTimeFormatterBuilder()
                    .appendValue(YEAR, 4, 10, SignStyle.EXCEEDS_PAD)

                    .appendLiteral('-')
                    .appendValue(MONTH_OF_YEAR, 1, 2, SignStyle.NOT_NEGATIVE)

                    .appendLiteral('-')
                    .appendValue(DAY_OF_MONTH, 1, 2, SignStyle.NOT_NEGATIVE)

                    .appendLiteral(' ')
                    .appendValue(HOUR_OF_DAY, 1, 2, SignStyle.NOT_NEGATIVE)

                    .appendLiteral(':')
                    .appendValue(MINUTE_OF_HOUR, 1, 2, SignStyle.NOT_NEGATIVE)

                    .appendLiteral(':')
                    .appendValue(SECOND_OF_MINUTE, 1, 2, SignStyle.NOT_NEGATIVE)

                    .appendLiteral('.')
                    .appendValue(MILLI_OF_SECOND, 1, 3, SignStyle.NOT_NEGATIVE)

                    .toFormatter();
        ISO_DATETIME_FORMAT.withZone(DEFAULT_TIMEZONE.toZoneId());
    }

    public static final DateTimeFormatter FLEXIBLE_ISO_LOCAL_DATE;

    static {
        FLEXIBLE_ISO_LOCAL_DATE = new DateTimeFormatterBuilder()
                .appendValue(YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
                .appendLiteral('-')
                .appendValue(MONTH_OF_YEAR, 1, 2, SignStyle.NOT_NEGATIVE)
                .appendLiteral('-')
                .appendValue(DAY_OF_MONTH, 1, 2, SignStyle.NOT_NEGATIVE)
                .toFormatter();
    }


    public static Date infinitePast() {
        return Date.from(MIN.atStartOfDay(ZoneId.of("UTC")).toInstant());
    }

    public static Date infiniteFuture() {
        return Date.from(MAX.atStartOfDay(ZoneId.of("UTC")).toInstant());
    }

    public static String formatDate(Date date, String pattern, Locale locale, TimeZone timeZone) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, locale);
        sdf.setTimeZone(timeZone);
        return sdf.format(date);
    }

    public static String printCustomDate(Date d) {
        if (d == null) {
            return StringUtils.EMPTY;
        }
        return CUSTOM_DATE_FORMAT.format(d);
    }

    public static String printDate(Date d) {
        if (d == null) {
            return StringUtils.EMPTY;
        }
        return SIMPLE_DATE_FORMAT.format(d);
    }

    public static String printTime(Date d) {
        if (d == null) {
            return StringUtils.EMPTY;
        }
        return SIMPLE_TIME_FORMAT.format(d);
    }

    public static String printDateTime(Date d) {
        if (d == null) {
            return StringUtils.EMPTY;
        }
        return SIMPLE_DATETIME_FORMAT.format(d);
    }

    public static Date plusOneDay(Date d) {
        if (d == null) {
            return null;
        }
        DateTime dtOrg = new DateTime(d);
        return dtOrg.plusDays(1).toDate();
    }

    public static Date plusMonths(Date d, int months) {
        if (d == null) {
            return null;
        }
        DateTime dtOrg = new DateTime(d);
        return dtOrg.plusMonths(months).toDate();
    }

    public static Date minusDays(Date d, int days) {
        if (d == null) {
            return null;
        }
        DateTime dtOrg = new DateTime(d);
        return dtOrg.minusDays(days).toDate();
    }

    public static Date minusMinutes(Date d, int minutes) {
        if (d == null) {
            return null;
        }
        DateTime dt = new DateTime(d);
        return dt.minusMinutes(minutes).toDate();
    }

    public static Date plusDays(Date d, int days) {
        if (d == null) {
            return null;
        }
        DateTime dtOrg = new DateTime(d);
        return dtOrg.plusDays(days).toDate();
    }

    public static Date plusHours(Date d, int hours) {
        if (d == null) {
            return null;
        }
        DateTime dtOrg = new DateTime(d);
        return dtOrg.plusHours(hours).toDate();
    }

    public static Date parseIsoDateOrInfinitePast(String s) {
        if (StringUtils.isNotBlank(s)) {
            return parseIsoDate(s);
        } else {
            return infinitePast();
        }
    }

    public static Date parseIsoDateOrInfiniteFuture(String s) {
        if (StringUtils.isNotBlank(s)) {
            return parseIsoDate(s);
        } else {
            return infiniteFuture();
        }
    }

    public static Date parseIsoDate(String s) {
        LocalDate date = LocalDate.parse(s, FLEXIBLE_ISO_LOCAL_DATE);
        ZonedDateTime zonedDateTime = date.atStartOfDay(DEFAULT_TIMEZONE.toZoneId());
        return Date.from(zonedDateTime.toInstant());
    }

    public static Date parseIsoDateTime(String s) {
        LocalDate date = LocalDate.parse(s, ISO_DATETIME_FORMAT);
        ZonedDateTime zonedDateTime = date.atStartOfDay(DEFAULT_TIMEZONE.toZoneId());
        return Date.from(zonedDateTime.toInstant());
    }

    public static long minutesDiff(Date inputFrom, Date inputTo) {
        if (inputFrom == null || inputTo == null) {
            return 0;
        }
        long minutes = ChronoUnit.MINUTES.between(inputFrom.toInstant(), inputTo.toInstant());
        return Math.abs(minutes);
    }

    public static Date nowAtHK() {
        return Date.from(ZonedDateTime.now(DEFAULT_TIMEZONE.toZoneId()).toInstant());
    }

    public static Date switchToHKTWithoutChangingValue(Date d) {
        if (d == null) {
            return null;
        }
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(d.toInstant(), DEFAULT_TIMEZONE.toZoneId());
        return Date.from(zonedDateTime.toInstant());
    }

    /**
     *
     * @param inputFrom
     * @param inputTo
     * @return inputTo - inputFrom
     */
    public static long daysDiff(Date inputFrom, Date inputTo) {
        if (inputFrom == null || inputTo == null) {
            return 0;
        }

        LocalDate localDateFrom = inputFrom.toInstant().atZone(DEFAULT_TIMEZONE.toZoneId()).toLocalDate();
        LocalDate localDateTo = inputTo.toInstant().atZone(DEFAULT_TIMEZONE.toZoneId()).toLocalDate();
        return ChronoUnit.DAYS.between(localDateFrom, localDateTo);
    }

    public static boolean dateBefore(Date a, Date b) {
        return daysDiff(a, b) > 0;
    }

    public static boolean dateAfter(Date a, Date b) {
        return daysDiff(a, b) < 0;
    }

    public static boolean dateSame(Date a, Date b) {
        return daysDiff(a, b) == 0;
    }

    public static long daysDiffAbs(Date inputFrom, Date inputTo) {
        return Math.abs(daysDiff(inputFrom, inputTo));
    }

    public static Date firstDayOfMonth() {
        ZonedDateTime now = ZonedDateTime.now(DEFAULT_TIMEZONE.toZoneId());
        ZonedDateTime firstDay = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        return Date.from(firstDay.toInstant());
    }

    public static Date startOfToday() {
        ZonedDateTime now = ZonedDateTime.now(DEFAULT_TIMEZONE.toZoneId());
        ZonedDateTime firstDay = now.withHour(0).withMinute(0).withSecond(0).withNano(0);
        return Date.from(firstDay.toInstant());
    }


    public static Date startOfDate(Date date) {
        Calendar calendar = Calendar.getInstance(DateTimeUtils.DEFAULT_TIMEZONE);
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.AM_PM, Calendar.AM);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date startOfDate(int plusDays) {
        Calendar calendar = Calendar.getInstance(DateTimeUtils.DEFAULT_TIMEZONE);
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.AM_PM, Calendar.AM);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DATE, plusDays);
        return calendar.getTime();
    }

    public static Date midnightOfToday() {
        ZonedDateTime now = ZonedDateTime.now(DEFAULT_TIMEZONE.toZoneId());
        ZonedDateTime firstDay = now.withHour(23).withMinute(59).withSecond(59).withNano(999_999_999);
        return Date.from(firstDay.toInstant());
    }

    public static Date midnightOfDate(int plusDays) {
        ZonedDateTime now = ZonedDateTime.now(DEFAULT_TIMEZONE.toZoneId());
        ZonedDateTime midnightDate = now.plusDays(plusDays)
                .withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(999_999_999);
        return Date.from(midnightDate.toInstant());
    }

    public static boolean isWeekend(Date date) {
        Calendar calendar = Calendar.getInstance(DEFAULT_TIMEZONE);
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ||calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
    }

    public static int getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance(DEFAULT_TIMEZONE);
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static Date parseDate(String date, String pattern, Locale locale) {
        try {
            DateFormat df = new SimpleDateFormat(pattern, locale);
            df.setTimeZone(DEFAULT_TIMEZONE);
            return df.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date firstDayOfYear() {
        ZonedDateTime now = ZonedDateTime.now(DEFAULT_TIMEZONE.toZoneId());
        ZonedDateTime firstDay = now.withMonth(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        return Date.from(firstDay.toInstant());
    }

    public static boolean isTodayInHK(Date d) {
        return DateTimeUtils.daysDiff(switchToHKTWithoutChangingValue(d), nowAtHK()) == 0;
    }

    public static boolean isBusinessDate(Date d) {
        return isBusinessDate(d, "USD");
    }

    public static boolean isBusinessDate(Date d, String currency) {
        if (isWeekend(d)) {
            return false;
        }
        return !isDateInList(getHolidaysByCurrency(currency), d);
    }

    public static List<Date> getHolidays(Date fromDate, Date toDate, String currency) {
        List<Date> res = new ArrayList<>();
        Set<String> holidays = getHolidaysByCurrency(currency);
        while (daysDiff(fromDate, toDate) >= 0) {
            Calendar calendar = Calendar.getInstance(DEFAULT_TIMEZONE);
            calendar.setTime(fromDate);
            if (holidays.contains(dateToString(calendar))) {
                res.add(fromDate);
            }
            fromDate = plusOneDay(fromDate);
        }
        return res;
    }

    private static Set<String> getHolidaysByCurrency(String currency) {
        Set<String> result = HOLIDAYS_BY_CURRENCY.get(currency);
        if (CollectionUtils.isEmpty(result)) {
            return new HashSet<>(HOLIDAYS_HK);
        }
        return result;
    }

    public static Date toNextBusinessDateIfNot(Date d) {
        return toNextBusinessDateIfNot(d, "USD");
    }

    public static Date toNextBusinessDateIfNot(Date d, String currency) {
        if (isBusinessDate(d, currency)) {
            return d;
        }
        return nextBusinessDate(d, currency);
    }

    public static Date nextBusinessDate(Date d) {
        return nextBusinessDate(d, "USD");
    }

    public static Date nextBusinessDate(Date d, String currency) {
        Date result = plusOneDay(startOfDate(d));
        while (!isBusinessDate(result, currency)) {
            result = plusOneDay(result);
        }
        return result;
    }

    public static Date lastBusinessDate(Date d) {
        return lastBusinessDate(d, "HKD");
    }

    public static Date last2BusinessDate(Date d) {
        return lastBusinessDate(lastBusinessDate(d));
    }

    public static Date last2BusinessDate(Date d, String currency) {
        return lastBusinessDate(lastBusinessDate(d, currency), currency);
    }

    public static Date lastBusinessDate(Date d, String currency) {
        Date result = minusDays(startOfDate(d), 1);
        while (!isBusinessDate(result, currency)) {
            result = minusDays(result, 1);
        }
        return result;
    }

    public static Date next2BusinessDate(Date d) {
        return next2BusinessDate(d, "USD");
    }

    public static Date next2BusinessDate(Date d, String currency) {
        return nextBusinessDate(nextBusinessDate(d, currency), currency);
    }

    public static boolean isBeforeVelotradeCutOffTime(Date d) {
        Date now = DateTimeUtils.nowAtHK();
        Calendar calendar = Calendar.getInstance(DateTimeUtils.DEFAULT_TIMEZONE);
        calendar.setTime(now);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return d.getTime() < calendar.getTime().getTime();
    }

    public static boolean isBeforeDBSCutOffTime(Date d) {
        Date now = DateTimeUtils.nowAtHK();
        Calendar calendar = Calendar.getInstance(DateTimeUtils.DEFAULT_TIMEZONE);
        calendar.setTime(now);
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE, 25);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return d.getTime() < calendar.getTime().getTime();
    }

    public static boolean isBeforeCitiCutOffTime(Date d) {
        Date now = DateTimeUtils.nowAtHK();
        Calendar calendar = Calendar.getInstance(DateTimeUtils.DEFAULT_TIMEZONE);
        calendar.setTime(now);
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE, 25);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return d.getTime() < calendar.getTime().getTime();
    }

    public static int getYearFromDate(Date d) {
        Calendar calendar = Calendar.getInstance(DateTimeUtils.DEFAULT_TIMEZONE);
        calendar.setTime(d);
        return calendar.get(Calendar.YEAR);
    }

    public static Long getDayDifference(Date fromDate, int diff) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(fromDate);
        cal.add(Calendar.DAY_OF_MONTH, diff);
        return cal.getTime().getTime();
    }

    public static Date firstDayOfWeek() {
        DateTime dateTime = new DateTime();
        return dateTime.withZone(DateTimeZone.forTimeZone(DEFAULT_TIMEZONE))
                .withDayOfWeek(1)
                .withHourOfDay(0)
                .withMinuteOfHour(0)
                .withSecondOfMinute(0)
                .withMillisOfSecond(0)
                .toDate();
    }

    public static Date lastDayOfWeek() {
        return new DateTime().withZone(DateTimeZone.forTimeZone(DEFAULT_TIMEZONE))
                .withDayOfWeek(7)
                .withHourOfDay(23)
                .withMinuteOfHour(59)
                .withSecondOfMinute(59)
                .withMillisOfSecond(59)
                .toDate();
    }
}
