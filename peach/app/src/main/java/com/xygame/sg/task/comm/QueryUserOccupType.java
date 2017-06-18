package com.xygame.sg.task.comm;

import com.xygame.sg.activity.personal.bean.CarrierBean;
import com.xygame.sg.utils.NetWorkUtil;
import com.xygame.sg.utils.SGApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import base.action.Action;

/**
 * Created by xy on 2015/11/21.
 * 查询职业类型
 */
public class QueryUserOccupType extends NetWorkUtil {
    @Override
    public Object run(String methodname, List<String> params, Action.Param param) {
        return super.run(methodname, params, param);
    }

    @Override
    public void callback(Action.Param aparam, Object object) {
        super.callback(aparam, object);
        List<Map> list = (List) ((Map) object).get("record");
        List<CarrierBean> carriers = new ArrayList<CarrierBean>();
        List<CarrierBean> modelCarriers = new ArrayList<CarrierBean>();
        List<CarrierBean> cmCarriers = new ArrayList<CarrierBean>();
        for (Map o : list) {
            CarrierBean carrierBean = new CarrierBean();
            carrierBean.setCarrierCode((String) o.get("exclusType"));
            carrierBean.setCarrierName((String) o.get("typeName"));
            carrierBean.setTypeId((String) o.get("typeId"));
            carriers.add(carrierBean);
            if (o.get("exclusType").equals("1")){
                modelCarriers.add(carrierBean);
            } else if (o.get("exclusType").equals("2")){
                cmCarriers.add(carrierBean);
            }
        }
        SGApplication.getInstance().setCarrierDatas(carriers);
        SGApplication.getInstance().setModelCarriers(modelCarriers);
        SGApplication.getInstance().setCmCarriers(cmCarriers);
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
