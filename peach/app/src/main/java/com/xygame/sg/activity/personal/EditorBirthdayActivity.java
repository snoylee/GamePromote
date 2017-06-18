package com.xygame.sg.activity.personal;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import base.action.Action;
import base.frame.VisitUnit;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.personal.bean.BirthdayBean;
import com.xygame.sg.bean.comm.FeedbackDateBean;
import com.xygame.sg.define.view.ChoiceDateView;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.Constants;

public class EditorBirthdayActivity extends SGBaseActivity implements OnClickListener {

	private View backButton, rightButton, brithdayView;
	private TextView titleName, rightButtonText, brithdayVaule, ageVaule;
	private BirthdayBean birthdayBean;
	private String birthdayValue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sg_editor_brithday_layout);
		initViews();
		initListeners();
		initDatas();
	}

	private void initViews() {
		// TODO Auto-generated method stub
		backButton = findViewById(R.id.backButton);
		rightButton = findViewById(R.id.rightButton);
		brithdayView = findViewById(R.id.brithdayView);
		titleName = (TextView) findViewById(R.id.titleName);
		rightButtonText = (TextView) findViewById(R.id.rightButtonText);
		brithdayVaule = (TextView) findViewById(R.id.brithdayVaule);
		ageVaule = (TextView) findViewById(R.id.ageVaule);
	}

	private void initListeners() {
		// TODO Auto-generated method stub
		backButton.setOnClickListener(this);
		rightButton.setOnClickListener(this);
		brithdayView.setOnClickListener(this);
	}

	private void initDatas() {
		// TODO Auto-generated method stub
		titleName.setText(getResources().getString(R.string.sg_comm_editor_brithday_title));
		rightButton.setVisibility(View.VISIBLE);
		rightButtonText.setVisibility(View.VISIBLE);
		rightButtonText.setText(getResources().getString(R.string.sg_editor_bodyinfo_comfirm));
		rightButtonText.setTextColor(getResources().getColor(R.color.dark_green));

		birthdayBean = new BirthdayBean();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backButton:
			finish();
			break;

		case R.id.rightButton:
			transferLocationService();
			break;

		case R.id.brithdayView:
			startActivityForResult(new Intent(this, ChoiceDateView.class), 0);
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 0:
			if (Activity.RESULT_OK != resultCode || null == data) {
				return;
			}
			Serializable item2 = data.getSerializableExtra("bean");
			if (item2 != null) {
				FeedbackDateBean ftBean = (FeedbackDateBean) item2;
				String szBirthday = ftBean.getYear() + "-" + ftBean.getMonth()
						+ "-" + ftBean.getDay();
				brithdayVaule.setText(szBirthday);
				birthdayBean.setBrithday(szBirthday);
				try {
					birthdayValue=CalendarUtils.getTime(szBirthday);
					String str=CalendarUtils.getDateStr(Long.parseLong(birthdayValue));
					int ageStr=getAge(szBirthday);
					ageVaule.setText(ageStr+"岁");
					birthdayBean.setAge(String.valueOf(ageStr));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
		default:
			break;
		}
	}
	
	public void finishActivity(){
		Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
		Intent intent=new Intent(Constants.ACTION_EDITOR_USER_INFO);
		sendBroadcast(intent);
		finish();
	}
	
	private void transferLocationService(){
		VisitUnit visit = new VisitUnit();
		new Action("#.personal.EditorUserBirthday(${editUser})",this,null,visit).run();
	}

	public String getBirthdayValue() {
		return birthdayValue;
	}

//	public long getTimeValue(String birthdayStr) throws ParseException{
//			 SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd" );
//			 Date date = format.parse(birthdayStr);
//		 return date.getTime();
//	}

	public static int getAge(String birthdayStr) throws ParseException {
		SimpleDateFormat init = new SimpleDateFormat("yyyy-MM-dd");

		Date birthDate = init.parse(birthdayStr);

		if (birthDate == null)
			throw new RuntimeException("出生日期不能为null");

		int age = 0;

		Date now = new Date();

		SimpleDateFormat format_y = new SimpleDateFormat("yyyy");
		SimpleDateFormat format_M = new SimpleDateFormat("MM");

		String birth_year = format_y.format(birthDate);
		String this_year = format_y.format(now);

		String birth_month = format_M.format(birthDate);
		String this_month = format_M.format(now);

		// 初步，估算
		age = Integer.parseInt(this_year) - Integer.parseInt(birth_year);

		// 如果未到出生月份，则age - 1
		if (this_month.compareTo(birth_month) < 0)
			age -= 1;
		if (age < 0)
			age = 0;
		return age;
	}
}
