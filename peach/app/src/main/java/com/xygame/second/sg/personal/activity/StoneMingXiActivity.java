package com.xygame.second.sg.personal.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.second.sg.jinpai.bean.JinPaiBean;
import com.xygame.second.sg.personal.adapter.StoneMingXiAdapter;
import com.xygame.second.sg.personal.bean.MingXinCacheBean;
import com.xygame.second.sg.personal.bean.StoneMXBean;
import com.xygame.second.sg.personal.bean.StoneMXItemBean;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.bean.comm.FeedbackDateBean;
import com.xygame.sg.define.view.ChoiceYMViewForStone;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.widget.refreash.PullToRefreshBase;
import com.xygame.sg.widget.refreash.PullToRefreshListView2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import base.ViewBinder;
import base.frame.VisitUnit;

public class StoneMingXiActivity extends SGBaseActivity implements OnClickListener,PullToRefreshBase.OnRefreshListener2 {
	/**
	 * 公用变量部分
	 */
	private TextView titleName,  monthText, yearMonthText;
	private View backButton, choiceView;
	private StoneMingXiAdapter adapter;
	private PullToRefreshListView2 listView;
	private String startTime, currTime,saveTime;
	private boolean isCurrTime=true;

	private int pageSize = 20;//每页显示的数量
	private int mCurrentPage = 1;//当前显示页数
	private String reqTime;
	private boolean isLoading = true;

	public String getStartTime() {
		return startTime;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		VisitUnit visitUnit = new VisitUnit(this);
		setContentView(new ViewBinder(this, visitUnit).inflate(R.layout.stone_mingxi_layout, null));
		initViews();
		initListensers();
		initDatas();
	}

	private void initViews() {
		// TODO Auto-generated method stub
		backButton = findViewById(R.id.backButton);
		choiceView = findViewById(R.id.choiceView);
		yearMonthText = (TextView) findViewById(R.id.yearMonthText);
		titleName = (TextView) findViewById(R.id.titleName);
		monthText = (TextView) findViewById(R.id.monthText);
		listView = (PullToRefreshListView2) findViewById(R.id.section_list_view);
		listView.setMode(PullToRefreshBase.Mode.BOTH);
	}

	private void initListensers() {
		backButton.setOnClickListener(this);
		choiceView.setOnClickListener(this);
		listView.setOnRefreshListener(this);
	}

