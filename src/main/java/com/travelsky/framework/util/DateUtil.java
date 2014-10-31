package com.travelsky.framework.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


//返回的值为:23Aug13,24Aug13,25Aug13...的列表

public class DateUtil {
	
	private static Log log = LogFactory.getLog(DateUtil.class);
	
	
	public static final String SHORT_NORMAL="yyyy-MM-dd";
	public static final String LONG_NORMAL="yyyy-MM-dd HH:mm:ss";
	public static final String SHORT_US="ddMMMyy";
	public static final String SHORT_US_MIN="ddMMMyyHHmm";
	
	
	
	public static String date2USdate(String normaldate){
		SimpleDateFormat sdf_short= new SimpleDateFormat(SHORT_NORMAL);
		SimpleDateFormat sdf_short_us = new SimpleDateFormat(SHORT_US,Locale.US);
		try {
			Date date = sdf_short.parse(normaldate);
			String usdate = sdf_short_us.format(date);
			usdate=usdate.toUpperCase();
			return usdate;
		} catch (ParseException e) {
			log.info("日期"+normaldate+"转换出错!");
		}
		return "";
	}
	
	
	/**
	 * 将23OCT13------->2013-10-23
	 * @param str
	 * @return
	 */
	public static String USdate2Date(String str){
		SimpleDateFormat sdf_short= new SimpleDateFormat(SHORT_NORMAL);
		SimpleDateFormat sdf_short_us = new SimpleDateFormat(SHORT_US,Locale.US);
		Date date;
		try {
			date = sdf_short_us.parse(str);
			String newdate = sdf_short.format(date);
			return newdate;
		} catch (ParseException e) {
			log.info("英文日期转换错原字符串为:"+str);
		}
		return "";
	}
	
	/**
	 * 将英文字符串时间转换成中文字符串时间(长整形时间,年月日时分秒)
	 * @param dateStr 要转换的字符串
	 * @return
	 */
	public static String usLongDateFormat(String dateStr) {
		try {
			SimpleDateFormat sdf_short_us_min = new SimpleDateFormat(SHORT_US_MIN,Locale.US);
			Date date = sdf_short_us_min.parse(dateStr);
			SimpleDateFormat sdf_long= new SimpleDateFormat(LONG_NORMAL);
			String newdate = sdf_long.format(date);
			return newdate;
		} catch (ParseException e) {
			log.info("将日期" + dateStr + "转换为" + LONG_NORMAL + "出错");
		}
		return "";
	}
	

