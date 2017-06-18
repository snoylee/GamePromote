package com.xygame.second.sg.utils;

import android.app.NotificationManager;
import android.content.Context;

/**
 * Created by tony on 2016/7/26.
 */
public class NotificationManagerHelper {

    private static NotificationManagerHelper instance;
    private NotificationManager notificationManager;
    private NotificationManagerHelper(Context context){
        notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
    }

    public static NotificationManagerHelper getInstance(Context context){
        if (instance==null){
            synchronized (NotificationManagerHelper.class){
                if (instance==null){
                    instance=new NotificationManagerHelper(context);
                }
            }
        }
        return instance;
    }

    public NotificationManager getNotificationManager(){
        return notificationManager;
    }
}
