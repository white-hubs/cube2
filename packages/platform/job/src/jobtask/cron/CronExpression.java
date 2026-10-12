package com.guhecloud.platform.job.jobtask.cron;

import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.job.jobtask.cron.dto.CronModelDTO;
import com.guhecloud.platform.job.jobtask.enums.JobLoopTypeEnum;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serial;
import java.io.Serializable;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.stream.Collectors;

public final class CronExpression implements Serializable {

    private static final long serialVersionUID = 12423409423L;

    protected static final int SECOND = 0;
    protected static final int MINUTE = 1;
    protected static final int HOUR = 2;
    protected static final int DAY_OF_MONTH = 3;
    protected static final int MONTH = 4;
    protected static final int DAY_OF_WEEK = 5;
    protected static final int YEAR = 6;
    protected static final int ALL_SPEC_INT = 99; // '*'
    protected static final int NO_SPEC_INT = 98; // '?'
    protected static final int MAX_LAST_DAY_OFFSET = 30;
    protected static final int LAST_DAY_OFFSET_START = 32; // "L-30"
    protected static final int LAST_DAY_OFFSET_END = LAST_DAY_OFFSET_START + MAX_LAST_DAY_OFFSET; // 'L'
    protected static final Integer ALL_SPEC = ALL_SPEC_INT;
    protected static final Integer NO_SPEC = NO_SPEC_INT;

    protected static final Map<String, Integer> monthMap = new HashMap<>(20);
    protected static final Map<String, Integer> dayMap = new HashMap<>(60);

    static {
        monthMap.put("JAN", 0);
        monthMap.put("FEB", 1);
        monthMap.put("MAR", 2);
        monthMap.put("APR", 3);
        monthMap.put("MAY", 4);
        monthMap.put("JUN", 5);
        monthMap.put("JUL", 6);
        monthMap.put("AUG", 7);
        monthMap.put("SEP", 8);
        monthMap.put("OCT", 9);
        monthMap.put("NOV", 10);
        monthMap.put("DEC", 11);

        dayMap.put("SUN", 1);
        dayMap.put("MON", 2);
        dayMap.put("TUE", 3);
        dayMap.put("WED", 4);
        dayMap.put("THU", 5);
        dayMap.put("FRI", 6);
        dayMap.put("SAT", 7);
    }

    private final String cronExpression;
    private TimeZone timeZone = null;
    protected transient TreeSet<Integer> seconds;
    protected transient TreeSet<Integer> minutes;
    protected transient TreeSet<Integer> hours;
    protected transient TreeSet<Integer> daysOfMonth;
    protected transient TreeSet<Integer> nearestWeekdays;
    protected transient TreeSet<Integer> months;
    protected transient TreeSet<Integer> daysOfWeek;
    protected transient TreeSet<Integer> years;

    protected transient boolean lastDayOfWeek = false;
    protected transient int nthDayOfWeek = 0;
    protected transient boolean expressionParsed = false;

    public static final int MAX_YEAR = Calendar.getInstance().get(Calendar.YEAR) + 100;

    /**
     * Constructs a new <CODE>CronExpression</CODE> based on the specified
     * parameter.
     *
     * @param cronExpression String representation of the cron expression the
     *                       new object should represent
     *
     * @throws java.text.ParseException if the string expression cannot be parsed into a valid
     *                                  <CODE>CronExpression</CODE>
     */
    public CronExpression(String cronExpression) throws ParseException {
        if (cronExpression == null) {
            throw new IllegalArgumentException("cronExpression cannot be null");
        }

        this.cronExpression = cronExpression.toUpperCase(Locale.US);

        buildExpression(this.cronExpression);
    }

    /**
     * Constructs a new {@code CronExpression} as a copy of an existing
     * instance.
     *
     * @param expression The existing cron expression to be copied
     */
    public CronExpression(CronExpression expression) {
        /*
         * We don't call the other constructor here since we need to swallow the
         * ParseException. We also elide some of the sanity checking as it is
         * not logically trippable.
         */
        this.cronExpression = expression.getCronExpression();
        try {
            buildExpression(cronExpression);
        } catch (ParseException ex) {
            throw new AssertionError("Could not parse expression!", ex);
        }
        if (expression.getTimeZone() != null) {
            setTimeZone((TimeZone) expression.getTimeZone().clone());
        }
    }

    public boolean isSatisfiedBy(LocalDateTime date) {
        ZonedDateTime zdt = date.atZone(getTimeZone().toZoneId())
                .truncatedTo(ChronoUnit.MILLIS);
        LocalDateTime originalDate = zdt.toLocalDateTime();

        zdt = zdt.withSecond(-1);

        LocalDateTime timeAfter = getTimeAfter(zdt.toLocalDateTime());

        return ((timeAfter != null) && (timeAfter.equals(originalDate)));
    }

    /**
     * Returns the next date/time <I>after</I> the given date/time which
     * satisfies the cron expression.
     *
     * @param date the date/time at which to begin the search for the next valid
     *             date/time
     *
     * @return the next valid date/time
     */
    public LocalDateTime getNextValidTimeAfter(LocalDateTime date) {
        return getTimeAfter(date);
    }

    /**
     * Returns the next date/time <I>after</I> the given date/time which does
     * <I>not</I> satisfy the expression
     *
     * @param date the date/time at which to begin the search for the next
     *             invalid date/time
     *
     * @return the next valid date/time
     */
    public LocalDateTime getNextInvalidTimeAfter(LocalDateTime date) {
        long difference = 1000;

        //move back to the nearest second so differences will be accurate
        ZonedDateTime adjustCal = date.atZone(getTimeZone().toZoneId())
                .truncatedTo(ChronoUnit.MILLIS);
        LocalDateTime lastDate = adjustCal.toLocalDateTime();

        LocalDateTime newDate;

        //FUTURE_TODO: (QUARTZ-481) IMPROVE THIS! The following is a BAD solution to this problem. Performance will be very bad here, depending on the cron expression. It is, however A solution.

        //keep getting the next included time until it's farther than one second
        // apart. At that point, lastDate is the last valid fire time. We return
        // the second immediately following it.
        while (difference == 1000) {
            newDate = getTimeAfter(lastDate);
            if (newDate == null) {
                break;
            }

            difference = DateUtils.toTimestamp(newDate) - DateUtils.toTimestamp(lastDate);

            if (difference == 1000) {
                lastDate = newDate;
            }
        }

        return lastDate.plusSeconds(1);
    }

