package com.xygame.sg.define.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.biggod.bean.GodLableBean;
import com.xygame.second.sg.biggod.bean.PriceBean;
import com.xygame.second.sg.jinpai.bean.JinPaiBigTypeBean;
import com.xygame.second.sg.xiadan.activity.OrderDetailActivity;
import com.xygame.second.sg.xiadan.activity.OrderPayActivity;
import com.xygame.second.sg.xiadan.activity.waitfor.BlackMemberBean;
import com.xygame.second.sg.xiadan.bean.GodQiangDanRebackBean;
import com.xygame.second.sg.xiadan.bean.TransIfoToDetailBean;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.ButtonOneListener;
import com.xygame.sg.activity.commen.OneButtonDialog;
import com.xygame.sg.bean.comm.TimerCountBean;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.im.TimerEngine;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.XMPPUtils;
import com.xygame.sg.widget.wheel.ArrayWheelAdapter;
import com.xygame.sg.widget.wheel.OnWheelChangedListener;
import com.xygame.sg.widget.wheel.WheelView;

import org.json.JSONObject;

import java.util.List;

public class XuanGodPopView extends SGBaseActivity implements OnClickListener{

	public  CircularImage avatar_iv;
	public TextView userName, sexAge,userLable,priceValue,timerText,pingleiText;
	public ImageView sexIcon;
	public  View sex_age_bg,comfirmButton,closeLoginWel;
	private GodQiangDanRebackBean blackMemberBean;
	private ImageLoader mImageLoader;
	private JinPaiBigTypeBean currPinLeiBean;
	private String singlePriceValueStr;
	private TimerCountBean timerCountBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xuangod_popview);
		initView();
		initListers();
		initDatas();
	}

	private void initView() {
		avatar_iv = (CircularImage) findViewById(R.id.avatar_iv);
		sexIcon = (ImageView) findViewById(R.id.sexIcon);
		userName = (TextView) findViewById(R.id.userName);
		sexAge = (TextView) findViewById(R.id.sexAge);
		userLable=(TextView)findViewById(R.id.userLable);

		priceValue=(TextView)findViewById(R.id.priceValue);
		timerText=(TextView)findViewById(R.id.timerText);
		pingleiText=(TextView)findViewById(R.id.pingleiText);

		closeLoginWel = findViewById(R.id.closeLoginWel);
		comfirmButton= findViewById(R.id.comfirmButton);
		sex_age_bg=findViewById(R.id.sex_age_bg);
	}

	private void initListers() {
		closeLoginWel.setOnClickListener(this);
		comfirmButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.comfirmButton){
			if (!CalendarUtils.isPassed10Min(UserPreferencesUtil.getorderExpireTime(SGApplication.getInstance().getBaseContext()),timerCountBean.getStartTime())){
				commitOrderAction();
			}else{
				showDilog("抱歉！该订单已过期");
			}
		}else if(v.getId()==R.id.closeLoginWel){
			finish();
		}
	}

	private void showDilog(String s) {
		OneButtonDialog dialog = new OneButtonDialog(this, s, R.style.dineDialog,
						new ButtonOneListener() {

					@Override
					public void confrimListener(Dialog dialog) {
						transBeanSelect();
					}
				});
				dialog.show();
	}

	protected void transBeanSelect() {
		timerCountBean.setStartTime("0");
		TimerEngine.updateTimerBean(SGApplication.getInstance().getBaseContext(), timerCountBean);
		CacheService.getInstance().clearGodQiangDanRebackBeanCache();
		Intent intent = new Intent();
		intent.setAction(XMPPUtils.ADD_FRIEND_QEQUEST);
		sendBroadcast(intent);
		finish();
	}

	private void initDatas() {
		timerCountBean= TimerEngine.quaryTimerBeansByDuringLength(this, UserPreferencesUtil.getUserId(this), Constants.QIANGDAN_TIMER);
		mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
		blackMemberBean=(GodQiangDanRebackBean)getIntent().getSerializableExtra("bean");

		timerText.setText(CalendarUtils.getYMDHMStr(Long.parseLong(blackMemberBean.getStartTime())).concat("  ").concat(blackMemberBean.getHoldTime()).concat("小时"));
		userName.setText(blackMemberBean.getUsernick());
		List<PriceBean> fuFeiDatas= CacheService.getInstance().getCachePriceDatas(ConstTaskTag.CACHE_GROUP_ROOM_DATAS);
		if (fuFeiDatas!=null){
			for (PriceBean t:fuFeiDatas){
				if (t.getId().equals(blackMemberBean.getPriceId())){
					int value=(Integer.parseInt(t.getPrice())*Integer.parseInt(blackMemberBean.getPriceRate()))/100;
					singlePriceValueStr=String.valueOf(value);
					priceValue.setText(singlePriceValueStr);
					break;
				}
			}
		}

		List<JinPaiBigTypeBean> typeDatas= CacheService.getInstance().getCacheActDatas_(ConstTaskTag.CACHE_ACT_TYPE);
		for (JinPaiBigTypeBean it:typeDatas) {
			if (it.getId().equals(blackMemberBean.getSkillCode())) {
				currPinLeiBean=it;
				pingleiText.setText(it.getName());
				List<GodLableBean> typeLable= Constants.getGodLableDatas(it.getSubStr());
				if (typeLable!=null){
					for (GodLableBean its:typeLable){
						if (its.getTitleId().equals(blackMemberBean.getSkillTitle())){
							userLable.setText(its.getTitleName());
							break;
						}
					}
				}
				break;
			}
		}

		String sexStr = blackMemberBean.getGender();
		if (Constants.SEX_WOMAN.equals(sexStr)) {
			sex_age_bg.setBackgroundResource(R.drawable.sex_bg);
			sexIcon.setImageResource(R.drawable.sg_woman_light_icon);
		} else if (Constants.SEX_MAN.equals(sexStr)) {
			sexIcon.setImageResource(R.drawable.sg_man_light_icon);
			sex_age_bg.setBackgroundResource(R.drawable.sex_male_bg);
		}
		mImageLoader.loadImage(blackMemberBean.getUserIcon(), avatar_iv, true);
	}

	private void commitOrderAction(){
		try {
			RequestBean item = new RequestBean();
			JSONObject obj = new JSONObject();
			obj.put("orderId", blackMemberBean.getOrderId());
			obj.put("inviteUserId", blackMemberBean.getUserId());
			ShowMsgDialog.showNoMsg(this, false);
			item.setData(obj);
			item.setServiceURL(ConstTaskTag.QUEST_COMMIT_ORDER_QIANG);
			ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_COMMIT_ORDER_QIANG);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}


	@Override
	protected void getResponseBean(ResponseBean data) {
		super.getResponseBean(data);
		switch (data.getPosionSign()) {
			case ConstTaskTag.QUERY_COMMIT_ORDER_QIANG:
				if ("0000".equals(data.getCode())) {
					showComfirmDialog("订单已提交成功，请在"+UserPreferencesUtil.getpayExpireTime(SGApplication.getInstance().getBaseContext())+"分钟内完成支付操作");
				} else {
					Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
				}
				break;
		}
	}

	private void showComfirmDialog(String s){
		OneButtonDialog dialog = new OneButtonDialog(this, s, R.style.dineDialog,
				new ButtonOneListener() {

					@Override
					public void confrimListener(Dialog dialog) {
						// TODO Auto-generated method stub
						doAction();
					}
				});
		dialog.show();
	}

	private void doAction() {
		String userId=blackMemberBean.getUserId();
		String userNick=blackMemberBean.getUsernick();
		String fialDate=blackMemberBean.getStartTime();
		String curTimeNums=blackMemberBean.getHoldTime();
		String addressTextString=blackMemberBean.getAddress();
		String oralTextStr=blackMemberBean.getOral();
		String userIcon=blackMemberBean.getUserIcon();
		String totalPriceValueStr=String.valueOf(Integer.parseInt(singlePriceValueStr)*Integer.parseInt(blackMemberBean.getHoldTime()));
		Intent intent=new Intent(this,OrderPayActivity.class);
		intent.putExtra("userId",userId);
		intent.putExtra("userNick",userNick);
		intent.putExtra("currPinLeiBean",currPinLeiBean);
		intent.putExtra("startTime", fialDate);
		intent.putExtra("curTimeNums",curTimeNums);
		intent.putExtra("addressTextString",addressTextString);
		intent.putExtra("oralTextStr",oralTextStr);
		intent.putExtra("userIcon",userIcon);
		intent.putExtra("orderId",blackMemberBean.getOrderId());
		intent.putExtra("timerText",timerText.getText().toString());
		intent.putExtra("singlePriceValue",singlePriceValueStr);
		intent.putExtra("totalPriceValue",totalPriceValueStr);
		intent.putExtra("payTimeStr",String.valueOf(System.currentTimeMillis()));
		intent.putExtra("fialDate",fialDate);
		intent.putExtra("whereFrom","qiangDan");
		startActivity(intent);
		transBeanSelect();
	}
}
