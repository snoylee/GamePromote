/*
 * 文 件 名:  ReportFristActivity.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月9日
 */
package com.xygame.sg.activity.commen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.adapter.comm.ReportAdapter;
import com.xygame.sg.bean.comm.ReportBean;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  王琪
 * @date 2015年11月9日
 * @action  [举报第一个界面]
 */
public class ReportFristActivity extends SGBaseActivity implements OnClickListener,OnItemClickListener{
	
	private TextView titleName,rightButtonText;
	private View backButton,rightButton,bottomView,tipText;
	private ListView reportList;
	private ReportAdapter adapter;
	private String resType;//举报的资源（1：作品图片，2：用户，3：相册）
	private String userId;//资源所属的用户ID
	private String resourceId;//举报的资源ID（图片ID，用户ID…）
	private ReportBean reBean;
	public ReportBean getReBean() {
		return reBean;
	}
	public String getResType() {
		return resType;
	}

	public String getUserId() {
		return userId;
	}

	public String getResourceId() {
		return resourceId;
	}

	/**
	 * 重载方法
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sg_report_frist_layout);
		initViews();
		initListeners();
		initDatas();
	}

	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @action [请添加内容描述]
	 */
	private void initListeners() {
		// TODO Auto-generated method stub
		backButton.setOnClickListener(this);
		rightButton.setOnClickListener(this);
		bottomView.setOnClickListener(this);
		reportList.setOnItemClickListener(this);
	}

	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @action [请添加内容描述]
	 */
	private void initViews() {
		// TODO Auto-generated method stub
		titleName=(TextView)findViewById(R.id.titleName);
		rightButtonText=(TextView)findViewById(R.id.rightButtonText);
		reportList=(ListView)findViewById(R.id.reportList);
		tipText=findViewById(R.id.tipText);
		backButton=findViewById(R.id.backButton);
		rightButton=findViewById(R.id.rightButton);
		bottomView=findViewById(R.id.bottomView);
	}

	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @action [请添加内容描述]
	 */
	private void initDatas() {
		// TODO Auto-generated method stub
		adapter=new ReportAdapter(this, null);
		reportList.setAdapter(adapter);		
		titleName.setText(getResources().getString(R.string.sg_report_title));
		rightButton.setVisibility(View.VISIBLE);
		rightButtonText.setVisibility(View.VISIBLE);
		rightButtonText.setText(getResources().getString(R.string.sg_report_top_commit));
		resourceId=getIntent().getStringExtra("resourceId");
		userId=getIntent().getStringExtra("userId");
		resType=getIntent().getStringExtra("resType");
		tipText.setVisibility(View.GONE);
		loadReportType();
	}

	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @see [类、类#方法、类#成员]
	 */
	private void loadReportType() {
		try {
			RequestBean item = new RequestBean();
			item.setIsPublic(false);
			JSONObject object = new JSONObject();
			item.setData(object);
			ShowMsgDialog.showNoMsg(this, true);
			item.setServiceURL(ConstTaskTag.QUEST_JUBAO_TYPE);
			ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_JUBAO_TYPE);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 重载方法
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		reBean=adapter.getSelectItem();
		if(v.getId()==R.id.backButton){
			finish();
		}else if(v.getId()==R.id.bottomView){
			Intent intent=new Intent(this, ReportSecondActivity.class);
			intent.putExtra("resType",resType);
        	intent.putExtra("userId",userId);
        	intent.putExtra("resourceId",resourceId );
//        	intent.putExtra("reBean", reBean);
			startActivityForResult(intent, 0);
		}else if(v.getId()==R.id.rightButton){
			commitComplaint();
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (Activity.RESULT_OK != resultCode || null == data) {
			return;
		}
		switch (requestCode) {
		case 0: {
			String result = data.getStringExtra(Constants.COMEBACK);
			if (Constants.COMEBACK.equals(result)) {
				finish();
			}
			break;
		}
		default:
			break;
		}
	}

	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @see [类、类#方法、类#成员]
	 */
	private void commitComplaint() {
		try {
			RequestBean item = new RequestBean();
			item.setIsPublic(false);
			JSONObject object = new JSONObject();
			String userId = getUserId();
			String resourceId = getResourceId();
			String compType = getReBean().getTypeId();
			String resType=getResType();
			object.put("userId", userId);
			object.put("resourceId", resourceId);
			object.put("compType", compType);
			object.put("resType", resType);
			item.setData(object);
			ShowMsgDialog.show(this, "提交中...", false);
			item.setServiceURL(ConstTaskTag.QUEST_JUBAO_COMMIT);
			ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_JUBAO_COMMIT);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 重载方法
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		adapter.selectItem(arg2);
	}

	private void parseUserActDatas(String record) {
		try {
			if (ConstTaskTag.isTrueForArrayObj(record)) {
				List<ReportBean> datas=new ArrayList<ReportBean>();
				JSONArray array2 = new JSONArray(record);
				for (int i = 0; i < array2.length(); i++) {
					JSONObject object1 = array2.getJSONObject(i);
					ReportBean reportBean=new ReportBean();
					reportBean.setReportTxt(StringUtils.getJsonValue(object1, "typeName"));
					reportBean.setReport(false);
					reportBean.setTypeId(StringUtils.getJsonValue(object1, "typeId"));
					datas.add(reportBean);
				}
				if(datas.size()>0){
					tipText.setVisibility(View.VISIBLE);
					datas.get(0).setReport(true);
					adapter.addDatas(datas);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void getResponseBean(ResponseBean data) {
		super.getResponseBean(data);
		switch (data.getPosionSign()) {
			case ConstTaskTag.QUERY_JUBAO_TYPE:
				if ("0000".equals(data.getCode())) {
					parseUserActDatas(data.getRecord());
				} else {
					Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
				}
				break;
			case ConstTaskTag.QUERY_JUBAO_COMMIT:
				if ("0000".equals(data.getCode())) {
					Toast.makeText(getApplicationContext(), "举报成功", Toast.LENGTH_SHORT).show();
					finish();
				} else {
					Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
				}
				break;
		}
	}
}
