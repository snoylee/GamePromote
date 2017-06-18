package com.xygame.sg.activity.personal;

import java.util.Map;

import com.xygame.second.sg.personal.activity.VideoCertifyActivity;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.notice.MingXiActivity;
import com.xygame.sg.activity.notice.MyComposeActivity;
import com.xygame.sg.activity.notice.bean.QianBaoBean;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.StringUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import base.ViewBinder;
import base.action.Action;
import base.frame.VisitUnit;

public class QianBaoActivity extends SGBaseActivity implements OnClickListener{
	/**
	 * 公用变量部分
	 */
	private TextView titleName,leftMoney,setPwd;
	private View backButton,ZhiFuMiMaView,youHuiView,mingXiView,tiXianView,chongZhiView,zuanShiView;
	private QianBaoBean qBean;
	private boolean isSetPwd=false,isHidMoneyFlag=false;
	private ImageView isHidMoney;
	private void updateAccountView(){
		if(isHidMoneyFlag){
			isHidMoney.setImageResource(R.drawable.qb_open_eyes);
			leftMoney.setText("￥".concat(String.valueOf(StringUtil.getPrice(Long.parseLong(qBean.getAmount())))));
		}else{
			isHidMoney.setImageResource(R.drawable.qb_close_eyes);
			leftMoney.setText("******");
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		VisitUnit visitUnit = new VisitUnit(this);
		setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.qianbao_layout, null));
		initViews();
		initListensers();
		initDatas();
	}

	private void initViews() {
		zuanShiView=findViewById(R.id.zuanShiView);
		chongZhiView=findViewById(R.id.chongZhiView);
		tiXianView=findViewById(R.id.tiXianView);
		mingXiView=findViewById(R.id.mingXiView);
		youHuiView=findViewById(R.id.youHuiView);
		ZhiFuMiMaView=findViewById(R.id.ZhiFuMiMaView);
		backButton = findViewById(R.id.backButton);
		titleName = (TextView) findViewById(R.id.titleName);
		leftMoney=(TextView)findViewById(R.id.leftMoney);
		setPwd=(TextView)findViewById(R.id.setPwd);
		isHidMoney=(ImageView)findViewById(R.id.isHidMoney);
		isHidMoney.setImageResource(R.drawable.qb_open_eyes);
	}

	private void initListensers() {
		zuanShiView.setOnClickListener(this);
		backButton.setOnClickListener(this);
		isHidMoney.setOnClickListener(this);
		ZhiFuMiMaView.setOnClickListener(this);
		youHuiView.setOnClickListener(this);
		mingXiView.setOnClickListener(this);
		tiXianView.setOnClickListener(this);
		chongZhiView.setOnClickListener(this);
	}

	private void initDatas() {
		titleName.setText("我的钱包");
		loadUserCount();
		isSetPassword();
		updateAccountView();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.backButton) {
			finish();
		} else if (v.getId() == R.id.ZhiFuMiMaView) {
			if(isSetPwd){
				Intent intent1=new Intent(this, ReSetPayPwdActivity.class);
				startActivity(intent1);
			}else{
				Intent intent=new Intent(this, SetPayPwdActivity.class);
				startActivityForResult(intent, 1);
			}
		} else if (v.getId() == R.id.youHuiView) {
			Intent intent=new Intent(this, MyComposeActivity.class);
			startActivity(intent);
		} else if (v.getId() == R.id.mingXiView) {
			Intent intent=new Intent(this, MingXiActivity.class);
			startActivity(intent);
		} else if (v.getId() == R.id.tiXianView) {
			Intent intent=new Intent(this, TiXianActivity.class);
			intent.putExtra("bean", qBean);
			startActivityForResult(intent, 2);
		} else if (v.getId() == R.id.chongZhiView) {
			Intent intent=new Intent(this, ChunZhiActivity.class);
			startActivityForResult(intent, 0);
		} else if(v.getId()==R.id.isHidMoney){
			isHidMoneyFlag=!isHidMoneyFlag;
			updateAccountView();
		}else if (v.getId()==R.id.zuanShiView){
			Intent intent=new Intent(this, VideoCertifyActivity.class);
			startActivity(intent);
		}
	}
	
	private void isSetPassword() {
		// TODO Auto-generated method stub
		VisitUnit visit = new VisitUnit();
		new Action("#.notice.SetPasswordTask(${hasOpsPassword})", this, null, visit).run();
	}
	
	private void loadUserCount() {
		// TODO Auto-generated method stub
		VisitUnit visit = new VisitUnit();
		new Action("#.notice.LoadQianBaoCountTask(${userMoney})", this, null, visit).run();
	}

	public void finishLoadUserCount(Map map) {
		// TODO Auto-generated method stub
		qBean = new QianBaoBean();
		qBean.setAmount(map.get("amount").toString());
		qBean.setAvailableCash(map.get("availableCash").toString());
		qBean.setUserId(map.get("userId").toString());
		updateAccountView();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 0: {
			if (Activity.RESULT_OK != resultCode || null == data) {
				return;
			}
			String money=data.getStringExtra(Constants.COMEBACK);
			if(money!=null){
				double totalMoney=StringUtil.getPrice(Long.parseLong(qBean.getAmount()))+Double.parseDouble(money);
				qBean.setAmount(String.valueOf(totalMoney*100));
				updateAccountView();
			}			
			break;
		}
		case 1:
			if (Activity.RESULT_OK != resultCode || null == data) {
				return;
			}
			String money=data.getStringExtra(Constants.COMEBACK);
			if(money!=null){
				isSetPwd=true;
//				UserPreferencesUtil.setHavePayPassword(this,true);
				setPwd.setText("密码已设置");
				setPwd.setTextColor(getResources().getColor(R.color.dark_green));
			}	
			break;
		case 2:
			if (Activity.RESULT_OK != resultCode || null == data) {
				return;
			}
			String money1=data.getStringExtra(Constants.COMEBACK);
			if(money1!=null){
				loadUserCount();
			}
			break;
		default:
			break;
		}

	}

	public void getHasPwd(String map) {
		// TODO Auto-generated method stub
		if("1".equals(map)){
			isSetPwd=false;
		}else if("2".equals(map)){
			isSetPwd=true;
			setPwd.setText("密码已设置");
			setPwd.setTextColor(getResources().getColor(R.color.dark_green));
		}
	}
}
