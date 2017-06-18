package com.xygame.sg.activity.notice;

import java.util.Map;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.notice.adapter.SignedForModelAdapter;
import com.xygame.sg.activity.notice.bean.ModelSignedBean;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import base.ViewBinder;
import base.action.Action;
import base.frame.VisitUnit;

public class SignedForModelActivity extends SGBaseActivity implements OnClickListener {
	
	/**
	 * 公用变量部分
	 */
	private TextView titleName;
	private ListView listView;
	private View backButton;
	
	private SignedForModelAdapter adapter;
	
	private String noticeId;
	
	public String getnoticeId(){
		return noticeId;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		VisitUnit visitUnit = new VisitUnit(this);
		setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.siged_formodel_layout, null));
		initViews();
		initListensers();
		initDatas();
	}

	private void initViews() {
		// TODO Auto-generated method stub
		backButton = findViewById(R.id.backButton);
		titleName = (TextView) findViewById(R.id.titleName);
		listView = (ListView) findViewById(R.id.listView);
	}

	private void initListensers() {
		backButton.setOnClickListener(this);
	}

	private void initDatas() {
		noticeId=getIntent().getStringExtra("noticeId");
		titleName.setText("拍摄签到");
		adapter=new SignedForModelAdapter(this, null);
		listView.setAdapter(adapter);
		loadSignDatas();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.backButton) {
			finish();
		}
	}

	private void loadSignDatas() {
		// TODO Auto-generated method stub
		VisitUnit visit = new VisitUnit();
		new Action("#.notice.QueryNoticeSignForModelTask(${queryModelNoticeSign})", this, null, visit).run();
	}


	public void getSignInfo(Map subMap) {
		// TODO Auto-generated method stub
		ModelSignedBean item =new ModelSignedBean();
		item.setSignTime(subMap.get("signTime").toString());
		item.setUserIcon(subMap.get("userIcon").toString());
		item.setUserId(subMap.get("userId").toString());
		item.setUsernick(subMap.get("usernick").toString());
		adapter.addItem(item);
	}
}
