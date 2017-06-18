package com.xygame.sg.activity.circle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.LoginWelcomActivity;
import com.xygame.sg.activity.commen.ReportFristActivity;
import com.xygame.sg.activity.commen.ShareBoardView;
import com.xygame.sg.activity.image.ImagePagerActivity;
import com.xygame.sg.activity.image.ImagePagerCircleActivity;
import com.xygame.sg.activity.personal.bean.BrowerPhotoesBean;
import com.xygame.sg.activity.personal.bean.TransferBrowersBean;
import com.xygame.sg.adapter.main.CirclePhotoAdapter;
import com.xygame.sg.adapter.main.CirclePriserAdapter;
import com.xygame.sg.adapter.main.CommentAdapter;
import com.xygame.sg.bean.circle.CircleBean;
import com.xygame.sg.bean.circle.CircleCommenter;
import com.xygame.sg.bean.circle.CircleCommenters;
import com.xygame.sg.bean.circle.CirclePraisers;
import com.xygame.sg.bean.circle.CircleRess;
import com.xygame.sg.bean.circle.CommentObject;
import com.xygame.sg.bean.circle.Commenter;
import com.xygame.sg.bean.circle.Commenters;
import com.xygame.sg.define.view.ChoiceShareReportActivity;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.CalendarUtils;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.VideoImageLoader;
import com.xygame.sg.vido.VideoPlayerActivity;
import com.xygame.sg.widget.refreash.PullToRefreshBase;
import com.xygame.sg.widget.refreash.PullToRefreshListView3;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CommentDetailActivity extends SGBaseActivity implements View.OnClickListener, PullToRefreshBase.OnRefreshListener2 {
    private View backButton,preview_play_iv,singleView,sendButton,rightButton;
    private TextView titleName;
    private PullToRefreshListView3 cicleList;
    private EditText inputContent;
    private CommentAdapter adapter;

    //topView部分
    private View topView,priseButton;
    private CircularImage userImage;
    private TextView userName,timerText,oralText,priserNum;
    private ImageView singleImage,singleVideoImage,priseImage,rightbuttonIcon;
    private GridView gridview,priserGrid;
    private ImageLoader imageLoader;
    private CircleBean circleBean;
    private VideoImageLoader videoImageLoader;
    private CirclePhotoAdapter circlePhotoAdapter;
    private CirclePriserAdapter circlePriserAdapter;
    private String shortStr;
    private int pageSize = 15;//每页显示的数量
    private int mCurrentPage = 1;//当前显示页数
    private long reqTime;
    private boolean isLoading=true;
    private List<Commenters> newComments;
    private boolean priseFlag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circle_comment_layout);
        initViews();
        addListener();
        initDatas();

    }

    private void initDatas() {
        rightbuttonIcon.setVisibility(View.VISIBLE);
        rightbuttonIcon.setImageResource(R.drawable.more_icon);
        newComments=new ArrayList<>();
        videoImageLoader  = VideoImageLoader.getInstance();
        imageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
        titleName.setText("动态详情");
        circleBean=(CircleBean)getIntent().getSerializableExtra("bean");
//        cicleList.setTranscriptMode();
        adapter=new CommentAdapter(this,null);
        cicleList.setAdapter(adapter);
        uptopViews();

        loadPriserAndCommentDatas();
    }

    private void addListener() {
        backButton.setOnClickListener(this);
        cicleList.setOnRefreshListener(this);
        sendButton.setOnClickListener(this);
        priseButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
    }

    private void initViews() {
        rightButton=findViewById(R.id.rightButton);
        inputContent=(EditText)findViewById(R.id.inputContent);
        backButton=findViewById(R.id.backButton);
        rightbuttonIcon=(ImageView)findViewById(R.id.rightbuttonIcon);
        titleName = (TextView)findViewById(R.id.titleName);
        cicleList = (PullToRefreshListView3) findViewById(R.id.cicleList);
        cicleList.setMode(PullToRefreshBase.Mode.BOTH);
        sendButton=findViewById(R.id.sendButton);

        topView= LayoutInflater.from(this).inflate(R.layout.circle_comment_topview,null);
        userImage=(CircularImage)topView.findViewById(R.id.userImage);
        userName=(TextView)topView.findViewById(R.id.userName);
        timerText=(TextView)topView.findViewById(R.id.timerText);
        oralText=(TextView)topView.findViewById(R.id.oralText);
        singleImage=(ImageView)topView.findViewById(R.id.singleImage);
        singleVideoImage=(ImageView)topView.findViewById(R.id.singleVideoImage);
        priseImage=(ImageView)topView.findViewById(R.id.priseImage);
        gridview=(GridView)topView.findViewById(R.id.gridview);
        priseButton=topView.findViewById(R.id.priseButton);
        priserNum=(TextView)topView.findViewById(R.id.priserNum);
        priserGrid=(GridView)topView.findViewById(R.id.priserGrid);
        preview_play_iv=topView.findViewById(R.id.preview_play_iv);
        singleView=topView.findViewById(R.id.singleView);
        cicleList.addHeadViewAction(topView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rightButton:
                Intent intent=new Intent(this,ChoiceShareReportActivity.class);
                startActivityForResult(intent,0);
                break;
            case R.id.priseButton:
                priseAction();
                break;
            case R.id.backButton:
                finish();
                break;
            case R.id.sendButton:
                shortStr=inputContent.getText().toString().trim();
                if (!"".equals(shortStr)){
                    if (shortStr.length()<=60){
                        commitComment(shortStr);
                    }else{
                        Toast.makeText(this,"评论内容60字以内",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this,"添加评论内容",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case 0:
                    String backStr = data.getStringExtra(Constants.COMEBACK);
                    if (backStr.equals("share")){
                        Intent intent = new Intent(this,ShareBoardView.class);
                        intent.putExtra(Constants.SHARE_TITLE_KEY,"模范儿");
                        intent.putExtra(Constants.SHARE_SUBTITLE_KEY,"快来看看"+circleBean.getPublisher().getUsernick()+"发了什么好看的照片和视频~！！");
                        startActivity(intent);
                    } else if(backStr.equals("report")){
                        boolean islogin = UserPreferencesUtil.isOnline(this);
                        if (!islogin) {
                            Intent intent = new Intent(this, LoginWelcomActivity.class);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(this, ReportFristActivity.class);
                            intent.putExtra("resType", Constants.JUBAO_TYPE_CIRCLE);
                            intent.putExtra("userId", circleBean.getPublisher().getUserId());
                            intent.putExtra("resourceId", circleBean.getMoodId());
                            startActivity(intent);
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (newComments.size()>0){
            List<CircleCommenters> commenters=circleBean.getCommenters();
            if (commenters==null){
                commenters=new ArrayList<>();
            }
            for (Commenters item:newComments){
                CircleCommenters it=new CircleCommenters();
                CircleCommenter ren=new CircleCommenter();
                ren.setUsernick(item.getCommenter().getUsernick());
                it.setCommenter(ren);
                it.setCommentTime(item.getCommentTime());
                it.setContent(item.getContent());
                commenters.add(0,it);
            }
            circleBean.setCommenters(commenters);
            Intent intent = new Intent(Constants.ACTION_RECORDER_CIRCLE_COMMENT);
            intent.putExtra("priseFlag", priseFlag);
            intent.putExtra("commentFlag", true);
            intent.putExtra(Constants.COMEBACK, circleBean);
            sendBroadcast(intent);
        }else{
            if (priseFlag){
                Intent intent = new Intent(Constants.ACTION_RECORDER_CIRCLE_COMMENT);
                intent.putExtra("priseFlag",priseFlag);
                intent.putExtra("commentFlag",false);
                intent.putExtra(Constants.COMEBACK, circleBean);
                sendBroadcast(intent);
            }
        }
    }

    private void uptopViews() {
        circlePhotoAdapter=new CirclePhotoAdapter(this,null);
        circlePriserAdapter=new CirclePriserAdapter(this,null);
        priserNum.setText("0");
        singleImage.setVisibility(View.GONE);
        preview_play_iv.setVisibility(View.GONE);
        if (circleBean.getHasPraise()>=1){
            priseImage.setImageResource(R.drawable.yizanaed);
        }else{
            priseImage.setImageResource(R.drawable.dianzana);
        }
        String userIcon = circleBean.getPublisher().getUserIcon();
        if (!StringUtils.isEmpty(userIcon)) {
            imageLoader.loadImage(userIcon, userImage, false);
        }
        userName.setText(circleBean.getPublisher().getUsernick());
        timerText.setText(CalendarUtils.getHenGongDateDis(circleBean.getCreateTime()));
        if (circleBean.getContent()!=null&&!"null".equals(circleBean.getContent())&&!"".equals(circleBean.getContent())){
            oralText.setVisibility(View.VISIBLE);
            oralText.setText(circleBean.getContent());
        }else{
            oralText.setVisibility(View.GONE);
        }

        if (circleBean.getRess().size()==1){
            singleVideoImage.setVisibility(View.GONE);
            singleView.setVisibility(View.VISIBLE);
            singleImage.setVisibility(View.GONE);
            gridview.setVisibility(View.GONE);
            if (circleBean.getRess().get(0).getResType()==1){
                singleImage.setVisibility(View.VISIBLE);
                singleVideoImage.setVisibility(View.GONE);
                imageLoader.loadImage(circleBean.getRess().get(0).getResUrl(), singleImage, false);
//                imageLoader.loadOrigaImage(circleBean.getRess().get(0).getResUrl(), singleImage);
                singleImage.setOnClickListener(new ShowSingleImage(circleBean.getRess().get(0).getResUrl(), circleBean.getPublisher().getUsernick(), circleBean.getPublisher().getUserId(), circleBean.getRess().get(0).getResId()));
            }else{
                singleVideoImage.setVisibility(View.VISIBLE);
                singleImage.setVisibility(View.GONE);
                preview_play_iv.setVisibility(View.VISIBLE);
                videoImageLoader.DisplayImage(circleBean.getRess().get(0).getResUrl(), singleVideoImage);
                singleVideoImage.setOnClickListener(new ShowVideoView(circleBean.getRess().get(0).getResUrl()));
            }
        }else{
            singleView.setVisibility(View.GONE);
            singleImage.setVisibility(View.GONE);
            gridview.setVisibility(View.VISIBLE);
            circlePhotoAdapter.addDatas(circleBean.getRess());
            gridview.setAdapter(circlePhotoAdapter);
            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    showImages(position, circleBean.getRess(), circleBean.getPublisher().getUsernick(), circleBean.getPublisher().getUserId());
                }
            });
        }
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
        Intent intent = new Intent(this, VideoPlayerActivity.class);
        intent.putExtra("vidoUrl", videoUrl);
//        String fileName= Constants.getMP4Name(this);
//        String vidoPath = FileUtil.MP4_ROOT_PATH.concat(fileName);
//        intent.putExtra("cache", vidoPath);
        startActivity(intent);
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
        Intent intent = new Intent(this, ImagePagerCircleActivity.class);
        intent.putExtra("bean", tsBean);
        intent.putExtra("userName", userName);
        intent.putExtra("seeUserId", seeUserId);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 0);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
        Intent intent = new Intent(this, ImagePagerCircleActivity.class);
        intent.putExtra("bean", tsBean);
        intent.putExtra("userName", userNick);
        intent.putExtra("seeUserId", seeUserId);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    private void loadPriserAndCommentDatas() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("page", new JSONObject().put("pageIndex", mCurrentPage).put("pageSize", pageSize));
            obj.put("moodId",circleBean.getMoodId());
            if (mCurrentPage>1){
                obj.put("reqTime",reqTime);
            }
            item.setData(obj);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        item.setServiceURL(ConstTaskTag.QUEST_CIRCLE_COMMENT_DETAIL);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_CIRCLE_COMMENT_DETAIL);
    }


    private void commitComment(String shortStr) {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("moodId",circleBean.getMoodId());
            obj.put("content",shortStr);
            item.setData(obj);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        ShowMsgDialog.showNoMsg(this,false);
        item.setServiceURL(ConstTaskTag.QUEST_CIRCLE_COMMENT);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_CIRCLE_COMMENT);
    }

    private void priseAction() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("moodId", circleBean.getMoodId());
            if (circleBean.getHasPraise()>=1){
                obj.put("praiseStatus", "2");
            }else{
                obj.put("praiseStatus", "1");
            }
            item.setData(obj);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        ShowMsgDialog.showNoMsg((Activity) this, false);
        item.setServiceURL(ConstTaskTag.QUEST_CIRCLE_PRAISE);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_CIRCLE_PRAISE_);
    }

    private void loadCommentData() {
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("page", new JSONObject().put("pageIndex", mCurrentPage).put("pageSize", pageSize));
            obj.put("moodId",circleBean.getMoodId());
            if (mCurrentPage>1){
                obj.put("reqTime",reqTime);
            }
            item.setData(obj);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        item.setServiceURL(ConstTaskTag.QUEST_CIRCLE_COMMENT_LIST);
        ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_CIRCLE_COMMENT_LIST);
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        super.getResponseBean(data);
        cicleList.onRefreshComplete();
        switch (data.getPosionSign()){
            case ConstTaskTag.QUERY_CIRCLE_COMMENT_LIST:
                parseListDatas(data);
                break;
            case ConstTaskTag.QUERY_CIRCLE_COMMENT_DETAIL:
                if ("0000".equals(data.getCode())){
                    parseDatas(data);
                }else{
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_CIRCLE_COMMENT:
                if ("0000".equals(data.getCode())){
                    inputContent.setText("");
                    Commenters item=new Commenters();
                    Commenter commenter=new Commenter();
                    commenter.setUserId(UserPreferencesUtil.getUserId(this));
                    commenter.setUsernick(UserPreferencesUtil.getUserNickName(this));
                    item.setCommenter(commenter);
                    item.setCommentTime(System.currentTimeMillis());
                    item.setContent(shortStr);
                    newComments.add(item);
                    adapter.addItem(item);
                    int priserCount=circleBean.getCommentCount();
                    priserCount=priserCount+1;
                    circleBean.setCommentCount(priserCount);
                }else{
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstTaskTag.QUERY_CIRCLE_PRAISE_:
                if ("0000".equals(data.getCode())){
                    priseFlag=true;
                    List<CirclePraisers> praisers=circleBean.getPraisers();
                    if (circleBean.getHasPraise()>=1){
                        circleBean.setHasPraise(0);
                        priseImage.setImageResource(R.drawable.dianzana);
                        for (int i=0;i<praisers.size();i++){
                            if (praisers.get(i).getUserId().equals(UserPreferencesUtil.getUserId(this))){
                                praisers.remove(i);
                            }
                        }
                        int priserCount=circleBean.getPraiseCount();
                        priserCount=priserCount-1;
                        circleBean.setPraiseCount(priserCount);
                    }else{
                        int priserCount=circleBean.getPraiseCount();
                        priserCount=priserCount+1;
                        circleBean.setPraiseCount(priserCount);
                        CirclePraisers item=new CirclePraisers();
                        item.setUserId(UserPreferencesUtil.getUserId(this));
                        item.setUserIcon(UserPreferencesUtil.getHeadPic(this));
                        praisers.add(item);
                        circleBean.setHasPraise(3);
                        priseImage.setImageResource(R.drawable.yizanaed);
                    }
                    circleBean.setPraisers(praisers);
                    updatePriseView();
                }else{
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void updatePriseView() {
        if (circleBean.getPraisers().size()>0){
            priserNum.setText(circleBean.getPraisers().size()+"");
            priserGrid.setVisibility(View.VISIBLE);
            circlePriserAdapter.updateAllDatas(circleBean.getPraisers());
            priserGrid.setAdapter(circlePriserAdapter);
        }else{
            priserNum.setText("0");
            priserGrid.setVisibility(View.GONE);
        }
        if (circleBean.getHasPraise()>=1){
            priseImage.setImageResource(R.drawable.yizanaed);
        }else{
            priseImage.setImageResource(R.drawable.dianzana);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void responseFaith(ResponseBean data) {
        super.responseFaith(data);
        cicleList.onRefreshComplete();
    }



    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        isLoading = true;
        mCurrentPage = 1;
        loadPriserAndCommentDatas();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        if (isLoading) {
            mCurrentPage = mCurrentPage + 1;
            loadCommentData();
        } else {
            falseDatas();
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    cicleList.onRefreshComplete();
                    break;
                default:
                    break;
            }

        }
    };

    private void falseDatas() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        android.os.Message m = handler.obtainMessage();
                        m.what = 1;
                        m.sendToTarget();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void parseDatas(ResponseBean data) {
        CommentObject comentObj=new CommentObject();
        if (!TextUtils.isEmpty(data.getRecord())&&!"null".equals(data.getRecord())){
            try {
                JSONObject dataJson=new JSONObject(data.getRecord());
                String comment=StringUtils.getJsonValue(dataJson, "comment");
                JSONObject commentsJson=new JSONObject(comment);
                reqTime=commentsJson.getLong("reqTime");
                comentObj.setReqTime(reqTime);
                String commentsStr=commentsJson.getString("comments");
                if (commentsStr!=null&&!"null".equals(commentsStr)&&!"".equals(commentsStr)){
                    List<Commenters> comments=new ArrayList<>();
                    JSONArray comentsArray=new JSONArray(commentsStr);
                    for (int i=0;i<comentsArray.length();i++){
                       JSONObject comObj=comentsArray.getJSONObject(i);
                        Commenters it=new Commenters();
                        it.setContent(StringUtils.getJsonValue(comObj, "content"));
                        String commentTime=StringUtils.getJsonValue(comObj, "commentTime");
                        it.setCommentTime(Long.parseLong(commentTime));
                        it.setCommentId(StringUtils.getJsonValue(comObj, "commentId"));
                        JSONObject comRen=new JSONObject(StringUtils.getJsonValue(comObj, "commenter"));
                        Commenter commenter=new Commenter();
                        commenter.setUsernick(StringUtils.getJsonValue(comRen, "usernick"));
                        commenter.setUserId(StringUtils.getJsonValue(comRen, "userId"));
                        it.setCommenter(commenter);
                        comments.add(it);
                    }
                    comentObj.setCommenters(comments);
                }
                String priseStr=StringUtils.getJsonValue(dataJson, "praisers");
                if (!TextUtils.isEmpty(priseStr)&&!"[]".equals(priseStr)&&!"[null]".equals(priseStr)&&!"null".equals(priseStr)){
                    JSONArray priseArray=new JSONArray(priseStr);
                    List<CirclePraisers> praisers=new ArrayList<>();
                    for (int i=0;i<priseArray.length();i++){
                        JSONObject pObj=priseArray.getJSONObject(i);
                        CirclePraisers item=new CirclePraisers();
                        item.setUserId(StringUtils.getJsonValue(pObj, "userId"));
                        item.setUserIcon(StringUtils.getJsonValue(pObj, "userIcon"));
                        praisers.add(item);
                    }
                    comentObj.setPraisers(praisers);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        if (comentObj.getPraisers()!=null){
            if (comentObj.getPraisers().size()>0){
                priserNum.setText(comentObj.getPraisers().size()+"");
                priserGrid.setVisibility(View.VISIBLE);
                circlePriserAdapter.updateAllDatas(comentObj.getPraisers());
                priserGrid.setAdapter(circlePriserAdapter);
            }else{
                priserNum.setText("0");
                priserGrid.setVisibility(View.GONE);
            }
        }else {
            priserNum.setText("0");
            priserGrid.setVisibility(View.GONE);
        }

        if (comentObj.getCommenters()!=null){
            if (comentObj.getCommenters().size()<pageSize){
                isLoading = false;
            }
            adapter.addDatas(comentObj.getCommenters(), mCurrentPage);
        }else{
            isLoading = false;
        }

        adapter.notifyDataSetChanged();
    }


    private void parseListDatas(ResponseBean data) {
        CommentObject comentObj=new CommentObject();
        if (!TextUtils.isEmpty(data.getRecord())&&!"null".equals(data.getRecord())){
            try {
                JSONObject commentsJson=new JSONObject(data.getRecord());
                reqTime=commentsJson.getLong("reqTime");
                comentObj.setReqTime(reqTime);
                String commentsStr=commentsJson.getString("comments");
                if (commentsStr!=null&&!"null".equals(commentsStr)&&!"".equals(commentsStr)){
                    List<Commenters> comments=new ArrayList<>();
                    JSONArray comentsArray=new JSONArray(commentsStr);
                    for (int i=0;i<comentsArray.length();i++){
                        JSONObject comObj=comentsArray.getJSONObject(i);
                        Commenters it=new Commenters();
                        it.setContent(StringUtils.getJsonValue(comObj, "content"));
                        String commentTime=StringUtils.getJsonValue(comObj, "commentTime");
                        it.setCommentTime(Long.parseLong(commentTime));
                        it.setCommentId(StringUtils.getJsonValue(comObj, "commentId"));
                        JSONObject comRen=new JSONObject(StringUtils.getJsonValue(comObj, "commenter"));
                        Commenter commenter=new Commenter();
                        commenter.setUsernick(StringUtils.getJsonValue(comRen, "usernick"));
                        commenter.setUserId(StringUtils.getJsonValue(comRen, "userId"));
                        it.setCommenter(commenter);
                        comments.add(it);
                    }
                    comentObj.setCommenters(comments);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        if (comentObj.getCommenters()!=null){
            if (comentObj.getCommenters().size()<pageSize){
                isLoading = false;
            }
            adapter.addDatas(comentObj.getCommenters(), mCurrentPage);
        }else{
            isLoading = false;
        }
    }
}
