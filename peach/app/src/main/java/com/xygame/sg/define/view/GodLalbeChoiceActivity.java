package com.xygame.sg.define.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xygame.second.sg.biggod.adapter.GodLableAdapter;
import com.xygame.second.sg.biggod.bean.GodLableBean;
import com.xygame.second.sg.jinpai.bean.JinPaiBigTypeBean;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GodLalbeChoiceActivity extends SGBaseActivity {
    private JinPaiBigTypeBean godTypeBean;
    private ListView listView;
    private GodLableAdapter adapter;
    private String fromWhere;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.god_lable);
        fromWhere=getIntent().getStringExtra("fromWhere");
        godTypeBean=(JinPaiBigTypeBean)getIntent().getSerializableExtra("bean");
        findViewById(R.id.dismiss).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.putExtra("flagStr", "null");
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        listView=(ListView)findViewById(R.id.listView);
        List<GodLableBean> fuFeiDatas = new ArrayList<>();
        String subStr=godTypeBean.getSubStr();
        if (!TextUtils.isEmpty(subStr)){
            try {
                if (!TextUtils.isEmpty(fromWhere)){
                    GodLableBean godLableBean=new GodLableBean();
                    godLableBean.setTitleName("全部");
                    godLableBean.setTitleId("-1");
                    fuFeiDatas.add(godLableBean);
                }
                JSONArray array = new JSONArray(subStr);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    GodLableBean item = new GodLableBean();
                    item.setTitleId(StringUtils.getJsonValue(object, "titleId"));
                    item.setTitleName(StringUtils.getJsonValue(object, "titleName"));
                    fuFeiDatas.add(item);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        adapter=new GodLableAdapter(this,fuFeiDatas);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GodLableBean item = adapter.getItem(position);
                Intent intent = new Intent();
                intent.putExtra(Constants.COMEBACK, item);
                intent.putExtra("flagStr", "have");
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

}
