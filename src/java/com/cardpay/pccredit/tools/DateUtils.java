/**
 * 
 */
package com.cardpay.pccredit.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.wicresoft.util.date.DateHelper;

/**
 * @author shaoming
 *
 * 2014年11月27日   下午5:00:54
 */
public class DateUtils {
	/**
	 * 统计开始时间到结束时间之间的天数(不包括开始时间)
	 * @param start
	 * @param end
	 * @return
	 */
	public static int getTimeInterval(Date start,Date end){
		long from =  DateHelper.normalizeDate(start, ("yyyy-MM-dd")).getTime();
		long to = DateHelper.normalizeDate(end, ("yyyy-MM-dd")).getTime();
		return (int)((to-from)/ (24*60*60*1000));
	}
	
	 /**
     * 获得指定日期的前一天
     * 
     * @param specifiedDay
     * @return
     * @throws Exception
     */
    public static String getSpecifiedDayBefore(String specifiedDay) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);

        String dayBefore = new SimpleDateFormat("yyyyMMdd").format(c
                .getTime());
        return dayBefore;
    }

    /**
     * 获得指定日期的后一天
     * 
     * @param specifiedDay
     * @return
     */
    public static String getSpecifiedDayAfter(String specifiedDay) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 1);

        String dayAfter = new SimpleDateFormat("yyyyMMdd")
                .format(c.getTime());
        return dayAfter;
    }
}
