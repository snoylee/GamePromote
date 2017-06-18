package com.xygame.sg.utils;

import android.text.InputFilter;
import android.text.Spanned;

import org.json.JSONObject;

/**
 * Created by xy on 2015/11/26.
 */
public class StringUtils {
    public static boolean isEmpty(String str) {
        return str == null || str.equals("null") || str.equals("");
    }

    //阿拉伯数字转中文小写？
    public static String transition(String si) {
        String str = "";
        String[] aa = {"", "十", "百", "千", "万", "十万", "百万", "千万", "亿", "十亿"};
        String[] bb = {"一", "二", "三", "四", "五", "六", "七", "八", "九"};
        char[] ch = si.toCharArray();
        int maxindex = ch.length;

        if (maxindex == 2) {
            for (int i = maxindex - 1, j = 0; i >= 0; i--, j++) {
                if (ch[j] != 48) {
                    if (j == 0 && ch[j] == 49) {
                        str += aa[i];
                    } else {
                        str += bb[ch[j] - 49] + aa[i];
                    }
                }
            }
        } else {
            for (int i = maxindex - 1, j = 0; i >= 0; i--, j++) {
                if (ch[j] != 48) {
                    str += bb[ch[j] - 49] + aa[i];
                }
            }
        }
        return str;
    }

    /**
     * 设置小数位数控制
     */
    public static InputFilter getLengthFilter() {

        InputFilter lengthfilter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                // 删除等特殊字符，直接返回   
                if ("".equals(source.toString())) {
                    return null;
                }
                String dValue = dest.toString();
                String[] splitArray = dValue.split("\\.");
                if (splitArray.length > 1) {
                    String dotValue = splitArray[1];
                    int diff = dotValue.length() + 1 - Constants.DECIMAL_DIGITS;
                    if (diff > 0) {
                        return source.subSequence(start, end - diff);
                    }
                }
                return null;
            }
        };
        return lengthfilter;
    }

    /**
     * 分转元
     *
     * @param str
     * @return
     */
    public static String getFormatMoney(String str) {
        String price = Double.parseDouble(str) / 100.0 + "";
        if (price.endsWith(".0")) {
            price = price.replace(".0", "");
        }
        return price;
    }

    public static String getZhaoMuIndex(String index) {
        String str = "";
        int j = Integer.parseInt(index);
        String[] numArray = Constants.CHARACTE_NUMS;
        if (j > numArray.length - 1) {
            str = "招募 " .concat(String.valueOf(j));
        } else {
            str = "招募".concat(numArray[j - 1]);
        }
        return str;
    }

    public static String getCupId(String cup) {
        if (StringUtils.isEmpty(cup)){
            return -1+"";
        }
        String str = "";
        if (cup.equals("A")){
            str = 1+"";
        }else if (cup.equals("B")){
            str = 2+"";
        }else if (cup.equals("C")){
            str = 3+"";
        }else if (cup.equals("D")){
            str = 4+"";
        }else if (cup.equals("E")){
            str = 5+"";
        }else if (cup.equals("F")){
            str = 6+"";
        }else if (cup.equals("G")){
            str = 7+"";
        }else if (cup.equals("H")){
            str = 8+"";
        }else if (cup.equals("I")){
            str = 9+"";
        }else if (cup.equals("J")){
            str = 10+"";
        }
        return str;
    }

    public static String getRatingComment(int starNum){
        String str = "";
        switch (starNum) {
            case 1:
                str = "很差";
                break;
            case 2:
                str = "差";
                break;
            case 3:
                str = "一般";
                break;
            case 4:
                str = "好";
                break;
            case 5:
                str = "很好";
                break;

            default:
                break;
        }
        return str;

    }

    public static String getJsonValue(JSONObject obj,String key){
        String value="";
        if (!obj.isNull(key)){
            try {
                value=obj.getString(key);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return value;
    }

    public static Integer getJsonIntValue(JSONObject obj,String key){
        int value=0;
        if (!obj.isNull(key)){
            try {
                value=obj.getInt(key);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return value;
    }

    public static Long getJsonLongValue(JSONObject obj,String key){
        long value=0;
        if (!obj.isNull(key)){
            try {
                value=obj.getLong(key);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return value;
    }
}
