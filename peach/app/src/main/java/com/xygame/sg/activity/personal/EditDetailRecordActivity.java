package com.xygame.sg.activity.personal;

import java.io.Serializable;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.personal.bean.RsumeBean;
import com.xygame.sg.bean.comm.FeedbackDateBean;
import com.xygame.sg.define.view.ChoiceDateView;
import com.xygame.sg.task.personal.AddUserResumeTask;
import com.xygame.sg.task.personal.EditorUserResumeTask;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.StringUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import base.action.Action;
import base.frame.VisitUnit;

public class EditDetailRecordActivity extends SGBaseActivity implements View.OnClickListener {
	private ImageView backView;
	private TextView backViewText;
	private TextView titleName;
	private TextView rightButtonText;
	private String resumeType, actionName;
	private EditText contentTxt;
	private RsumeBean resumeBean;

	private RelativeLayout start_rl;
	private TextView start_tv;
	private RelativeLayout end_rl;
	private TextView end_tv;
	private String sTimer, eTimer;
	private int indexBean;
	private final String FORMAT = "yyyy-MM-dd";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_detail_record);
		initViews();
		addListener();
	}

	private void initViews() {

		backView = (ImageView) findViewById(R.id.backView);
		backViewText = (TextView) findViewById(R.id.backViewText);
		titleName = (TextView) findViewById(R.id.titleName);
		rightButtonText = (TextView) findViewById(R.id.rightButtonText);

		backView.setVisibility(View.GONE);
		backViewText.setVisibility(View.VISIBLE);
		backViewText.setText(getText(R.string.cancel));
		backViewText.setTextColor(getResources().getColor(R.color.tab_select));

		contentTxt = (EditText) findViewById(R.id.contentTxt);

		rightButtonText.setVisibility(View.VISIBLE);
		rightButtonText.setText(getText(R.string.sure));
		rightButtonText.setTextColor(getResources().getColor(R.color.tab_select));

		start_rl = (RelativeLayout) findViewById(R.id.start_rl);
		start_tv = (TextView) findViewById(R.id.start_tv);
		end_rl = (RelativeLayout) findViewById(R.id.end_rl);
		end_tv = (TextView) findViewById(R.id.end_tv);

		resumeType = getIntent().getStringExtra("type");
		actionName = getIntent().getStringExtra("actionName");
		if (!"new".equals(resumeType)) {
			resumeBean = (RsumeBean) getIntent().getSerializableExtra("bean");
			titleName.setText(getText(R.string.title_activity_edit_detail_record));
			updateViews();
		} else {
			resumeBean = new RsumeBean();
			titleName.setText(actionName);
			indexBean = getIntent().getIntExtra("index", 0);
			indexBean = indexBean + 1;
		}
	}

	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @see [类、类#方法、类#成员]
	 */
	private void updateViews() {
		// TODO Auto-generated method stub
		sTimer=CalendarUtils.getDateStr(Long.parseLong(resumeBean.getStartTime()));
		eTimer=CalendarUtils.getDateStr(Long.parseLong(resumeBean.getEndTime()));
		String startT=CalendarUtils.getXieGongYMDStr(Long.parseLong(resumeBean.getStartTime()));
		String endT=CalendarUtils.getXieGongYMDStr(Long.parseLong(resumeBean.getEndTime()));
		String content=resumeBean.getExperDesc();
		start_tv.setText(startT);
		end_tv.setText(endT);
		contentTxt.setText(content);
	}

	private void addListener() {
		backViewText.setOnClickListener(this);
		rightButtonText.setOnClickListener(this);
		start_rl.setOnClickListener(this);
		end_rl.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.backViewText:
			finish();
			break;
		case R.id.rightButtonText:
			if ("new".equals(resumeType)) {
				if (judgmentAction()) {
					transferLocationService();
				}
			} else {
				if (judgmentOldAction()) {
					transferModifyService();
				}
			}
			break;
		case R.id.start_rl:
			startActivityForResult(new Intent(this, ChoiceDateView.class), 2);
			break;
		case R.id.end_rl:
			startActivityForResult(new Intent(this, ChoiceDateView.class), 3);
			break;

		}
	}

	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	private boolean judgmentOldAction() {
		boolean flag = false;
		String startT = start_tv.getText().toString();
		String endT = end_tv.getText().toString();
		String content = contentTxt.getText().toString().trim();
		if ("".equals(startT)) {
			Toast.makeText(getApplicationContext(), "请选择开始时间", Toast.LENGTH_SHORT).show();
		} else {
			if ("".equals(endT)) {
				Toast.makeText(getApplicationContext(), "请选择结束时间", Toast.LENGTH_SHORT).show();
			} else {
				if (StringUtil.isLower(endT, startT)) {
					Toast.makeText(getApplicationContext(), "结束时间不能小于开始时间", Toast.LENGTH_SHORT).show();
				} else {
					if ("".equals(content)) {
						Toast.makeText(getApplicationContext(), "履历描述不能为空", Toast.LENGTH_SHORT).show();
					} else {
						resumeBean.setExperDesc(content);
						resumeBean.setEndTime(CalendarUtils.getTimeFromStr(eTimer,FORMAT));
						resumeBean.setStartTime(CalendarUtils.getTimeFromStr(sTimer,FORMAT));
						flag = true;
					}
				}
			}
		}

		return flag;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {

		case 2:
			if (Activity.RESULT_OK != resultCode || null == data) {
				return;
			}
			Serializable item2 = data.getSerializableExtra("bean");
			if (item2 != null) {
				FeedbackDateBean ftBean = (FeedbackDateBean) item2;
				sTimer = ftBean.getYear() + "-" + ftBean.getMonth() + "-" + ftBean.getDay();
				String szBirthday = ftBean.getYear() + "/" + ftBean.getMonth()+ "/" +ftBean.getDay();
				start_tv.setText(szBirthday);
			}
			break;
		case 3:
			if (Activity.RESULT_OK != resultCode || null == data) {
				return;
			}
			Serializable item3 = data.getSerializableExtra("bean");
			if (item3 != null) {
				FeedbackDateBean ftBean = (FeedbackDateBean) item3;
				eTimer = ftBean.getYear() + "-" + ftBean.getMonth() + "-" + ftBean.getDay();
				String szBirthday = ftBean.getYear() + "/" + ftBean.getMonth()+ "/" +ftBean.getDay();
				end_tv.setText(szBirthday);
			}
			break;

		default:
			break;
		}
	}

	private boolean judgmentAction() {
		boolean flag = false;
		String startT = start_tv.getText().toString();
		String endT = end_tv.getText().toString();
		String content = contentTxt.getText().toString().trim();
		if ("".equals(startT)) {
			Toast.makeText(getApplicationContext(), "请选择开始时间", Toast.LENGTH_SHORT).show();
		} else {
			if ("".equals(endT)) {
				Toast.makeText(getApplicationContext(), "请选择结束时间", Toast.LENGTH_SHORT).show();
			} else {
				if (StringUtil.isLower(endT, startT)) {
					Toast.makeText(getApplicationContext(), "结束时间不能小于开始时间", Toast.LENGTH_SHORT).show();
				} else {
					if ("".equals(content)) {
						Toast.makeText(getApplicationContext(), "编辑内容不能为空", Toast.LENGTH_SHORT).show();
					} else {
						resumeBean.setLocIndex(indexBean);
						resumeBean.setExperDesc(content);
//						resumeBean.setEndTime(CalendarUtils.getTime(eTimer));
//						resumeBean.setStartTime(CalendarUtils.getTime(sTimer));
						resumeBean.setEndTime(CalendarUtils.getTimeFromStr(eTimer, FORMAT));
						resumeBean.setStartTime(CalendarUtils.getTimeFromStr(sTimer, FORMAT));
						flag = true;
					}
				}
			}
		}

		return flag;
	}

	public int getIndexBean() {
		return indexBean;
	}

	public RsumeBean getResumeBean() {
		return resumeBean;
	}

	public void finishUpload(String id) {
		resumeBean.setResumeId(id);
		Toast.makeText(getApplicationContext(), "添加成功", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent();
		intent.putExtra(Constants.COMEBACK, resumeBean);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}

	private void transferLocationService() {
		VisitUnit visit = new VisitUnit();
//		new Action("#.personal.AddUserResumeTask(${cudModelResume})", this, null, visit).run();
		new Action(AddUserResumeTask.class,"${cudModelResume}", this, null, visit).run();
	}

	/**
	 * <一句话功能简述> <功能详细描述>
	 * 
	 * @see [类、类#方法、类#成员]
	 */
	private void transferModifyService() {
		VisitUnit visit = new VisitUnit();
//		new Action("#.personal.EditorUserResumeTask(${cudModelResume})", this, null, visit).run();
		new Action(EditorUserResumeTask.class,"${cudModelResume}", this, null, visit).run();
	}

	public void finishEditor() {
		Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent();
		intent.putExtra(Constants.COMEBACK, resumeBean);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}
}
