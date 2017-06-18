package com.xygame.sg.activity.notice.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.xygame.sg.R;
import com.xygame.sg.activity.commen.ButtonTwoListener;
import com.xygame.sg.activity.commen.TwoButtonDialog;
import com.xygame.sg.activity.notice.NoticePaymentActivity;
import com.xygame.sg.activity.notice.ZhuiJiaPaymentActivity;
import com.xygame.sg.activity.notice.bean.ModelRequestBean;
import com.xygame.sg.activity.notice.bean.NoticeDetailVo;
import com.xygame.sg.activity.notice.bean.NoticeItemBean;
import com.xygame.sg.activity.notice.bean.NoticeMemberUpdateBean;
import com.xygame.sg.activity.notice.bean.NoticeMemberUpdateFirstVo;
import com.xygame.sg.activity.notice.bean.NoticeRecruitVo;
import com.xygame.sg.activity.notice.bean.PlushNoticeBean;
import com.xygame.sg.activity.notice.bean.ZhuiJiaBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xy on 2015/12/31.
 */
public class UIHelper {
    public static void toFirstPay(final Activity context,final NoticeDetailVo notice) {
        TwoButtonDialog dialog=new TwoButtonDialog(context, "您的通告还未付款请先付款！","确定","取消", R.style.dineDialog,new ButtonTwoListener() {
            @Override
            public void confrimListener() {

                PlushNoticeBean pnBean = new PlushNoticeBean();
                pnBean.setNoticeId(notice.getNoticeId()+"");
                pnBean.setCameraTheme(notice.getSubject());
                pnBean.setStarTime(notice.getShoot().getStartTime().getTime()+"");
                pnBean.setEndTime(notice.getShoot().getEndTime().getTime()+"");
                List<ModelRequestBean> modelDatas = new ArrayList<ModelRequestBean>();
                for (int j = 0; j < notice.getRecruits().size(); j++) {
                    NoticeRecruitVo it = notice.getRecruits().get(j);
                    ModelRequestBean mItem = new ModelRequestBean();
                    if ("1".equals(it.getGender()+"")) {
                        mItem.setSexId("1");
                        mItem.setSexName("男");
                    } else if ("0".equals(it.getGender()+"")) {
                        mItem.setSexId("0");
                        mItem.setSexName("女");
                    }
                    mItem.setNeedPrice(it.getReward()+"");
                    mItem.setNeedNum(it.getCount()+"");
                    modelDatas.add(mItem);
                }
                pnBean.setModelBeans(modelDatas);
                Intent intent = new Intent(context, NoticePaymentActivity.class);
                intent.putExtra("payforchat",true);
                intent.putExtra("bean", pnBean);
                intent.putExtra("strFlag", "fromNoticeManagment");
                context.startActivityForResult(intent, 0);
            }
            @Override
            public void cancelListener() {
            }
        });

        dialog.show();

    }

    public static void toAddPay(final Activity context,final NoticeMemberUpdateFirstVo memberUpdateFirstVo,final NoticeMemberUpdateBean noticeMemberUpdateBean) {
        TwoButtonDialog dialog=new TwoButtonDialog(context, "您的预付款金额不足，请先追加预付款！","确定","取消", R.style.dineDialog,new ButtonTwoListener() {
            @Override
            public void confrimListener() {
                Intent intent = new Intent(context, ZhuiJiaPaymentActivity.class);
                ZhuiJiaBean zjBean = new ZhuiJiaBean();
                String memIds = "";
                for (Long memId : noticeMemberUpdateBean.getMemIds()){
                    memIds+=memId+",";
                }
                memIds.substring(0,memIds.length()-1);
                zjBean.setMemIds(memIds);
                zjBean.setBenCiLuYunRen(noticeMemberUpdateBean.getMemIds().size() + "");
                zjBean.setYiPaiSheRen(memberUpdateFirstVo.getShootCount() + "");
                zjBean.setYiLuYunRen(memberUpdateFirstVo.getFrozenCount() + "");
                zjBean.setStatus(noticeMemberUpdateBean.getStatus() + "");
                zjBean.setYuFuMoney(memberUpdateFirstVo.getParpayAmount() + "");
                zjBean.setXuDongJIeMoney(memberUpdateFirstVo.getAddAmount() + "");
                zjBean.setYiJieSuanMoney(memberUpdateFirstVo.getIncomeAmount() + "");
                zjBean.setDaiDongJieMoney(memberUpdateFirstVo.getFrozenAmount()+"");
                intent.putExtra("bean",zjBean);
                context.startActivityForResult(intent, 1);

            }
            @Override
            public void cancelListener() {
            }
        });

        dialog.show();

    }
}
