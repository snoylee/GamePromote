package com.xygame.sg.activity.commen;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xygame.sg.R;

public class ImageOptionDialog extends Dialog implements View.OnClickListener{
	private View saveImageView;
	private ImageOptionListener comfirmListener;

	public ImageOptionDialog(Context context, int attrsr) {
		super(context,attrsr);
	}

	public ImageOptionDialog(Context context,int attrs, ImageOptionListener comfirmListener) {
		super(context,attrs);
		this.comfirmListener=comfirmListener;
	}
	
	/**
	 * 重载方法
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_option_dialog_layout);
		initViews();
		initListener();
		initDatas();

	}

	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @action [请添加内容描述]
	 */
	private void initViews() {
		saveImageView=findViewById(R.id.saveImageView);
	}

	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @action [请添加内容描述]
	 */
	private void initListener() {
		// TODO Auto-generated method stub
		saveImageView.setOnClickListener(this);
	}

	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @action [请添加内容描述]
	 */
	private void initDatas() {
		setCanceledOnTouchOutside(true);
	}

	/**
	 * 重载方法
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.saveImageView){
			comfirmListener.saveImageListener(this);
			dismiss();
		}
	}
}
