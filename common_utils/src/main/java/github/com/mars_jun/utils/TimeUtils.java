package github.com.mars_jun.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class TimeUtils {
    public static String addTime(String endTime, String formater, int year, int month, int day)
            throws Exception {
        formater = StringUtils.isEmpty(formater) ? "yyyy-MM-dd" : formater;
        if (!StringUtils.isEmpty(endTime)) {
            SimpleDateFormat sdf = new SimpleDateFormat(formater);
            Calendar c = Calendar.getInstance();
            c.setTime(sdf.parse(endTime));
            if (year != 0) {
                c.add(1, year);
            }
            if (month != 0) {
                c.add(2, month);
            }
            if (day != 0) {
                c.add(5, day);
            }
            return sdf.format(c.getTime());
        }
        throw new Exception("日期格式不正确");
    }

    private static boolean equalsTime(String currTime, String startTime, String endTime, String formater) {
        boolean flag = false;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(formater);

            java.util.Date currTimeDate = sdf.parse(currTime);
            java.util.Date startTimeDate = sdf.parse(startTime);
            java.util.Date endTimeDate = sdf.parse(endTime);

            int startFlag = currTimeDate.compareTo(startTimeDate);
            int endFlag = currTimeDate.compareTo(endTimeDate);

            if ((startFlag == 0) || (endFlag == 0) || (
                    (startFlag > 0) && (endFlag < 0)))
                flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static String sysdate() {
        return sysdate("yyyy-MM-dd");
    }

    public static String sysdate(String formater) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(formater);
        return sdf.format(c.getTime());
    }

    public static String firstDayOfMonth(String dateTime, String formater) {
        String result = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(formater);
            java.util.Date date = sdf.parse(dateTime);
            date.setDate(1);
            result = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String lastDayOfMonth(String datetime, String formater) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(formater);
            java.util.Date date = sdf.parse(datetime);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int value = cal.getActualMaximum(5);
            cal.set(5, value);
            datetime = sdf.format(cal.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datetime;
    }

    public static String getFullDate() {
        String formater = "yyyy-MM-dd";
        SimpleDateFormat format = new SimpleDateFormat(formater);
        format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        java.util.Date myDate = new java.util.Date();
        return format.format(myDate);
    }

    public static String getSimpleDate() {
        String formater = "yyyy-M-d";
        SimpleDateFormat format = new SimpleDateFormat(formater);
        format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        java.util.Date myDate = new java.util.Date();
        return format.format(myDate);
    }

    public static String[] getSimpleDateRangeOfWeek() {
        String today = getFullDate();
        int endWeekDay = getWeekDay(today);

        String startDate = getSomeDate(today + " 00:00:00",
                -(endWeekDay - 1), "yyyy-MM-dd");
        String endDate = getSomeDate(startDate, 6, "yyyy-MM-dd")
                .substring(0, 10) +
                " 24:00:00";
        String[] DateRang = new String[2];
        DateRang[0] = startDate;
        DateRang[1] = endDate;
        return DateRang;
    }

    public static String getSomeDate(String sDate, int iDay, String formter) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(formter);
            format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            java.util.Date date = format.parse(sDate);
            long Time = date.getTime() / 1000L + 86400 * iDay;
            date.setTime(Time * 1000L);
            return format.format(date);
        } catch (Exception ex) {
        }
        return "";
    }

    public static long getDaysFrom2Dates(String sDate1, String sDate2, String formter) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(formter);
            java.util.Date date = format.parse(sDate1);
            java.util.Date date1 = format.parse(sDate2);
            return (date.getTime() - date1.getTime()) / 86400000L;
        } catch (Exception ex) {
            System.out.println("Error in TimeUtil.getDaysFrom2Dates," + ex.toString());
        }
        return 0L;
    }

    public static String getSomeTime(String sDate, int iDay, String formter) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(formter);
            java.util.Date date = format.parse(sDate);
            long Time = date.getTime() / 1000L + 86400 * iDay;
            date.setTime(Time * 1000L);
            return format.format(date);
        } catch (Exception ex) {
        }
        return "";
    }

    public static int getWeekDay(String strDate) {
        int strWeekDay = 0;
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date mydate = null;
        Calendar cal = Calendar.getInstance();
        try {
            mydate = myFormatter.parse(strDate);
            cal.setTime(mydate);
            strWeekDay = cal.get(7);
            if (strWeekDay == 1)
                strWeekDay = 7;
            else
                strWeekDay--;
        } catch (ParseException e) {
            System.out.println("Error in TimeUtil.getWeekDay()" +
                    e.getMessage());
        }
        return strWeekDay;
    }

    public static boolean isDateLater(String sDate1, String sDate2) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            java.util.Date date1 = format.parse(sDate1);
            java.util.Date date2 = format.parse(sDate2);

            return date1.after(date2);
        } catch (Exception ex) {
        }
        return false;
    }

    public static int getEndday(int year, int month) {
        int endDay = 0;
        if ((month < 1) || (month > 12) || (year < 1753) || (year > 9999))
            return 0;
        switch (month) {
            case 4:
                endDay = 30;
                break;
            case 6:
                endDay = 30;
                break;
            case 9:
                endDay = 30;
                break;
            case 11:
                endDay = 30;
                break;
            case 2:
                if (year % 4 == 0)
                    endDay = 29;
                else
                    endDay = 28;
                break;
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            default:
                endDay = 31;
        }

        return endDay;
    }

    public static boolean isDate(String sDate, String sFormat) {
        if (sDate == null)
            return false;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(sFormat);
            dateFormat.setLenient(false);
            dateFormat.parse(sDate);
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public static int getYear() {
        GregorianCalendar gc = new GregorianCalendar();
        return gc.get(1);
    }

    public static int getMonth() {
        GregorianCalendar gc = new GregorianCalendar();
        return gc.get(2) + 1;
    }

    public static int getDayOfMonth() {
        GregorianCalendar gc = new GregorianCalendar();
        return gc.get(5);
    }

    public static int getDayOfWeek() {
        GregorianCalendar gc = new GregorianCalendar();
        return gc.get(7);
    }

    public static String formatDate(java.util.Date date, String formater) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(formater);
            return format.format(date);
        } catch (Exception e) {
            System.out.println("Format error :" + e.getMessage());
        }
        return "";
    }

    public static String formatDate(String date, String formater) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(formater);
            java.util.Date fd = format.parse(date);
            return format.format(fd);
        } catch (Exception e) {
            System.out.println("Format error :" + e.getMessage());
        }
        return date;
    }

    public static String getCurDateTime() {
        String formater = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(formater);
        java.util.Date myDate = new java.util.Date();
        return format.format(myDate);
    }

    public static String[] getDatesRangeOfLastMonth(String formater) {
        String[] DateRang = new String[2];
        SimpleDateFormat format = new SimpleDateFormat(formater);
        java.util.Date myDate = new java.util.Date();
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(myDate);
        calendar.set(calendar.get(1), calendar
                .get(2), 1);
        java.util.Date cDate = calendar.getTime();
        DateRang[1] = format.format(cDate);
        calendar.add(2, -1);
        cDate = calendar.getTime();
        DateRang[0] = format.format(cDate);
        return DateRang;
    }

    public static String getEndDate(java.sql.Date startDate, int numberOfDays) {
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(startDate);
            calendar.add(6, numberOfDays);
            java.util.Date endDate = calendar.getTime();
            return endDate.toString();
        } catch (Exception e) {
            System.out.println("Error:TimeUtil.getEndDate()," + e.getMessage());
        }
        return "";
    }
}