    /**
     * Returns the time zone for which this <code>CronExpression</code>
     * will be resolved.
     */
    public TimeZone getTimeZone() {
        if (timeZone == null) {
            timeZone = TimeZone.getDefault();
        }

        return timeZone;
    }

    /**
     * Sets the time zone for which  this <code>CronExpression</code>
     * will be resolved.
     */
    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    @Override
    public String toString() {
        return cronExpression;
    }

    /**
     * Indicates whether the specified cron expression can be parsed into a
     * valid cron expression
     *
     * @param cronExpression the expression to evaluate
     *
     * @return a boolean indicating whether the given expression is a valid cron
     * expression
     */
    public static boolean isValidExpression(String cronExpression) {

        try {
            new CronExpression(cronExpression);
        } catch (ParseException pe) {
            return false;
        }

        return true;
    }

    public static void validateExpression(String cronExpression) throws ParseException {

        new CronExpression(cronExpression);
    }

    ////////////////////////////////////////////////////////////////////////////
    //
    // Expression Parsing Functions
    //

    /// /////////////////////////////////////////////////////////////////////////

    protected void buildExpression(String expression) throws ParseException {
        expressionParsed = true;

        try {

            if (seconds == null) {
                seconds = new TreeSet<>();
            }
            if (minutes == null) {
                minutes = new TreeSet<>();
            }
            if (hours == null) {
                hours = new TreeSet<>();
            }
            if (daysOfMonth == null) {
                daysOfMonth = new TreeSet<>();
            }
            if (nearestWeekdays == null) {
                nearestWeekdays = new TreeSet<>();
            }
            if (months == null) {
                months = new TreeSet<>();
            }
            if (daysOfWeek == null) {
                daysOfWeek = new TreeSet<>();
            }
            if (years == null) {
                years = new TreeSet<>();
            }

            int exprOn = SECOND;

            StringTokenizer exprsTok = new StringTokenizer(expression, " \t",
                                                           false);

            if (exprsTok.countTokens() > 7) {
                throw new ParseException("Invalid expression has too many terms: " + expression, -1);
            }

            while (exprsTok.hasMoreTokens() && exprOn <= YEAR) {
                String expr = exprsTok.nextToken().trim();

                // throw an exception if L is used with other days of the week
                if (exprOn == DAY_OF_WEEK && expr.indexOf('L') != -1 && expr.length() > 1 && expr.contains(",")) {
                    throw new ParseException("Support for specifying 'L' with other days of the week is not implemented", -1);
                }
                if (exprOn == DAY_OF_WEEK && expr.indexOf('#') != -1 && expr.indexOf('#', expr.indexOf('#') + 1) != -1) {
                    throw new ParseException("Support for specifying multiple \"nth\" days is not implemented.", -1);
                }

                StringTokenizer vTok = new StringTokenizer(expr, ",");
                while (vTok.hasMoreTokens()) {
                    String v = vTok.nextToken();
                    storeExpressionVals(0, v, exprOn);
                }

                exprOn++;
            }

            if (exprOn <= DAY_OF_WEEK) {
                throw new ParseException("Unexpected end of expression.",
                                         expression.length());
            }

            if (exprOn <= YEAR) {
                storeExpressionVals(0, "*", YEAR);
            }

            TreeSet<Integer> dow = getSet(DAY_OF_WEEK);
            TreeSet<Integer> dom = getSet(DAY_OF_MONTH);

            // Copying the logic from the UnsupportedOperationException below
            boolean dayOfMSpec = !dom.contains(NO_SPEC);
            boolean dayOfWSpec = !dow.contains(NO_SPEC);

            if (!dayOfMSpec || dayOfWSpec) {
                if (!dayOfWSpec || dayOfMSpec) {
                    throw new ParseException(
                            "Support for specifying both a day-of-week AND a day-of-month parameter is not implemented.", 0);
                }
            }
        } catch (ParseException pe) {
            throw pe;
        } catch (Exception e) {
            throw new ParseException("Illegal cron expression format ("
                                     + e + ")", 0);
        }
    }

