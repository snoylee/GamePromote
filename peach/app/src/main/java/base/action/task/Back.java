package base.action.task;

import java.util.List;

import base.action.Action;
import base.action.Task;

/**
 * Created by minhua on 2015/11/10.
 */
public class Back extends Task {
    @Override
    public Object run(String methodname, List<String> params, Action.Param param) {
        param.getActivity().finish();
        return super.run(methodname, params, param);
    }
}
