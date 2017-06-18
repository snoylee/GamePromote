package base.action.task;

import android.widget.Toast;

import java.util.List;

import base.action.Action;
import base.action.Task;

/**
 * Created by minhua on 2015/11/10.
 */
public class Check extends Task {
    @Override
    public Object run(String methodname, List<String> params, Action.Param param) {
        for(String msg: params){
            String[] m = msg.split(":");
            if(m[0]==null||m[0].trim().equals("")||m[0].equals("null")){
                Toast.makeText(param.getActivity(),m[1],Toast.LENGTH_SHORT).show();
                return null;
            }

        }
        return super.run(methodname, params, param);
    }
}
