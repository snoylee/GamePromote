package com.xygame.sg.activity.notice;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.notice.adapter.MingXiAdapter;
import com.xygame.sg.activity.notice.bean.MingXiBean;
import com.xygame.sg.bean.comm.FeedbackDateBean;
import com.xygame.sg.define.view.ChoiceYMView;
import com.xygame.sg.utils.CalendarUtils;

import android.app.Activity;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import base.ViewBinder;
import base.action.Action;
import base.frame.VisitUnit;

public class MingXiActivity extends SGBaseActivity implements OnClickListener {
	/**
	 * 公用变量部分
	 */
	private TextView titleName, rightButtonText, monthText, yearMonthText;
	private View backButton, rightButton, choiceView;
	private MingXiAdapter adapter;
	private ImageView rightbuttonIcon;
	private ListView listView;
	private String startTime, endTime;

	public String getEndTime() {
		return endTime;
	}

	public String getStartTime() {
		return startTime;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		VisitUnit visitUnit = new VisitUnit(this);
		setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.mingxi_layout, null));
		initViews();
		initListensers();
		initDatas();
	}

	private void initViews() {
		// TODO Auto-generated method stub
		backButton = findViewById(R.id.backButton);
		choiceView = findViewById(R.id.choiceView);
		rightbuttonIcon=(ImageView)findViewById(R.id.rightbuttonIcon);
		yearMonthText = (TextView) findViewById(R.id.yearMonthText);
		titleName = (TextView) findViewById(R.id.titleName);
		monthText = (TextView) findViewById(R.id.monthText);
		rightButtonText = (TextView) findViewById(R.id.rightButtonText);
		listView = (ListView) findViewById(R.id.section_list_view);
		rightButton = findViewById(R.id.rightButton);
	}

	private void initListensers() {
		backButton.setOnClickListener(this);
		rightButton.setOnClickListener(this);
		choiceView.setOnClickListener(this);
	}

	private void initDatas() {
		String currDate = CalendarUtils.getHenGongYearMonth(System.currentTimeMillis());
		startTime = getTime(currDate.concat("-01 00:00"));
		String[] dateArray = currDate.split("-");
		int maxDate = CalendarUtils.getMonthLastDay(Integer.parseInt(dateArray[0]), Integer.parseInt(dateArray[1]));
		endTime = getTime(currDate.concat("-").concat(String.valueOf(maxDate)).concat(" 23:59"));
		monthText.setText("本月");
		yearMonthText.setText(currDate);
		titleName.setText("收支明细");
		rightButtonText.setVisibility(View.GONE);
		rightbuttonIcon.setVisibility(View.VISIBLE);
		rightbuttonIcon.setImageResource(R.drawable.search);
		adapter = new MingXiAdapter(this, getLayoutInflater(), null);
		listView.setAdapter(adapter);
		loadDaiJieDatas();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.backButton) {
			finish();
		} else if (v.getId() == R.id.rightButton) {
			Intent intent = new Intent(this, MingXiSearchActivity.class);
			startActivity(intent);
		} else if (v.getId() == R.id.choiceView) {
			Intent intent = new Intent(this, ChoiceYMView.class);
			startActivityForResult(intent, 0);
		}
	}

	private void loadDaiJieDatas() {
		// TODO Auto-generated method stub
		VisitUnit visit = new VisitUnit();
		new Action("#.notice.LoadMingXiTask(${listChange})", this, null, visit).run();
	}

	public void finishLoadPays(List<Map> map) {
		List<MingXiBean> datas = new ArrayList<MingXiBean>();
		for (Map sMap : map) {
			MingXiBean it = new MingXiBean();
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 0: {
			if (Activity.RESULT_OK != resultCode || null == data) {
				return;
			}
			Serializable item2 = data.getSerializableExtra("bean");
			if (item2 != null) {
				FeedbackDateBean ftBean = (FeedbackDateBean) item2;
				String szBirthday = ftBean.getYear() + "-" + ftBean.getMonth();
				int subMonth = Integer.parseInt(ftBean.getMonth());
				monthText.setText(String.valueOf(subMonth) + "月");
				yearMonthText.setText(ftBean.getYear() + "-" + ftBean.getMonth());
				startTime = getTime(szBirthday.concat("-01 00:00"));
				int maxDate = CalendarUtils.getMonthLastDay(Integer.parseInt(ftBean.getYear()),
						Integer.parseInt(ftBean.getMonth()));
				endTime = getTime(szBirthday.concat("-").concat(String.valueOf(maxDate)).concat(" 23:59"));
				adapter.clearDatas();
				adapter.notifyDataSetChanged();
				loadDaiJieDatas();
			}
			break;
		}
		default:
			break;
		}

	}

	public static String getTime(String user_time) {
		String re_time = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date d;
		try {

			d = sdf.parse(user_time);
			long l = d.getTime();
			re_time = String.valueOf(l);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return re_time;
	}
}
