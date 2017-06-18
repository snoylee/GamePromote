package com.xygame.sg.utils;

import java.util.regex.Pattern;

public class PatternUtils {
	public static final Pattern getSujectPattern() {
		Pattern pattern = Pattern.compile(SUJECT_PATTERN);
		return pattern;
	}
	
	public static final String SPACIAL_LETTERS="[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？]";
	
	public static final String SUJECT_PATTERN="(#([\u4e00-\u9fa5]{1,}|[a-zA-Z0-9]{1,})#)|(#([\u4e00-\u9fa5]*[a-zA-Z0-9]*)#)|(#([a-zA-Z0-9]*[\u4e00-\u9fa5]*)#)|(#([a-zA-Z0-9]*[\u4e00-\u9fa5]*[a-zA-Z0-9]*)#)|(#([\u4e00-\u9fa5]*[a-zA-Z0-9]*[\u4e00-\u9fa5]*)#)";
	
	public static final String LETTER_RULES="[A-Za-z]+";
	
	public static final String INPUT_PATTERN="[0-9]{1,}|[a-z]{1,}";

	public static final String EMAIL = "([a-zA-Z0-9]|[_])+@([a-zA-Z0-9_-])+(\\.[a-zA-Z0-9_-]{2,}){1,}";
	public static final String NUMBER = "[0-9]*[0-9]{5,}";
	public static final String NUM_NUMBER = "[1-9]{1,}";
	public static final String MONEY_NUMBER = "[0-9]{1,}";
	public static final String MOBILE_PHONE = "((\\+86)|(86))?(1)[1-9]\\d{9}";
	public static final String TELE_PHONE = "(0[0-9]{2,3}(-)?)?([2-9][0-9]{6,7})+((-)?[0-9]{1,4})?";
	public static final String PHONE = "(" + TELE_PHONE + ")|(" + MOBILE_PHONE
			+ ")";
	public static final String URL = "(http://|ftp://|https://|www|WWW){1}[^\u4e00-\u9fa5\\s]*?\\.(com|net|cn|me|tw|fr|org|){1}";// [^\u4e00-\u9fa5\\s]*
																																// "[a-zA-z]+://[^\\s]*";//"[a-zA-z]+://[^\\s]+";//[a-zA-z]+://[^\s]
	public static final String DAY = "[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}";
	public static final String DATE_TIME = "[0-9]{4}-[0-9]{1,2}-[0-9]{1,2} [0-9]{1,2}:[0-9]{1,2}";
	public static final String TIME_FORMAT = "[0-9]{1,2}:[0-9]{1,2}";
	public static final String DATE_TIME_TWO = "[0-9]{4}.[0-9]{1,2}.[0-9]{1,2} [0-9]{1,2}:[0-9]{1,2}";
	public static final String DATE_TIME_THREE = "[0-9]{4}.[0-9]{1,2}.[0-9]{1,2}";

	public static final String DAY_FORMAT = "yyyy-MM-dd";
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd kk:mm";
	public static final String TIME_ONE = "[0-9]{4}/[0-9]{1,2}/[0-9]{1,2}";
	public static final String TIME_TWO = "[0-9]{4}/[0-9]{1,2}/[0-9]{1,2} [0-9]{1,2}:[0-9]{1,2}";
	

	public static final String NO_RULES_NUMBER = "\\+([0-9]{1,})";

	public static final String LOGN_NUMBER = "(" + "[0-9]{1,}" + ")|(" + PHONE
			+ ")";

	public static final String PHONE_NUMBER="(" + PHONE + ")|("
			+ NUMBER + ")";
	

	public static final Pattern getEmailPattern() {
		Pattern pattern = Pattern.compile(EMAIL);
		return pattern;
	}

	public static final Pattern getNumberPattern() {
		Pattern pattern = Pattern.compile(NUMBER);
		return pattern;
	}
	
	public static final Pattern getPoneNumberPattern() {
		Pattern pattern = Pattern.compile(PHONE_NUMBER);
		return pattern;
	}

	public static final Pattern getMobilePhonePattern() {
		Pattern pattern = Pattern.compile(MOBILE_PHONE);
		return pattern;
	}

	public static final Pattern getTelePhonePattern() {
		Pattern pattern = Pattern.compile(TELE_PHONE);
		return pattern;
	}

	public static final Pattern getPhonePattern() {
		Pattern pattern = Pattern.compile(PHONE);
		return pattern;
	}

	public static final Pattern getUrlPattern() {
		Pattern pattern = Pattern.compile(URL);
		return pattern;
	}

	public static final Pattern getDatetime() {
		Pattern pattern = Pattern.compile(DATE_TIME);
		return pattern;
	}
}
