package com.xygame.sg.activity.commen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.utils.Constants;

public class EditorTextContentActivity extends SGBaseActivity implements OnClickListener,TextWatcher{
	
	private TextView titleName, rightButtonText,numCount;
	
	private EditText contentTxt;
	
	private View backButton, rightButton;
	
	private int maxNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sg_editor_text_layout);
		initViews();
		intListeners();
		initDatas();
	}

	private void initViews() {
		// TODO Auto-generated method stub
		titleName = (TextView) findViewById(R.id.titleName);
		rightButtonText = (TextView) findViewById(R.id.rightButtonText);
		numCount = (TextView) findViewById(R.id.numCount);
		backButton = findViewById(R.id.backButton);
		rightButton = findViewById(R.id.rightButton);
		contentTxt=(EditText)findViewById(R.id.contentTxt);
	}

	private void initDatas() {
		// TODO Auto-generated method stub
		titleName.setText(getIntent().getStringExtra(Constants.EDITOR_TEXT_TITLE));
		String oral=getIntent().getStringExtra("oral");
		String hint=getIntent().getStringExtra("hint");
		if(oral!=null&&!"null".equals(oral)){
			contentTxt.setText(oral);
		}else{
			if(oral!=null){
				contentTxt.setHint(hint);
			}
		}
		maxNum=getIntent().getIntExtra(Constants.TEXT_EDITOR_NUM, 0);
		rightButton.setVisibility(View.VISIBLE);
		rightButtonText.setVisibility(View.VISIBLE);
		rightButtonText.setText(getResources().getString(
				R.string.sg_comm_comfirm));
		rightButtonText.setTextColor(getResources().getColor(R.color.dark_green));
		if(oral!=null&&!"null".equals(oral)){
			numCount.setText(String.valueOf(oral.length()).concat("/").concat(String.valueOf(maxNum)));
		}else{
			numCount.setText("0/".concat(String.valueOf(maxNum)));
		}
		contentTxt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxNum)}); 
	}

	private void intListeners() {
		// TODO Auto-generated method stub
		backButton.setOnClickListener(this);
		rightButton.setOnClickListener(this);
		contentTxt.addTextChangedListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.backButton) {
			finish();
		}  else if (v.getId() == R.id.rightButton) {
			String str=contentTxt.getText().toString().trim();
			Intent intent = new Intent();
			intent.putExtra(Constants.EDITOR_TEXT_TITLE, str);
			setResult(Activity.RESULT_OK, intent);
			finish();
//			if(!"".equals(str)){
//				Intent intent = new Intent();
//				intent.putExtra(Constants.EDITOR_TEXT_TITLE, str);
//				setResult(Activity.RESULT_OK, intent);
//				finish();
//			}else{
//				Toast.makeText(getApplicationContext(), "内容不能为空", Toast.LENGTH_SHORT).show();
//			}
		} 
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

	@Override
	public void finish() {
		InputMethodManager imm = (InputMethodManager)getSystemService(
				Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(contentTxt.getWindowToken(), 0);
		super.finish();
	}
}
