package com.xygame.sg.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.xygame.sg.R;

import android.content.Context;

public class TimeUtils {

	public static String formatTime(long date) {
		Calendar curTime = Calendar.getInstance();
		curTime.setTimeInMillis(date);
		int week = curTime.get(Calendar.WEEK_OF_MONTH);
		int year = curTime.get(Calendar.YEAR);
		int month = curTime.get(Calendar.MONTH);
		int day = curTime.get(Calendar.DATE);
		int hour = curTime.get(Calendar.HOUR_OF_DAY);
		int minute = curTime.get(Calendar.MINUTE);
		int second = curTime.get(Calendar.SECOND);
		int dayOfWeek = curTime.get(Calendar.DAY_OF_WEEK);
		int dayOfYear = curTime.get(Calendar.DAY_OF_YEAR);

		Calendar time = Calendar.getInstance();
		int curWeek = time.get(Calendar.WEEK_OF_MONTH);
		int curYear = time.get(Calendar.YEAR);
		int curMonth = time.get(Calendar.MONTH);
		int curDay = time.get(Calendar.DATE);
		int curHour = time.get(Calendar.HOUR_OF_DAY);
		int curMinute = time.get(Calendar.MINUTE);
		int curSecond = time.get(Calendar.SECOND);
		int curDayOfWeek = time.get(Calendar.DAY_OF_WEEK);
		int curDayOfYear = time.get(Calendar.DAY_OF_YEAR);

		if (year == curYear) {
			if (week == curWeek) {
				if (dayOfWeek == curDayOfWeek) {
					if (hour == curHour) {
						if (minute == curMinute) {
							int tempSecond=((curSecond - second) > 0 ? (curSecond - second) : -(curSecond - second));
							if (tempSecond==0){
								return "刚刚";
							}else{
								return tempSecond + "秒钟前";
							}
						} else {
							return ((curMinute - minute) > 0 ? (curMinute - minute) : -(curMinute - minute)) + "分钟前";
						}
					} else {
						return ((curHour - hour) > 0 ? (curHour - hour) : -(curHour - hour)) + "小时前";
					}
				} else {
					return formatDayOfWeek(dayOfWeek);
				}
			} else {
				return year + "-" + (month + 1) + "-" + day;
			}
		} else {
			return year + "-" + (month + 1) + "-" + day;
		}
	}

