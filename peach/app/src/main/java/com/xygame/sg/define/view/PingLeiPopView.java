package com.xygame.sg.define.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.xygame.second.sg.jinpai.bean.JinPaiBigTypeBean;
import com.xygame.second.sg.xiadan.bean.TransferTypeBean;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.widget.wheel.ArrayWheelAdapter;
import com.xygame.sg.widget.wheel.OnWheelChangedListener;
import com.xygame.sg.widget.wheel.WheelView;

import java.util.List;

public class PingLeiPopView extends SGBaseActivity implements OnClickListener{

	private WheelView yearWv;
	private String[] years;
	private Button mBtnConfirm,btn_cancel;
	private String curYear;
	private TransferTypeBean transferTypeBean;
	private List<JinPaiBigTypeBean> myTypes;
	private JinPaiBigTypeBean currBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ping_lei_type);
		initView();
		initDatas();
		initListers();
		yearWv.setWheelBackground(R.color.white);
	}

	private void initView() {
		yearWv = (WheelView) findViewById(R.id.yearWv);

		mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
		btn_cancel= (Button) findViewById(R.id.btn_cancel);
	}

	private void initListers() {
		btn_cancel.setOnClickListener(this);
		mBtnConfirm.setOnClickListener(this);
		yearWv.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				curYear = years[yearWv.getCurrentItem()];
			}
		});
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_confirm){
			if(curYear!=null){
				transBeanSelect();
			}
		}else if(v.getId()==R.id.btn_cancel){
			finish();
		}
	}

	protected void transBeanSelect() {
		for (int i=0;i<myTypes.size();i++){
			if(curYear.equals(myTypes.get(i).getName())){
				currBean=myTypes.get(i);
				break;
			}
		}
		Intent intent = new Intent();
		intent.putExtra("bean", currBean);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}

	private void initDatas() {
		transferTypeBean=(TransferTypeBean)getIntent().getSerializableExtra("bean");
		myTypes=transferTypeBean.getMyTypes();
		years=new String[myTypes.size()];
		for (int i=0;i<myTypes.size();i++){
			years[i]=myTypes.get(i).getName();
		}
		yearWv.setViewAdapter(new ArrayWheelAdapter<String>(this, years));
		// 设置可见条目数量
		yearWv.setVisibleItems(7);
		yearWv.setCurrentItem(0);
		curYear = years[yearWv.getCurrentItem()];
	}
}
