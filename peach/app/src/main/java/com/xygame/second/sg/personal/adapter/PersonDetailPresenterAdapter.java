package com.xygame.second.sg.personal.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xygame.second.sg.personal.activity.PersonalDetailActivity;
import com.xygame.second.sg.personal.bean.PayMoneyBean;
import com.xygame.second.sg.personal.bean.PersonDetailPersenterBean;
import com.xygame.second.sg.personal.bean.PhotoBean;
import com.xygame.second.sg.sendgift.bean.Presenter;
import com.xygame.sg.R;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.image.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 2016/8/31.
 */
public class PersonDetailPresenterAdapter extends BaseAdapter {
    private Context context;
    private List<PersonDetailPersenterBean> vector;
    private ImageLoader mImageLoader;

    public PersonDetailPresenterAdapter(Context context, List<PersonDetailPersenterBean> vector) {
        this.context = context;
        mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        if (vector != null) {
            this.vector = vector;
        } else {
            this.vector = new ArrayList<>();
        }
    }

    @Override
    public int getCount() {
        return vector.size();
    }

    @Override
    public PersonDetailPersenterBean getItem(int position) {
        return vector.get(position);
    }

    public void addDatas(List<PersonDetailPersenterBean> datas) {
        vector.clear();
        vector.addAll(datas);
    }

    public List<PersonDetailPersenterBean> getDatas() {
        return vector;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 初始化View
     */
    private class ViewHolder {
        private CircularImage headImage;
        private ImageView indexIcon;
        private TextView userNick;
    }

    /**
     * 添加数据
     */
    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.person_detail_presenter_item,
                    null);
            viewHolder = new ViewHolder();
            viewHolder.userNick=(TextView)convertView.findViewById(R.id.userNick);
            viewHolder.indexIcon=(ImageView)convertView.findViewById(R.id.indexIcon);
            viewHolder.headImage=(CircularImage)convertView.findViewById(R.id.headImage);
            convertView.setTag(viewHolder);
        } else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PersonDetailPersenterBean item=vector.get(position);
        switch (position){
            case 0:
                viewHolder.userNick.setVisibility(View.GONE);
                viewHolder.indexIcon.setVisibility(View.VISIBLE);
                viewHolder.indexIcon.setImageResource(R.drawable.index_no1);
                break;
            case 1:
                viewHolder.userNick.setVisibility(View.GONE);
                viewHolder.indexIcon.setVisibility(View.VISIBLE);
                viewHolder.indexIcon.setImageResource(R.drawable.index_no2);
                break;
            case 2:
                viewHolder.userNick.setVisibility(View.GONE);
                viewHolder.indexIcon.setVisibility(View.VISIBLE);
                viewHolder.indexIcon.setImageResource(R.drawable.index_no3);
                break;
            default:
                viewHolder.userNick.setVisibility(View.VISIBLE);
                viewHolder.indexIcon.setVisibility(View.GONE);
                viewHolder.userNick.setText("No.".concat(String.valueOf(position+1)));
                break;
        }
        mImageLoader.loadImage(getItem(position).getUserIcon(), viewHolder.headImage, true);
        convertView.setOnClickListener(new IntoPersonalDeltail(item));
        return convertView;
    }

    private class IntoPersonalDeltail implements View.OnClickListener{
        private PersonDetailPersenterBean presenter;
        public IntoPersonalDeltail(PersonDetailPersenterBean presenter){
            this.presenter=presenter;
        }

        @Override
        public void onClick(View v) {
            intoPersonalAct(presenter);
        }
    }

    private void intoPersonalAct(PersonDetailPersenterBean presenter) {
        Intent intent = new Intent(context, PersonalDetailActivity.class);
        intent.putExtra("userNick",presenter.getUsernick());
        intent.putExtra("userId", presenter.getUserId());
        context.startActivity(intent);
    }
}