	public static String formatMsgTime(final long date, Context context, boolean flag) {
		Calendar curTime = Calendar.getInstance();
		curTime.setTimeInMillis(date);
		int week = curTime.get(Calendar.WEEK_OF_MONTH);
		int dayOfYear = curTime.get(Calendar.DAY_OF_YEAR);
		Calendar time = Calendar.getInstance();
		int curWeek = time.get(Calendar.WEEK_OF_MONTH);
		int curDayOfYear = time.get(Calendar.DAY_OF_YEAR);

		String timerStr = "";
		String[] weeks = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		SimpleDateFormat df_sameDayDifferentHour = new SimpleDateFormat("yyyy年MM月dd日");
		SimpleDateFormat df_DifferentDay = new SimpleDateFormat("MM-dd");
		SimpleDateFormat df_DifferentYear = new SimpleDateFormat("yyyy");
		SimpleDateFormat df_DifferentHour = new SimpleDateFormat("HH:mm");
		Date mDate = new Date(date);
		Date currtDate = new Date();

		String currtYear = df_DifferentYear.format(currtDate);
		String mYear = df_DifferentYear.format(mDate);
		if (currtYear.equals(mYear)) {
			String currtForDay = df_DifferentDay.format(currtDate);
			String mForDay = df_DifferentDay.format(mDate);
			if (currtForDay.equals(mForDay)) {
				String mHour = df_DifferentHour.format(mDate);
				String hour = mHour.substring(0, 2);
				String min = mHour.substring(2, mHour.length());
				if (Integer.parseInt(hour) <= 12) {
					timerStr = "今天上午".concat(mHour);
				} else {
					switch (Integer.parseInt(hour)) {
					case 13:
						timerStr = "今天下午01".concat(min);
						break;
					case 14:
						timerStr = "今天下午02".concat(min);
						break;
					case 15:
						timerStr = "今天下午03".concat(min);
						break;
					case 16:
						timerStr = "今天下午04".concat(min);
						break;
					case 17:
						timerStr = "今天下午05".concat(min);
						break;
					case 18:
						timerStr = "今天下午06".concat(min);
						break;
					case 19:
						timerStr = "今天下午07".concat(min);
						break;
					case 20:
						timerStr = "今天下午08".concat(min);
						break;
					case 21:
						timerStr = "今天下午09".concat(min);
						break;
					case 22:
						timerStr = "今天下午10".concat(min);
						break;
					case 23:
						timerStr = "今天下午11".concat(min);
						break;
					case 24:
						timerStr = "今天下午12".concat(min);
						break;
					default:
						break;
					}
				}
			} else {
				if (week == curWeek) {
					if (dayOfYear == curDayOfYear - 1) {
						timerStr = context.getResources().getString(R.string.yesterday);
					} else if (dayOfYear == curDayOfYear - 2) {
						timerStr = context.getResources().getString(R.string.reyesterday);
					} else {
						long twoDays = getTwoDay(mDate, currtDate);
						if (twoDays < 7) {
							Calendar cal = Calendar.getInstance();
							cal.setTime(mDate);
							int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
							if (week_index < 0) {
								week_index = 0;
							}
							timerStr = weeks[week_index];
						}
					}
				} else {
					timerStr = new SimpleDateFormat("yyyy年MM月dd日").format(mDate);
				}
			}
		} else {
			timerStr = df_sameDayDifferentHour.format(mDate);
		}

		return timerStr;
	}

