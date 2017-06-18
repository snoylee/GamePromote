package com.xygame.sg.activity.commen;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.activity.main.MainFrameActivity;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;

import base.adapter.ListenerBox;
import base.adapter.MapAdapter;
import base.adapter.MapContent;
import base.frame.VisitUnit;

public class HostDialog extends Dialog implements View.OnClickListener{
	private Context context;
	private TextView tipText;
	private View comfirmView;
	private ButtonOneListener comfirmListener;
	private String textTip;

	public HostDialog(Context context, int attrsr) {
		super(context,attrsr);this.context = context;
	}

	public HostDialog(Context context, String textTip, int attrs, ButtonOneListener comfirmListener) {
		super(context,attrs);
		this.comfirmListener=comfirmListener;
		this.textTip=textTip;
		this.context = context;
	}
	
	/**
	 * 重载方法
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sg_dialog_host);
		MapAdapter.AdaptInfo adaptInfo= new MapAdapter.AdaptInfo();
		adaptInfo.listviewItemLayoutId = R.layout.sg_model_list_item_host;
		adaptInfo.viewIds = new Integer[]{R.id.address};
		adaptInfo.addActionListener(new MapAdapter.ActionListener(R.id.addressitem, MapAdapter.ActionListener.OnClick) {
			@Override
			public void handle(MapAdapter mapAdapter, View view, int pos, ListenerBox listenerBox) {

				Constants.baseUrl = mapAdapter.getItem(pos).toString();
				Constants.init();
				Constants.Configure.getInstance().reset();
				ShowMsgDialog.cancel();
			}
		});
		MapAdapter mapAdapter = new MapAdapter(getContext(),adaptInfo){
			@Override
			protected boolean setView(int pos, Object item, Object value, View convertView, View theView) {

				if(Constants.baseUrls.indexOf(Constants.baseUrl)==pos){
					convertView.setBackgroundColor(getContext().getResources().getColor(R.color.dark_green));
				}else{
					convertView.setBackgroundColor(getContext().getResources().getColor(R.color.transparent));
				}

				super.setView(pos, item, value, convertView, theView);
				((TextView) theView).setText(((TextView) theView).getText().toString().replace("/nsg-ws/base",""));
				if(((TextView) theView).getText().toString().contains("1.5")){
					((TextView)convertView.findViewById(R.id.text)).setText(" 田星瑞");
//					((android.app.Activity)context).findViewById(R.id.clicktotest).setVisibility(View.VISIBLE);Constants.Mode=Constants.MODE_TEST;
				}else if(((TextView) theView).getText().toString().contains("2.85")){
					((TextView)convertView.findViewById(R.id.text)).setText(" 孙芳芳");
					Constants.Mode=Constants.MODE_TEST;
//					((android.app.Activity)context).findViewById(R.id.clicktotest).setVisibility(View.VISIBLE);
				}else if(((TextView) theView).getText().toString().contains("2.32")){
					((TextView)convertView.findViewById(R.id.text)).setText(" 刘宏玮");
					Constants.Mode=Constants.MODE_TEST;
//					((android.app.Activity)context).findViewById(R.id.clicktotest).setVisibility(View.VISIBLE);
				}else if(((TextView) theView).getText().toString().contains("open")){
					((TextView)convertView.findViewById(R.id.text)).setText(" 阿里云");
				}else if(((TextView) theView).getText().toString().contains("1.223")){
					((TextView)convertView.findViewById(R.id.text)).setText(" 内网");
				}
				return false;
			}
		};

				((ListView) findViewById(R.id.listview)).setAdapter(mapAdapter);

		mapAdapter.setItemDataSrc(new MapContent(Constants.baseUrls));
		mapAdapter.notifyDataSetChanged();

	}

	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @action [请添加内容描述]
	 */
	private void initViews() {
		// TODO Auto-generated method stub
		tipText=(TextView)findViewById(R.id.tipText);
		comfirmView=findViewById(R.id.comfirmView);
	}

	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @action [请添加内容描述]
	 */
	private void initListener() {
		// TODO Auto-generated method stub
		comfirmView.setOnClickListener(this);
	}

	/** 
	 * <一句话功能简述>
	 * <功能详细描述>
	 * @action [请添加内容描述]
	 */
	private void initDatas() {
		// TODO Auto-generated method stub
		setCanceledOnTouchOutside(false);
	}

	/**
	 * 重载方法
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.comfirmView){
			comfirmListener.confrimListener(this);
			dismiss();
		}
	}
}
