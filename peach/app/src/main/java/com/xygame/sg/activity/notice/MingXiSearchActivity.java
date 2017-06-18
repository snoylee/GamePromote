package com.xygame.sg.activity.notice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.notice.adapter.MingXiAdapter;
import com.xygame.sg.activity.notice.bean.MingXiBean;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import base.ViewBinder;
import base.action.Action;
import base.frame.VisitUnit;

public class MingXiSearchActivity extends SGBaseActivity implements OnClickListener{
	/**
	 * 公用变量部分
	 */
	private MingXiAdapter adapter;
	private ListView listView;
	private EditText inputText;
	private View clearButton,rightButton;
	
	public String getSearchContent(){
		return inputText.getText().toString().trim();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		VisitUnit visitUnit = new VisitUnit(this);
		setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.mingxi_search_layout, null));
		initViews();
		initListensers();
		initDatas();
	}

	private void initViews() {
		rightButton=findViewById(R.id.rightButton);
		clearButton=findViewById(R.id.clearButton);
		inputText=(EditText)findViewById(R.id.inputText);
		listView = (ListView) findViewById(R.id.section_list_view);
	}

	private void initListensers() {
		clearButton.setOnClickListener(this);
		rightButton.setOnClickListener(this);
	}

	private void initDatas() {
		adapter=new MingXiAdapter(this,getLayoutInflater(), null);
		listView.setAdapter(adapter);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.clearButton) {
			inputText.setText("");
		} else if(v.getId()==R.id.rightButton){
			if(!"".equals(inputText.getText().toString().trim())){
				loadDaiJieDatas();
			}else{
				Toast.makeText(getApplicationContext(), "搜索内容不能为空",Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	private void loadDaiJieDatas() {
		// TODO Auto-generated method stub
		VisitUnit visit = new VisitUnit();
		new Action("#.notice.LoadMingXiSearchTask(${listChange})", this, null, visit).run();
	}


	public void finishLoadPays(List<Map> map) {
		// TODO Auto-generated method stub
		List<MingXiBean> datas=new ArrayList<MingXiBean>();
		for(Map sMap:map){
			MingXiBean it=new MingXiBean();
			it.setAmount(sMap.get("amount").toString());
			it.setChangeRecordId(sMap.get("changeRecordId").toString());
			it.setDealChannel(sMap.get("dealChannel").toString());
			it.setDealDesc(sMap.get("dealDesc").toString());
			it.setDealNote(sMap.get("dealNote").toString());
			it.setDealTime(sMap.get("dealTime").toString());
			it.setDealType(sMap.get("dealType").toString());
			it.setFinanceType(sMap.get("financeType").toString());
			it.setId(sMap.get("id").toString());
			it.setNoticeId(sMap.get("noticeId").toString());
			it.setUserId(sMap.get("userId").toString());
			datas.add(it);
		}
		adapter.addDatas(datas);
	}

}
