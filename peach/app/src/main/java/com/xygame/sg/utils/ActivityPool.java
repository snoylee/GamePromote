package com.xygame.sg.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import android.app.Activity;

/**
 * Created by minhua on 2015/11/14.
 */
public class ActivityPool {
    public static Map<Class,Activity> map = new HashMap<Class,Activity>();

    public static Map<Class, Activity> getMap() {
        return map;
    }
    public static Map<Class, Activity> put(Activity activity) {
        map.put(activity.getClass(),activity);
        return map;
    }
}
