package base.demo;

import java.util.List;

import base.action.Action.Param;
import base.action.Task;

/**
 * Created by minhua on 2015/10/28.
 */
public class Add extends Task {
    @Override
    public Object run(String methodname, List<String> params, Param param) {
        int i = Integer.parseInt(params.get(0));
        i++;
        return i+"";
    }
}
