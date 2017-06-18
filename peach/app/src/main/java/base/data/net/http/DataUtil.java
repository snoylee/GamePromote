/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package base.data.net.http;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author minhua
 */
public class DataUtil {

    public static void mergeHashMap(Collection mapList, String[] keykeys, String[] valuekeys) {
        if (keykeys.length != valuekeys.length) {
            throw new IllegalStateException("argument couple length unmatched");
        }
        Map<String, String> keykey_valuekey = new HashMap<String, String>();
        for (int i = 0; i < keykeys.length; i++) {
            keykey_valuekey.put(keykeys[i], valuekeys[i]);
        }
        for (Object map : mapList) {
            Map record = ((Map) map);
            Map copy = new HashMap();
            copy.putAll(record);
            extract(copy, record, keykey_valuekey);
        }
    }

    public static void extract(Map record, Map putin, Map<String, String> keykey_valuekey) {
        Set set = record.keySet();
        for (Object o : set) {
            Object value = record.get(o);
            if (value instanceof List) {
                for (Object m : ((List) value)) {
                    if (m instanceof Map) {
                        Set<String> keySet = keykey_valuekey.keySet();
                        for (String keyskey : keySet) {
                            if (((Map) m).containsKey(keyskey)) {
                                putin.put(((Map) m).get(keyskey), ((Map) m).get(keykey_valuekey.get(keyskey)));

                            }
                        }
                        extract((Map) m, putin, keykey_valuekey);
                    }

                }
            }
        }
    }
}
