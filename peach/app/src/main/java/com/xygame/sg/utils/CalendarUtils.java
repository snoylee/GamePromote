package com.xygame.sg.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CalendarUtils {

    public static int getCurrentYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    public static int getCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MONTH) + 1;
    }

    public static int getCurrentDay() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 取得指定年月的当月总天数
     *
     * @param year  年
     * @param month 月
     * @return 当月总天数
     */
    public static String[] getLastDay(int year, int month) {
        return getLastDay(year, month, 1);
    }

    /**
     * 取得指定年月的当月总天数，并从指定日期开始
     *
     * @param year  年
     * @param month 月
     * @return 当月总天数
     */
    public static String[] getLastDay(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day);
        int last = cal.getActualMaximum(Calendar.DATE);
        String[] item = new String[last - day + 1];
        for (int i = 0; day + i <= last; i++) {
            item[i] = String.valueOf(day + i) + "日";
        }
        return item;
    }

    public static String[] getYears(int startYear, int endYear) {
        // Calendar cal = Calendar.getInstance();
        // int a = cal.get(Calendar.YEAR)-startYear;
        int a = endYear - startYear;
        String[] item = new String[a];
        for (int i = 0; i < a; i++) {
            item[i] = String.valueOf(startYear + i) + "年";
        }
        return item;
    }

    public static String[] getMonths() {
        return getMonths(1);
    }

    public static String[] getCurrExistMonths() {
        return getExistMonths(getCurrentMonth());
    }

    public static String[] getExistMonths(int endMonth) {
        String[] item = new String[endMonth];
        for (int i = 0; i < endMonth; i++) {
            item[i] = String.valueOf(1 + i) + "月";
        }

        return item;
    }

    public static String[] getMonths(int startMonth) {
        String[] item = new String[12 - startMonth + 1];
        for (int i = 0; i < item.length; i++) {
            item[i] = String.valueOf(startMonth + i) + "月";
        }

        return item;
    }

    public static int getIndexOfItem(String[] item, String value) {
        for (int i = 0; i < item.length; i++) {

            if (item[i].equals(value)) {
                return i;
            }
        }

        return -1;
    }

    public static String[] getHours() {
        String[] hours = new String[24];
        for (int i = 0; i < 24; i++) {
            hours[i] = String.valueOf(i + 1);
        }
        return hours;
    }

    public static String[] getMins() {
        String[] mins = new String[60];
        for (int i = 0; i < 60; i++) {
            mins[i] = String.valueOf(i + 1);
        }
        return mins;
    }

    public static int getCurrHour() {
        SimpleDateFormat df = new SimpleDateFormat("HH");
        String hh = df.format(new Date());
        return Integer.parseInt(hh);
    }

    public static int getCurrMin() {
        SimpleDateFormat df = new SimpleDateFormat("mm");
        String mm = df.format(new Date());
        return Integer.parseInt(mm);
    }

    public static String getDateStr(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dt = new Date(time);
        return sdf.format(dt);
    }

    public static String getCaracterDateStr(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        Date dt = new Date(time);
        return sdf.format(dt);
    }

    public static String getYMDHMStr(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        Date dt = new Date(time);
        return sdf.format(dt);
    }

    public static String getXieGongDateStr(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
        Date dt = new Date(time);
        return sdf.format(dt);
    }

    public static String getXieGongYMDStr(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date dt = new Date(time);
        return sdf.format(dt);
    }

    public static String getXieGongDateDis(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date dt = new Date(time);
        return sdf.format(dt);
    }

    public static String getHenGongDateDis(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date dt = new Date(time);
        return sdf.format(dt);
    }

    public static String getHenGongYearMonth(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date dt = new Date(time);
        return sdf.format(dt);
    }

    public static String getHenGongYearMonthDay(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dt = new Date(time);
        return sdf.format(dt);
    }

    public static String getTimeLong(String timeStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = sdf.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long dateTime = date.getTime();

        return String.valueOf(dateTime);
    }

    public static String getTimeLong1(String timeStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        Date date = null;
        try {
            date = sdf.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long dateTime = date.getTime();

        return String.valueOf(dateTime);
    }

    public static String getHG_Y_M_DTimeLong(String timeStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long dateTime = date.getTime();

        return String.valueOf(dateTime);
    }

    public static String geTimeLongYMD(String timeStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = null;
        try {
            date = sdf.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long dateTime = date.getTime();

        return String.valueOf(dateTime);
    }

    public static String getMonth_DayFromLongToString(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        Date dt = new Date(time);
        return sdf.format(dt);
    }

    public static String getHour_MinFromLongToString(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date dt = new Date(time);
        return sdf.format(dt);
    }

    public static String getCurrDateStr() {
        return String.valueOf(System.currentTimeMillis());
    }

    public static String getTime(String user_time) {
        String[] dateStr = user_time.split("-");
        GregorianCalendar gc = new GregorianCalendar(Integer.parseInt(dateStr[0]), Integer.parseInt(dateStr[1]),
                Integer.parseInt(dateStr[2]));
        long dateTime = gc.getTime().getTime();

        return String.valueOf(dateTime);
    }

    public static String getTimeFromStr(String user_time, String formatStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        Date date = null;
        try {
            date = sdf.parse(user_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long dateTime = date.getTime();

        return String.valueOf(dateTime);
    }

    public static String getLeftTimeDistance(long endTime, long currTime) {
        Calendar curTime = Calendar.getInstance();
        curTime.setTimeInMillis(endTime);
        curTime.add(Calendar.DATE, 7);// 30为增加的天数，可以改变的
        return getLeftTime(curTime.getTime().getTime(), currTime);
    }

    public static String getLeftTime(long endTime, long currTime) {
        long hours;
        long minutes;
        long diff;
        long days;
        StringBuffer sb = new StringBuffer();
        try {
            Date d1 = new Date(endTime);
            Date d2;
            if (currTime == 0) {
                d2 = new Date();
            } else {
                d2 = new Date(currTime);
            }
            diff = d1.getTime() - d2.getTime();// 这样得到的差值是微秒级别
            long limitTime = diff / 1000;
            days = limitTime / (24 * 60 * 60);
            hours = (limitTime % (24 * 60 * 60)) / (60 * 60);
            minutes = ((limitTime % (24 * 60 * 60)) % (60 * 60)) / 60;
            if (days != 0) {
                String dayStr = String.valueOf(days).concat("天");
                sb.append(dayStr);
            }
            if (hours != 0) {
                String hourStr = String.valueOf(hours).concat("小时");
                sb.append(hourStr);
            }
            if (minutes != 0) {
                String minStr = String.valueOf(minutes).concat("分");
                sb.append(minStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString().replace("-", "");
    }

    public static boolean isTimeGone(long endTime, long currTime) {
        boolean flag = false;
        long hours;
        long minutes;
        long diff;
        long days;
        try {
            Date d1 = new Date(endTime);
            Date d2;
            if (currTime == 0) {
                d2 = new Date();
            } else {
                d2 = new Date(currTime);
            }
            diff = d1.getTime() - d2.getTime();// 这样得到的差值是微秒级别
            long limitTime = diff / 1000;
            days = limitTime / (24 * 60 * 60);
            hours = (limitTime % (24 * 60 * 60)) / (60 * 60);
            minutes = ((limitTime % (24 * 60 * 60)) % (60 * 60)) / 60;
            if (days == 0 && hours == 0 && minutes == 0) {
                flag = true;
            }
            if (days < 0) {
                flag = true;
            }
            if (hours < 0) {
                flag = true;
            }
            if (minutes < 0) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static String getLeftTimeDistanceForJieSuan(long endTime, long currTime) {
        Calendar curTime = Calendar.getInstance();
        curTime.setTimeInMillis(endTime);
        curTime.add(Calendar.DATE, 7);// 30为增加的天数，可以改变的
        return getLeftTimeForJieSuan(curTime.getTime().getTime(), currTime);
    }

    public static String getLeftTimeForJieSuan(long endTime, long currTime) {
        long hours;
        long minutes;
        long diff = 0;
        long days;
        String mix;
        StringBuffer sb = new StringBuffer();
        try {
            Date d1 = new Date(endTime);
            Date d2;
            if (currTime == 0) {
                d2 = new Date();
            } else {
                d2 = new Date(currTime);
            }
            diff = d1.getTime() - d2.getTime();// 这样得到的差值是微秒级别
            long limitTime = diff / 1000;
            days = limitTime / (24 * 60 * 60);
            hours = (limitTime % (24 * 60 * 60)) / (60 * 60);
            minutes = ((limitTime % (24 * 60 * 60)) % (60 * 60)) / 60;
            if (days != 0) {
                String dayStr = String.valueOf(days).concat("天");
                sb.append(dayStr);
            }
            if (hours != 0) {
                String hourStr = String.valueOf(hours).concat("小时");
                sb.append(hourStr);
            }
            if (minutes != 0) {
                String minStr = String.valueOf(minutes).concat("分");
                sb.append(minStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (diff <= 0) {
            mix = "-";
        } else {
            mix = sb.toString();
        }
        return mix;
    }


    /**
     * 得到指定月的天数
     */
    public static int getMonthLastDay(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    public static String[] getDays(int year, int month) {

        int daysSize = getMonthLastDay(year, month);
        String[] item = new String[daysSize];
        for (int i = 0; i < daysSize; i++) {
            item[i] = String.valueOf(i + 1) + "日";
        }
        return item;
    }

    public static boolean isBigger5Seconds(long startTime) {
        boolean flag = false;
        long hours;
        long minutes;
        long diff;
        long days;
        long limitTime = 0;
        StringBuffer sb = new StringBuffer();
        try {
            Date d2 = new Date(startTime);
            Date d1 = new Date();
            diff = d1.getTime() - d2.getTime();// 这样得到的差值是微秒级别
            limitTime = diff / 1000;
            days = limitTime / (24 * 60 * 60);
            hours = (limitTime % (24 * 60 * 60)) / (60 * 60);
            minutes = ((limitTime % (24 * 60 * 60)) % (60 * 60)) / 60;
            if (days != 0) {
                String dayStr = String.valueOf(days).concat("天");
                sb.append(dayStr);
            }
            if (hours != 0) {
                String hourStr = String.valueOf(hours).concat("小时");
                sb.append(hourStr);
            }
            if (minutes != 0) {
                String minStr = String.valueOf(minutes).concat("分");
                sb.append(minStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (limitTime > 5) {
            flag = true;
        }
        return flag;
    }

    public static String[] WEEKS = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

    public static int getWeekIndex(String yy, String MM, String dd) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int week_index = 0;
        String dateStr = yy.concat("-").concat(MM).concat("-").concat(dd);
        try {
            Date date = sdf.parse(dateStr);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
            if (week_index < 0) {
                week_index = 0;
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return week_index;
    }

    /**
     * 获取给定日期N天后的日期
     *
     * @author GaoHuanjie
     */
    public static String getDateAfterNDays(String dateTime, int days) {
        Calendar calendar = Calendar.getInstance();
        String[] dateTimeArray = dateTime.split("-");
        int year = Integer.parseInt(dateTimeArray[0]);
        int month = Integer.parseInt(dateTimeArray[1]);
        int day = Integer.parseInt(dateTimeArray[2]);
        calendar.set(year, month - 1, day);
        long time = calendar.getTimeInMillis();// 给定时间与1970 年 1 月 1 日的00:00:00.000的差，以毫秒显示
        calendar.setTimeInMillis(time + days * 1000 * 60 * 60 * 24);// 用给定的 long值设置此Calendar的当前时间值
        return calendar.get(Calendar.YEAR)// 应还书籍时间——年
                + "-" + (calendar.get(Calendar.MONTH) + 1)// 应还书籍时间——月
                + "-" + calendar.get(Calendar.DAY_OF_MONTH);// 应还书籍时间——日
    }

    /**
     * 获取给定日期N天后的日期
     * 指定日期的duringNums天后日期
     * @param someTime
     * @param duringNums
     * @return
     */
    public static String getDateDistance(long someTime, int duringNums) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(someTime);
        calendar.add(Calendar.DATE, duringNums);// duringNums为增加的天数，可以改变的
        return calendar.get(Calendar.YEAR)// 应还书籍时间——年
                + "-" + (calendar.get(Calendar.MONTH) + 1)// 应还书籍时间——月
                + "-" + calendar.get(Calendar.DAY_OF_MONTH);// 应还书籍时间——日
    }

    public static String getDate(long someTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(someTime);
        return calendar.get(Calendar.YEAR)// 应还书籍时间——年
                + "-" + (calendar.get(Calendar.MONTH) + 1)// 应还书籍时间——月
                + "-" + calendar.get(Calendar.DAY_OF_MONTH);// 应还书籍时间——日
    }

    /**
     * 根据时间磋获取前几天或后几天的时间磋
     * @param someTime
     * @param duringNums
     * @return
     */
    public static Long getTimeLongForDuring(long someTime, int duringNums) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(someTime);
        calendar.add(Calendar.DATE, duringNums);// duringNums为增加的天数，可以改变的
        return calendar.getTime().getTime();
    }

    /**
     * 获取给定日期N天后的日期
     * 指定日期的duringNums天后日期
     * @param someTime
     * @param duringNums
     * @return
     */
    public static String getDateFromLong(long someTime, int duringNums) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(someTime);
        calendar.add(Calendar.DATE, duringNums);// duringNums为增加的天数，可以改变的
        return calendar.get(Calendar.YEAR)// 应还书籍时间——年
                + "年" + (calendar.get(Calendar.MONTH) + 1)// 应还书籍时间——月
                + "月" + calendar.get(Calendar.DAY_OF_MONTH)+"日";// 应还书籍时间——日
    }

    public static int getYearInt(long someTime, int duringNums) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(someTime);
        calendar.add(Calendar.DATE, duringNums);// duringNums为增加的天数，可以改变的
        return calendar.get(Calendar.YEAR);// 应还书籍时间——日
    }

    public static int getMonthInt(long someTime, int duringNums) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(someTime);
        calendar.add(Calendar.DATE, duringNums);// duringNums为增加的天数，可以改变的
        return calendar.get(Calendar.MONTH) + 1;// 应还书籍时间——日
    }

    public static int getDayInt(long someTime, int duringNums) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(someTime);
        calendar.add(Calendar.DATE, duringNums);// duringNums为增加的天数，可以改变的
        return calendar.get(Calendar.DAY_OF_MONTH);// 应还书籍时间——日
    }

    public static int getHourInt(long someTime, int duringNums) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(someTime);
        calendar.add(Calendar.HOUR, duringNums);// duringNums为增加的天数，可以改变的
        return calendar.get(Calendar.HOUR_OF_DAY);// 应还书籍时间——日
    }

     /*

    获取当前时间之前(传负数)或之后几小时（传正数） hour

   */

    public static String getTimeByHour(int hour) {

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + hour);

        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());

    }

    /**
     *  指定时间几小时后
     * @param someTime 指定时间
     * @param hour 相差小时数量
     * @return
     */
    public static String getTimeByTimeAndHour(long someTime,int hour) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(someTime);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + hour);

        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(calendar.getTime());

    }

    /*

    获取当前时间之前(传负数)或之后几分钟（传正数） minute

    */

    public static String getTimeByMinute(int minute) {

        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.MINUTE, minute);

        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());

    }


    public static Date getStartTime(String startTime){
        return new Date(Long.parseLong(startTime));
    }

    public static boolean isPassed10Min(String holdTime,String startTime){
        boolean flag=true;
        long pastTime=XMPPUtils.getLeftSecondTime(Long.parseLong(startTime));
        long totalTime=Long.parseLong(holdTime)*60;
        if (pastTime<totalTime){
            flag=false;
        }
        return flag;
    }
}
