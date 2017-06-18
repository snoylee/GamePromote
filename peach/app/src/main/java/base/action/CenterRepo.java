package base.action;

import java.util.Map;
import java.util.TreeMap;

import base.frame.DataUnit;

/**
 * Created by minhua on 2015/10/26.
 */
public class CenterRepo extends DataUnit {
    private static CenterRepo centerRepo = new CenterRepo();

    public static CenterRepo getInsatnce() {
        return centerRepo;
    }
}
