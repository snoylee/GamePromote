package com.xygame.sg.task.comm;

import android.widget.Toast;

import com.xygame.sg.bean.comm.ModelStyleBean;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.NetWorkUtil;
import com.xygame.sg.utils.SGApplication;

import java.util.List;
import java.util.Map;

import base.action.Action;

/**
 * Created by xy on 2015/11/21.
 * 查询职业类型
 */
public class QueryModelStyleType extends NetWorkUtil {
    @Override
    public Object run(String methodname, List<String> params, Action.Param param) {
        return super.run(methodname, params, param);
    }

    @Override
    public void callback(Action.Param aparam, Object object) {
        super.callback(aparam, object);
        String resultCode = aparam.getResultunit().getRawMap().get("success");
        if (Constants.RESULT_CODE.equals(resultCode)) {
            List<Map> resultList=(List) aparam.getResultunit().get("record");
            //将拍摄类型缓存到全局变量
            if (SGApplication.getInstance().getTypeList()!=null){
//                String jsonStr = JSON.toJSONString(aparam.getResultunit().get("record"));
//                SGApplication.setModelTypeList(JSON.parseArray(jsonStr, ModelStyleBean.class));
            }
        } else {
            String msg=aparam.getResultunit().getRawMap().get("msg");
            Toast.makeText(aparam.getActivity(), msg, Toast.LENGTH_SHORT).show();
        }
        super.callback(aparam, object);
    }

    @Override
    public String runUrl(String url) {
        System.out.println(url);
        return super.runUrl(url);
    }

    @Override
    public String runResult(String result) {
        System.out.println(result);
        return super.runResult(result);
    }
}
