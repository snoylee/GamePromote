package com.xygame.second.sg.utils;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.duanqu.qupai.sdk.android.QupaiManager;
import com.duanqu.qupai.sdk.android.QupaiService;
import com.duanqu.qupai.utils.AppGlobalSetting;
import com.xygame.second.sg.comm.activity.MoreMusicActivity;

import java.util.UUID;

public class Contant {

    /**
     * 默认时长
     */
    public static  float DEFAULT_DURATION_LIMIT = 15;
    /**
     * 默认最小时长
     */
    public static  float DEFAULT_MIN_DURATION_LIMIT = 2;
    /**
     * 默认码率
     */
    public static  int DEFAULT_BITRATE =2000 * 1000;
    /**
     * 默认Video保存路径，请开发者自行指定
     */
//    public static  String VIDEOPATH = FileUtils.newOutgoingFilePath();
    /**
     * 默认缩略图保存路径，请开发者自行指定
     */
//    public static  String THUMBPATH =  VIDEOPATH + ".jpg";
    /**
     * 水印本地路径，文件必须为rgba格式的PNG图片
     */
    public static  String WATER_MARK_PATH ="assets://Qupai/watermark/qupai_logo.png";

    public static final String APP_KEY = "20bc294c4218982";
    public static final String APP_SECRET = "b5d5497e4f2c40d68b39adeab8393488";
    public static String tags = "tags";
    public static String description = "description";
    public static int shareType = 0; //是否公开 0公开分享 1私有(default) 公开类视频不需要AccessToken授权

//    public static String accessToken;//accessToken 通过调用授权接口得到

    public static final String space = UUID.randomUUID().toString().replace("-",""); //存储目录 建议使用uid cid之类的信息,不要写死
//    public static final String domain="http://qupai-live.s.qupai.me";//当前TEST应用的域名。该地址每个应用都不同

    public static void startRecordActivity(Context context,int backPostion){
        QupaiService qupaiService = QupaiManager
                .getQupaiService(context);
        if (qupaiService == null) {
            Toast.makeText(context, "插件没有初始化，无法获取 QupaiService",
                    Toast.LENGTH_LONG).show();
        }else{
            //是否需要更多音乐页面--如果不需要更多音乐可以干掉
            Intent moreMusic = new Intent();
            if (false) {
                moreMusic.setClass(context, MoreMusicActivity.class);
            } else {
                moreMusic = null;
            }
            qupaiService.hasMroeMusic(moreMusic);

            //引导，只显示一次，这里用SharedPreferences记录
            final AppGlobalSetting sp = new AppGlobalSetting(context);
            Boolean isGuideShow = sp.getBooleanGlobalItem(
                    "Qupai_has_video_exist_in_user_list_pref", true);

            /**
             * 建议上面的initRecord只在application里面调用一次。这里为了能够开发者直观看到改变所以可以调用多次
             */
            qupaiService.showRecordPage((Activity)context,backPostion, isGuideShow);

            sp.saveGlobalConfigItem(
                    "Qupai_has_video_exist_in_user_list_pref", false);
        }
    }
}