	public static long getTwoDay(Date begin_date, Date end_date) {
		long day = 0;
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String sdate = format.format(Calendar.getInstance().getTime());

			if (begin_date == null) {
				begin_date = format.parse(sdate);
			}
			if (end_date == null) {
				end_date = format.parse(sdate);
			}
			day = (end_date.getTime() - begin_date.getTime()) / (24 * 60 * 60 * 1000);
		} catch (Exception e) {
			return -1;
		}
		return day;
	}

	private static String formatDayOfWeek(int dayOfWeek) {
		String dayOfWeekString = "";
		switch (dayOfWeek) {
		case 1:
			dayOfWeekString = SGApplication.getInstance().getResources().getString(R.string.sunday);
			break;
		case 2:
			dayOfWeekString = SGApplication.getInstance().getResources().getString(R.string.monday);
			break;
		case 3:
			dayOfWeekString = SGApplication.getInstance().getResources().getString(R.string.tuesday);
			break;
		case 4:
			dayOfWeekString = SGApplication.getInstance().getResources().getString(R.string.wednesday);
			break;

		case 5:
			dayOfWeekString = SGApplication.getInstance().getResources().getString(R.string.thursday);
			break;
		case 6:
			dayOfWeekString = SGApplication.getInstance().getResources().getString(R.string.friday);
			break;
		case 7:
			dayOfWeekString = SGApplication.getInstance().getResources().getString(R.string.saturday);
			break;
		default:
			break;
		}
		return dayOfWeekString;
	}

	public static boolean isLeapYear(int year) {
		return ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0);
	}

	public static String nonOrAfter(final long date, Context context) {
		SimpleDateFormat df_DifferentHour = new SimpleDateFormat("HH:mm");
		String timerStr = "";
		Date mDate = new Date(date);
		String mHour = df_DifferentHour.format(mDate);
		String hour = mHour.substring(0, 2);
		String min = mHour.substring(2, mHour.length());
		if (Integer.parseInt(hour) <= 12) {
			timerStr = "上午".concat(mHour);
		} else {
			switch (Integer.parseInt(hour)) {
			case 13:
				timerStr = "下午01".concat(min);
				break;
			case 14:
				timerStr = "下午02".concat(min);
				break;
			case 15:
				timerStr = "下午03".concat(min);
				break;
			case 16:
				timerStr = "下午04".concat(min);
				break;
			case 17:
				timerStr = "下午05".concat(min);
				break;
			case 18:
				timerStr = "下午06".concat(min);
				break;
			case 19:
				timerStr = "下午07".concat(min);
				break;
			case 20:
				timerStr = "下午08".concat(min);
				break;
			case 21:
				timerStr = "下午09".concat(min);
				break;
			case 22:
				timerStr = "下午10".concat(min);
				break;
			case 23:
				timerStr = "下午11".concat(min);
				break;
			case 24:
				timerStr = "下午12".concat(min);
				break;
			default:
				break;
			}
		}
		return timerStr;
	}

	public static String logTimeShow(final long date, Context context) {
		Calendar curTime = Calendar.getInstance();
		curTime.setTimeInMillis(date);
		int week = curTime.get(Calendar.WEEK_OF_MONTH);
		int dayOfYear = curTime.get(Calendar.DAY_OF_YEAR);
		Calendar time = Calendar.getInstance();
		int curWeek = time.get(Calendar.WEEK_OF_MONTH);
		int curDayOfYear = time.get(Calendar.DAY_OF_YEAR);

		String timerStr = "";
		String[] weeks = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		SimpleDateFormat df_sameDayDifferentHour = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat df_DifferentDay = new SimpleDateFormat("MM-dd");
		SimpleDateFormat df_DifferentYear = new SimpleDateFormat("yyyy");
		SimpleDateFormat df_DifferentHour = new SimpleDateFormat("HH:mm");
		Date mDate = new Date(date);
		Date currtDate = new Date();

		String currtYear = df_DifferentYear.format(currtDate);
		String mYear = df_DifferentYear.format(mDate);
		if (currtYear.equals(mYear)) {
			String currtForDay = df_DifferentDay.format(currtDate);
			String mForDay = df_DifferentDay.format(mDate);
			if (currtForDay.equals(mForDay)) {
				String mHour = df_DifferentHour.format(mDate);
				String hour = mHour.substring(0, 2);
				String min = mHour.substring(2, mHour.length());
				if (Integer.parseInt(hour) <= 12) {
					timerStr = "上午".concat(mHour);
				} else {
					switch (Integer.parseInt(hour)) {
					case 13:
						timerStr = "下午01".concat(min);
						break;
					case 14:
						timerStr = "下午02".concat(min);
						break;
					case 15:
						timerStr = "下午03".concat(min);
						break;
					case 16:
						timerStr = "下午04".concat(min);
						break;
					case 17:
						timerStr = "下午05".concat(min);
						break;
					case 18:
						timerStr = "下午06".concat(min);
						break;
					case 19:
						timerStr = "下午07".concat(min);
						break;
					case 20:
						timerStr = "下午08".concat(min);
						break;
					case 21:
						timerStr = "下午09".concat(min);
						break;
					case 22:
						timerStr = "下午10".concat(min);
						break;
					case 23:
						timerStr = "下午11".concat(min);
						break;
					case 24:
						timerStr = "下午12".concat(min);
						break;
					default:
						break;
					}
				}
			} else {
				if (week == curWeek) {
					if (dayOfYear == curDayOfYear - 1) {
						timerStr = context.getResources().getString(R.string.yesterday)
								.concat(nonOrAfter(date, context));
					} else if (dayOfYear == curDayOfYear - 2) {
						timerStr = context.getResources().getString(R.string.reyesterday)
								.concat(nonOrAfter(date, context));
					} else {
						long twoDays = getTwoDay(mDate, currtDate);
						if (twoDays < 7) {
							Calendar cal = Calendar.getInstance();
							cal.setTime(mDate);
							int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
							if (week_index < 0) {
								week_index = 0;
							}
							timerStr = weeks[week_index];
						}
					}
				} else {
					timerStr = new SimpleDateFormat("yyyy-MM-dd").format(mDate);
				}
			}
		} else {
			timerStr = df_sameDayDifferentHour.format(mDate);
		}

		return timerStr;
	}

	public static String detailTimeShow(final long date, Context context) {
		Calendar curTime = Calendar.getInstance();
		curTime.setTimeInMillis(date);
		int week = curTime.get(Calendar.WEEK_OF_MONTH);
		int dayOfYear = curTime.get(Calendar.DAY_OF_YEAR);
		Calendar time = Calendar.getInstance();
		int curWeek = time.get(Calendar.WEEK_OF_MONTH);
		int curDayOfYear = time.get(Calendar.DAY_OF_YEAR);

		String timerStr = "";
		String[] weeks = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		SimpleDateFormat df_sameDayDifferentHour = new SimpleDateFormat("yyyy年MM月dd日");
		SimpleDateFormat df_DifferentDay = new SimpleDateFormat("MM-dd");
		SimpleDateFormat df_DifferentYear = new SimpleDateFormat("yyyy");
		SimpleDateFormat df_DifferentHour = new SimpleDateFormat("HH:mm");
		Date mDate = new Date(date);
		Date currtDate = new Date();

		String currtYear = df_DifferentYear.format(currtDate);
		String mYear = df_DifferentYear.format(mDate);
		if (currtYear.equals(mYear)) {
			String currtForDay = df_DifferentDay.format(currtDate);
			String mForDay = df_DifferentDay.format(mDate);
			if (currtForDay.equals(mForDay)) {
				String mHour = df_DifferentHour.format(mDate);
				String hour = mHour.substring(0, 2);
				String min = mHour.substring(2, mHour.length());
				if (Integer.parseInt(hour) <= 12) {
					timerStr = "上午".concat(mHour);
				} else {
					switch (Integer.parseInt(hour)) {
					case 13:
						timerStr = "下午01".concat(min);
						break;
					case 14:
						timerStr = "下午02".concat(min);
						break;
					case 15:
						timerStr = "下午03".concat(min);
						break;
					case 16:
						timerStr = "下午04".concat(min);
						break;
					case 17:
						timerStr = "下午05".concat(min);
						break;
					case 18:
						timerStr = "下午06".concat(min);
						break;
					case 19:
						timerStr = "下午07".concat(min);
						break;
					case 20:
						timerStr = "下午08".concat(min);
						break;
					case 21:
						timerStr = "下午09".concat(min);
						break;
					case 22:
						timerStr = "下午10".concat(min);
						break;
					case 23:
						timerStr = "下午11".concat(min);
						break;
					case 24:
						timerStr = "下午12".concat(min);
						break;
					default:
						break;
					}
				}
			} else {
				if (week == curWeek) {
					if (dayOfYear == curDayOfYear - 1) {
						timerStr = context.getResources().getString(R.string.yesterday)
								.concat(nonOrAfter(date, context));
					} else if (dayOfYear == curDayOfYear - 2) {
						timerStr = context.getResources().getString(R.string.reyesterday)
								.concat(nonOrAfter(date, context));
					} else {
						long twoDays = getTwoDay(mDate, currtDate);
						if (twoDays < 7) {
							Calendar cal = Calendar.getInstance();
							cal.setTime(mDate);
							int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
							if (week_index < 0) {
								week_index = 0;
							}
							timerStr = weeks[week_index];
						}
					}
				} else {
					timerStr = new SimpleDateFormat("yyyy年MM月dd日").format(mDate);
				}
			}
		} else {
			timerStr = df_sameDayDifferentHour.format(mDate);
		}

		return timerStr;
	}
}
