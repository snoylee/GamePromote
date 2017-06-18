package base.action.task;

import java.util.List;

import base.action.Action;
import base.action.CenterRepo;
import base.action.Task;

public class Put extends Task {
    @Override
    public Object run(String methodname, List<String> params, Action.Param param) {
        param.getDataUnit().getRepo().put(params.get(0),params.get(1));
        return super.run(methodname, params, param);
    }
}