	/**
	 * 如果填的是10到20 那么就是第十天到第二十天的日期list 不包括第十天包括第二十天
	 * @param from
	 * @param end
	 * @return
	 */
	public  static List getDateList(int from ,int end){
		SimpleDateFormat sdf_short= new SimpleDateFormat(SHORT_NORMAL);
		List datelist = new LinkedList();
		Date date = new Date();
		long between = 24*60*60*1000;
		for(int i=from;i<end;i++){
			Date date2 = new Date(date.getTime()+between*i);
			datelist.add(sdf_short.format(date2));
		}
		return datelist;
	}
	
	
	public static List getInputDaysAsList(int last){
		SimpleDateFormat sdf_short= new SimpleDateFormat(SHORT_NORMAL);
		last++;
		List datelist = new LinkedList();
		Date date = new Date();
		//这代表一天的时间
		int between = 24 * 60 * 60 * 1000;
//		datelist.add(Date2String(date));
		for(int i=0;i<last;i++){
			date=new Date(date.getTime()+between);
			datelist.add(sdf_short.format(date));
		}
		return datelist;
	}
	/**
	 * 字符串转换为timestamp     2013-9-16 9:56:07-------->timestamp
	 * @param str
	 * @return
	 */
	public static Timestamp string2timestamp(String str){
		SimpleDateFormat sdf_long= new SimpleDateFormat(LONG_NORMAL);
		Timestamp timestamp=null;
		Date date = null;
		try {
			date = sdf_long.parse(str);
			timestamp = new Timestamp(date.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return timestamp;
	}

	/**
	 * 将"2013-9-16 09:56:07"的date转换为 2013-9-16 9:56:07的字符串
	 * @param date
	 * @return
	 */
	public static String timestamp2string(Date date){
		SimpleDateFormat sdf_long= new SimpleDateFormat(LONG_NORMAL);
		String str = sdf_long.format(date);
		return str;
	}
	
	
	
	
	/**
	 * 将date+time类型是数据转换为 2013-9-16 9:56:07的字符串
	 * @param date1
	 * @param time1
	 * @return
	 */
	public static String changeEnglishDateTONomalDate(String date1,String time1){
		String newdate = USdate2Date(date1);
		String newtime="";
		if(time1.length()==4){
			newtime=time1.substring(0, 2)+":"+time1.substring(2)+":00";
		}
		String returndate = newdate+" "+newtime;
		return returndate;
	}
	
	/**
	 * 将数据库里面得到的timestamp类型的数据转换为 2013-9-16 9:56:07的字符串
	 * @param timestamp
	 * @return
	 */
	public static List<String> getDateList(String startdate, String enddate) {
		SimpleDateFormat sdf_short= new SimpleDateFormat(SHORT_NORMAL);
		List<String> datelist = new ArrayList<String> ();
		try {
			Date start = sdf_short.parse(startdate);
			Date end = sdf_short.parse(enddate);
			Date tempdate = start;
			int between = 24 * 60 * 60 * 1000;
			while(tempdate.getTime()<end.getTime()){
					tempdate=new Date(tempdate.getTime()+between);
					datelist.add(sdf_short.format(tempdate));
			}
		} catch (ParseException e) {
			log.error("日期转换出错:"+startdate+"----------->"+enddate);
		}
		return datelist;
	}
	/**
	 * 获取days天前的日期表 如果是2,1那么返回三天前的日期
	 * @param days
	 * @return
	 */
	public static List<String> getDatesBeforeList(int end,int length) {
		SimpleDateFormat sdf_short= new SimpleDateFormat(SHORT_NORMAL);
			List datelist = new LinkedList();
			Date date = new Date();
			int between = 24*60*60*1000;
			for(int i=0;i<length;i++){
				Date date2 = new Date(date.getTime()-(i+1+end)*between);
				datelist.add(sdf_short.format(date2));
			}
			return datelist;
	}
	
	/**
	 * 得到B2BShopping执行任务查询的日期集合
	 * @author zhangli
	 * @param dayBein 从多少天后开始
	 * @param days 执行的天数
	 * @return
	 */
	public static List<String> getB2BTaskRunDays(int dayBein, int days) {
		SimpleDateFormat sdf_short= new SimpleDateFormat(SHORT_US, Locale.US);
		if(days < 1) {
			return new ArrayList<String>();
		}
		Calendar now =Calendar.getInstance();
		String nowTime = sdf_short.format(new Date());
		try {
			now.setTime(sdf_short.parse(nowTime));
			now.set(Calendar.DATE, now.get(Calendar.DATE) + dayBein);
			List<String> list = new ArrayList<String>();
			list.add(sdf_short.format(now.getTime()).toUpperCase());
			for (int i = 1; i < days; i++) {
				now.set(Calendar.DATE, now.get(Calendar.DATE) + 1);
				list.add(sdf_short.format(now.getTime()).toUpperCase());
			}
			return list;
		} catch (ParseException e) {
			log.error("B2B执行shopping日期的参数配置错误");
			return new ArrayList<String>();
		}
	}
	
	/**
	 * 获取指定时间对应的毫秒数
	 * @param time "HH:mm:ss"
	 * @return
	 */
	public static long getTimeMillis(String time) {
		SimpleDateFormat sdf_long= new SimpleDateFormat(LONG_NORMAL);
		SimpleDateFormat sdf_short= new SimpleDateFormat(SHORT_NORMAL);
			try {
				Date curDate = sdf_long.parse(sdf_short.format(new Date())+" "+time);
				return curDate.getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return 0;
	}
	
	public static String date2longNormal(Date date){
		SimpleDateFormat sdf_long= new SimpleDateFormat(LONG_NORMAL);
		return sdf_long.format(date);
	}
	
	public static void main(String[] args) {
		List<String> datesBeforeList = getB2BTaskRunDays(1,5);
		for (String string : datesBeforeList) {
			System.out.println(string);
		}
	}
	
}
