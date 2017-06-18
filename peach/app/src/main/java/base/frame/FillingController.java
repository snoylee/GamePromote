package base.frame;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import base.action.CenterRepo;
import base.adapter.Logs;

/**
 * Created by minhua on 2015/10/26.
 */
public class FillingController {
    public static ParentFragment getShowfragment() {
        return showfragment;
    }

    public static void setShowfragment(ParentFragment showfragment) {
        FillingController.showfragment = showfragment;
    }

    public static ParentFragment showfragment;

    public static void parseParentFragmentFillingArea(VisitUnit visitUnit, Object onetimedata) {
        Map<String,Object> addeddata = new TreeMap<String, Object>();
        if(onetimedata instanceof Map){
            Map map = ((Map)onetimedata);
            Set kys = new TreeSet(map.keySet());
            for(Object str:kys){

                String key = str.toString();
                Object value =  map.get(str.toString());
                putinData(addeddata, map, key, value);
            }

        }else if(onetimedata instanceof List){
            List map = ((List)onetimedata);
//            putinData(addeddata, map, key, value);
        }
        //key name hole
        long t = System.currentTimeMillis();

        visitUnit.putFillsignarea(t+"",addeddata.keySet());
        visitUnit.putDisplayKeyValue(addeddata);
        visitUnit.getDataUnit().putinRepo(addeddata);

        visitUnit.putValues(t+"");
    }

    public static void putinData(Map<String, Object> addeddata, Map map, String key, Object value) {
        if(value instanceof Map){
            Map map2 = ((Map)value);
            Set kys2 = new TreeSet(map2.keySet());
            for(Object str2:kys2) {

                String key2 = str2.toString();
                Object value2 = map2.get(str2.toString());
                putinData(addeddata,map2,key2,value2);
            }
        }else if(value instanceof List){
            List list = ((List)value);
            addeddata.put(key,list);
        }else{
            Logs.i("---------- "+key);
            if(value==null){
                value="null";
            }
                addeddata.put(key,value.toString());
        }
    }

}
