package com.xygame.sg.activity.commen;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xygame.sg.R;

public class PresentDialog extends Dialog implements View.OnClickListener{

	private View closeView,sendPresentButton,parentView,jianButton,addButton;
	private TextView stoneValues,totalStone;
	private EditText giftNums;
	private ViewPager popPager;
	private LinearLayout popDotView;
	private PresentActionInterface actionListenser;

	public PresentDialog(Context context, int attrsr) {
		super(context,attrsr);
	}

	public void closeDiloag(){
		dismiss();
	}

	public void addPresentActionListener(PresentActionInterface actionListenser){
		this.actionListenser=actionListenser;
	}

	public TextView getStoneValues(){
		return stoneValues;
	}

	public TextView getTotalStone(){
		return totalStone;
	}

	public EditText getGiftNumsEditText(){
		return giftNums;
	}

	public LinearLayout getPopDotView(){
		return popDotView;
	}

	public ViewPager getPopPager(){
		return popPager;
	}

	/**
	 * 重载方法
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sg_present_dialog_layout);
		initViews();
		initLinsteners();
	}

	private void initLinsteners() {
		sendPresentButton.setOnClickListener(this);
		closeView.setOnClickListener(this);
		jianButton.setOnClickListener(this);
		addButton.setOnClickListener(this);
	}

	private void initViews() {
		addButton=findViewById(R.id.addButton);
		parentView=findViewById(R.id.parentView);
		sendPresentButton=findViewById(R.id.sendPresentButton);
		closeView=findViewById(R.id.closeView);
		jianButton=findViewById(R.id.jianButton);
		stoneValues=(TextView)findViewById(R.id.leaveStone);
		giftNums=(EditText)findViewById(R.id.giftNums);
		totalStone=(TextView)findViewById(R.id.totalStone);
		popPager=(ViewPager)findViewById(R.id.popPager);
		popDotView=(LinearLayout)findViewById(R.id.popDotView);
		parentView.getBackground().setAlpha(178);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.closeView:
				dismiss();
				break;
			case R.id.jianButton:
				actionListenser.jianAction();
				break;
			case R.id.addButton:
				actionListenser.addAction();
				break;
			case R.id.sendPresentButton:
				actionListenser.sendPresentAction();
				break;
		}
	}

	public interface PresentActionInterface{
		void jianAction();
		void sendPresentAction();
		void addAction();
	}
}
