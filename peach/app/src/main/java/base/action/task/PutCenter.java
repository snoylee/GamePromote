package base.action.task;

import java.util.List;

import base.action.Action;
import base.action.CenterRepo;
import base.action.Task;

/**
 * Created by minhua on 2015/11/24.
 */
public class PutCenter extends Task {
    @Override
    public Object run(String methodname, List<String> params, Action.Param param) {
       CenterRepo.getInsatnce().getRepo().put(params.get(0), params.get(1));
        return super.run(methodname, params, param);
    }
}
