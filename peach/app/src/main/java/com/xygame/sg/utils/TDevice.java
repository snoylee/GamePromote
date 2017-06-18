package com.xygame.sg.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;


public class TDevice {
    /**
     * 获取当前本地版本号
     *
     * @return
     */
    public static int getCurrentVersion(Context context) {
        int currentVer = 0;
        try {
            currentVer = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return currentVer;
    }

    /**
     * 获取当前本地版本名称
     *
     * @return
     */
    public static String getCurrentVersionName(Context context) {
        String currentVerName = "";
        try {
            currentVerName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return currentVerName;
    }

    /**
     * 获取IMEI
     *
     * @return
     */
    public static String getIMEI(Context context) {
        String imeiStr =((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        return imeiStr;
    }
}