    protected int storeExpressionVals(int pos, String s, int type)
            throws ParseException {

        int incr = 0;
        int i = skipWhiteSpace(pos, s);
        if (i >= s.length()) {
            return i;
        }
        char c = s.charAt(i);
        if ((c >= 'A') && (c <= 'Z') && (!s.equals("L")) && (!s.equals("LW")) && (!s.matches("^L-[0-9]*[W]?"))) {
            String sub = s.substring(i, i + 3);
            int sval = -1;
            int eval = -1;
            if (type == MONTH) {
                sval = getMonthNumber(sub) + 1;
                if (sval <= 0) {
                    throw new ParseException("Invalid Month value: '" + sub + "'", i);
                }
                if (s.length() > i + 3) {
                    c = s.charAt(i + 3);
                    if (c == '-') {
                        i += 4;
                        sub = s.substring(i, i + 3);
                        eval = getMonthNumber(sub) + 1;
                        if (eval <= 0) {
                            throw new ParseException("Invalid Month value: '" + sub + "'", i);
                        }
                    }
                }
            } else if (type == DAY_OF_WEEK) {
                sval = getDayOfWeekNumber(sub);
                if (sval < 0) {
                    throw new ParseException("Invalid Day-of-Week value: '"
                                             + sub + "'", i);
                }
                if (s.length() > i + 3) {
                    c = s.charAt(i + 3);
                    if (c == '-') {
                        i += 4;
                        sub = s.substring(i, i + 3);
                        eval = getDayOfWeekNumber(sub);
                        if (eval < 0) {
                            throw new ParseException(
                                    "Invalid Day-of-Week value: '" + sub
                                    + "'", i);
                        }
                    } else if (c == '#') {
                        try {
                            i += 4;
                            nthDayOfWeek = Integer.parseInt(s.substring(i));
                            if (nthDayOfWeek < 1 || nthDayOfWeek > 5) {
                                throw new Exception();
                            }
                        } catch (Exception e) {
                            throw new ParseException(
                                    "A numeric value between 1 and 5 must follow the '#' option",
                                    i);
                        }
                    } else if (c == 'L') {
                        lastDayOfWeek = true;
                        i++;
                    }
                }

            } else {
                throw new ParseException(
                        "Illegal characters for this position: '" + sub + "'",
                        i);
            }
            if (eval != -1) {
                incr = 1;
            }
            addToSet(sval, eval, incr, type);
            return (i + 3);
        }

        if (c == '?') {
            i++;
            if ((i + 1) < s.length()
                && (s.charAt(i) != ' ' && s.charAt(i + 1) != '\t')) {
                throw new ParseException("Illegal character after '?': "
                                         + s.charAt(i), i);
            }
            if (type != DAY_OF_WEEK && type != DAY_OF_MONTH) {
                throw new ParseException(
                        "'?' can only be specified for Day-of-Month or Day-of-Week.",
                        i);
            }
            if (type == DAY_OF_WEEK) {
                if (!daysOfMonth.isEmpty() && daysOfMonth.last() == NO_SPEC_INT) {
                    throw new ParseException(
                            "'?' can only be specified for Day-of-Month -OR- Day-of-Week.",
                            i);
                }
            }

            addToSet(NO_SPEC_INT, -1, 0, type);
            return i;
        }

        if (c == '*' || c == '/') {
            if (c == '*' && (i + 1) >= s.length()) {
                addToSet(ALL_SPEC_INT, -1, incr, type);
                return i + 1;
            } else if (c == '/'
                       && ((i + 1) >= s.length() || s.charAt(i + 1) == ' ' || s
                                                                                      .charAt(i + 1) == '\t')) {
                throw new ParseException("'/' must be followed by an integer.", i);
            } else if (c == '*') {
                i++;
            }
            c = s.charAt(i);
            if (c == '/') { // is an increment specified?
                i++;
                if (i >= s.length()) {
                    throw new ParseException("Unexpected end of string.", i);
                }

                incr = getNumericValue(s, i);

                i++;
                if (incr > 10) {
                    i++;
                }
                checkIncrementRange(incr, type, i);
            } else {
                incr = 1;
            }

            addToSet(ALL_SPEC_INT, -1, incr, type);
            return i;
        } else if (c == 'L') {
            i++;
            if (type == DAY_OF_WEEK) {
                addToSet(7, 7, 0, type);
            }
            if (type == DAY_OF_MONTH) {
                int dom = LAST_DAY_OFFSET_END;
                boolean nearestWeekday = false;
                if (s.length() > i) {
                    c = s.charAt(i);
                    if (c == '-') {
                        ValueSet vs = getValue(0, s, i + 1);
                        int offset = vs.value;
                        if (offset > MAX_LAST_DAY_OFFSET) {
                            throw new ParseException("Offset from last day must be <= " + MAX_LAST_DAY_OFFSET, i + 1);
                        }
                        dom -= offset;
                        i = vs.pos;
                    }
                    if (s.length() > i) {
                        c = s.charAt(i);
                        if (c == 'W') {
                            nearestWeekday = true;
                            i++;
                        }
                    }
                }
                if (nearestWeekday) {
                    nearestWeekdays.add(dom);
                } else {
                    daysOfMonth.add(dom);
                }
            }
            return i;
        } else if (c >= '0' && c <= '9') {
            int val = Integer.parseInt(String.valueOf(c));
            i++;
            if (i >= s.length()) {
                addToSet(val, -1, -1, type);
            } else {
                c = s.charAt(i);
                if (c >= '0' && c <= '9') {
                    ValueSet vs = getValue(val, s, i);
                    val = vs.value;
                    i = vs.pos;
                }
                i = checkNext(i, s, val, type);
                return i;
            }
        } else {
            throw new ParseException("Unexpected character: " + c, i);
        }

        return i;
    }

    private void checkIncrementRange(int incr, int type, int idxPos) throws ParseException {
        if (incr > 59 && (type == SECOND || type == MINUTE)) {
            throw new ParseException("Increment > 60 : " + incr, idxPos);
        } else if (incr > 23 && (type == HOUR)) {
            throw new ParseException("Increment > 24 : " + incr, idxPos);
        } else if (incr > 31 && (type == DAY_OF_MONTH)) {
            throw new ParseException("Increment > 31 : " + incr, idxPos);
        } else if (incr > 7 && (type == DAY_OF_WEEK)) {
            throw new ParseException("Increment > 7 : " + incr, idxPos);
        } else if (incr > 12 && (type == MONTH)) {
            throw new ParseException("Increment > 12 : " + incr, idxPos);
        }
    }

