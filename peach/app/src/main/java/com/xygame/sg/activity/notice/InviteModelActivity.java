/*
 * 文 件 名:  ReportFristActivity.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月9日
 */
package com.xygame.sg.activity.notice;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.ButtonOneListener;
import com.xygame.sg.activity.commen.ButtonTwoListener;
import com.xygame.sg.activity.commen.OneButtonDialog;
import com.xygame.sg.activity.commen.TwoButtonDialog;
import com.xygame.sg.activity.notice.adapter.ComposeAdapter;
import com.xygame.sg.activity.notice.adapter.InviteAdapter;
import com.xygame.sg.activity.notice.bean.ComposeBean;
import com.xygame.sg.activity.notice.bean.InviteBean;
import com.xygame.sg.activity.notice.bean.PayWaysBean;
import com.xygame.sg.activity.notice.bean.PlushNoticeBean;
import com.xygame.sg.activity.personal.CMIdentyFirstActivity;

import com.xygame.sg.define.view.ChoiceNoticeView;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.UserPreferencesUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import base.ViewBinder;
import base.action.Action;
import base.frame.VisitUnit;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author 王琪
 * @date 2015年11月9日
 * @action [作品相册页面]
 */
public class InviteModelActivity extends SGBaseActivity implements OnClickListener{
	private ComposeBean SelectItem;
	private TextView titleName,rightButtonText;
	private View backButton,comfirmButton,rightButton,plushButton,numllView;
	private ListView composeList;
	private InviteAdapter adapter;
	private List<InviteBean> inviteNo,inviteYes,selectedDatas;
	private String modelId;
	private ImageView rightbuttonIcon;

	public String getModelId(){
		return modelId;
	}

