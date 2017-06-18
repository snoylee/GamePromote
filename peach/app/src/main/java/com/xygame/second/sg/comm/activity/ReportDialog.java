package com.xygame.second.sg.comm.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.xygame.sg.R;

public class ReportDialog extends Dialog implements View.OnClickListener{

	private View closeView,sendGiftButton;
	private PresentActionInterface actionListenser;

	public ReportDialog(Context context, int attrsr) {
		super(context,attrsr);
	}

	public void closeDiloag(){
		dismiss();
	}

	public void addPresentActionListener(PresentActionInterface actionListenser){
		this.actionListenser=actionListenser;
	}


	/**
	 * 重载方法
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_report_dialog_layout);
		initViews();
		initLinsteners();
	}

	private void initLinsteners() {
		sendGiftButton.setOnClickListener(this);
		closeView.setOnClickListener(this);
	}

	private void initViews() {
		sendGiftButton=findViewById(R.id.sendGiftButton);
		closeView=findViewById(R.id.closeView);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.closeView:
				dismiss();
				break;
			case R.id.sendGiftButton:
				actionListenser.sendPresentAction();
				break;
		}
	}

	public interface PresentActionInterface{
		void sendPresentAction();
	}
}
