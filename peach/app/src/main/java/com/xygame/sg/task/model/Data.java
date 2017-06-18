package com.xygame.sg.task.model;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import base.frame.VisitUnit;

/**
 * Created by minhua on 2015/11/28.
 */
public class Data {
    public static VisitUnit filter = new VisitUnit(true);
    public static List cat;
    public static List styleMale;
    public static List styleFemale;

    public static VisitUnit getSort() {

        List<TreeMap<String, String>> list = new ArrayList<TreeMap<String, String>>();
        TreeMap<String, String> map = new TreeMap<String, String>();
        map.put("sort_item_name", "默认排序");
        map.put("ntelligentSort", "1");
        map.put("hook","visibility-gone");
        list.add(map);

        map = new TreeMap<String, String>();
        map.put("sort_item_name", "价格最高");
        map.put("ntelligentSort", "2");
        map.put("hook","visibility-gone");
        list.add(map);
        map = new TreeMap<String, String>();
        map.put("sort_item_name", "价格最低");
        map.put("ntelligentSort", "3");
        map.put("hook","visibility-gone");
        list.add(map);
        map = new TreeMap<String, String>();
        map.put("sort_item_name", "接单量最多");
        map.put("ntelligentSort", "4");
        map.put("hook","visibility-gone");
        list.add(map);
        VisitUnit visitUnit = Data.all;
        visitUnit.getDataUnit().getRepo().put("sort_list", list);
        return visitUnit;
    }

    public static VisitUnit all;



}