	public List<InviteBean> getSelectedDatas(){
		return selectedDatas;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		VisitUnit visitUnit = new VisitUnit(this);
		setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.inite_model_layout, null));

		initViews();
		initListeners();
		initDatas();
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initListeners() {
		// TODO Auto-generated method stub
		backButton.setOnClickListener(this);
		comfirmButton.setOnClickListener(this);
		rightButton.setOnClickListener(this);
		plushButton.setOnClickListener(this);
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initViews() {
		titleName = (TextView) findViewById(R.id.titleName);
		rightButtonText=(TextView)findViewById(R.id.rightButtonText);
		composeList=(ListView)findViewById(R.id.composeList);
		backButton = findViewById(R.id.backButton);
		rightbuttonIcon=(ImageView)findViewById(R.id.rightbuttonIcon);
		rightButton=findViewById(R.id.rightButton);
		comfirmButton=findViewById(R.id.comfirmButton);
		plushButton=findViewById(R.id.plushButton);
		numllView=findViewById(R.id.numllView);
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @action [请添加内容描述]
	 */
	private void initDatas() {
		modelId=getIntent().getStringExtra("modelId");
		rightButtonText.setVisibility(View.GONE);
		rightbuttonIcon.setVisibility(View.VISIBLE);
		titleName.setText("通告邀请");
		rightbuttonIcon.setImageResource(R.drawable.new_notice_icon);
		adapter=new InviteAdapter(this, null);
		composeList.setAdapter(adapter);
		composeList.setVisibility(View.VISIBLE);
		numllView.setVisibility(View.GONE);
		loadAllCouponsDatas();
	}

	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @see [类、类#方法、类#成员]
	 */
	private void loadAllCouponsDatas() {
		// TODO Auto-generated method stub
		VisitUnit visit = new VisitUnit();
		new Action("#.notice.LoadAllNoticeTask(${inviteList})", this, null, visit).run();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.backButton) {
			finish();
		}else if(v.getId()==R.id.comfirmButton){
			selectedDatas=adapter.getSelectedDatas();
			if (selectedDatas.size()>0){
				uploadPushToModel();
			}else{
				Toast.makeText(this,"至少选择一个通告",Toast.LENGTH_SHORT).show();
			}
		}else if (v.getId()==R.id.rightButton){
			Intent intent = new Intent(this, ChoiceNoticeView.class);
			startActivityForResult(intent, 0);
		}else if(v.getId()==R.id.plushButton){
			Intent intent = new Intent(this, ChoiceNoticeView.class);
			startActivityForResult(intent, 0);
		}
	}

	private void uploadPushToModel() {
		VisitUnit visit = new VisitUnit();
		new Action("#.notice.SendNoticeToModelTask(${inviteAction})", this, null, visit).run();
	}

	/**
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @param map
	 * @see [类、类#方法、类#成员]
	 */
	public void finishLoadCompose(List<Map> map) {
		inviteNo=new ArrayList<InviteBean>();
		inviteYes=new ArrayList<InviteBean>();
		List<InviteBean> inviteAll=new ArrayList<InviteBean>();
		for(Map subMap:map){
			InviteBean item=new InviteBean();
			item.setIsSelect(false);
			item.setEndTime(subMap.get("endTime").toString());
			item.setFlag(subMap.get("flag").toString());
			item.setNoticeId(subMap.get("noticeId").toString());
			item.setSubject(subMap.get("subject").toString());
			item.setStartTime(subMap.get("startTime").toString());
			if("0".equals(subMap.get("flag").toString())){//未发送
				inviteNo.add(item);
			}else if("1".equals(subMap.get("flag").toString())) {//已发送
				inviteYes.add(item);
			}
		}
		if (inviteNo.size()>0){
			inviteAll.addAll(inviteNo);
		}
		if (inviteYes.size()>0){
			inviteAll.addAll(inviteYes);
		}
		if (inviteAll.size()>0){
			composeList.setVisibility(View.VISIBLE);
			numllView.setVisibility(View.GONE);
			adapter.addDatas(inviteAll);
		}else{
			composeList.setVisibility(View.GONE);
			numllView.setVisibility(View.VISIBLE);
		}
	}

	public void finishSend() {
		List<InviteBean> inviteAll=new ArrayList<InviteBean>();
		for (InviteBean item:selectedDatas){
			for (int i=0;i<inviteNo.size();i++){
				if (item.getNoticeId().equals(inviteNo.get(i).getNoticeId())){
					inviteNo.remove(i);
				}
			}
			item.setFlag("1");
			inviteYes.add(item);
		}
		if (inviteNo.size()>0){
			inviteAll.addAll(inviteNo);
		}
		if (inviteYes.size()>0){
			inviteAll.addAll(inviteYes);
		}
		adapter.addDatas(inviteAll);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case 0: {
				if (Activity.RESULT_OK != resultCode || null == data) {
					return;
				}
				String flag = data.getStringExtra(Constants.COMEBACK);
				if ("galary".equals(flag)) {
					Intent intent = new Intent(this, PlushNoticeActivity.class);
					intent.putExtra("noticeFlag", "pay");
					startActivity(intent);
				} else if ("camera".equals(flag)) {
					if(Constants.USER_VERIFIED_CODE.equals(UserPreferencesUtil.getUserVerifyStatus(this))){
						Intent intent = new Intent(this, PlushNoticeActivity.class);
						intent.putExtra("noticeFlag", "noPay");
						startActivity(intent);
					}else if(Constants.USER_NO_VERIFIED_CODE.equals(UserPreferencesUtil.getUserVerifyStatus(this))){
						showDialog(getResources().getString(R.string.to_publish_no_verify));
					}else if(Constants.USER_VERIFING_CODE.equals(UserPreferencesUtil.getUserVerifyStatus(this))){
						showOneButtonDialog(getResources().getString(R.string.to_publish_verifing));
					}else if(Constants.USER_VERIFING_REFUSED_CODE.equals(UserPreferencesUtil.getUserVerifyStatus(this))){
						showDialog(getResources().getString(R.string.to_publish_refused));
					}
				}
				break;
			}
			default:
				break;
		}
	}

	private void showOneButtonDialog(String tip){
		OneButtonDialog dialog=new OneButtonDialog(this, tip, R.style.dineDialog, new ButtonOneListener() {

			@Override
			public void confrimListener(Dialog dialog) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	private void showDialog(String tip){
		TwoButtonDialog dialog=new TwoButtonDialog(this, tip, R.style.dineDialog,new ButtonTwoListener() {

			@Override
			public void confrimListener() {
				// TODO Auto-generated method stub
				goToCerti();
			}

			@Override
			public void cancelListener() {
			}
		});
		dialog.show();
	}

	private void goToCerti(){
		Intent intent = new Intent(this, CMIdentyFirstActivity.class);
		startActivity(intent);
	}
}
