/**
 * 
 */
package com.xygame.sg.task;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author huiming
 *
 */
public class ValueLenFilter implements InputFilter {
	private int maxLen;
	public ValueLenFilter(int maxLen){
		super();
		this.maxLen = maxLen;
	}

	/* (non-Javadoc)
	 * @see android.text.InputFilter#filter(java.lang.CharSequence, int, int, android.text.Spanned, int, int)
	 */
	@Override
	public CharSequence filter(CharSequence source, int start, int end,
			Spanned dest, int dstart, int dend) {
		int destCount = dest.length();// + getChineseCount(dest.toString());
		int sourceCount = source.toString().length() + getChineseCount(source.toString());
		if(destCount + sourceCount > maxLen){
			return "";
		}
		return source;
	}
	
	private int getChineseCount(String input){
		char[] chars = input.toCharArray();
		int count = 0;
		for(int c = 0;c<chars.length;c++){
			if(isChineseChar(chars[c])){
				count++;
			}
		}
		return count;
	}
	private static final String regex = "[\\u4e00-\\u9fa5]";
	private static final Pattern chinese_pattern = Pattern.compile(regex);

	public static boolean isChineseChar(char input) {
		Matcher matcher = chinese_pattern.matcher(String.valueOf(input));
		return matcher.find();
	}

	public static boolean isChineseChar(String input) {
		Matcher matcher = chinese_pattern.matcher(String.valueOf(input));
		return matcher.find();
	}
}
