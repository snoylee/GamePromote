package com.xygame.sg.task.certification;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import base.action.Action;
import base.action.Task;

/**
 * Created by minhua on 2015/11/14.
 */
public class TransferCup extends Task {
    Map<String,Integer> maps = new TreeMap<String,Integer>();
    {
        maps.put("A",1);
        maps.put("B",2);
        maps.put("C",3);
        maps.put("D",4);
        maps.put("E",5);
        maps.put("F",6);
        maps.put("G",7);
        maps.put("H",8);
        maps.put("I",9);
        maps.put("J",10);

    }
    @Override
    public Object run(String methodname, List<String> params, Action.Param param) {
        return maps.get(params.get(0))+"";
    }
}
