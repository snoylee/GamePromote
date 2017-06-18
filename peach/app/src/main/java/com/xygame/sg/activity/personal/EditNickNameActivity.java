package com.xygame.sg.activity.personal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import base.action.Action;
import base.frame.VisitUnit;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.UserPreferencesUtil;

public class EditNickNameActivity extends SGBaseActivity implements View.OnClickListener {
	private ImageView backView;
	private TextView backViewText;
	private TextView titleName;
	private TextView rightButtonText;
	private EditText userNick;
	
	private String nickStr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_nick_name);
		initViews();
		addListener();
	}

	private void initViews() {

		backView = (ImageView) findViewById(R.id.backView);
		backViewText = (TextView) findViewById(R.id.backViewText);
		titleName = (TextView) findViewById(R.id.titleName);
		rightButtonText = (TextView) findViewById(R.id.rightButtonText);
		userNick = (EditText)findViewById(R.id.userNick);

		backView.setVisibility(View.GONE);
		backViewText.setVisibility(View.VISIBLE);
		backViewText.setText(getText(R.string.cancel));
		backViewText.setTextColor(getResources().getColor(R.color.tab_select));

		titleName.setText(getText(R.string.title_activity_edit_nick_name));
		rightButtonText.setVisibility(View.VISIBLE);
		rightButtonText.setText(getText(R.string.sure));
		rightButtonText.setTextColor(getResources().getColor(R.color.tab_select));
		
		userNick.setText(UserPreferencesUtil.getUserNickName(this));

	}

	private void addListener() {
		backViewText.setOnClickListener(this);
		rightButtonText.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.backViewText:
			finish();
			break;
		case R.id.rightButtonText:
			nickStr=userNick.getText().toString().trim();
			if("".equals(nickStr)){
				Toast.makeText(getApplicationContext(), "请输入昵称", Toast.LENGTH_SHORT).show();
			}else{
				transferLocationService();
			}
			break;

		}
	}

	public String getUserNick() {
		return nickStr;
	}

	public void finishActivity(){
		UserPreferencesUtil.setUserNickName(this, nickStr);
		Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
		Intent intent=new Intent(Constants.ACTION_EDITOR_USER_INFO);
		intent.putExtra("flagStr",true);
		sendBroadcast(intent);
		finish();
	}
	
	private void transferLocationService(){
		VisitUnit visit = new VisitUnit();
		new Action("#.personal.EditorUserNickName(${editUser})",this,null,visit).run();
	}
}