	private void initDatas() {
		String currDate = CalendarUtils.getHenGongYearMonth(System.currentTimeMillis());
		currTime=currDate.concat("-01 00:00");
		startTime = getTime(currTime);
		monthText.setText("本月");
		yearMonthText.setText(currDate);
		titleName.setText("钻石收支明细");
		adapter = new StoneMingXiAdapter(this, getLayoutInflater(), null);
		listView.setAdapter(adapter);
		loadData();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.backButton) {
			finish();
		} else if (v.getId() == R.id.choiceView) {
			Intent intent = new Intent(this, ChoiceYMViewForStone.class);
			startActivityForResult(intent, 0);
		}
	}

	private void loadData() {
		RequestBean item = new RequestBean();
		try {
			JSONObject obj = new JSONObject();
			obj.put("page", new JSONObject().put("pageIndex", mCurrentPage).put("pageSize", pageSize));
			obj.put("rangeTime", getStartTime());
			if (mCurrentPage > 1) {
				if (isCurrTime){
					obj.put("reqTime", reqTime);
				}
			} else {
				ShowMsgDialog.showNoMsg(this, true);
			}
			item.setData(obj);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		item.setServiceURL(ConstTaskTag.QUEST_STONE_DETAIL);
		ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_STONE_DETAIL);
	}

	@Override
	protected void getResponseBean(ResponseBean data) {
		super.getResponseBean(data);
		listView.onRefreshComplete();
		switch (data.getPosionSign()) {
			case ConstTaskTag.QUERY_STONE_DETAIL:
				if ("0000".equals(data.getCode())) {
					parseDatas(data);
				} else {
					Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
				}
				break;
		}
	}

	@Override
	protected void responseFaith(ResponseBean data) {
		super.responseFaith(data);
		listView.onRefreshComplete();
	}

	public void parseDatas(ResponseBean data) {
		if (!TextUtils.isEmpty(data.getRecord()) && !"null".equals(data.getRecord())) {
			List<StoneMXItemBean> datas = new ArrayList<>();
			try {
				JSONObject object1 = new JSONObject(data.getRecord());
				String actions = StringUtils.getJsonValue(object1, "flows");
				reqTime = StringUtils.getJsonValue(object1, "reqTime");
				if (!TextUtils.isEmpty(actions) && !"[]".equals(actions) && !"[null]".equals(actions) && !"null".equals(actions)) {
					JSONArray jsonArray = new JSONArray(actions);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject object2 = jsonArray.getJSONObject(i);
						StoneMXItemBean item = new StoneMXItemBean();
						item.setAmount(StringUtils.getJsonLongValue(object2, "amount"));
						item.setDealDesc(StringUtils.getJsonValue(object2, "dealDesc"));
						item.setDealTime(StringUtils.getJsonLongValue(object2, "dealTime"));
						item.setDealType(StringUtils.getJsonIntValue(object2, "dealType"));
						item.setFinanceType(StringUtils.getJsonIntValue(object2, "financeType"));
						datas.add(item);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (datas.size() < pageSize) {
				isLoading = false;
			}
			adapter.addDatas(datas, mCurrentPage);
		} else {
			isLoading = false;
		}
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
				if (!isCurrTime){
					MingXinCacheBean cacheBean=CacheService.getInstance().getCacheDatas(saveTime.concat(UserPreferencesUtil.getUserId(this)));
					if (cacheBean==null){
						cacheBean=new MingXinCacheBean();
					}
					cacheBean.setPageIndex(mCurrentPage);
					cacheBean.setIsLoading(isLoading);
					cacheBean.setReqTime(reqTime);
					CacheService.getInstance().cacheDatas(saveTime.concat(UserPreferencesUtil.getUserId(this)), cacheBean);
					List<StoneMXItemBean> datas=new ArrayList<>();
					datas.addAll(adapter.getAllDatas());
					CacheService.getInstance().cacheDatas2(saveTime.concat(UserPreferencesUtil.getUserId(this)),datas);
				}

				FeedbackDateBean ftBean = (FeedbackDateBean) item2;
				String szBirthday = ftBean.getYear() + "-" + ftBean.getMonth();
				int subMonth = Integer.parseInt(ftBean.getMonth());
				monthText.setText(String.valueOf(subMonth) + "月");
				yearMonthText.setText(ftBean.getYear() + "-" + ftBean.getMonth());
				String temTime=szBirthday.concat("-01 00:00");
				if (currTime.equals(temTime)){
					isCurrTime=true;
					mCurrentPage = 1;//当前显示页数
					isLoading = true;
					startTime = getTime(temTime);
					adapter.clearDatas();
					adapter.notifyDataSetChanged();
					loadData();
				}else{
					saveTime=temTime;
					isCurrTime=false;
					MingXinCacheBean cacheBean=CacheService.getInstance().getCacheDatas(temTime.concat(UserPreferencesUtil.getUserId(this)));
					if (cacheBean==null){
						mCurrentPage = 1;//当前显示页数
						isLoading = true;
						startTime = getTime(temTime);
						adapter.clearDatas();
						adapter.notifyDataSetChanged();
						loadData();
					}else{
						startTime = getTime(temTime);
						mCurrentPage=cacheBean.getPageIndex();
						isLoading=cacheBean.isLoading();
						reqTime=cacheBean.getReqTime();
						adapter.addDatas(CacheService.getInstance().getCacheDatas2(temTime.concat(UserPreferencesUtil.getUserId(this))),1);
					}
				}

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

	@Override
	public void onPullDownToRefresh(PullToRefreshBase refreshView) {
		isLoading = true;
		mCurrentPage = 1;
		loadData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase refreshView) {
		if (isLoading) {
			mCurrentPage = mCurrentPage + 1;
			loadData();
		} else {
			falseDatas();
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 1:
					listView.onRefreshComplete();
					Toast.makeText(StoneMingXiActivity.this, "已全部加载", Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
			}

		}
	};

	private void falseDatas() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					android.os.Message m = handler.obtainMessage();
					m.what = 1;
					m.sendToTarget();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}).start();
	}
}
