package com.xygame.sg.activity.commen;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xygame.sg.R;

public class NoButtonDialog extends Dialog implements View.OnClickListener{
	private TextView tipText;
	private View comfirmView;
	private ButtonOneListener comfirmListener;
	private String textTip;

	public NoButtonDialog(Context context, int attrsr) {
		super(context,attrsr);
	}

	public NoButtonDialog(Context context, String textTip, int attrs, ButtonOneListener comfirmListener) {
		super(context,attrs);
		this.comfirmListener=comfirmListener;
		this.textTip=textTip;
	}
	
	/**
	 * 重载方法
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sg_one_button_dialog_layout);
		initViews();
		initListener();
		initDatas();
		findViewById(R.id.titleLine).setVisibility(View.GONE);
		findViewById(R.id.comfirmView).setVisibility(View.GONE);

	}

	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @action [请添加内容描述]
	 */
	private void initViews() {
		// TODO Auto-generated method stub
		tipText=(TextView)findViewById(R.id.tipText);
		comfirmView=findViewById(R.id.comfirmView);
	}

	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @action [请添加内容描述]
	 */
	private void initListener() {
		// TODO Auto-generated method stub
		comfirmView.setOnClickListener(this);
	}

	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @action [请添加内容描述]
	 */
	private void initDatas() {
		if (textTip.equals("")){
			tipText.setVisibility(View.GONE);
		} else {
			tipText.setText(textTip);
		}


		setCanceledOnTouchOutside(false);
	}

	/**
	 * 重载方法
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.comfirmView){
			comfirmListener.confrimListener(this);
			dismiss();
		}
	}
}
