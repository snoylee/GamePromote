package com.xygame.second.sg.personal.guanzhu.group;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.xygame.second.sg.comm.inteface.DeleteGZ_GroupListener;
import com.xygame.second.sg.comm.inteface.JustClickListener;
import com.xygame.second.sg.personal.guanzhu.member.GZ_MemberGroupListActivity;
import com.xygame.sg.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 2016/9/2.
 */
public class SlideAdapter extends BaseAdapter implements SectionIndexer {
    private Context context;
    private List<GZ_GroupBean> vector;
    private SlideView.OnSlideListener onSlideListener;
    private DeleteGZ_GroupListener cancelBlackListListener;

    public SlideAdapter(Context context, List<GZ_GroupBean> vector) {
        this.context = context;
        if (vector != null) {
            this.vector = vector;
        } else {
            this.vector = new ArrayList<>();
        }
    }

    public List<GZ_GroupBean> getDatas(){
        return vector;
    }

    public void addSlidViewListener(SlideView.OnSlideListener onSlideListener) {
        this.onSlideListener = onSlideListener;
    }

    public void addCancelBlackListListener(DeleteGZ_GroupListener cancelBlackListListener) {
        this.cancelBlackListListener = cancelBlackListListener;
    }

    @Override
    public int getCount() {
        return vector.size();
    }

    @Override
    public GZ_GroupBean getItem(int position) {
        return vector.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GZ_GroupBean item = vector.get(position);
        ViewHolder viewHolder = null;
        SlideView slideView = (SlideView) convertView;
        if (null == slideView) {

            View itemView = LayoutInflater.from(context).inflate(
                    R.layout.gz_list_item, parent, false);
            slideView = new SlideView(context);
            slideView.setContentView(itemView);
            viewHolder = new ViewHolder(slideView);
            slideView.setOnSlideListener(onSlideListener);
            slideView.addClickListener(new AddClickListener(item));
            slideView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) slideView.getTag();
        }
        item.slideView = slideView;
        item.slideView.shrink();
//        //根据position获取分类的首字母的Char ascii值
//        int section = getSectionForPosition(position);
//        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
//        if (position == getPositionForSection(section)) {
//            viewHolder.tvLetter.setVisibility(View.VISIBLE);
//            viewHolder.tvLetter.setText(item.getSortLetters());
//        } else {
//            viewHolder.tvLetter.setVisibility(View.GONE);
//        }

        viewHolder.userName.setText(item.getName());
        viewHolder.personalCount.setText(item.getCount().concat("人"));
        viewHolder.deleteHolder.setOnClickListener(new CancelBlackListAction(position, item));
        return slideView;
    }

    public void clearDatas() {
        this.vector.clear();
        notifyDataSetChanged();
    }

    public void addDatas(List<GZ_GroupBean> datas) {
        this.vector = datas;
        notifyDataSetChanged();
    }

    public void updateDatas(GZ_GroupBean item) {
        for (int i=0;i<vector.size();i++){
            if (item.getId().equals(vector.get(i).getId())){
                vector.remove(i);
                break;
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    @Override
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = vector.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        return vector.get(position).getSortLetters().charAt(0);
    }

    public void updateGroupCount(String sourceGroupId, String targGroupId, boolean isAdd) {
        if (isAdd){
            for (int i=0;i<vector.size();i++){
                if (sourceGroupId.equals(vector.get(i).getId())){
                    String count=vector.get(i).getCount();
                    vector.get(i).setCount(String.valueOf(Integer.parseInt(count)-1));
                    break;
                }
            }

            for (int i=0;i<vector.size();i++){
                if (targGroupId.equals(vector.get(i).getId())){
                    String count=vector.get(i).getCount();
                    vector.get(i).setCount(String.valueOf(Integer.parseInt(count)+1));
                    break;
                }
            }
        }else{
            for (int i=0;i<vector.size();i++){
                if (sourceGroupId.equals(vector.get(i).getId())){
                    String count=vector.get(i).getCount();
                    vector.get(i).setCount(String.valueOf(Integer.parseInt(count)-1));
                    break;
                }
            }
        }
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        public TextView userName,tvLetter,personalCount;
        public ViewGroup deleteHolder;

        ViewHolder(View view) {
            userName = (TextView) view.findViewById(R.id.userName);
            deleteHolder = (ViewGroup) view.findViewById(R.id.holder);
            tvLetter=(TextView)view.findViewById(R.id.catalog);
            personalCount=(TextView)view.findViewById(R.id.personalCount);
        }
    }

    private class CancelBlackListAction implements View.OnClickListener {
        private GZ_GroupBean item;
        private int position;

        public CancelBlackListAction(int position,GZ_GroupBean item) {
            this.item = item;
            this.position=position;
        }

        @Override
        public void onClick(View v) {
            cancelBlackListListener.cancelBlackListListener(position,item);
        }
    }

    private class AddClickListener implements JustClickListener{
        private  GZ_GroupBean item;
        public AddClickListener( GZ_GroupBean item ){
            this.item=item;
        }

        @Override
        public void justClickAction() {
            GZ_GroupBeanTemp itC = new GZ_GroupBeanTemp();
            itC.setId("0");
            itC.setName("未分组");
            GZ_GroupBeanTemp it = new GZ_GroupBeanTemp();
            it.setId(item.getId());
            it.setName(item.getName());
            GZ_GroupBeanTransfer transfer = new GZ_GroupBeanTransfer();
            transfer.setCurrBean(it);
            List<GZ_GroupBeanTemp> alldatas = new ArrayList<>();
            for (GZ_GroupBean its : getDatas()) {
                GZ_GroupBeanTemp it1 = new GZ_GroupBeanTemp();
                it1.setId(its.getId());
                it1.setName(its.getName());
                alldatas.add(it1);
            }
            alldatas.add(0,itC);
            transfer.setAlldatas(alldatas);
            Intent intent = new Intent(context, GZ_MemberGroupListActivity.class);
            intent.putExtra("bean", transfer);
            context.startActivity(intent);
        }
    }
}
