package com.xygame.second.sg.personal.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.xygame.second.sg.comm.adapter.TestPingYingAdapter;
import com.xygame.second.sg.comm.inteface.OnTouchingLetterChangedListener;
import com.xygame.second.sg.defineview.SideBar;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;

import java.util.ArrayList;
import java.util.List;

public class GroupTestActivity extends SGBaseActivity implements OnClickListener {
    /**
     * 公用变量部分
     */
    private TextView titleName;
    private View backButton;

    private TestPingYingAdapter adapter;
    private ExpandableListView eListView;
    private List<String> names;
    private SideBar sideBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guan_zhu_layout);
        initViews();
        initListensers();
        initDatas();
    }

    private void initViews() {
        eListView = (ExpandableListView) findViewById(R.id.elist);
        backButton = findViewById(R.id.backButton);
        titleName = (TextView) findViewById(R.id.titleName);
        sideBar = (SideBar) findViewById(R.id.sideBar);
    }

    private void initListensers() {
        backButton.setOnClickListener(this);
    }

    private void initDatas() {
        titleName.setText("关注");
        names=new ArrayList<String>();
        names.add("lxz");
        names.add("A酱");
        names.add("芙兰");
        names.add("鱼鱼");
        names.add("妹妹");
        names.add("你好");
        names.add("林小姐");
        names.add("联盟");
        names.add("L");
        names.add("xdsfsdggsdsf");
        names.add("星星");
        names.add("靴刀誓死");
        names.add("Java");
        names.add("@wang");
        names.add("&qi");
        names.add("#jlk");
        names.add("倒塌");
        names.add("黑人");
        names.add("a妹");
        names.add("aYa");
        names.add("lxz");
        names.add("A酱");
        names.add("芙兰");
        names.add("鱼鱼");
        names.add("妹妹");
        names.add("你好");
        names.add("林小姐");
        names.add("联盟");
        names.add("L");
        names.add("xdsfsdggsdsf");
        names.add("星星");
        names.add("靴刀誓死");
        names.add("Java");
        names.add("@wang");
        names.add("&qi");
        names.add("#jlk");
        names.add("倒塌");
        names.add("黑人");
        names.add("a妹");
        names.add("aYa");
        adapter = new TestPingYingAdapter(this,names);
        eListView.setAdapter(adapter);

        //展开所有
        for (int i = 0, length = adapter.getGroupCount(); i < length; i++) {
            eListView.expandGroup(i);
        }

        sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                int index=adapter.getAssort().getHashList().indexOfKey(s);
                if(index!=-1)
                {
                    eListView.setSelectedGroup(index);;
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.backButton) {
            finish();
        }
    }
}
