package com.xygame.sg.activity.testpay;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.xygame.sg.R;
import com.xygame.sg.define.listview.OnRefreshListener;
import com.xygame.sg.define.listview.RefreshListView;

public class TestRefreshList extends Activity implements OnRefreshListener {

	private RefreshListView listview;
	private TestListAdapter adapter;
	private List<Integer> datas;
	private int currFlag = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sg_test_list);
		listview = (RefreshListView) findViewById(R.id.listview);
		listview.setOnRefreshListener(this);
		datas=new ArrayList<Integer>();
		addDatas();
		listview.setAdapter(adapter);
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		adapter.clearDatas();
		adapter.notifyDataSetChanged();
		currFlag = 0;
		refreshDatas();
	}

	@Override
	public void onLoadMoring() {
		// TODO Auto-generated method stub
		loadMoreDatas();
	}

	private void addDatas() {
		for (int i = 0; i < 20; i++) {
			currFlag = currFlag + 1;
			datas.add(currFlag);
		}
		if (adapter == null) {
			adapter = new TestListAdapter(this, datas);
		} else {
			adapter.addDatas(datas);
			adapter.notifyDataSetChanged();
		}
	}
	
	private void refreshDatas(){
		new Thread(new Runnable() {
			public void run() {
				loadData();
				handler.sendEmptyMessage(2);
			}

			public void loadData() {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void loadMoreDatas() {
		new Thread(new Runnable() {
			public void run() {
				loadData();
				handler.sendEmptyMessage(1);
			}

			public void loadData() {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (null != msg) {
				switch (msg.what) {
				case 1:
					addDatas();
					listview.onRefreshFinish();
					break;
				case 2:
					addDatas();
					listview.onRefreshFinish();
					break;
				default:
					break;
				}
			}
		}
	};
}
