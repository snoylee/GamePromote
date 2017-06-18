package com.xygame.sg.activity.personal.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.activity.personal.adapter.LikeListAdapter;
import com.xygame.sg.activity.personal.adapter.PriceAdapter;
import com.xygame.sg.define.listview.RefreshListView;


public class LikeListFragment extends Fragment {

    private TextView like_num_tv;
    private RefreshListView like_lv;
    private LikeListAdapter adapter;

    public LikeListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_like_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findViews();
        initListView();
    }

    private void findViews() {
        like_num_tv = (TextView) getView().findViewById(R.id.like_num_tv);
        like_lv = (RefreshListView) getView().findViewById(R.id.like_lv);
    }

    private void initListView() {
        adapter = new LikeListAdapter(getActivity());
        like_lv.setAdapter(adapter);
    }







}
