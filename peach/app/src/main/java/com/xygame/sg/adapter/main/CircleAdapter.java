package com.xygame.sg.adapter.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xygame.sg.R;
import com.xygame.sg.activity.circle.CommentDetailActivity;
import com.xygame.sg.activity.commen.LoginWelcomActivity;
import com.xygame.sg.activity.commen.ReportFristActivity;
import com.xygame.sg.activity.image.ImagePagerActivity;
import com.xygame.sg.activity.image.ImagePagerCircleActivity;
import com.xygame.sg.activity.personal.bean.BrowerPhotoesBean;
import com.xygame.sg.activity.personal.bean.TransferBrowersBean;
import com.xygame.sg.bean.circle.CircleBean;
import com.xygame.sg.bean.circle.CircleCommenters;
import com.xygame.sg.bean.circle.CirclePraisers;
import com.xygame.sg.bean.circle.CircleRess;
import com.xygame.sg.bean.comm.ProvinceBean;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.task.utils.AssetDataBaseManager;
import com.xygame.sg.task.utils.AssetDataBaseManager.CityBean;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.VideoImageLoader;
import com.xygame.sg.vido.VideoPlayerActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xy on 2015/11/16.
 */
public class CircleAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private ImageLoader imageLoader;
    private List<CircleBean> datas;
    private VideoImageLoader videoImageLoader;
    private int currPosition=0;
    private Handler.Callback callback;
    private ShowPresant showPresant;

    public CircleAdapter(Context context,Handler.Callback callback, List<CircleBean> attentions) {
        super();
        this.context = context;
        if (attentions==null){
            this.datas = new ArrayList<CircleBean>();
        }else{
            this.datas = attentions;
        }
        this.callback=callback;
        videoImageLoader  = VideoImageLoader.getInstance();
        inflater = LayoutInflater.from(context);
        imageLoader =ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
    }

    public void addPresentDialogListener(ShowPresant showPresant){
        this.showPresant=showPresant;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public CircleBean getItem(int i) {
        return datas.get(i);
    }

    public void updateComment(CircleBean item){
        for (int i=0;i<datas.size();i++){
            if (item.getMoodId().equals(datas.get(i).getMoodId())){
                datas.get(i).setCommenters(item.getCommenters());
                datas.get(i).setPraiseCount(item.getPraiseCount());
                datas.get(i).setCommentCount(item.getCommentCount());
                break;
            }
        }
        notifyDataSetChanged();
    }

    public void updatePrise(CircleBean item){
        for (int i=0;i<datas.size();i++){
            if (item.getMoodId().equals(datas.get(i).getMoodId())){
                datas.get(i).setHasPraise(item.getHasPraise());
                datas.get(i).setPraisers(item.getPraisers());
                break;
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int posion, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.circle_item, null);
            holder = new ViewHolder();
            holder.priseView=convertView.findViewById(R.id.priseView);
            holder.singleView=convertView.findViewById(R.id.singleView);
            holder.moreView=convertView.findViewById(R.id.moreView);
            holder.commentButton=convertView.findViewById(R.id.commentButton);
            holder.priseButton=convertView.findViewById(R.id.priseButton);
            holder.juBaoButton=convertView.findViewById(R.id.juBaoButton);
            holder.sendPresantView=convertView.findViewById(R.id.sendPresantView);
            holder.preview_play_iv=(ImageView)convertView.findViewById(R.id.preview_play_iv);

            holder.verticalImage = (ImageView) convertView.findViewById(R.id.verticalImage);
            holder.horizontalImage=(ImageView)convertView.findViewById(R.id.horizontalImage);
            holder.cubImage=(ImageView)convertView.findViewById(R.id.cubImage);
            holder.singleVideoImage=(ImageView)convertView.findViewById(R.id.singleVideoImage);
            holder.priseImage=(ImageView)convertView.findViewById(R.id.priseImage);
            holder.userImage = (CircularImage) convertView.findViewById(R.id.userImage);

            holder.priserGrid = (GridView) convertView.findViewById(R.id.priserGrid);
            holder.gridview = (GridView) convertView.findViewById(R.id.gridview);

            holder.timerText = (TextView) convertView.findViewById(R.id.timerText);
            holder.oralText = (TextView) convertView.findViewById(R.id.oralText);
            holder.locationText = (TextView) convertView.findViewById(R.id.locationText);
            holder.userName = (TextView) convertView.findViewById(R.id.userName);
            holder.commentCount=(TextView)convertView.findViewById(R.id.commentCount);
            holder.priseCount=(TextView)convertView.findViewById(R.id.priseCount);
            holder.presantNums=(TextView)convertView.findViewById(R.id.presantNums);

            holder.commentContent = (LinearLayout) convertView.findViewById(R.id.commentContent);
            holder.circlePhotoAdapter=new CirclePhotoAdapter(context,null);
            holder.circlePriserAdapter=new CirclePriserAdapter(context,null);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.commentContent.removeAllViews();
        holder.singleView.setVisibility(View.GONE);
        holder.preview_play_iv.setVisibility(View.GONE);
        CircleBean item=datas.get(posion);
        holder.commentCount.setText("("+item.getCommentCount()+")");
        holder.priseCount.setText("("+item.getPraiseCount()+")");
        holder.presantNums.setText("("+item.getGiftCount()+")");
        if (item.getHasPraise()>=1){
            holder.priseImage.setImageResource(R.drawable.yizanaed);
        }else{
            holder.priseImage.setImageResource(R.drawable.dianzana);
        }
        String userIcon = item.getPublisher().getUserIcon();
        if (!StringUtils.isEmpty(userIcon)) {
            imageLoader.loadImage(userIcon, holder.userImage, false);
        }
        holder.userName.setText(item.getPublisher().getUsernick());
        String address="";
        if (item.getLocProvince()!=0){
            ProvinceBean provinceBean=AssetDataBaseManager.getManager().queryProviceById(item.getLocProvince());
            if (provinceBean!=null){
                address=provinceBean.getName();
            }
        }
        boolean flag=false;
        for (String str : Constants.CITY_STRICT) {
            if (address.contains(str)) {
                flag=true;
            }
        }
        if (!flag){
            if (item.getLocCity()!=0){
                CityBean it = AssetDataBaseManager.getManager().queryCityById(item.getLocCity());
                if (it.getName()!=null){
                    address=address.concat(it.getName());
                }
            }
        }

        if (!"".equals(address)){
            holder.locationText.setText(address);
        }else{
            holder.locationText.setVisibility(View.GONE);
        }

        if (item.getContent()!=null&&!"null".equals(item.getContent())&&!"".equals(item.getContent())){
            holder.oralText.setVisibility(View.VISIBLE);
            holder.oralText.setText(item.getContent());
        }else{
            holder.oralText.setVisibility(View.GONE);
        }

        if (item.getRess().size()==1){
            holder.singleVideoImage.setVisibility(View.GONE);
            holder.singleView.setVisibility(View.VISIBLE);
            holder.gridview.setVisibility(View.GONE);
            if (item.getRess().get(0).getResType()==1){
                holder.singleVideoImage.setVisibility(View.GONE);
                if (item.getRess().get(0).getResHeight()!=null&&item.getRess().get(0).getResWidth()!=null){
                    float px=(float)item.getRess().get(0).getResHeight()/item.getRess().get(0).getResWidth();
                    if (px>1){
                        holder.horizontalImage.setVisibility(View.GONE);
                        holder.verticalImage.setVisibility(View.VISIBLE);
                        holder.cubImage.setVisibility(View.GONE);
                        imageLoader.loadImage(item.getRess().get(0).getResUrl(), holder.verticalImage, true);
//                        imageLoader.loadOrigaImage(item.getRess().get(0).getResUrl(), holder.verticalImage);
                    }else if(px<1){
                        holder.horizontalImage.setVisibility(View.VISIBLE);
                        holder.verticalImage.setVisibility(View.GONE);
                        holder.cubImage.setVisibility(View.GONE);
                        imageLoader.loadImage(item.getRess().get(0).getResUrl(), holder.horizontalImage, true);
//                        imageLoader.loadOrigaImage(item.getRess().get(0).getResUrl(), holder.horizontalImage);
                    }else if(px==1){
                        holder.horizontalImage.setVisibility(View.GONE);
                        holder.verticalImage.setVisibility(View.GONE);
                        holder.cubImage.setVisibility(View.VISIBLE);
                        imageLoader.loadImage(item.getRess().get(0).getResUrl(), holder.cubImage, true);
//                        imageLoader.loadOrigaImage(item.getRess().get(0).getResUrl(), holder.horizontalImage);
                    }
                }else{
                    holder.horizontalImage.setVisibility(View.VISIBLE);
                    holder.verticalImage.setVisibility(View.GONE);
                    holder.cubImage.setVisibility(View.GONE);
                    imageLoader.loadImage(item.getRess().get(0).getResUrl(), holder.horizontalImage, true);
//                    imageLoader.loadOrigaImage(item.getRess().get(0).getResUrl(), holder.horizontalImage);
                }
//                imageLoader.loadImage(item.getRess().get(0).getResUrl(), holder.singleImage, false);
                holder.singleView.setOnClickListener(new ShowSingleImage(item.getRess().get(0).getResUrl(),item.getPublisher().getUsernick(),item.getPublisher().getUserId(),item.getRess().get(0).getResId()));
            }else{
                holder.singleVideoImage.setVisibility(View.VISIBLE);
                holder.horizontalImage.setVisibility(View.GONE);
                holder.verticalImage.setVisibility(View.GONE);
                holder.cubImage.setVisibility(View.GONE);
                holder.preview_play_iv.setVisibility(View.VISIBLE);
                videoImageLoader.DisplayImage(item.getRess().get(0).getResUrl(), holder.singleVideoImage);
                holder.singleVideoImage.setOnClickListener(new ShowVideoView(item.getRess().get(0).getResUrl()));
            }
        }else{
            holder.singleView.setVisibility(View.GONE);
            holder.horizontalImage.setVisibility(View.GONE);
            holder.cubImage.setVisibility(View.GONE);
            holder.verticalImage.setVisibility(View.GONE);
            holder.gridview.setVisibility(View.VISIBLE);
            holder.circlePhotoAdapter.addDatas(item.getRess());
            holder.gridview.setAdapter(holder.circlePhotoAdapter);
            holder.gridview.setOnItemClickListener(new ShowImages(item));
        }
        holder.timerText.setText(CalendarUtils.getHenGongDateDis(item.getCreateTime()));
        if (item.getPraisers()!=null){
            if (item.getPraisers().size()>0){
                holder.priseView.setVisibility(View.VISIBLE);
                holder.priserGrid.setVisibility(View.VISIBLE);
                if (item.getPraisers().size()>9){
                    holder.moreView.setVisibility(View.VISIBLE);
                    holder.circlePriserAdapter.addDatas(item.getPraisers(),9);
                    holder.priserGrid.setAdapter(holder.circlePriserAdapter);
                }else{
                    holder.moreView.setVisibility(View.GONE);
                    holder.circlePriserAdapter.addDatas(item.getPraisers(),0);
                    holder.priserGrid.setAdapter( holder.circlePriserAdapter);
                }
            }else{
                holder.priseView.setVisibility(View.GONE);
                holder.priserGrid.setVisibility(View.GONE);
            }
        }else {
            holder.priseView.setVisibility(View.GONE);
            holder.priserGrid.setVisibility(View.GONE);
        }
        if (item.getCommenters()!=null){
            holder.commentContent.setVisibility(View.VISIBLE);
            holder.commentContent.removeAllViews();
            for (CircleCommenters ccIt:item.getCommenters()){
                View commetView=inflater.inflate(R.layout.comment_item, null);
                TextView nameText=(TextView)commetView.findViewById(R.id.nameText);
                TextView commentText=(TextView)commetView.findViewById(R.id.commentText);
                nameText.setText(ccIt.getCommenter().getUsernick().concat("："));
                commentText.setText(ccIt.getContent());
                holder.commentContent.addView(commetView);
            }
        }else{
            holder.commentContent.setVisibility(View.GONE);
        }
        holder.juBaoButton.setOnClickListener(new FeedBackListenter(item));
        holder.priseButton.setOnClickListener(new PriserListenter(item,posion));
        holder.commentButton.setOnClickListener(new IntoCommentDetail(item));
        holder.sendPresantView.setOnClickListener(new SendPresentListener(item,posion));
        return convertView;
    }

    public void updatePriser() {
        if (datas.get(currPosition).getHasPraise()>=1){
            datas.get(currPosition).setHasPraise(0);
            List<CirclePraisers> praisers=datas.get(currPosition).getPraisers();
            praisers.remove(praisers.size() - 1);
            datas.get(currPosition).setPraisers(praisers);
            int priserCount=datas.get(currPosition).getPraiseCount();
            priserCount=priserCount-1;
            datas.get(currPosition).setPraiseCount(priserCount);
        }else{
            datas.get(currPosition).setHasPraise(3);
            List<CirclePraisers> praisers=datas.get(currPosition).getPraisers();
            if (praisers==null){
                praisers=new ArrayList<>();
            }
            CirclePraisers item =new CirclePraisers();
            item.setUserIcon(UserPreferencesUtil.getHeadPic(context));
            item.setUserId(UserPreferencesUtil.getUserId(context));
            praisers.add(item);
            datas.get(currPosition).setPraisers(praisers);
            int priserCount=datas.get(currPosition).getPraiseCount();
            priserCount=priserCount+1;
            datas.get(currPosition).setPraiseCount(priserCount);
        }
        notifyDataSetChanged();
    }

    public void updatePresentCount(CircleBean circleBean) {
        datas.get(currPosition).setGiftCount(circleBean.getGiftCount());
        //++++++++++++++++++临时加++++++++++++++++++++++

//        datas.get(currPosition).setCommentCount(circleBean.getCommentCount());
//        datas.get(currPosition).setCommenters(circleBean.getCommenters());

        //++++++++++++++++++临时加++++++++++++++++++++++
        notifyDataSetChanged();
    }

    private class ViewHolder {
        private View moreView,commentButton,priseButton,juBaoButton,singleView,priseView,sendPresantView;
        private LinearLayout commentContent;
        private GridView priserGrid,gridview;
        private TextView timerText,oralText,locationText,userName,commentCount,priseCount,presantNums;
        private ImageView verticalImage,preview_play_iv,singleVideoImage,priseImage,horizontalImage,cubImage;
        private CircularImage userImage;
        private CirclePhotoAdapter circlePhotoAdapter;
        private CirclePriserAdapter circlePriserAdapter;
    }

    public void addDatas(List<CircleBean> datas,int mCurrentPage) {
        if (mCurrentPage==1){
            this.datas=datas;
        }else {
            this.datas.addAll(datas);
        }
        notifyDataSetChanged();
    }

    class ShowVideoView implements View.OnClickListener{

        private String videoUrl;

        public ShowVideoView(String videoUrl){
            this.videoUrl=videoUrl;
        }

        @Override
        public void onClick(View v) {
            videoPlayer(videoUrl);
        }
    }

    private void videoPlayer(String videoUrl) {
        Intent intent = new Intent(context, VideoPlayerActivity.class);
        intent.putExtra("vidoUrl",videoUrl);
//        String fileName=Constants.getMP4Name(context);
//        String vidoPath = FileUtil.MP4_ROOT_PATH.concat(fileName);
//        intent.putExtra("cache", vidoPath);
        context.startActivity(intent);
    }

    class ShowSingleImage implements View.OnClickListener{

        private String videoUrl,userName,seeUserId,imageId;

        public ShowSingleImage(String videoUrl,String userName,String seeUserId,String imageId){
            this.videoUrl=videoUrl;
            this.userName=userName;
            this.seeUserId=seeUserId;
            this.imageId=imageId;
        }

        @Override
        public void onClick(View v) {
            picShow(videoUrl, userName, seeUserId, imageId);
        }
    }

    private void picShow(String videoUrl,String userName,String seeUserId,String imageId) {
        List<BrowerPhotoesBean> browersDatas=new ArrayList<>();
        BrowerPhotoesBean item=new BrowerPhotoesBean();
        item.setImageUrl(videoUrl);
        item.setImageId(imageId);
        browersDatas.add(item);
        TransferBrowersBean tsBean=new TransferBrowersBean();
        tsBean.setBrowersDatas(browersDatas);
        Intent intent = new Intent(context, ImagePagerCircleActivity.class);
        intent.putExtra("bean", tsBean);
        intent.putExtra("userName", userName);
        intent.putExtra("seeUserId", seeUserId);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 0);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    class ShowImages implements AdapterView.OnItemClickListener{
        private CircleBean item;
        public ShowImages(CircleBean item){
            this.item=item;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            showImages(position, item.getRess(), item.getPublisher().getUsernick(), item.getPublisher().getUserId());
        }
    }

    private void showImages(int position, List<CircleRess> ress,String userNick,String seeUserId) {
        List<BrowerPhotoesBean> browersDatas=new ArrayList<>();
        for (int i=0;i<ress.size();i++){
            BrowerPhotoesBean item=new BrowerPhotoesBean();
            item.setImageUrl(ress.get(i).getResUrl());
            item.setImageId(ress.get(i).getResId());
            browersDatas.add(item);
        }
        TransferBrowersBean tsBean=new TransferBrowersBean();
        tsBean.setBrowersDatas(browersDatas);
        Intent intent = new Intent(context, ImagePagerCircleActivity.class);
        intent.putExtra("bean", tsBean);
        intent.putExtra("userName", userNick);
        intent.putExtra("seeUserId", seeUserId);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    class FeedBackListenter implements View.OnClickListener{
        private CircleBean item;
        public FeedBackListenter(CircleBean item){
            this.item=item;
        }

        @Override
        public void onClick(View v) {
            juBao(item);
        }
    }

    private void juBao(CircleBean item) {
        boolean islogin = UserPreferencesUtil.isOnline(context);
        if (!islogin) {
            Intent intent = new Intent(context, LoginWelcomActivity.class);
            context.startActivity(intent);
        }else{
            Intent intent = new Intent(context, ReportFristActivity.class);
            intent.putExtra("resType", Constants.JUBAO_TYPE_CIRCLE);
            intent.putExtra("userId", item.getPublisher().getUserId());
            intent.putExtra("resourceId", item.getMoodId());
            context.startActivity(intent);
        }
    }

    class PriserListenter implements View.OnClickListener{

        private CircleBean item;
        private int index;

        public PriserListenter( CircleBean item,int index){
            this.item=item;
            this.index=index;
        }

        @Override
        public void onClick(View v) {
            priseAction(item, index);
        }
    }

    private void priseAction(CircleBean bean,int index) {
        boolean islogin = UserPreferencesUtil.isOnline(context);
        if (!islogin) {
            Intent intent = new Intent(context, LoginWelcomActivity.class);
            context.startActivity(intent);
        }else{
            this.currPosition=index;
            RequestBean item = new RequestBean();
            try {
                JSONObject obj = new JSONObject();
                obj.put("moodId", bean.getMoodId());
                if (bean.getHasPraise()>=1){
                    obj.put("praiseStatus", "2");
                }else{
                    obj.put("praiseStatus", "1");
                }
                item.setData(obj);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            ShowMsgDialog.showNoMsg((Activity) context, false);
            item.setServiceURL(ConstTaskTag.QUEST_CIRCLE_PRAISE);
            ThreadPool.getInstance().excuseAction(callback, item, ConstTaskTag.QUERY_CIRCLE_PRAISE);
        }
    }

    class IntoCommentDetail implements View.OnClickListener{
        private CircleBean item;
        public IntoCommentDetail(CircleBean item){
            this.item=item;
        }
        @Override
        public void onClick(View v) {
            intoCommentDetail(item);
        }
    }

    private void intoCommentDetail(CircleBean item) {
        boolean islogin = UserPreferencesUtil.isOnline(context);
        if (!islogin) {
            Intent intent = new Intent(context, LoginWelcomActivity.class);
            context.startActivity(intent);
        }else{
            Intent intent = new Intent(context, CommentDetailActivity.class);
            intent.putExtra("bean",item);
            context.startActivity(intent);
        }
    }

    class SendPresentListener implements View.OnClickListener{
        private CircleBean item;
        private int index;
        public SendPresentListener(CircleBean item,int index){
            this.item=item;
            this.index=index;
        }
        @Override
        public void onClick(View v) {
            showPresantDialog(item, index);
        }
    }

    private void showPresantDialog(CircleBean item, int index) {
        this.currPosition=index;
        showPresant.ShowPresantDialog(item);
    }

    public interface ShowPresant{
        void ShowPresantDialog(CircleBean bean);
    }
}
