package com.xygame.sg.activity.personal;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import base.action.Action;
import base.action.CenterRepo;
import base.frame.VisitUnit;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.define.view.SingleWheelView;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.UserPreferencesUtil;

public class EditorBodyInfoActivity extends SGBaseActivity implements
		OnClickListener {

	private View modifyBodyHightView, modifyBodyWeightView,
			modifyBodyHeartView, modifyBodyThreeHeartView, modifyBodyShoesView,
			backButton, rightButton;
	private TextView bodyHightValues, bodyWeightValues, bodyHeartValues,
			bodyThreeHeartValues, bodyShoesValues, titleName, rightButtonText;
	
	private String userHeight,userWeight,userBust,userWaist,userHip,userCup,userShoesCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sg_editor_bodyinfo_layout);
		initViews();
		initListeners();
		initDatas();
	}

	private void initViews() {
		// TODO Auto-generated method stub
		modifyBodyHightView = findViewById(R.id.modifyBodyHightView);
		modifyBodyWeightView = findViewById(R.id.modifyBodyWeightView);
		modifyBodyHeartView = findViewById(R.id.modifyBodyHeartView);
		modifyBodyThreeHeartView = findViewById(R.id.modifyBodyThreeHeartView);
		modifyBodyShoesView = findViewById(R.id.modifyBodyShoesView);
		backButton = findViewById(R.id.backButton);
		rightButton = findViewById(R.id.rightButton);

		titleName = (TextView) findViewById(R.id.titleName);
		rightButtonText = (TextView) findViewById(R.id.rightButtonText);
		bodyHightValues = (TextView) findViewById(R.id.bodyHightValues);
		bodyWeightValues = (TextView) findViewById(R.id.bodyWeightValues);
		bodyHeartValues = (TextView) findViewById(R.id.bodyHeartValues);
		bodyThreeHeartValues = (TextView) findViewById(R.id.bodyThreeHeartValues);
		bodyShoesValues = (TextView) findViewById(R.id.bodyShoesValues);
	}

	private void initListeners() {
		// TODO Auto-generated method stub
		modifyBodyHightView.setOnClickListener(this);
		modifyBodyWeightView.setOnClickListener(this);
		modifyBodyHeartView.setOnClickListener(this);
		modifyBodyThreeHeartView.setOnClickListener(this);
		modifyBodyShoesView.setOnClickListener(this);
		backButton.setOnClickListener(this);
		rightButton.setOnClickListener(this);
	}

	private void initDatas() {
		// TODO Auto-generated method stub
		titleName.setText(getResources().getString(
				R.string.sg_editor_bodyinfo_title));
		rightButton.setVisibility(View.VISIBLE);
		rightButtonText.setVisibility(View.VISIBLE);
		rightButtonText.setText(getResources().getString(
				R.string.sg_editor_bodyinfo_comfirm));
		rightButtonText.setTextColor(getResources()
				.getColor(R.color.dark_green));
		
		userHeight=getIntent().getStringExtra("userHeight");
		userWeight=getIntent().getStringExtra("userWeight");
		userBust=getIntent().getStringExtra("userBust");
		userWaist=getIntent().getStringExtra("userWaist");
		userHip=getIntent().getStringExtra("userHip");
		userCup=getIntent().getStringExtra("userCup");
		userShoesCode=getIntent().getStringExtra("userShoesCode");
		
		initViewValue();
	}

	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @see [类、类#方法、类#成员]
	 */
	private void initViewValue() {
		String[] scope = new String[] { "A", "B", "C", "D", "E", "F", "G"};
		if(userHeight!=null){
			bodyHightValues.setText(userHeight.concat("CM"));
		}
		if(userWeight!=null){
			bodyWeightValues.setText(userWeight.concat("KG"));
		}
		if(userBust!=null){
			bodyThreeHeartValues.setText(userBust.concat("-").concat(userWaist).concat("-").concat(userHip));
		}
		if(userCup!=null){
			bodyHeartValues.setText(scope[Integer.parseInt(userCup)-1]);
		}
		if(userShoesCode!=null){
			bodyShoesValues.setText(userShoesCode.concat("码"));
		}
	}

	@Override
	public void onClick(View v) {

		String title;
		String[] scope = null;
		int j = 1;
		int[] init = new int[1];
		switch (v.getId()) {
		case R.id.modifyBodyHightView:
			title = ((TextView) findViewById(R.id.bodyHightTitle)).getText()
					.toString();
			title += " (cm)";
			scope = genScope(150, 190, 1);
			init[0] = 20;
			Intent intent1 = new Intent(this, SingleWheelView.class);
			if (scope != null) {
				for (int i = 1; i <= j; i++) {
					intent1.putExtra(Constants.SIGLE_WHEEL_TITLE + i, title)
							.putExtra(Constants.SIGLE_WHEEL_VALUE + i, scope)
							.putExtra(Constants.SIGLE_WHEEL_ITEM + i,
									init[i - 1]);
				}
				startActivityForResult(intent1, 1);

			}
			break;
		case R.id.modifyBodyWeightView:
			title = ((TextView) findViewById(R.id.bodyWeightTitle)).getText()
					.toString();
			title += " (kg)";
			scope = genScope(40, 75, 1);
			init[0] = 5;
			Intent intent2 = new Intent(this, SingleWheelView.class);
			if (scope != null) {
				for (int i = 1; i <= j; i++) {
					intent2.putExtra(Constants.SIGLE_WHEEL_TITLE + i, title)
							.putExtra(Constants.SIGLE_WHEEL_VALUE + i, scope)
							.putExtra(Constants.SIGLE_WHEEL_ITEM + i,
									init[i - 1]);
				}
				startActivityForResult(intent2, 2);

			}
			break;
		case R.id.modifyBodyThreeHeartView:
			title = ((TextView) findViewById(R.id.bodyThreeHeartTitle))
					.getText().toString();
			scope = genScope(50, 110, 1);
			init = new int[3];
			init[0] = 20;
			init[1] = 0;
			init[2] = 20;
			j = 3;
			Intent intent3 = new Intent(this, SingleWheelView.class);
			if (scope != null) {
				for (int i = 1; i <= j; i++) {
					intent3.putExtra(Constants.SIGLE_WHEEL_TITLE + i, title)
							.putExtra(Constants.SIGLE_WHEEL_VALUE + i, scope)
							.putExtra(Constants.SIGLE_WHEEL_ITEM + i,
									init[i - 1]);
				}
				startActivityForResult(intent3, 3);

			}
			break;
		case R.id.modifyBodyHeartView:
			title = ((TextView) findViewById(R.id.bodyHeartTitle)).getText()
					.toString();
			scope = new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I",
					"J" };
			init[0] = 1;
			Intent intent4 = new Intent(this, SingleWheelView.class);
			if (scope != null) {
				for (int i = 1; i <= j; i++) {
					intent4.putExtra(Constants.SIGLE_WHEEL_TITLE + i, title)
							.putExtra(Constants.SIGLE_WHEEL_VALUE + i, scope)
							.putExtra(Constants.SIGLE_WHEEL_ITEM + i,
									init[i - 1]);
				}
				startActivityForResult(intent4, 4);

			}
			break;
		case R.id.modifyBodyShoesView:
			title = ((TextView) findViewById(R.id.bodyShoesTitle)).getText()
					.toString();
			title += " (码)";
			scope = genScope(30, 45, 1);
			init[0] = 6;
			Intent intent5 = new Intent(this, SingleWheelView.class);
			if (scope != null) {
				for (int i = 1; i <= j; i++) {
					intent5.putExtra(Constants.SIGLE_WHEEL_TITLE + i, title)
							.putExtra(Constants.SIGLE_WHEEL_VALUE + i, scope)
							.putExtra(Constants.SIGLE_WHEEL_ITEM + i,
									init[i - 1]);
				}
				startActivityForResult(intent5, 5);

			}
			break;

		case R.id.backButton:
			finish();
			break;

		case R.id.rightButton:
			transferLocationService();
			break;

		}

	}

	public String[] genScope(float b, float e, float step) {
		List<String> l = new ArrayList<String>();
		for (float i = b; i <= e; i += step) {
			l.add("" + new Float(i).intValue());
		}
		String[] a = new String[l.size()];
		l.toArray(a);
		return a;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null) {
			return;
		}
		String s = data.getStringExtra(Constants.SIGLE_WHEEL_BACK_VALUE);
		switch (requestCode) {
		case 1:
			bodyHightValues.setText(s.concat("CM"));
			userHeight=s;
			break;
		case 2:
			bodyWeightValues.setText(s.concat("KG"));
			userWeight=s;
			break;
		case 3:
			bodyThreeHeartValues.setText(s);
			String[] array=s.split("-");
			userBust=array[0];
			userWaist=array[1];
			userHip=array[2];
			break;
		case 4:
			bodyHeartValues.setText(s);
			userCup=s;
			break;
		case 5:
			bodyShoesValues.setText(s.concat("码"));
			userShoesCode=s;
			break;
		default:
			break;
		}
	}

	private void transferLocationService(){
		VisitUnit visit = new VisitUnit();
		new Action("#.personal.EditorUserBodyTask(${editUserBody})",this,null,visit).run();
	}
	
	public void finishActivity() {
		// TODO Auto-generated method stub
		Intent intent=new Intent(Constants.ACTION_EDITOR_USER_INFO);
		sendBroadcast(intent);
		finish();
	}

	public String getUserHeight() {
		return userHeight;
	}

	public String getUserWeight() {
		return userWeight;
	}

	public String getUserBust() {
		return userBust;
	}

	public String getUserWaist() {
		return userWaist;
	}

	public String getUserHip() {
		return userHip;
	}

	public String getUserCup() {
		return userCup;
	}

	public String getUserShoesCode() {
		return userShoesCode;
	}
	
	
}
