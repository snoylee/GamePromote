package base.demo;

import java.util.List;

import base.action.Action;
import base.action.task.Http;

/**
 * Created by minhua on 2015/10/27.
 */
public class Fetch extends Http{
    @Override
    public Object run(String methodname, List<String> params, Action.Param aparam) {
        return super.run(methodname, params, aparam);
    }
}