    protected int checkNext(int pos, String s, int val, int type)
            throws ParseException {

        int end = -1;
        int i = pos;

        if (i >= s.length()) {
            addToSet(val, end, -1, type);
            return i;
        }

        char c = s.charAt(pos);

        if (c == 'L') {
            if (type == DAY_OF_WEEK) {
                if (val < 1 || val > 7) {
                    throw new ParseException("Day-of-Week values must be between 1 and 7", -1);
                }
                lastDayOfWeek = true;
            } else {
                throw new ParseException("'L' option is not valid here. (pos=" + i + ")", i);
            }
            TreeSet<Integer> set = getSet(type);
            set.add(val);
            i++;
            return i;
        }

        if (c == 'W') {
            if (type != DAY_OF_MONTH) {
                throw new ParseException("'W' option is not valid here. (pos=" + i + ")", i);
            }
            if (val > 31) {
                throw new ParseException("The 'W' option does not make sense with values larger than 31 (max number of days in a month)", i);
            }
            nearestWeekdays.add(val);
            i++;
            return i;
        }

        if (c == '#') {
            if (type != DAY_OF_WEEK) {
                throw new ParseException("'#' option is not valid here. (pos=" + i + ")", i);
            }
            i++;
            try {
                nthDayOfWeek = Integer.parseInt(s.substring(i));
                if (nthDayOfWeek < 1 || nthDayOfWeek > 5) {
                    throw new Exception();
                }
            } catch (Exception e) {
                throw new ParseException(
                        "A numeric value between 1 and 5 must follow the '#' option",
                        i);
            }

            TreeSet<Integer> set = getSet(type);
            set.add(val);
            i++;
            return i;
        }

        if (c == '-') {
            i++;
            c = s.charAt(i);
            int v = Integer.parseInt(String.valueOf(c));
            end = v;
            i++;
            if (i >= s.length()) {
                addToSet(val, end, 1, type);
                return i;
            }
            c = s.charAt(i);
            if (c >= '0' && c <= '9') {
                ValueSet vs = getValue(v, s, i);
                end = vs.value;
                i = vs.pos;
            }
            if (i < s.length() && ((c = s.charAt(i)) == '/')) {
                i++;
                c = s.charAt(i);
                int v2 = Integer.parseInt(String.valueOf(c));
                i++;
                if (i >= s.length()) {
                    addToSet(val, end, v2, type);
                    return i;
                }
                c = s.charAt(i);
                if (c >= '0' && c <= '9') {
                    ValueSet vs = getValue(v2, s, i);
                    int v3 = vs.value;
                    addToSet(val, end, v3, type);
                    i = vs.pos;
                    return i;
                } else {
                    addToSet(val, end, v2, type);
                    return i;
                }
            } else {
                addToSet(val, end, 1, type);
                return i;
            }
        }

        if (c == '/') {
            if ((i + 1) >= s.length() || s.charAt(i + 1) == ' ' || s.charAt(i + 1) == '\t') {
                throw new ParseException("'/' must be followed by an integer.", i);
            }

            i++;
            c = s.charAt(i);
            int v2 = Integer.parseInt(String.valueOf(c));
            i++;
            if (i >= s.length()) {
                checkIncrementRange(v2, type, i);
                addToSet(val, end, v2, type);
                return i;
            }
            c = s.charAt(i);
            if (c >= '0' && c <= '9') {
                ValueSet vs = getValue(v2, s, i);
                int v3 = vs.value;
                checkIncrementRange(v3, type, i);
                addToSet(val, end, v3, type);
                i = vs.pos;
                return i;
            } else {
                throw new ParseException("Unexpected character '" + c + "' after '/'", i);
            }
        }

        addToSet(val, end, 0, type);
        i++;
        return i;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public String getExpressionSummary() {
        StringBuilder buf = new StringBuilder();

        buf.append("seconds: ");
        buf.append(getExpressionSetSummary(seconds));
        buf.append("\n");
        buf.append("minutes: ");
        buf.append(getExpressionSetSummary(minutes));
        buf.append("\n");
        buf.append("hours: ");
        buf.append(getExpressionSetSummary(hours));
        buf.append("\n");
        buf.append("daysOfMonth: ");
        buf.append(getExpressionSetSummary(daysOfMonth));
        buf.append("\n");
        buf.append("nearestWeekdays: ");
        buf.append(getExpressionSetSummary(nearestWeekdays));
        buf.append("\n");
        buf.append("months: ");
        buf.append(getExpressionSetSummary(months));
        buf.append("\n");
        buf.append("daysOfWeek: ");
        buf.append(getExpressionSetSummary(daysOfWeek));
        buf.append("\n");
        buf.append("lastDayOfWeek: ");
        buf.append(lastDayOfWeek);
        buf.append("\n");
        buf.append("NthDayOfWeek: ");
        buf.append(nthDayOfWeek);
        buf.append("\n");
        buf.append("years: ");
        buf.append(getExpressionSetSummary(years));
        buf.append("\n");

        return buf.toString();
    }

    protected String getExpressionSetSummary(java.util.Set<Integer> set) {

        if (set.contains(NO_SPEC)) {
            return "?";
        }
        if (set.contains(ALL_SPEC)) {
            return "*";
        }

        StringBuilder buf = new StringBuilder();

        Iterator<Integer> itr = set.iterator();
        boolean first = true;
        while (itr.hasNext()) {
            Integer iVal = itr.next();
            String val = iVal.toString();
            if (!first) {
                buf.append(",");
            }
            buf.append(val);
            first = false;
        }

        return buf.toString();
    }

    protected String getExpressionSetSummary(java.util.ArrayList<Integer> list) {

        if (list.contains(NO_SPEC)) {
            return "?";
        }
        if (list.contains(ALL_SPEC)) {
            return "*";
        }

        StringBuilder buf = new StringBuilder();

        Iterator<Integer> itr = list.iterator();
        boolean first = true;
        while (itr.hasNext()) {
            Integer iVal = itr.next();
            String val = iVal.toString();
            if (!first) {
                buf.append(",");
            }
            buf.append(val);
            first = false;
        }

        return buf.toString();
    }

    protected int skipWhiteSpace(int i, String s) {
        for (; i < s.length() && (s.charAt(i) == ' ' || s.charAt(i) == '\t'); i++) {
        }

        return i;
    }

    protected int findNextWhiteSpace(int i, String s) {
        for (; i < s.length() && (s.charAt(i) != ' ' || s.charAt(i) != '\t'); i++) {
        }

        return i;
    }

    protected void addToSet(int val, int end, int incr, int type)
            throws ParseException {

        TreeSet<Integer> set = getSet(type);

        if (type == SECOND || type == MINUTE) {
            if ((val < 0 || val > 59 || end > 59) && (val != ALL_SPEC_INT)) {
                throw new ParseException(
                        "Minute and Second values must be between 0 and 59",
                        -1);
            }
        } else if (type == HOUR) {
            if ((val < 0 || val > 23 || end > 23) && (val != ALL_SPEC_INT)) {
                throw new ParseException(
                        "Hour values must be between 0 and 23", -1);
            }
        } else if (type == DAY_OF_MONTH) {
            if ((val < 1 || val > 31 || end > 31) && (val != ALL_SPEC_INT)
                && (val != NO_SPEC_INT)) {
                throw new ParseException(
                        "Day of month values must be between 1 and 31", -1);
            }
        } else if (type == MONTH) {
            if ((val < 1 || val > 12 || end > 12) && (val != ALL_SPEC_INT)) {
                throw new ParseException(
                        "Month values must be between 1 and 12", -1);
            }
        } else if (type == DAY_OF_WEEK) {
            if ((val == 0 || val > 7 || end > 7) && (val != ALL_SPEC_INT)
                && (val != NO_SPEC_INT)) {
                throw new ParseException(
                        "Day-of-Week values must be between 1 and 7", -1);
            }
        }

        if ((incr == 0 || incr == -1) && val != ALL_SPEC_INT) {
            if (val != -1) {
                set.add(val);
            } else {
                set.add(NO_SPEC);
            }

            return;
        }

        int startAt = val;
        int stopAt = end;

        if (val == ALL_SPEC_INT && incr <= 0) {
            incr = 1;
            set.add(ALL_SPEC); // put in a marker, but also fill values
        }

        if (type == SECOND || type == MINUTE) {
            if (stopAt == -1) {
                stopAt = 59;
            }
            if (startAt == -1 || startAt == ALL_SPEC_INT) {
                startAt = 0;
            }
        } else if (type == HOUR) {
            if (stopAt == -1) {
                stopAt = 23;
            }
            if (startAt == -1 || startAt == ALL_SPEC_INT) {
                startAt = 0;
            }
        } else if (type == DAY_OF_MONTH) {
            if (stopAt == -1) {
                stopAt = 31;
            }
            if (startAt == -1 || startAt == ALL_SPEC_INT) {
                startAt = 1;
            }
        } else if (type == MONTH) {
            if (stopAt == -1) {
                stopAt = 12;
            }
            if (startAt == -1 || startAt == ALL_SPEC_INT) {
                startAt = 1;
            }
        } else if (type == DAY_OF_WEEK) {
            if (stopAt == -1) {
                stopAt = 7;
            }
            if (startAt == -1 || startAt == ALL_SPEC_INT) {
                startAt = 1;
            }
        } else if (type == YEAR) {
            if (stopAt == -1) {
                stopAt = MAX_YEAR;
            }
            if (startAt == -1 || startAt == ALL_SPEC_INT) {
                startAt = 1970;
            }
        }

        // if the end of the range is before the start, then we need to overflow into
        // the next day, month etc. This is done by adding the maximum amount for that
        // type, and using modulus max to determine the value being added.
        int max = -1;
        if (stopAt < startAt) {
            switch (type) {
                case SECOND:
                    max = 60;
                    break;
                case MINUTE:
                    max = 60;
                    break;
                case HOUR:
                    max = 24;
                    break;
                case MONTH:
                    max = 12;
                    break;
                case DAY_OF_WEEK:
                    max = 7;
                    break;
                case DAY_OF_MONTH:
                    max = 31;
                    break;
                case YEAR:
                    throw new IllegalArgumentException("Start year must be less than stop year");
                default:
                    throw new IllegalArgumentException("Unexpected type encountered");
            }
            stopAt += max;
        }

        for (int i = startAt; i <= stopAt; i += incr) {
            if (max == -1) {
                // ie: there's no max to overflow over
                set.add(i);
            } else {
                // take the modulus to get the real value
                int i2 = i % max;

                // 1-indexed ranges should not include 0, and should include their max
                if (i2 == 0 && (type == MONTH || type == DAY_OF_WEEK || type == DAY_OF_MONTH)) {
                    i2 = max;
                }

                set.add(i2);
            }
        }
    }

    TreeSet<Integer> getSet(int type) {
        switch (type) {
            case SECOND:
                return seconds;
            case MINUTE:
                return minutes;
            case HOUR:
                return hours;
            case DAY_OF_MONTH:
                return daysOfMonth;
            case MONTH:
                return months;
            case DAY_OF_WEEK:
                return daysOfWeek;
            case YEAR:
                return years;
            default:
                return null;
        }
    }

    protected ValueSet getValue(int v, String s, int i) {
        char c = s.charAt(i);
        StringBuilder s1 = new StringBuilder(String.valueOf(v));
        while (c >= '0' && c <= '9') {
            s1.append(c);
            i++;
            if (i >= s.length()) {
                break;
            }
            c = s.charAt(i);
        }
        ValueSet val = new ValueSet();

        val.pos = (i < s.length()) ? i : i + 1;
        val.value = Integer.parseInt(s1.toString());
        return val;
    }

    protected int getNumericValue(String s, int i) {
        int endOfVal = findNextWhiteSpace(i, s);
        String val = s.substring(i, endOfVal);
        return Integer.parseInt(val);
    }

    protected int getMonthNumber(String s) {
        Integer integer = monthMap.get(s);

        if (integer == null) {
            return -1;
        }

        return integer;
    }

    protected int getDayOfWeekNumber(String s) {
        Integer integer = dayMap.get(s);

        if (integer == null) {
            return -1;
        }

        return integer;
    }

    ////////////////////////////////////////////////////////////////////////////
    //
    // Computation Functions
    //

    /// /////////////////////////////////////////////////////////////////////////

    public LocalDateTime getTimeAfter(LocalDateTime afterTime) {
        LocalDateTime zdt = LocalDateTime.ofInstant(afterTime.atZone(getTimeZone().toZoneId()).toInstant(), getTimeZone().toZoneId())
                .plusSeconds(1) // 向后移动1秒（原逻辑）
                .withNano(0);
        boolean gotOne = false;
        // loop until we've computed the next time, or we've past the endTime
        while (!gotOne) {
            //if (endTime != null && cl.getTime().after(endTime)) return null;
            if (zdt.getYear() > 2999) { // prevent endless loop...
                return null;
            }

            SortedSet<Integer> st = null;
            int t = 0;

            int sec = zdt.getSecond();
            int min = zdt.getMinute();

            // get second.................................................
            st = seconds.tailSet(sec);
            if (st != null && !st.isEmpty()) {
                sec = st.first();
            } else {
                sec = seconds.first();
                min++;
                zdt = zdt.withMinute(min);
            }
            zdt = zdt.withSecond(sec);

            min = zdt.getMinute();
            int hr = zdt.getHour();
            t = -1;

            // get minute.................................................
            st = minutes.tailSet(min);
            if (st != null && !st.isEmpty()) {
                t = min;
                min = st.first();
            } else {
                min = minutes.first();
                hr++;
            }
            if (min != t) {
                zdt = zdt.withSecond(0);
                zdt = zdt.withMinute(min);
                zdt = setHour(zdt, hr);
                continue;
            }
            zdt = zdt.withMinute(min);

            hr = zdt.getHour();
            int day = zdt.getDayOfMonth();
            t = -1;

            // get hour...................................................
            st = hours.tailSet(hr);
            if (st != null && !st.isEmpty()) {
                t = hr;
                hr = st.first();
            } else {
                hr = hours.first();
                int lastDay = zdt.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
                if (day >= lastDay) {
                    day = 1;
                    zdt = zdt.withMonth(zdt.getMonth().getValue() + 1);
                } else {
                    day++;
                }
            }
            if (hr != t) {
                zdt = zdt.withDayOfMonth(day)
                        .withMinute(0)
                        .withSecond(0);
                zdt = setHour(zdt, hr);
                continue;
            }
            zdt = zdt.withHour(hr);

            day = zdt.getDayOfMonth();
            int mon = zdt.getMonth().getValue();
            // '+ 1' because calendar is 0-based for this field, and we are
            // 1-based
            t = -1;
            int tmon = mon;

            // get day...................................................
            boolean dayOfMSpec = !daysOfMonth.contains(NO_SPEC);
            boolean dayOfWSpec = !daysOfWeek.contains(NO_SPEC);
            if (dayOfMSpec && !dayOfWSpec) { // get day by day of month rule
                Optional<Integer> smallestDay = findSmallestDay(day, mon, zdt.getYear(), daysOfMonth);
                Optional<Integer> smallestDayForWeekday = findSmallestDay(day, mon, zdt.getYear(), nearestWeekdays);
                t = day;
                day = -1;
                if (smallestDayForWeekday.isPresent()) {
                    day = smallestDayForWeekday.get();

                    ZonedDateTime tzdt = ZonedDateTime.of(zdt.getYear(),
                                                          mon,
                                                          day,
                                                          0,
                                                          0,
                                                          0,
                                                          0,
                                                          getTimeZone().toZoneId());

                    int ldom = getLastDayOfMonth(mon, zdt.getYear());
                    DayOfWeek dow = tzdt.getDayOfWeek();
                    if (dow == DayOfWeek.SATURDAY && day == 1) {
                        day += 2;
                    } else if (dow == DayOfWeek.SATURDAY) {
                        day -= 1;
                    } else if (dow == DayOfWeek.SUNDAY && day == ldom) {
                        day -= 2;
                    } else if (dow == DayOfWeek.SUNDAY) {
                        day += 1;
                    }
                    tzdt = tzdt.withMonth(mon)
                            .withDayOfMonth(day)
                            .withHour(hr)
                            .withMinute(min)
                            .withSecond(sec);
                    if (tzdt.toLocalDateTime().isBefore(afterTime)) {
                        day = -1;
                    }
                }
                if (smallestDay.isPresent()) {
                    if (day == -1 || smallestDay.get() < day) {
                        day = smallestDay.get();
                    }
                } else if (day == -1) {
                    day = 1;
                    if (mon > 11) {
                        mon = 1;
                        zdt = zdt.withYear(zdt.getYear() + 1);
                    } else {
                        mon++;
                    }
                }
                if (day != t || mon != tmon) {
                    zdt = zdt.withMonth(mon)
                            .withDayOfMonth(day)
                            .withHour(0)
                            .withMinute(0)
                            .withSecond(0);
                    // '- 1' because calendar is 0-based for this field, and we
                    // are 1-based
                    continue;
                }
            } else if (dayOfWSpec && !dayOfMSpec) { // get day by day of week rule
                if (lastDayOfWeek) { // are we looking for the last XXX day of
                    // the month?
                    int dow = daysOfWeek.first(); // desired
                    // d-o-w
                    int cDow = zdt.getDayOfWeek().getValue(); // current d-o-w
                    //使用老的方式兼容星期天为1的情况
                    if (DayOfWeek.SUNDAY == zdt.getDayOfWeek()) {
                        cDow = 1;
                    } else {
                        cDow = cDow + 1;
                    }
                    int daysToAdd = 0;
                    if (cDow < dow) {
                        daysToAdd = dow - cDow;
                    }
                    if (cDow > dow) {
                        daysToAdd = dow + (7 - cDow);
                    }

                    int lDay = getLastDayOfMonth(mon, zdt.getYear());

                    if (day + daysToAdd > lDay) { // did we already miss the
                        // last one?
                        zdt = zdt.withMonth(mon + 1)
                                .withDayOfMonth(1)
                                .withHour(0)
                                .withMinute(0)
                                .withSecond(0);
                        // no '- 1' here because we are promoting the month
                        continue;
                    }

                    // find date of last occurrence of this day in this month...
                    while ((day + daysToAdd + 7) <= lDay) {
                        daysToAdd += 7;
                    }

                    day += daysToAdd;

                    if (daysToAdd > 0) {
                        zdt = zdt.withMonth(mon)
                                .withDayOfMonth(day)
                                .withHour(0)
                                .withMinute(0)
                                .withSecond(0);
                        // '- 1' here because we are not promoting the month
                        continue;
                    }

                } else if (nthDayOfWeek != 0) {
                    // are we looking for the Nth XXX day in the month?
                    int dow = daysOfWeek.first(); // desired
                    // d-o-w
                    int cDow = zdt.getDayOfWeek().getValue(); // current d-o-w
                    //使用老的方式兼容星期天为1的情况
                    if (DayOfWeek.SUNDAY == zdt.getDayOfWeek()) {
                        cDow = 1;
                    } else {
                        cDow = cDow + 1;
                    }
                    int daysToAdd = 0;
                    if (cDow < dow) {
                        daysToAdd = dow - cDow;
                    } else if (cDow > dow) {
                        daysToAdd = dow + (7 - cDow);
                    }

                    boolean dayShifted = daysToAdd > 0;

                    day += daysToAdd;
                    int weekOfMonth = day / 7;
                    if (day % 7 > 0) {
                        weekOfMonth++;
                    }

                    daysToAdd = (nthDayOfWeek - weekOfMonth) * 7;
                    day += daysToAdd;
                    if (daysToAdd < 0
                        || day > getLastDayOfMonth(mon, zdt.getYear())) {
                        zdt = zdt.withMonth(mon + 1)
                                .withDayOfMonth(1)
                                .withHour(0)
                                .withMinute(0)
                                .withSecond(0);
                        continue;
                    } else if (daysToAdd > 0 || dayShifted) {
                        zdt = zdt.withMonth(mon)
                                .withDayOfMonth(day)
                                .withHour(0)
                                .withMinute(0)
                                .withSecond(0);
                        // '- 1' here because we are NOT promoting the month
                        continue;
                    }
                } else {
                    int cDow = zdt.getDayOfWeek().getValue(); // current d-o-w
                    //使用老的方式兼容星期天为1的情况
                    if (DayOfWeek.SUNDAY == zdt.getDayOfWeek()) {
                        cDow = 1;
                    } else {
                        cDow = cDow + 1;
                    }
                    int dow = daysOfWeek.first(); // desired
                    // d-o-w
                    st = daysOfWeek.tailSet(cDow);
                    if (st != null && !st.isEmpty()) {
                        dow = st.first();
                    }

                    int daysToAdd = 0;
                    if (cDow < dow) {
                        daysToAdd = dow - cDow;
                    }
                    if (cDow > dow) {
                        daysToAdd = dow + (7 - cDow);
                    }

                    int lDay = getLastDayOfMonth(mon, zdt.getYear());

                    if (day + daysToAdd > lDay) { // will we pass the end of
                        // the month?
                        zdt = zdt.withMonth(mon + 1)
                                .withDayOfMonth(1)
                                .withHour(0)
                                .withMinute(0)
                                .withSecond(0);
                        // no '- 1' here because we are promoting the month
                        continue;
                    } else if (daysToAdd > 0) { // are we switching days?
                        zdt = zdt.withMonth(mon)
                                .withDayOfMonth(day + daysToAdd)
                                .withHour(0)
                                .withMinute(0)
                                .withSecond(0);
                        // '- 1' because calendar is 0-based for this field,
                        // and we are 1-based
                        continue;
                    }
                }
            } else { // dayOfWSpec && !dayOfMSpec
                throw new UnsupportedOperationException(
                        "Support for specifying both a day-of-week AND a day-of-month parameter is not implemented.");
            }
            zdt = zdt.withDayOfMonth(day);

            mon = zdt.getMonth().getValue();
            // '+ 1' because calendar is 0-based for this field, and we are
            // 1-based
            int year = zdt.getYear();
            t = -1;

            // test for expressions that never generate a valid fire date,
            // but keep looping...
            if (year > MAX_YEAR) {
                return null;
            }

            // get month...................................................
            st = months.tailSet(mon);
            if (st != null && !st.isEmpty()) {
                t = mon;
                mon = st.first();
            } else {
                mon = months.first();
                year++;
            }
            if (mon != t) {
                zdt = zdt.withYear(year)
                        .withMonth(mon)
                        .withDayOfMonth(1)
                        .withHour(0)
                        .withMinute(0)
                        .withSecond(0);
                continue;
            }
            zdt = zdt.withMonth(mon);
            // '- 1' because calendar is 0-based for this field, and we are
            // 1-based

            year = zdt.getYear();
            t = -1;

            // get year...................................................
            st = years.tailSet(year);
            if (st != null && !st.isEmpty()) {
                t = year;
                year = st.first();
            } else {
                return null; // ran out of years...
            }

            if (year != t) {
                zdt = zdt.withYear(year)
                        .withMonth(1)
                        .withDayOfMonth(1)
                        .withHour(0)
                        .withMinute(0)
                        .withSecond(0);
                continue;
            }
            zdt = zdt.withYear(year);

            gotOne = true;
        }
        return zdt;
    }

    /**
     * NOT YET IMPLEMENTED: Returns the time before the given time
     * that the <code>CronExpression</code> matches.
     */
    public Date getTimeBefore(Date endTime) {
        // FUTURE_TODO: implement QUARTZ-423
        return null;
    }

    /**
     * NOT YET IMPLEMENTED: Returns the final time that the
     * <code>CronExpression</code> will match.
     */
    public Date getFinalFireTime() {
        // FUTURE_TODO: implement QUARTZ-423
        return null;
    }

    protected boolean isLeapYear(int year) {
        return ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0));
    }

    protected int getLastDayOfMonth(int monthNum, int year) {

        switch (monthNum) {
            case 1:
                return 31;
            case 2:
                return (isLeapYear(year)) ? 29 : 28;
            case 3:
                return 31;
            case 4:
                return 30;
            case 5:
                return 31;
            case 6:
                return 30;
            case 7:
                return 31;
            case 8:
                return 31;
            case 9:
                return 30;
            case 10:
                return 31;
            case 11:
                return 30;
            case 12:
                return 31;
            default:
                throw new IllegalArgumentException("Illegal month number: "
                                                   + monthNum);
        }
    }

    private Optional<Integer> findSmallestDay(int day, int mon, int year, TreeSet<Integer> set) {
        if (set.isEmpty()) {
            return Optional.empty();
        }

        final int lastDay = getLastDayOfMonth(mon, year);
        // For "L", "L-1", etc.
        int smallestDay = Optional.ofNullable(set.ceiling(LAST_DAY_OFFSET_END - (lastDay - day)))
                .map(d -> d - LAST_DAY_OFFSET_START + 1)
                .orElse(Integer.MAX_VALUE);

        // For "1", "2", etc.
        SortedSet<Integer> st = set.subSet(day, LAST_DAY_OFFSET_START);
        // make sure we don't over-run a short month, such as february
        if (!st.isEmpty() && st.first() < smallestDay && st.first() <= lastDay) {
            smallestDay = st.first();
        }

        return smallestDay == Integer.MAX_VALUE ? Optional.empty() : Optional.of(smallestDay);
    }

    /**
     * 构建Cron表达式
     * 目前支持三种常用的cron表达式
     * 1.每天的某个时间点执行 例:12 12 12 * * 表示每天12时12分12秒执行
     * 2.每周的哪几天执行    例:12 12 12 ? * 1,2,3表示每周的周1周2周3 ,12时12分12秒执行
     * 3.每月的哪几天执行    例:12 12 12 1,21,13 * ?表示每月的1号21号13号 12时12分12秒执行
     */
    public static String createCronExpression(CronModelDTO cronModel) {
        var cronExp = new StringBuilder();

        if (null == cronModel.getJobType()) {
            throw new SecurityException("未找到指定执行类型");//执行周期未配置
        }

        if (null != cronModel.getSecond()
            && null != cronModel.getMinute()
            && null != cronModel.getHour()) {
            //秒
            cronExp.append(cronModel.getSecond()).append(" ");
            //分
            cronExp.append(cronModel.getMinute()).append(" ");
            //小时
            cronExp.append(cronModel.getHour()).append(" ");

            switch (cronModel.getJobType()) {
                case EVERY_DAY:
                    setDayCron(cronModel, cronExp);
                    break;
                case EVERY_WEEK:
                    setWeekCron(cronModel, cronExp);
                    break;
                case EVERY_MONTH:
                    var x = setEveryMonthCron(cronModel, cronExp);
                    if (x != null) {
                        return x;
                    }
                    break;
                case EVERY_YEAR:
                    setYearCron(cronModel, cronExp);
                    break;
                default:
                    throw new ServiceException("未找到指定执行类型");
            }
        }
        return cronExp.toString();
    }

    /**
     * 根据固定参数解释cron表达式
     *
     * @param jobEnum    表达式类型
     * @param selection  勾选日期
     * @param hourMinute 执行时间
     *
     * @return
     */
    public static String explainCronExpression(JobLoopTypeEnum jobEnum, String selection, String hourMinute) {
        if (null == jobEnum || null == hourMinute) {
            throw new ServiceException("未找到指定Job执行类型");
        }
        List<String> selectDateList = new ArrayList<>();
        if (StringUtils.isNotBlank(selection)) {
            selectDateList = Arrays.stream(selection.split(","))
                    .collect(Collectors.toList());
        }
        var cronModel = new CronModelDTO();
        cronModel.setJobType(jobEnum);
        switch (jobEnum) {
            case EVERY_WEEK:
                cronModel.setDayOfWeeks(selectDateList);
                break;
            case EVERY_MONTH:
                cronModel.setDayOfMonths(selectDateList);
                break;
            default:
                break;
        }
        var time = hourMinute.split(":");
        cronModel.setHour(Integer.parseInt(time[0]));
        cronModel.setMinute(Integer.parseInt(time[1]));
        return CronExpression.createCronExpression(cronModel);
    }

    /**
     * 设置年表达式
     *
     * @param cronModel 表达式模型
     * @param cronExp   表达式
     */
    private static void setYearCron(CronModelDTO cronModel, StringBuilder cronExp) {
        //一个年中的哪几天
        var days = cronModel.getDayOfMonths();
        if (CollectionUtils.isEmpty(days)) {
            cronExp.append("*");
        } else {
            for (int i = 0; i < days.size(); i++) {
                if (i == 0) {
                    cronExp.append(days.get(i));
                } else {
                    cronExp.append(",").append(days.get(i));
                }
            }
        }
        //月份
        var months = cronModel.getMonths();
        if (CollectionUtils.isEmpty(months)) {
            cronExp.append(" *");
        } else {
            for (int i = 0; i < months.size(); i++) {
                var month = months.get(i);
                if (Integer.parseInt(month) > 12) {
                    throw new RuntimeException("月份数据异常: " + months.toString());
                }
                if (i == 0) {
                    cronExp.append(" ").append(month);
                } else {
                    cronExp.append(",").append(month);
                }
            }
        }
        cronExp.append(" ?");
    }

    /**
     * 设置月表达式
     *
     * @param cronModel 表达式模型
     * @param cronExp   表达式
     *
     * @return 最后一天直接返回
     */
    private static String setEveryMonthCron(CronModelDTO cronModel, StringBuilder cronExp) {
        //一个月中的哪几天
        var days = cronModel.getDayOfMonths();
        for (int i = 0; i < days.size(); i++) {
            if (i == 0) {
                if (Integer.parseInt(days.get(i)) == 32) {
                    //本月最后一天
                    return "0 0 0 L * ?";
                } else {
                    cronExp.append(days.get(i));
                }
            } else {
                cronExp.append(",").append(days.get(i));
            }
        }
        //月份
        cronExp.append(" * ");
        //周
        cronExp.append("?");
        return null;
    }

    /**
     * 设置天表达式
     *
     * @param cronModel 表达式模型
     * @param cronExp   表达式
     */
    private static void setDayCron(CronModelDTO cronModel, StringBuilder cronExp) {
        //12 12 12 1/2 * ? *
        //12 12 12 * * ?
        if (cronModel.getBeApart() != null) {
            cronExp.append("1");//日
            cronExp.append("/");
            cronExp.append(cronModel.getBeApart() + 1);//月
            cronExp.append(" ");
            cronExp.append("* ");
            cronExp.append("?");
        } else {
            cronExp.append("* ");//日
            cronExp.append("* ");//月
            cronExp.append("?");
        }
    }

    /**
     * 设置周表达式
     *
     * @param cronModel 表达式模型
     * @param cronExp   表达式
     */
    private static void setWeekCron(CronModelDTO cronModel, StringBuilder cronExp) {
        //一个月中第几天
        cronExp.append("? ");
        //月份
        cronExp.append("* ");
        //周
        var weeks = cronModel.getDayOfWeeks();
        for (int i = 0; i < weeks.size(); i++) {
            if (i == 0) {
                cronExp.append(weeks.get(i));
            } else {
                cronExp.append(",").append(weeks.get(i));
            }
        }
    }

    protected LocalDateTime setHour(LocalDateTime zdt, int hour) {
        zdt = zdt.withHour(hour);
        if (zdt.getHour() != hour && hour != 24) {
            zdt = zdt.withHour(hour + 1);
        }
        return zdt;
    }

    public static String getIntervalWeeksCron(List<Integer> weeks) {
        return "0 0 0 ? * " + weeks.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    public static String getIntervalCloseWeeksCron(List<Integer> weeks) {
        return "59 59 23 ? * " + weeks.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    public static String getIntervalLocalTimeCron(LocalTime localTime) {
        return "0 " + localTime.getMinute() + " " + localTime.getHour() + " * * ?";
    }

    public static String getIntervalYearMonthCron(List<Integer> months) {
        return "0 0 0 1 " + months.stream().map(String::valueOf).collect(Collectors.joining(",")) + " ?";
    }

    @Serial
    private void readObject(ObjectInputStream stream)
            throws IOException, ClassNotFoundException {

        stream.defaultReadObject();
        try {
            buildExpression(cronExpression);
        } catch (Exception ignore) {
        } // never happens
    }

    public static class ValueSet {
        public int value;

        public int pos;
    }
}
