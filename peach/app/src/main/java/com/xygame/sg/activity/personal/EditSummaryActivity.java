package com.xygame.sg.activity.personal;


import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.utils.Constants;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import base.action.Action;
import base.frame.VisitUnit;

public class EditSummaryActivity extends SGBaseActivity implements View.OnClickListener,TextWatcher {
    private ImageView backView;
    private TextView backViewText,numCount;
    private TextView titleName;
    private TextView rightButtonText;
    private EditText contentTxt;
    private String personalInfo;
    private int maxNum=500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_summary);
        initViews();
        addListener();
    }

    private void initViews() {

        backView = (ImageView) findViewById(R.id.backView);
        backViewText = (TextView) findViewById(R.id.backViewText);
        titleName = (TextView) findViewById(R.id.titleName);
        rightButtonText = (TextView) findViewById(R.id.rightButtonText);
        numCount = (TextView) findViewById(R.id.numCount);
        backView.setVisibility(View.GONE);
        backViewText.setVisibility(View.VISIBLE);
        backViewText.setText(getText(R.string.cancel));
        backViewText.setTextColor(getResources().getColor(R.color.tab_select));
        contentTxt=(EditText)findViewById(R.id.contentTxt);
        titleName.setText(getText(R.string.title_activity_edit_summary));
        rightButtonText.setVisibility(View.VISIBLE);
        rightButtonText.setText(getText(R.string.sure));
        rightButtonText.setTextColor(getResources().getColor(R.color.tab_select));
        String summary=getIntent().getStringExtra("summary");
        if(summary!=null&&!"null".equals(summary)){
        	contentTxt.setText(summary);
        	numCount.setText(summary.length()+"/".concat(String.valueOf(maxNum)));
        }else{
        	numCount.setText("0/".concat(String.valueOf(maxNum)));
        }
        contentTxt.setSelection(contentTxt.getText().length());
    }

    private void addListener() {
        backViewText.setOnClickListener(this);
        rightButtonText.setOnClickListener(this);
        contentTxt.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backViewText:
                finish();
                break;
            case R.id.rightButtonText:
            	personalInfo=contentTxt.getText().toString().trim();
    			if(!"".equals(personalInfo)){
    				transferLocationService();
    			}else{
    				Toast.makeText(getApplicationContext(), "内容不能为空", Toast.LENGTH_SHORT).show();
    			}
                break;


        }
    }
    
    public String getPersonalInfo(){
    	return personalInfo;
    }

    @Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		String shortStr=s.toString();
		if("".equals(shortStr)){
			numCount.setText("0/".concat(String.valueOf(maxNum)));
		}else{
			numCount.setText(String.valueOf(shortStr.length()).concat("/").concat(String.valueOf(maxNum)));
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
		new Action("#.personal.EditorSTSIntoduceInfo(${editUserIntro})",this,null,visit).run();
	}
}
