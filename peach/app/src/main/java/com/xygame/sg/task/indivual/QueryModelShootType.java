package com.xygame.sg.task.indivual;

import com.xygame.sg.activity.commen.bean.ShootTypeBean;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.NetWorkUtil;
import com.xygame.sg.utils.SGApplication;

import java.util.List;
import java.util.Map;

import base.action.Action;

/**
 * Created by xy on 2015/11/21.
 * 查询模特拍摄类型
 */
public class QueryModelShootType extends NetWorkUtil {
    @Override
    public Object run(String methodname, List<String> params, Action.Param param) {
        return super.run(methodname, params, param);
    }

    @Override
    public void callback(Action.Param aparam, Object object) {
        String resultCode = aparam.getResultunit().getRawMap().get("success");
        if (Constants.RESULT_CODE.equals(resultCode)) {
            List<Map> resultList=(List) aparam.getResultunit().get("record");

//            String jsonStr = JSON.toJSONString(aparam.getResultunit().get("record"));
//            ModelShootTypeCache.setTypeList(JSON.parseArray(jsonStr, ShootTypeBean.class));
//            ModelShootTypeCache.setTypeMapList(resultList);
//            SGApplication.getInstance().setTypeList(JSON.parseArray(jsonStr, ShootTypeBean.class));
//            SGApplication.setTypeMapList(resultList);

//            if (aparam.getFragment() instanceof NoticeFragment){
//                NoticeFragment noticeFragment = (NoticeFragment) aparam.getFragment();
//                noticeFragment.responseShootType(resultList);
//            }
//            else if (aparam.getActivity() instanceof  SelectFirstCategoryActivity){
//                SelectFirstCategoryActivity selectFirstCategoryActivity = (SelectFirstCategoryActivity) aparam.getActivity();
//                selectFirstCategoryActivity.responseHandler(resultList);
//            }
//            else if (aparam.getActivity() instanceof EditPriceActivity){
//                EditPriceActivity editPriceActivity = (EditPriceActivity) aparam.getActivity();
//                editPriceActivity.queryModelShootTypeResponseHandler(resultList);
//            }
//            else if (aparam.getFragment() instanceof CMNoticeTabFragment){
//                CMNoticeTabFragment noticeFragment = (CMNoticeTabFragment) aparam.getFragment();
//                noticeFragment.responseShootType(resultList);
//            }

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
