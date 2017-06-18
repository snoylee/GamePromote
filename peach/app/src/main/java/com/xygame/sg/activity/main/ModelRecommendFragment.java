package com.xygame.sg.activity.main;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseFragment;
import com.xygame.sg.widget.refreash.PullToRefreshBase;
import com.xygame.sg.widget.refreash.PullToRefreshListView3;

import java.util.ArrayList;
import java.util.List;


public class ModelRecommendFragment extends SGBaseFragment implements PullToRefreshBase.OnRefreshListener2{
    private PullToRefreshListView3 listView;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        initDatas();
        addListener();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.model_recommend_layout, null);
    }

    private void initViews() {
        listView = (PullToRefreshListView3) getView().findViewById(R.id.lv);
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);//.BOTH);
    }

    private void initDatas() {
        List<Integer> datas=new ArrayList<Integer>();
        for(int i=0;i<100;i++){
            datas.add(i);
        }
        listView.setAdapter(new MainAdapter(getActivity(), datas));
    }

    private void addListener() {
        listView.setOnRefreshListener(this);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
//        isLoading=true;
//        mCurrentPage=1;
//        requestNoticesList();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
//        if (isLoading){
//            mCurrentPage=mCurrentPage+1;
//            requestNoticesList();
//        }else{
//            falseDatas();
//        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2:
                    listView.onRefreshComplete();
                    break;
                default:
                    break;
            }

        }
    };

    private void falseDatas(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(1000);
                        Message m = handler.obtainMessage();
                        m.what=2;
                        m.sendToTarget();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }).start();
    }

    class MainAdapter extends BaseAdapter {

        private Context context;
        private List<Integer> datas;

        public MainAdapter(Context context,List<Integer> datas){
            this.context=context;
            this.datas=datas;
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return datas.size();
        }

        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return datas.get(arg0);
        }

        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        public View getView(int arg0, View arg1, ViewGroup arg2) {
            // TODO Auto-generated method stub
            View view=LayoutInflater.from(context).inflate(R.layout.fragment_main, null);
            TextView value=(TextView)view.findViewById(R.id.value);
            value.setText(datas.get(arg0)+"");
            return view;
        }

    }
}
