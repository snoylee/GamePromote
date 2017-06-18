package com.xygame.sg.bean.comm;

import java.io.Serializable;

public class FeedbackDateBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String year,month,day,hour,min;
	
	private String province,city;
	
	private String type;
	
	private int flag;//日期类型
	
	private String absenceMoney;//鸽子费
	
	private String maxPeople;//最大人数
	
	private String minPeople;//最小人数
	
	private String weekendFrom,weekendTo,week;
	
	private String dateAllDesc,timeLong,formatDateStr;
	
	
	
	public String getFormatDateStr() {
		return formatDateStr;
	}

	public void setFormatDateStr(String formatDateStr) {
		this.formatDateStr = formatDateStr;
	}

	public String getDateAllDesc() {
		return dateAllDesc;
	}

	public void setDateAllDesc(String dateAllDesc) {
		this.dateAllDesc = dateAllDesc;
	}

	public String getTimeLong() {
		return timeLong;
	}

	public void setTimeLong(String timeLong) {
		this.timeLong = timeLong;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	private int weekendFromIndex,weekendToIndex;
	
	public String getWeekendFrom() {
		return weekendFrom;
	}

	public void setWeekendFrom(String weekendFrom) {
		this.weekendFrom = weekendFrom;
	}

	public String getWeekendTo() {
		return weekendTo;
	}

	public void setWeekendTo(String weekendTo) {
		this.weekendTo = weekendTo;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public int getWeekendFromIndex() {
		return weekendFromIndex;
	}

	public void setWeekendFromIndex(int weekendFromIndex) {
		this.weekendFromIndex = weekendFromIndex;
	}

	public int getWeekendToIndex() {
		return weekendToIndex;
	}

	public void setWeekendToIndex(int weekendToIndex) {
		this.weekendToIndex = weekendToIndex;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAbsenceMoney() {
		return absenceMoney;
	}

	public void setAbsenceMoney(String absenceMoney) {
		this.absenceMoney = absenceMoney;
	}

	public String getMaxPeople() {
		return maxPeople;
	}

	public void setMaxPeople(String maxPeople) {
		this.maxPeople = maxPeople;
	}

	public String getMinPeople() {
		return minPeople;
	}

	public void setMinPeople(String minPeople) {
		this.minPeople = minPeople;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public String getMin() {
		return min;
	}

	public void setMin(String min) {
		this.min = min;
	}
	
	
}
