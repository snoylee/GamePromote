package com.xygame.sg.activity.testpay;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.xygame.sg.R;
import com.xygame.sg.activity.personal.bean.PriseBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 2016/12/13.
 */
public class TestCeAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<PriseBean> strList;
    private Context context;
    public TestCeAdapter(Context context, List<PriseBean> strList) {
        this.context = context;
        if (strList == null) {
            this.strList = new ArrayList<PriseBean>();
        }else{
            this.strList = strList;
        }
    }


    @Override
    public com.xygame.sg.activity.testpay.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = ViewHolder.get(context, parent, R.layout.test_ce_item);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(com.xygame.sg.activity.testpay.ViewHolder holder, int position) {
        holder.setImageResource(R.id.userImage,R.drawable.default_avatar);
    }

    @Override
    public int getItemCount() {
        return strList.size();
    }
}
