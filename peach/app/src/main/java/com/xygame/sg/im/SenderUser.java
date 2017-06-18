package com.xygame.sg.im;

import android.content.Context;
import android.text.TextUtils;

import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;

import org.json.JSONObject;

/**
 * Created by tony on 2016/1/14.
 */
public class SenderUser {

    public  static String getFromUserJsonStr(Context context){
        String info=null;
        try{
            JSONObject obj=new JSONObject();
            obj.put("userId", UserPreferencesUtil.getUserId(context));
            obj.put("userIcon", UserPreferencesUtil.getHeadPic(context));
            obj.put("usernick", UserPreferencesUtil.getUserNickName(context));
            info=obj.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return  info;
    }

    public static String getToUserJsonStr(String userId,String userIcon,String nickName){
        String info=null;
        try{
            JSONObject obj=new JSONObject();
            obj.put("userId", userId);
            obj.put("userIcon", userIcon);
            obj.put("usernick",nickName);
            info=obj.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return  info;
    }

    public  static  String getFromUserIcon(SGNewsBean item,Context context){
        String info=null;
        try{
            JSONObject obj=new JSONObject(item.getFromUser());
            info= StringUtils.getJsonValue(obj,"userIcon");
        }catch (Exception e){
            e.printStackTrace();
        }
        return  info;
    }

    public  static  String getFromUserId(SGNewsBean item,Context context){
        String info=null;
        try{
            JSONObject obj=new JSONObject(item.getFromUser());
            info=StringUtils.getJsonValue(obj, "userId");
        }catch (Exception e){
            e.printStackTrace();
        }
        return  info;
    }

    public  static  String getFromUserName(SGNewsBean item,Context context){
        String info=null;
        try{
            JSONObject obj=new JSONObject(item.getFromUser());
            info=StringUtils.getJsonValue(obj, "usernick");
        }catch (Exception e){
            e.printStackTrace();
        }
        return  info;
    }

    public  static  String getToUserIcon(SGNewsBean item,Context context){
        String info=null;
        try{
            if (!TextUtils.isEmpty(item.getToUser())){
                JSONObject obj=new JSONObject(item.getToUser());
                info=StringUtils.getJsonValue(obj, "userIcon");
            }else{
                info="";
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return  info;
    }

    public  static  String getToUserId(SGNewsBean item,Context context){
        String info=null;
        try{
            if (!TextUtils.isEmpty(item.getToUser())){
                JSONObject obj=new JSONObject(item.getToUser());
                info=StringUtils.getJsonValue(obj, "userId");
            }else{
                info="";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  info;
    }

    public  static  String getToUserName(SGNewsBean item,Context context){
        String info=null;
        try{
            if (!TextUtils.isEmpty(item.getToUser())){
                JSONObject obj=new JSONObject(item.getToUser());
                info=StringUtils.getJsonValue(obj, "usernick");
            }else{
                info="";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  info;
    }
}
