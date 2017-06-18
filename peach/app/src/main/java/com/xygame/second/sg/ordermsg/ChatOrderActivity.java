package com.xygame.second.sg.ordermsg;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;
import com.xygame.second.sg.comm.inteface.ControlFlowView;
import com.xygame.second.sg.io.vov.vitamio.activity.VideoPlayer;
import com.xygame.second.sg.personal.activity.PersonalDetailActivity;
import com.xygame.second.sg.utils.Contant;
import com.xygame.second.sg.utils.RecordResult;
import com.xygame.second.sg.utils.Uuid16;
import com.xygame.sg.AudioRecorder2Mp3Util;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.image.ImageChooserActivity;
import com.xygame.sg.activity.notice.ChatDetailActivity;
import com.xygame.sg.activity.notice.JJRNoticeDetailActivity;
import com.xygame.sg.activity.notice.NoticeDetailActivity;
import com.xygame.sg.activity.notice.RecruitActivity;
import com.xygame.sg.bean.comm.TransferImagesBean;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.define.view.roundimageview.RoundedImageView;
import com.xygame.sg.http.AliySSOHepler;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.im.BaiduLocation;
import com.xygame.sg.im.NewsEngine;
import com.xygame.sg.im.SGNewsBean;
import com.xygame.sg.im.SenderUser;
import com.xygame.sg.im.ShareLocationBean;
import com.xygame.sg.im.ShowBaiduLocation;
import com.xygame.sg.im.ToChatBean;
import com.xygame.sg.im.TransferBean;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.FileUtil;
import com.xygame.sg.utils.HttpCallBack;
import com.xygame.sg.utils.ImageLocalLoader;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtil;
import com.xygame.sg.utils.StringUtils;
import com.xygame.sg.utils.TimeUtils;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.VideoImageLoader;
import com.xygame.sg.utils.XMPPUtils;
import com.xygame.sg.vido.VideoPlayerActivity;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by tony on 2016/9/9.
 */
public class ChatOrderActivity extends SGBaseActivity
        implements View.OnClickListener, EmojiconGridFragment.OnEmojiconClickedListener,
        EmojiconsFragment.OnEmojiconBackspaceClickedListener, TextWatcher ,ControlFlowView{
    private View titleBack, faceButton, addButton, messageButton, moreOption, picPhoto, tokePhoto, locationButton,tgInfo,personalInfo,flowView,yesView,noView,videoButton;
    private FrameLayout emojicons;
    private Chat chat = null;
    private MessageListAdapter adapter = null;
    private EditText messageInput = null;
    private ImageView messageSendBtn = null;
    private ListView listView;
    private List<SGNewsBean> MessageDatas;
    private TextView tvChatTitle;
    private Animation alphaIn, alphaOut;
    private FragmentManager fragmentManager;
    private EmojiconsFragment emojFragment;
    private Button recordButton;
    private String photoName;
    private static final int TAKE_PICTURE = 0;
    private static final int CHOOSE_PICTURE = 1;
    private InputMethodManager imm;
    private SGNewsBean msgBean;
    private ToChatBean toChatBean;
    private SGNewsBean sendBean;
    private int voiceLength = 0,currOperatorIndex=0;
    private long aliyTimeStamp = 0;
    private ShareLocationBean locationBean;
    private String sendMessageType = "1";
    private boolean isJJR=false;
    private String fromFlag;
    private SGNewsBean operatorMessageBean;
    private List<SGNewsBean> sendImages=new ArrayList<>();
    private int  currSendingIndex=0;

    /**
     * 录音相关
     * @param savedInstanceState
     */
    private static int[] res = { R.drawable.mic_2, R.drawable.mic_3,
            R.drawable.mic_4, R.drawable.mic_5 };
    private static ImageView view;
    public final static int   MAX_TIME =60;//一分钟
    private String mFileName = null,fromVoicePath;
    private long startTime;
    private Dialog recordIndicator;
    private static final int MIN_INTERVAL_TIME = 2000;
    private AudioRecorder2Mp3Util util = null;
    private Handler volumeHandler;
    private ObtainDecibelThread thread;
    private long intervalTime;
    private MediaPlayer mediaPlayer;

    private String actDate,actStartTime,actEndTime,singlePrice,totalPrice,actTitle,actionId,memberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_chat_single);
        actDate=getIntent().getStringExtra("actDate");
        actStartTime=getIntent().getStringExtra("actStartTime");
        actEndTime=getIntent().getStringExtra("actEndTime");
        singlePrice=getIntent().getStringExtra("singlePrice");
        totalPrice=getIntent().getStringExtra("totalPrice");
        actTitle=getIntent().getStringExtra("actTitle");
        fromFlag=getIntent().getStringExtra("fromFlag");
        actionId=getIntent().getStringExtra("actionId");
        memberId=getIntent().getStringExtra("memberId");
        initViews();
        initListeners();
        initDatas();
    }

    private void initViews() {
        videoButton=findViewById(R.id.videoButton);
        personalInfo=findViewById(R.id.personalInfo);
        tokePhoto = findViewById(R.id.tokePhoto);
        picPhoto = findViewById(R.id.picPhoto);
        tgInfo=findViewById(R.id.tgInfo);
        locationButton = findViewById(R.id.locationButton);
        moreOption = findViewById(R.id.moreOption);
        addButton = findViewById(R.id.addButton);
        recordButton = (Button) findViewById(R.id.recordButton);
        messageButton = findViewById(R.id.messageButton);
        faceButton = findViewById(R.id.faceButton);
        titleBack = findViewById(R.id.backButton);
        emojicons = (FrameLayout) findViewById(R.id.emojicons);
        // 与谁聊天
        tvChatTitle = (TextView) findViewById(R.id.titleName);
        listView = (ListView) findViewById(R.id.chat_list);
        listView.setCacheColorHint(0);
        messageInput = (EditText) findViewById(R.id.chat_content);
        messageSendBtn = (ImageView)findViewById(R.id.chat_sendbtn);
        flowView=findViewById(R.id.flowView);
        yesView=findViewById(R.id.yesView);
        noView=findViewById(R.id.noView);
        flowView.getBackground().setAlpha(150);
    }

    private void initListeners() {
        noView.setOnClickListener(this);
        yesView.setOnClickListener(this);
        personalInfo.setOnClickListener(this);
        tgInfo.setOnClickListener(this);
        messageButton.setOnClickListener(this);
        titleBack.setOnClickListener(this);
        messageSendBtn.setOnClickListener(this);
        faceButton.setOnClickListener(this);
        addButton.setOnClickListener(this);
        picPhoto.setOnClickListener(this);
        tokePhoto.setOnClickListener(this);
        videoButton.setOnClickListener(this);
        locationButton.setOnClickListener(this);
        messageInput.addTextChangedListener(this);
        messageInput.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                // TODO Auto-generated method stub
                updateActionView(2);
                imm.showSoftInput(messageInput, 0);
                return false;
            }
        });
        recordButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if (FileUtil.ExistSDCard()){
                            recordButton.setBackgroundResource(R.drawable.record_press_bg);
                            recordButton.setTextColor(getResources().getColor(R.color.white));
                            recordButton.setText("松开发送");
                            initDialogAndStartRecord();
                        }else{
                            Toast.makeText(ChatOrderActivity.this, "请插入SD卡", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (FileUtil.ExistSDCard()){
                            recordButton.setBackgroundResource(R.drawable.record_unpress_bg);
                            recordButton.setTextColor(getResources().getColor(R.color.black));
                            recordButton.setText("按住录音");
                            finishRecord();
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        if (FileUtil.ExistSDCard()) {
                            recordButton.setBackgroundResource(R.drawable.shape_circle_green);
                            recordButton.setTextColor(getResources().getColor(R.color.white));
                            recordButton.setText("按住录音");
                            finishRecord();
                        }
                        break;
                }
                return false;
            }
        });
    }

    private void initDialogAndStartRecord() {
        startTime = System.currentTimeMillis();
        recordIndicator = new Dialog(this,
                R.style.like_toast_dialog_style);
        view = new ImageView(this);
        view.setImageResource(R.drawable.mic_2);
        recordIndicator.setContentView(view, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        WindowManager.LayoutParams lp = recordIndicator.getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;
        recordIndicator.show();
        startRecording();
    }

    private void finishRecord() {
        recordIndicator.dismiss();
        intervalTime = System.currentTimeMillis() - startTime;
        if (intervalTime < MIN_INTERVAL_TIME) {
            Toast.makeText(this, "时间太短！", Toast.LENGTH_SHORT).show();
            File file = new File(mFileName);
            if(file.exists()){
                file.delete();
            }
            stopThread();
        }else{
            stopRecording();
        }
    }

    private void startRecording() {
        fromVoicePath="/sdcard/test_audio_recorder_for_mp3.mp3";
        mFileName= FileUtil.RECORD_ROOT_PATH.concat(Constants.getMP3Name(this));
        if (util == null) {
            util = new AudioRecorder2Mp3Util(null,
                    "/sdcard/test_audio_recorder_for_mp3.raw",fromVoicePath);
        }
        util.startRecording();
        if(thread==null){
            thread = new ObtainDecibelThread();
        }
        thread.start();
    }

    private  void stopThread(){
        util.cleanFile(AudioRecorder2Mp3Util.RAW);
        // 如果要关闭可以
        if (thread != null) {
            thread.exit();
            thread = null;
        }
        util.close();
        util = null;
    }

    private void stopRecording() {
        util.stopRecordingAndConvertFile();
        stopThread();
        int isSccuss=FileUtil.CopySdcardFile(fromVoicePath,mFileName);
        if (isSccuss==0){
            changToMp3(mFileName, (int) (intervalTime / 1000));
        }
    }

    @Override
    public void flowViewStatus(boolean flag,String actionId,String memberId,SGNewsBean item) {
        this.actionId=actionId;
        this.memberId=memberId;
        this.operatorMessageBean=item;
        ThreadPool.getInstance().excuseThread(new DelayDespFlowView(flag));
    }

    private class  DelayDespFlowView implements Runnable{
        private boolean flag;
        public DelayDespFlowView(boolean flag){
            this.flag=flag;
        }
        @Override
        public void run() {
            try {
                Thread.sleep(500);
                android.os.Message m = handler.obtainMessage();
                m.obj=flag;
                m.what = 9;
                m.sendToTarget();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private class ObtainDecibelThread extends Thread {

        private volatile boolean running = true;

        public void exit() {
            running = false;
        }

        @Override
        public void run() {
            while (running) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int max=38;
                int min=1;
                Random random = new Random();

                int s = random.nextInt(max)%(max-min+1) + min;
                if (s != 0) {
                    if (s < 26)
                        volumeHandler.sendEmptyMessage(0);
                    else if (s < 32)
                        volumeHandler.sendEmptyMessage(1);
                    else if (s < 38)
                        volumeHandler.sendEmptyMessage(2);
                    else
                        volumeHandler.sendEmptyMessage(3);

                }

            }
        }

    }

    static class ShowVolumeHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            view.setImageResource(res[msg.what]);
        }
    }

    private void initDatas() {
        flowView.setVisibility(View.GONE);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        volumeHandler = new ShowVolumeHandler();
        msgBean = (SGNewsBean) getIntent().getSerializableExtra("bean");
        isJJR=getIntent().getBooleanExtra("isJJR",false);
        if (msgBean == null) {
            msgBean = new SGNewsBean();
            toChatBean = (ToChatBean) getIntent().getSerializableExtra("toChatBean");
            msgBean.setFromUser(SenderUser.getFromUserJsonStr(this));
            msgBean.setToUser(SenderUser.getToUserJsonStr(toChatBean.getUserId(), toChatBean.getUserIcon(),
                    toChatBean.getUsernick()));
            msgBean.setNoticeSubject(toChatBean.getNoticeSubject());
            msgBean.setRecruitLocIndex(toChatBean.getRecruitLocIndex());
            msgBean.setNoticeId(toChatBean.getNoticeId());
            msgBean.setFriendNickName(toChatBean.getUsernick());
            msgBean.setFriendUserIcon(toChatBean.getUserIcon());
            msgBean.setFriendUserId(toChatBean.getUserId());
            msgBean.setRecruitId(toChatBean.getRecruitId());
//            SGNewsBean tempBean = NewsEngine.getSGNewBeanByFriendUserId(this, UserPreferencesUtil.getUserId(this),
//                    msgBean.getFriendUserId());
//            if (tempBean != null) {
//                msgBean = tempBean;
//                msgBean.setIsShow(Constants.NEWS_SHOW);
//                NewsEngine.updateHideOrShowChatItem(this, msgBean, UserPreferencesUtil.getUserId(this));
//            }
        } else{
            try{
                String tempFromUserId=msgBean.getFromUser();
                String fromUser=msgBean.getToUser();
                String toUser=msgBean.getFromUser();
                JSONObject fromUserJson=new JSONObject(tempFromUserId);
                String fromUserId=fromUserJson.getString("userId");
                String userId=UserPreferencesUtil.getUserId(this);
                if (!fromUserId.equals(userId)){
                    msgBean.setFromUser(fromUser);
                    msgBean.setToUser(toUser);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        ThreadPool.getInstance().excuseThread(new InitChatRoom());

        registerBoradcastReceiver();
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        emojFragment = new EmojiconsFragment();
        fragmentManager = getSupportFragmentManager();
        alphaIn = AnimationUtils.loadAnimation(this, R.anim.push_bottom_in);
        alphaOut = AnimationUtils.loadAnimation(this, R.anim.push_bottom_out);
        tvChatTitle.setText(msgBean.getFriendNickName());
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.emojicons, emojFragment);
        transaction.commit();
        adapter = new MessageListAdapter(ChatOrderActivity.this, null);
        adapter.addControlViewListener(this);
        listView.setAdapter(adapter);
        messageInput.setSingleLine(false);
        messageInput.setHorizontallyScrolling(false);
        messageInput.setVerticalScrollBarEnabled(true);
        getDatas();
        MediaRecorder mRecorders = new MediaRecorder();
        mRecorders.setAudioSource(MediaRecorder.AudioSource.MIC);
//		if (SGApplication.getInstance().getConnection()!=null){
//			if (!SGApplication.getInstance().getConnection().isConnected()){
//				reConnect();
//			}
//		}
    }

    private class InitChatRoom implements Runnable{
        @Override
        public void run() {
            try {
                if (SGApplication.getInstance().getConnection()!=null){
                    chat = SGApplication.getInstance().getConnection().getChatManager()
                            .createChat(XMPPUtils.getUserJid(ChatOrderActivity.this, msgBean.getFriendUserId()), null);
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }

    public void changToMp3(final String audioPath, int minute) {
        android.os.Message msg1 = new android.os.Message();
        msg1.what = 5;
        msg1.obj = audioPath;
        msg1.arg1 = minute;
        handler.sendMessage(msg1);
    }

    private void getDatas() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (msgBean != null) {
                    uploadLocalStatus(msgBean);
                }
                loadHistoryDatas();
                android.os.Message msg = new android.os.Message();
                msg.what = 3;
                handler.sendMessage(msg);
            }
        }).start();
    }

    private void uploadLocalStatus(SGNewsBean msgBean) {
        NewsEngine.updateNoticeChatNewsStatus(this, msgBean, UserPreferencesUtil.getUserId(this));
    }

    private void loadHistoryDatas() {
        MessageDatas = NewsEngine.quaryGroupNoticeChatHistory(this, msgBean, UserPreferencesUtil.getUserId(this));
    }

    @Override
    public void onDestroy() {
        unregisterBroadcastReceiver();
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        super.onDestroy();
    }

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(XMPPUtils.NEW_MESSAGE_ACTION);
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    public void unregisterBroadcastReceiver() {
        unregisterReceiver(mBroadcastReceiver);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (XMPPUtils.NEW_MESSAGE_ACTION.equals(intent.getAction())) {
                boolean flag = intent.getBooleanExtra("newsMessage", false);
                if (flag) {
                    SGNewsBean recivedMsgBean = (SGNewsBean) intent.getSerializableExtra("newBean");
                    android.os.Message message=new android.os.Message();
                    message.what=4;
                    message.obj=recivedMsgBean;
                    handler.sendMessage(message);
                }
            }
        }
    };

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.backButton) {
            finish();
        } else if (v.getId() == R.id.chat_sendbtn) {
            updateActionView(3);
            recordButton.setBackgroundResource(R.drawable.record_unpress_bg);
            recordButton.setTextColor(getResources().getColor(R.color.black));
            recordButton.setText("按住录音");
        } else if (v.getId() == R.id.faceButton) {
            updateActionView(0);
            // messageInput.setFocusable(false);
        } else if (v.getId() == R.id.messageButton) {
            // messageInput.setFocusable(false);
            updateActionView(2);
            String message = messageInput.getText().toString();
            if ("".equals(message)) {
                Toast.makeText(ChatOrderActivity.this, "不能为空", Toast.LENGTH_SHORT).show();
            } else {
                messageInput.setText("");
                sendMessageType = "1";
                sendTxtMessage(message);
            }
        } else if (v.getId() == R.id.addButton) {
            // messageInput.setFocusable(false);
            updateActionView(1);
        } else if (v.getId() == R.id.picPhoto) {
            chicePhoto();
            updateActionView(4);
        } else if (v.getId() == R.id.tokePhoto) {
            takePhoto();
            updateActionView(4);
        } else if (v.getId() == R.id.locationButton) {
            Intent intent = new Intent(this, BaiduLocation.class);
            startActivityForResult(intent, 2);
            updateActionView(4);
        }else if(v.getId()==R.id.tgInfo){
            if (isJJR){
                Intent intent = new Intent(this, JJRNoticeDetailActivity.class);
                intent.putExtra("noticeId", msgBean.getNoticeId());
                startActivity(intent);
            }else{
                if(UserPreferencesUtil.isModel(this)){
                    Intent intent = new Intent(this, NoticeDetailActivity.class);
                    intent.putExtra("noticeId",msgBean.getNoticeId());
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(this, RecruitActivity.class);
                    intent.putExtra("noticeId",msgBean.getNoticeId());
                    intent.putExtra("recruitId",msgBean.getRecruitId());
                    intent.putExtra("position",Integer.parseInt(msgBean.getRecruitLocIndex()));
                    startActivity(intent);
                }
            }
        }else if (v.getId()==R.id.personalInfo){
            Intent intent = new Intent(this, ChatDetailActivity.class);
            intent.putExtra("whereFromFlag","orderChat");
            intent.putExtra("bean",msgBean);
            startActivityForResult(intent, 3);
        }else if (v.getId()==R.id.noView){
            newOperatorAct(3);
        }else if (v.getId()==R.id.yesView){
            newOperatorAct(2);
        }else if (v.getId()==R.id.videoButton){
            Contant.startRecordActivity(this, 5);
            updateActionView(4);
        }
    }

    private void newOperatorAct(int index) {//index:2是同意，3是不同意
        currOperatorIndex=index;
        RequestBean item = new RequestBean();
        try {
            JSONObject obj = new JSONObject();
            obj.put("actId",actionId);
            obj.put("memberId", memberId);
            obj.put("memStatus",index );
            obj.put("id",msgBean.getRecruitId());
            ShowMsgDialog.showNoMsg(this, false);
            item.setData(obj);
            item.setServiceURL(ConstTaskTag.QUEST_ACT_APPLY);
            ThreadPool.getInstance().excuseAction(this, item, ConstTaskTag.QUERY_ACT_APPLY);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    protected void getResponseBean(ResponseBean data) {
        switch (data.getPosionSign()) {
            case ConstTaskTag.QUERY_ACT_APPLY:
                if ("0000".equals(data.getCode())) {
                    flowView.setVisibility(View.GONE);
                    adapter.updateItemStatus(operatorMessageBean);
                    NewsEngine.updateNoticeChatNewsOperatorStatus(ChatOrderActivity.this, operatorMessageBean, UserPreferencesUtil.getUserId(ChatOrderActivity.this));
                    switch (currOperatorIndex){
                        case 2:
                            sendMessageType = "1";
                            sendTxtMessage("我同意了你的约会请求");
                            break;
                        case 3:
                            sendMessageType = "1";
                            sendTxtMessage("我拒绝了你的约会请求");
                            break;
                    }
                } else {
                    Toast.makeText(this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.getResponseBean(data);
    }

    private void takePhoto() {
        // 跳转至拍照界面
        photoName = Constants.getImageName(this);
        Intent intentPhote = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentPhote.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        File out = new File(FileUtil.getPhotopath(photoName));
        Uri uri = Uri.fromFile(out);
        // 获取拍照后未压缩的原图片，并保存在uri路径中
        intentPhote.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intentPhote, TAKE_PICTURE);
    }

    private void chicePhoto() {
        TransferImagesBean dataBean = new TransferImagesBean();
        LinkedList<String> picDatas = new LinkedList<>();
        dataBean.setSelectImagePah(picDatas);
        Intent intent = new Intent(this, ImageChooserActivity.class);
        intent.putExtra(Constants.TRANS_PIC_NUM, Constants.CHAT_PIC_NUM);
        intent.putExtra("images", dataBean);
        startActivityForResult(intent, 4);
//        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
//        openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        EmojiconsFragment.backspace(messageInput);
    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(messageInput, emojicon);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        updateActionView(5);
        if (s.toString().length() > 0) {
            messageButton.setVisibility(View.VISIBLE);
            messageSendBtn.setVisibility(View.GONE);
        } else {
            messageButton.setVisibility(View.GONE);
            messageSendBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private class MessageListAdapter extends BaseAdapter {
        private List<SGNewsBean> datas;
        private Context context;
        private ImageLoader mImageLoader;
        private ControlFlowView controlFlowView;
        private VideoImageLoader videoImageLoader;

        public void addControlViewListener(ControlFlowView controlFlowView){
            this.controlFlowView=controlFlowView;
        }

        public MessageListAdapter(Context context, List<SGNewsBean> datas) {
            this.context = context;
            mImageLoader = ImageLoader.getInstance(3, ImageLoader.Type.LIFO);
            videoImageLoader  = VideoImageLoader.getInstance();
            if (datas != null) {
                this.datas = datas;
            } else {
                this.datas = new ArrayList<SGNewsBean>();
            }
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public SGNewsBean getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void clearDatas(){
            datas.clear();
        }

        public void addDatas(List<SGNewsBean> datas) {
            this.datas=datas;
            notifyDataSetChanged();
        }

        public void addItem(SGNewsBean it) {
            datas.add(it);
            notifyDataSetChanged();
        }

        public void updateItemSendStatus(SGNewsBean it) {
            for (int i = 0; i < datas.size(); i++) {
                if (it.getTimestamp().equals(datas.get(i).getTimestamp())) {
                    datas.get(i).setMsgStatus(it.getMsgStatus());
                }
            }
            notifyDataSetChanged();
        }

        public void updateItemStatus(SGNewsBean it) {
            for (int i = 0; i < datas.size(); i++) {
                if (it.getTimestamp().equals(datas.get(i).getTimestamp())) {
                    datas.get(i).setOperatorFlag(Constants.NEWS_OPERATOR);
                    break;
                }
            }
            notifyDataSetChanged();
        }

        public void updateItemSendStatusForImages(SGNewsBean it) {
            for (int i = 0; i < datas.size(); i++) {
                if (it.getRecruitLocIndex().equals(datas.get(i).getRecruitLocIndex())) {
                    datas.get(i).setOperatorFlag(Constants.NEWS_OPERATOR);
                    break;
                }
            }
            notifyDataSetChanged();
        }

        class HoldView {
            RoundedImageView imageView, mapImageView, imageViewHon;
            TextView useridView, dateView, msgView, mapAdress, voiceTime,messageTimer,actTitle,actTimer,actPrice;
            View soundView, mapImageLayout, photoView, otherView, sendFaith,reportView,playIcon;
            SeekBar skbProgress;
            ProgressBar sending;
            CircularImage from_head;
        }

        private class IntoPersonalDeltail implements View.OnClickListener{
            private SGNewsBean presenter;
            public IntoPersonalDeltail(SGNewsBean presenter){
                this.presenter=presenter;
            }

            @Override
            public void onClick(View v) {
                intoPersonalAct(presenter);
            }
        }

        private void intoPersonalAct(SGNewsBean message) {
            Intent intent = new Intent(context, PersonalDetailActivity.class);
            if (Constants.NEWS_RECIEVE.equals(message.getInout())) {
                intent.putExtra("userNick", SenderUser.getFromUserName(message,context));
                intent.putExtra("userId",SenderUser.getFromUserId(message,context));
            } else {
                intent.putExtra("userNick", UserPreferencesUtil.getUserNickName(context));
                intent.putExtra("userId",UserPreferencesUtil.getUserId(context));
            }
            context. startActivity(intent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HoldView holdView;
            final SGNewsBean message = datas.get(position);

            holdView = new HoldView();
            if (Constants.NEWS_RECIEVE.equals(message.getInout())) {
                convertView = LayoutInflater.from(context).inflate(R.layout.order_chat_in, null);
            } else {
                convertView = LayoutInflater.from(context).inflate(R.layout.order_chat_out, null);
                holdView.sending = (ProgressBar) convertView.findViewById(R.id.sending);
                holdView.sendFaith = convertView.findViewById(R.id.sendFaith);
            }
            holdView.from_head = (CircularImage) convertView.findViewById(R.id.from_head);
            holdView.useridView = (TextView) convertView.findViewById(R.id.formclient_row_userid);
            holdView.voiceTime = (TextView) convertView.findViewById(R.id.voiceTime);
            holdView.mapAdress = (TextView) convertView.findViewById(R.id.mapAdress);
            holdView.dateView = (TextView) convertView.findViewById(R.id.formclient_row_date);
            holdView.msgView = (TextView) convertView.findViewById(R.id.formclient_row_msg);
            holdView.messageTimer=(TextView)convertView.findViewById(R.id.messageTimer);
            holdView.otherView = convertView.findViewById(R.id.otherView);
            holdView.photoView = convertView.findViewById(R.id.photoView);
            holdView.playIcon=convertView.findViewById(R.id.playIcon);

            holdView.reportView=convertView.findViewById(R.id.reportView);
            holdView.actTitle=(TextView)convertView.findViewById(R.id.actTitle);
            holdView.actTimer=(TextView)convertView.findViewById(R.id.actTimer);
            holdView.actPrice=(TextView)convertView.findViewById(R.id.actPrice);

            holdView.soundView = convertView.findViewById(R.id.soundView);
            holdView.skbProgress = (SeekBar) convertView.findViewById(R.id.skbProgress);
            holdView.imageView = (RoundedImageView) convertView.findViewById(R.id.imageView);
            holdView.imageViewHon = (RoundedImageView) convertView.findViewById(R.id.imageViewHon);
            holdView.mapImageView = (RoundedImageView) convertView.findViewById(R.id.mapImageView);
            holdView.mapImageLayout = convertView.findViewById(R.id.mapImageLayout);
            holdView.playIcon.setVisibility(View.GONE);
            holdView.dateView.setVisibility(View.GONE);
            holdView.messageTimer.setText(TimeUtils.formatTime(Long.parseLong(message.getTimestamp())));
            if (Constants.NEWS_RECIEVE.equals(message.getInout())) {
                String userHead = SenderUser.getFromUserIcon(message, context);
                holdView.from_head.setImageResource(R.drawable.male);
                if (StringUtil.notEmpty(userHead)) {
                    if (userHead.contains("http://")) {
                        mImageLoader.loadImage(userHead, holdView.from_head, true);
                    } else {
                        ImageLocalLoader.getInstance().loadImage(userHead, holdView.from_head);
                    }
                }
            } else {
                if (Constants.NEWS_SEND_FAITH.equals(message.getMsgStatus())) {
                    holdView.sending.setVisibility(View.GONE);
                    holdView.sendFaith.setVisibility(View.VISIBLE);
                } else if (Constants.NEWS_SENDING.equals(message.getMsgStatus())) {
                    holdView.sending.setVisibility(View.VISIBLE);
                    holdView.sendFaith.setVisibility(View.GONE);
                } else if (Constants.NEWS_SEND_SCUESS.equals(message.getMsgStatus())) {
                    holdView.sending.setVisibility(View.GONE);
                    holdView.sendFaith.setVisibility(View.GONE);
                }
                holdView.useridView.setText("我");
                holdView.sendFaith.setOnClickListener(new ResendMessageListenser(message, position));

                String userHead = UserPreferencesUtil.getHeadPic(context);
                holdView.from_head.setImageResource(R.drawable.male);
                if (StringUtil.notEmpty(userHead)) {
                    if (userHead.contains("http://")) {
                        mImageLoader.loadImage(userHead, holdView.from_head, true);
                    } else {
                        ImageLocalLoader.getInstance().loadImage(userHead, holdView.from_head);
                    }
                }
            }
            holdView.from_head.setOnClickListener(new IntoPersonalDeltail(message));
            if (Constants.SEND_ACT.equals(message.getType())){
                holdView.playIcon.setVisibility(View.GONE);
                holdView.otherView.setVisibility(View.GONE);
                holdView.photoView.setVisibility(View.GONE);
                holdView.reportView.setVisibility(View.VISIBLE);
                holdView.msgView.setVisibility(View.GONE);
                try {
                    JSONObject object=new JSONObject(message.getMsgContent());
                    String actionId=StringUtils.getJsonValue(object,"actionId");
                    String totalPrice=StringUtils.getJsonValue(object,"totalPrice");
                    String singlePrice=StringUtils.getJsonValue(object,"singlePrice");
                    String actDate=StringUtils.getJsonValue(object,"actDate");
                    String actStartTime=StringUtils.getJsonValue(object,"actStartTime");
                    String actEndTime=StringUtils.getJsonValue(object,"actEndTime");
                    String actTitle=StringUtils.getJsonValue(object,"actTitle");
                    String memberId=StringUtils.getJsonValue(object,"memberId");
                    int totalInt=Integer.parseInt(totalPrice);
                    int singleInt=Integer.parseInt(singlePrice);
                    holdView.actPrice.setText(String.valueOf(totalInt).concat("(").concat(String.valueOf(totalInt / singleInt)).concat("小时)"));
                    holdView.actTitle.setText(actTitle);
                    holdView.actTimer.setText(actDate.concat(" ").concat(actStartTime).concat("-").concat(actEndTime));
                    if (Constants.NEWS_RECIEVE.equals(message.getInout())) {
                        if (!Constants.NEWS_OPERATOR.equals(message.getOperatorFlag())){
                            controlFlowView.flowViewStatus(true,actionId,memberId,message);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else if (Constants.SEND_TEXT.equals(message.getType())) {
                holdView.otherView.setVisibility(View.VISIBLE);
                holdView.playIcon.setVisibility(View.GONE);
                holdView.photoView.setVisibility(View.GONE);
                holdView.reportView.setVisibility(View.GONE);
                holdView.msgView.setVisibility(View.VISIBLE);
                String ss = message.getMsgContent();
                holdView.msgView.setText(ss);
                holdView.soundView.setVisibility(View.GONE);
                holdView.imageView.setVisibility(View.GONE);
                holdView.mapImageLayout.setVisibility(View.GONE);
                holdView.imageViewHon.setVisibility(View.GONE);
                holdView.msgView.setOnLongClickListener(new CopyTextLinesner(ss));
            } else if (Constants.SEND_SOUND.equals(message.getType())) {
                holdView.playIcon.setVisibility(View.GONE);
                holdView.otherView.setVisibility(View.VISIBLE);
                holdView.photoView.setVisibility(View.GONE);
                holdView.msgView.setVisibility(View.GONE);
                holdView.reportView.setVisibility(View.GONE);
                holdView.soundView.setVisibility(View.VISIBLE);
                holdView.imageView.setVisibility(View.GONE);
                holdView.imageViewHon.setVisibility(View.GONE);
                holdView.mapImageLayout.setVisibility(View.GONE);
                try {
                    JSONObject msgObj = new JSONObject(message.getMsgContent());
                    String voiceUrl = msgObj.getString("voiceUrl");
                    String voiceTime = msgObj.getString("length");
                    holdView.voiceTime.setText(voiceTime.concat("\""));
                    holdView.soundView.setOnClickListener(new PlayMp3Listener(voiceUrl));
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }else if (Constants.SEND_VIDEO.equals(message.getType())) {
                holdView.playIcon.setVisibility(View.VISIBLE);
                holdView.otherView.setVisibility(View.GONE);
                holdView.reportView.setVisibility(View.GONE);
                holdView.photoView.setVisibility(View.VISIBLE);
                holdView.imageView.setVisibility(View.VISIBLE);
                if (message.getMsgContent().contains("http://")) {
                    holdView.imageView.setImageResource(R.drawable.default_avatar);
                    videoImageLoader.DisplayImage(message.getMsgContent(), holdView.imageView);
                    holdView.imageView.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            Intent intent = new Intent(context, VideoPlayerActivity.class);
                            intent.putExtra("vidoUrl", message.getMsgContent());
                            context.startActivity(intent);
                        }
                    });
                    holdView.playIcon.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            Intent intent = new Intent(context, VideoPlayerActivity.class);
                            intent.putExtra("vidoUrl", message.getMsgContent());
                            context.startActivity(intent);
                        }
                    });
                } else {
                    ImageLocalLoader.getInstance(3, ImageLocalLoader.Type.LIFO).loadImage(message.getRecruitLocIndex(), holdView.imageView);
                    holdView.imageView.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            Intent intent = new Intent(context, VideoPlayerActivity.class);
                            intent.putExtra("vidoUrl", message.getMsgContent());
                            context.startActivity(intent);
                        }
                    });
                    holdView.playIcon.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            Intent intent = new Intent(context, VideoPlayerActivity.class);
                            intent.putExtra("vidoUrl", message.getMsgContent());
                            context.startActivity(intent);
                        }
                    });
                }
            } else if (Constants.SEND_IMAGE.equals(message.getType())) {
                holdView.playIcon.setVisibility(View.GONE);
                holdView.otherView.setVisibility(View.GONE);
                holdView.reportView.setVisibility(View.GONE);
                holdView.photoView.setVisibility(View.VISIBLE);
                holdView.imageView.setVisibility(View.VISIBLE);
                if (message.getMsgContent().contains(FileUtil.CHAT_IMAGES_ROOT_PATH)) {
                    ImageLocalLoader.getInstance(3, ImageLocalLoader.Type.LIFO).loadImage(message.getMsgContent(), holdView.imageView);
                } else {
                    mImageLoader.loadImage(message.getMsgContent(), holdView.imageView, true);
                }
                holdView.imageView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        Constants.imageBrower(context, 0, new String[]{message.getMsgContent()}, false);
                    }
                });
            } else if (Constants.SEND_LOCATION.equals(message.getType())) {
                holdView.playIcon.setVisibility(View.GONE);
                holdView.otherView.setVisibility(View.GONE);
                holdView.photoView.setVisibility(View.VISIBLE);
                holdView.reportView.setVisibility(View.GONE);
                holdView.msgView.setVisibility(View.GONE);
                holdView.soundView.setVisibility(View.GONE);
                holdView.imageView.setVisibility(View.GONE);
                holdView.imageViewHon.setVisibility(View.GONE);
                holdView.mapImageLayout.setVisibility(View.VISIBLE);

                try {
                    JSONObject obj = new JSONObject(message.getMsgContent());
                    final String lng = obj.getString("lng");
                    final String lat = obj.getString("lat");
                    String address = obj.getString("address");
                    holdView.mapAdress.setText(address);
                    holdView.mapAdress.getBackground().setAlpha(100);
                    holdView.mapImageLayout.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            TransferBean tBean = new TransferBean();
                            tBean.setLatitude(lat);
                            tBean.setLongitude(lng);
                            Intent intent = new Intent();
                            intent.putExtra("bean", tBean);
                            intent.setClass(context, ShowBaiduLocation.class);
                            intent.putExtra("titleStr", "查看地理位置");
                            startActivity(intent);
                        }
                    });
                    String imageUrl = "http://api.map.baidu.com/staticimage?center=" + lng + "," + lat
                            + "&width=150&height=100";
                    mImageLoader.loadImage(imageUrl, holdView.mapImageView, true);

                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
            return convertView;
        }

        private class CopyTextLinesner implements View.OnLongClickListener{
            private String content;
            public CopyTextLinesner(String ss){
                this.content=ss;
            }


            @Override
            public boolean onLongClick(View v) {
                alertDialog(content);
                return false;
            }
        }

        private class PlayMp3Listener implements View.OnClickListener {
            private String url;
            public PlayMp3Listener(String url){
                this.url=url;
            }


            @Override
            public void onClick(View v) {
                playerWorker(url);
            }
        }

        private void playerWorker(String url){
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void updateActionView(int index) {
        switch (index) {
            case 0:// 显示表情
                imm.hideSoftInputFromWindow(messageInput.getWindowToken(), 0);
                if (recordButton.getVisibility() == View.VISIBLE) {
                    recordButton.setVisibility(View.GONE);
                    messageInput.setVisibility(View.VISIBLE);
                    messageSendBtn.setImageResource(R.drawable.xmpp_sound_button);
                }
                if (moreOption.getVisibility() == View.VISIBLE) {
                    moreOption.startAnimation(alphaOut);
                    moreOption.setVisibility(View.GONE);
                }
                if (emojicons.getVisibility() == View.VISIBLE) {
                    emojicons.startAnimation(alphaOut);
                    emojicons.setVisibility(View.GONE);
                } else {
                    emojicons.startAnimation(alphaIn);
                    emojicons.setVisibility(View.VISIBLE);
                }
                break;
            case 1:// 显示更多
                imm.hideSoftInputFromWindow(messageInput.getWindowToken(), 0);
                if (recordButton.getVisibility() == View.VISIBLE) {
                    recordButton.setVisibility(View.GONE);
                    messageInput.setVisibility(View.VISIBLE);
                    messageSendBtn.setImageResource(R.drawable.xmpp_sound_button);
                }
                if (emojicons.getVisibility() == View.VISIBLE) {
                    emojicons.startAnimation(alphaOut);
                    emojicons.setVisibility(View.GONE);
                }
                if (moreOption.getVisibility() == View.VISIBLE) {
                    moreOption.startAnimation(alphaOut);
                    moreOption.setVisibility(View.GONE);
                } else {
                    moreOption.startAnimation(alphaIn);
                    moreOption.setVisibility(View.VISIBLE);
                }
                break;
            case 2:// 文本输入,发送
                if (moreOption.getVisibility() == View.VISIBLE) {
                    moreOption.startAnimation(alphaOut);
                    moreOption.setVisibility(View.GONE);
                }
                if (emojicons.getVisibility() == View.VISIBLE) {
                    emojicons.startAnimation(alphaOut);
                    emojicons.setVisibility(View.GONE);
                }
                break;
            case 3:// 语音
                imm.hideSoftInputFromWindow(messageInput.getWindowToken(), 0);
                if (recordButton.getVisibility() == View.GONE) {
                    recordButton.setVisibility(View.VISIBLE);
                    messageInput.setVisibility(View.GONE);
                    messageSendBtn.setImageResource(R.drawable.notice_msg);
                } else {
                    recordButton.setVisibility(View.GONE);
                    messageInput.setVisibility(View.VISIBLE);
                    messageSendBtn.setImageResource(R.drawable.xmpp_sound_button);
                }

                if (moreOption.getVisibility() == View.VISIBLE) {
                    moreOption.startAnimation(alphaOut);
                    moreOption.setVisibility(View.GONE);
                }
                if (emojicons.getVisibility() == View.VISIBLE) {
                    emojicons.startAnimation(alphaOut);
                    emojicons.setVisibility(View.GONE);
                }
                break;
            case 4:
                if (recordButton.getVisibility() == View.VISIBLE) {
                    recordButton.setVisibility(View.GONE);
                    messageInput.setVisibility(View.VISIBLE);
                }
                if (emojicons.getVisibility() == View.VISIBLE) {
                    emojicons.startAnimation(alphaOut);
                    emojicons.setVisibility(View.GONE);
                }
                if (moreOption.getVisibility() == View.VISIBLE) {
                    moreOption.startAnimation(alphaOut);
                    moreOption.setVisibility(View.GONE);
                }
                break;

            case 5:// 文本输入,发送
                if (moreOption.getVisibility() == View.VISIBLE) {
                    moreOption.startAnimation(alphaOut);
                    moreOption.setVisibility(View.GONE);
                }
                // if (emojicons.getVisibility() == View.VISIBLE) {
                // emojicons.startAnimation(alphaOut);
                // emojicons.setVisibility(View.GONE);
                // }
                break;
            default:
                break;
        }
    }

    private void alertDialog(final String content){
        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        dlg.show();
        LayoutInflater factory = LayoutInflater.from(this);
        View view = factory.inflate(R.layout.chat_history_pop, null);
        View copyView=view.findViewById(R.id.copyView);
        copyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringUtil.copy(content,ChatOrderActivity.this);
                dlg.dismiss();
            }
        });
        dlg.getWindow().setContentView(view);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            Bitmap bitmap = FileUtil.compressImages(FileUtil.getPhotopath(photoName));
            if (bitmap != null) {
                FileUtil.saveScalePhoto(FileUtil.getPhotopath(photoName), bitmap);
                String picPath = FileUtil.getPhotopath(photoName);
                try {
                    initImageMessage(picPath, Uuid16.uuid());
                    excuseSendPicAction();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            ContentResolver resolver = getContentResolver();
            // 照片的原始资源地址
            Uri originalUri = data.getData();
            try {
                // 使用ContentProvider通过URI获取原始图片
                Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                if (photo != null) {
                    Bitmap bitmap = FileUtil.compressImages(photo);
                    int angle = FileUtil.getOrientation(this, originalUri);
                    if (angle > 0) {
                        bitmap = FileUtil.rotaingImageView(angle, bitmap);
                    }
                    photoName = Constants.getImageName(this);
                    FileUtil.saveScalePhoto(FileUtil.getPhotopath(photoName), bitmap);
                    String picPath = FileUtil.getPhotopath(photoName);
                    try {
//                        initImageMessage(picPath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            Serializable obj = data.getSerializableExtra("resultObject");
            if (obj != null) {
                locationBean = (ShareLocationBean) obj;
                sendBaiduLocation();
            }
        }else if (requestCode == 3 && resultCode == Activity.RESULT_OK) {
            boolean flag=data.getBooleanExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG,false);
            if (flag){
                getDatas();
            }
        }else if (requestCode == 4 && resultCode == Activity.RESULT_OK) {
            Object result = data.getSerializableExtra(Constants.COMEBACK);
            if (result != null) {
                TransferImagesBean dataBean = (TransferImagesBean) result;
                LinkedList<String> tempStrs = dataBean.getSelectImagePath();
                for (int i = 0; i < tempStrs.size(); i++) {
                    if (tempStrs.get(i)
                            .contains(FileUtil.CHAT_IMAGES_ROOT_PATH)) {
                        initImageMessage(tempStrs.get(i), Uuid16.uuid());
                    } else {
                        Bitmap subBitmap = FileUtil.compressImages(tempStrs
                                .get(i));
                        if (subBitmap != null) {
                            String photoName = Constants.getImageName(this);
                            FileUtil.saveScalePhoto(
                                    FileUtil.getPhotopath(photoName), subBitmap);
                            subBitmap.recycle();
                            initImageMessage(FileUtil.getPhotopath(photoName), Uuid16.uuid());
                        }
                    }
                }
                excuseSendPicAction();
            }
        }else if (requestCode == 5 && resultCode == Activity.RESULT_OK) {
            RecordResult result =new RecordResult(data);
            String videoFile;
            String [] thum;
            videoFile = result.getPath();
            thum = result.getThumbnail();
            result.getDuration();
            ShowMsgDialog.showNoMsg(this, false);
            ThreadPool.getInstance().excuseThread(new MovingMp4(videoFile,thum[0]));
        }
    }

    private class MovingMp4 implements Runnable{
        private String videoFile;
        private String videoImage;
        public MovingMp4(String videoFile,String videoImage){
            this.videoFile=videoFile;
            this.videoImage=videoImage;
        }
        @Override
        public void run() {
            String mFileName= FileUtil.RECORD_ROOT_PATH.concat(Constants.getMP4Name(ChatOrderActivity.this));
            int isSccuss=FileUtil.CopySdcardFile(videoFile,mFileName);
            if (isSccuss==0){
                Bundle bundle=new Bundle();
                bundle.putString("videoFile",mFileName);
                bundle.putString("videoImage",videoImage);
                android.os.Message msg = new android.os.Message();
                msg.what = 10;
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
        }
    }

    private void uploadImageEngine(String path) {
        AliySSOHepler.getInstance().uploadImageEngine(this, Constants.MESSAGE_PATH, path, new HttpCallBack() {

            @Override
            public void onSuccess(String imageUrl, int requestCode) {
                android.os.Message msg = new android.os.Message();
                msg.obj = imageUrl;
                msg.what = 6;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(int errorCode, String msg, int requestCode) {
                // TODO Auto-generated method stub
                android.os.Message msg1 = new android.os.Message();
                msg1.what = 7;
                handler.sendMessage(msg1);
            }

            @Override
            public void onProgress(String objectKey, int byteCount, int totalSize) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void uploadMedia(String path) {
        AliySSOHepler.getInstance().uploadMedia(this,Constants.MESSAGE_PATH, path, new HttpCallBack() {

            @Override
            public void onSuccess(String imageUrl, int requestCode) {
                android.os.Message msg = new android.os.Message();
                msg.obj = imageUrl;
                msg.what = 6;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(int errorCode, String msg, int requestCode) {
                // TODO Auto-generated method stub
                android.os.Message msg1 = new android.os.Message();
                msg1.what = 7;
                handler.sendMessage(msg1);
            }

            @Override
            public void onProgress(String objectKey, int byteCount, int totalSize) {
                // TODO Auto-generated method stub
            }
        });
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (null != msg) {
                switch (msg.what) {
                    case 1:
                        String imageUrl = (String) msg.obj;

                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(), "图片上传失败", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        if (MessageDatas != null) {
                            adapter.addDatas(MessageDatas);
                        }
                        if (!"noticeNewsList".equals(fromFlag)){
                            sendActMessage();
                        }
                        break;
                    case 4:
                        SGNewsBean recivedMsgBean=(SGNewsBean)msg.obj;
                        if (msgBean.getFriendUserId().equals(recivedMsgBean.getFriendUserId())) {
                            adapter.addItem(recivedMsgBean);
                            uploadLocalStatus(recivedMsgBean);
                        }
                        break;
                    case 5:
                        String mp3Path = (String) msg.obj;
                        int minute = msg.arg1;
                        initVoiceMessage(mp3Path, minute);
                        break;
                    case 6:
                        String aliyUrl = (String) msg.obj;
                        if (Constants.SEND_SOUND.equals(sendMessageType)) {
                            sendVoiceMessage(aliyUrl);
                        } else if (Constants.SEND_IMAGE.equals(sendMessageType)) {
                            sendImageMessage(aliyUrl);
                        } else if (Constants.SEND_VIDEO.equals(sendMessageType)) {
                            sendVideoMessage(aliyUrl);
                        }
                        break;
                    case 7:
                        if (Constants.SEND_IMAGE.equals(sendMessageType)) {
                            sendBean.setMsgStatus(Constants.NEWS_SEND_FAITH);
                            NewsEngine.updateNoticeChatSendStatusForImages(ChatOrderActivity.this, sendBean,
                                    UserPreferencesUtil.getUserId(ChatOrderActivity.this));
                            adapter.updateItemSendStatusForImages(sendBean);
                            excuseSendPicAction();
                        } else{
                            sendBean.setMsgStatus(Constants.NEWS_SEND_FAITH);
                            NewsEngine.updateNoticeChatSendStatus(ChatOrderActivity.this, sendBean,
                                    UserPreferencesUtil.getUserId(ChatOrderActivity.this));
                            adapter.updateItemSendStatus(sendBean);
                        }
                        break;
                    case 9:
                        boolean flag = (Boolean) msg.obj;
                        if (flag){
                            flowView.setVisibility(View.VISIBLE);
                        }else{
                            flowView.setVisibility(View.GONE);
                        }
                        break;
                    case 8:
                        int resInt = (Integer) msg.obj;
//					dealWithResult(resInt);
                        break;
                    case 10:
                        ShowMsgDialog.cancel();
                        Bundle bundle = msg.getData();
                        String videoFile = bundle.getString("videoFile");
                        String videoImage = bundle.getString("videoImage");
                        initVidelMessage(videoFile, videoImage);
                        break;
                    default:
                        break;
                }
            }
        }
    };

    private void sendVideoMessage(String aliyUrl) {
        try {
            String messageContent = "";
            JSONObject bodyMsg = new JSONObject();
            bodyMsg.put("msgContent", aliyUrl);
            bodyMsg.put("type", Constants.SEND_VIDEO);
            JSONObject ext = new JSONObject();
            ext.put("fromUser", new JSONObject(msgBean.getFromUser()));
            ext.put("toUser", new JSONObject(msgBean.getToUser()));
            ext.put("noticeSubject", msgBean.getNoticeSubject());
            ext.put("recruitLocIndex", msgBean.getRecruitLocIndex());
            bodyMsg.put("ext", ext);
            bodyMsg.put("timestamp", aliyTimeStamp);
            bodyMsg.put("noticeId", msgBean.getNoticeId());
            bodyMsg.put("recruitId", msgBean.getRecruitId());
            messageContent = bodyMsg.toString();
            String time = XMPPUtils.date2Str(aliyTimeStamp, XMPPUtils.MS_FORMART);
            Message message = new Message();
//            message.setProperty("immessage.time", time);
            message.setSubject("501");
            message.setBody(messageContent);
            sendBean.setMsgContent(aliyUrl);
            NewsEngine.updateNoticeChatSendContent(this, sendBean, UserPreferencesUtil.getUserId(this));
            DeliveryReceiptManager.addDeliveryReceiptRequest(message);//发送回执消息前
            chat.sendMessage(message);
            sendBean.setMsgStatus(Constants.NEWS_SEND_SCUESS);
            NewsEngine.updateNoticeChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatus(sendBean);
        } catch (Exception e) {
            sendBean.setMsgStatus(Constants.NEWS_SEND_FAITH);
            NewsEngine.updateNoticeChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatus(sendBean);
            e.printStackTrace();
        }
    }

    private void initVidelMessage(String videoFile, String videoImage) {
        sendMessageType = Constants.SEND_VIDEO;
        sendBean = new SGNewsBean();
        try {
            // 创建消息实体
            aliyTimeStamp = System.currentTimeMillis();
            // 添加实体对象
            sendBean.setFromUser(msgBean.getFromUser());
            sendBean.setToUser(msgBean.getToUser());
            sendBean.setNoticeSubject(msgBean.getNoticeSubject());
            sendBean.setRecruitLocIndex(videoImage);
            sendBean.setNoticeId(msgBean.getNoticeId());
            sendBean.setFriendNickName(msgBean.getFriendNickName());
            sendBean.setFriendUserIcon(msgBean.getFriendUserIcon());
            sendBean.setFriendUserId(msgBean.getFriendUserId());
            // +++++++++++++++++++++++++++++++++++++++++++++++
            sendBean.setMsgContent(videoFile);
            sendBean.setNewType(Constants.NEWS_CHAT);
            sendBean.setRecruitId(msgBean.getRecruitId());
            sendBean.setTimestamp(String.valueOf(aliyTimeStamp));
            sendBean.setType(Constants.SEND_VIDEO);
            sendBean.setUserId(UserPreferencesUtil.getUserId(this));
            sendBean.setMessageStatus(Constants.NEWS_READ);
            sendBean.setInout(Constants.NEWS_END);
            sendBean.setIsShow(Constants.NEWS_SHOW);
            sendBean.setMsgStatus(Constants.NEWS_SENDING);
            uploadMedia(videoFile);
            // 新增本地数据
            NewsEngine.inserNoticeChatNew(this, sendBean);
            adapter.addItem(sendBean);
        } catch (Exception e) {
            e.printStackTrace();
            sendBean.setMsgStatus(Constants.NEWS_SEND_FAITH);
            NewsEngine.updateNoticeChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatus(sendBean);
        }
    }


    protected void sendImageMessage(String aliyUrl) {

        try {
            String messageContent = "";
            JSONObject bodyMsg = new JSONObject();
            bodyMsg.put("msgContent", aliyUrl);
            bodyMsg.put("type", Constants.SEND_IMAGE);
            JSONObject ext = new JSONObject();
            ext.put("fromUser", new JSONObject(msgBean.getFromUser()));
            ext.put("toUser", new JSONObject(msgBean.getToUser()));
            ext.put("noticeSubject", msgBean.getNoticeSubject());
            ext.put("recruitLocIndex", msgBean.getRecruitLocIndex());
            bodyMsg.put("ext", ext);
            bodyMsg.put("timestamp", aliyTimeStamp);
            bodyMsg.put("noticeId", msgBean.getNoticeId());
            bodyMsg.put("recruitId", msgBean.getRecruitId());
            messageContent = bodyMsg.toString();
            String time = XMPPUtils.date2Str(aliyTimeStamp, XMPPUtils.MS_FORMART);
            Message message = new Message();
//            message.setProperty("immessage.time", time);
            message.setSubject("501");
            message.setBody(messageContent);
            sendBean.setMsgContent(aliyUrl);
            NewsEngine.updateNoticeChatSendContentForImages(this, sendBean, UserPreferencesUtil.getUserId(this));
            DeliveryReceiptManager.addDeliveryReceiptRequest(message);//发送回执消息前
            chat.sendMessage(message);
            sendBean.setMsgStatus(Constants.NEWS_SEND_SCUESS);
            NewsEngine.updateNoticeChatSendStatusForImages(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatusForImages(sendBean);
            excuseSendPicAction();
        } catch (Exception e) {
            sendBean.setMsgStatus(Constants.NEWS_SEND_FAITH);
            NewsEngine.updateNoticeChatSendStatusForImages(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatusForImages(sendBean);
            excuseSendPicAction();
            e.printStackTrace();
        }

    }

    private void excuseSendPicAction(){
        if (currSendingIndex<sendImages.size()){
            sendBean=sendImages.get(currSendingIndex);
            currSendingIndex=currSendingIndex+1;
            // 发送图片
            uploadImageEngine(sendBean.getMsgContent());
        }else{
            currSendingIndex=0;
            sendImages.clear();
        }
    }

    private void initImageMessage(String imagePath,String recruitLocIndex) {
        sendMessageType = Constants.SEND_IMAGE;
        sendBean = new SGNewsBean();
        try {
            // 创建消息实体
            aliyTimeStamp = System.currentTimeMillis();
            // 添加实体对象
            sendBean.setFromUser(msgBean.getFromUser());
            sendBean.setToUser(msgBean.getToUser());
            sendBean.setNoticeSubject(msgBean.getNoticeSubject());
            sendBean.setRecruitLocIndex(recruitLocIndex);
            sendBean.setNoticeId(msgBean.getNoticeId());
            sendBean.setFriendNickName(msgBean.getFriendNickName());
            sendBean.setFriendUserIcon(msgBean.getFriendUserIcon());
            sendBean.setFriendUserId(msgBean.getFriendUserId());
            // +++++++++++++++++++++++++++++++++++++++++++++++
            sendBean.setMsgContent(imagePath);
            sendBean.setNewType(Constants.NEWS_CHAT);
            sendBean.setRecruitId(msgBean.getRecruitId());
            sendBean.setTimestamp(String.valueOf(aliyTimeStamp));
            sendBean.setType(Constants.SEND_IMAGE);
            sendBean.setUserId(UserPreferencesUtil.getUserId(this));
            sendBean.setMessageStatus(Constants.NEWS_READ);
            sendBean.setInout(Constants.NEWS_END);
            sendBean.setIsShow(Constants.NEWS_SHOW);
            sendBean.setMsgStatus(Constants.NEWS_SENDING);
            sendImages.add(sendBean);
            // 新增本地数据
            NewsEngine.inserNoticeChatNew(this, sendBean);
            adapter.addItem(sendBean);
        } catch (Exception e) {
            e.printStackTrace();
            sendBean.setMsgStatus(Constants.NEWS_SEND_FAITH);
            NewsEngine.updateNoticeChatSendStatusForImages(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatusForImages(sendBean);
        }
    }

    private void sendBaiduLocation() {
        sendBean = new SGNewsBean();
        try {
            // 创建消息实体locationBean
            aliyTimeStamp = System.currentTimeMillis();
            JSONObject msgObj = new JSONObject();
            msgObj.put("lng", locationBean.getLongitude());
            msgObj.put("lat", locationBean.getLatitude());
            msgObj.put("address", locationBean.getAddress());
            // 添加实体对象
            sendBean.setFromUser(msgBean.getFromUser());
            sendBean.setToUser(msgBean.getToUser());
            sendBean.setNoticeSubject(msgBean.getNoticeSubject());
            sendBean.setRecruitLocIndex(msgBean.getRecruitLocIndex());
            sendBean.setNoticeId(msgBean.getNoticeId());
            sendBean.setFriendNickName(msgBean.getFriendNickName());
            sendBean.setFriendUserIcon(msgBean.getFriendUserIcon());
            sendBean.setFriendUserId(msgBean.getFriendUserId());
            // +++++++++++++++++++++++++++++++++++++++++++++++
            sendBean.setMsgContent(msgObj.toString());
            sendBean.setNewType(Constants.NEWS_CHAT);
            sendBean.setRecruitId(msgBean.getRecruitId());
            sendBean.setTimestamp(String.valueOf(aliyTimeStamp));
            sendBean.setType(Constants.SEND_LOCATION);
            sendBean.setUserId(UserPreferencesUtil.getUserId(this));
            sendBean.setMessageStatus(Constants.NEWS_READ);
            sendBean.setInout(Constants.NEWS_END);
            sendBean.setIsShow(Constants.NEWS_SHOW);
            sendBean.setMsgStatus(Constants.NEWS_SENDING);
            // 新增本地数据
            NewsEngine.inserNoticeChatNew(this, sendBean);
            adapter.addItem(sendBean);
            // 发送消息
            String messageContent = "";
            JSONObject bodyMsg = new JSONObject();
            bodyMsg.put("msgContent", msgObj);
            bodyMsg.put("type", Constants.SEND_LOCATION);
            JSONObject ext = new JSONObject();
            ext.put("fromUser", new JSONObject(msgBean.getFromUser()));
            ext.put("toUser", new JSONObject(msgBean.getToUser()));
            ext.put("noticeSubject", msgBean.getNoticeSubject());
            ext.put("recruitLocIndex", msgBean.getRecruitLocIndex());
            bodyMsg.put("ext", ext);
            bodyMsg.put("timestamp", aliyTimeStamp);
            bodyMsg.put("noticeId", msgBean.getNoticeId());
            bodyMsg.put("recruitId", msgBean.getRecruitId());
            messageContent = bodyMsg.toString();
            String time = XMPPUtils.date2Str(aliyTimeStamp, XMPPUtils.MS_FORMART);
            Message message = new Message();
//            message.setProperty("immessage.time", time);
            message.setSubject("501");
            message.setBody(messageContent);
            DeliveryReceiptManager.addDeliveryReceiptRequest(message);//发送回执消息前
            chat.sendMessage(message);
            sendBean.setMsgStatus(Constants.NEWS_SEND_SCUESS);
            NewsEngine.updateNoticeChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatus(sendBean);
        } catch (Exception e) {
            e.printStackTrace();
            sendBean.setMsgStatus(Constants.NEWS_SEND_FAITH);
            NewsEngine.updateNoticeChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatus(sendBean);
        }
    }

    protected void sendVoiceMessage(String aliyUrl) {

        try {
            String messageContent = "";
            JSONObject bodyMsg = new JSONObject();
            JSONObject msgObj = new JSONObject();
            msgObj.put("voiceUrl", aliyUrl);
            msgObj.put("length", voiceLength);
            bodyMsg.put("msgContent", msgObj);
            bodyMsg.put("type", Constants.SEND_SOUND);
            JSONObject ext = new JSONObject();
            ext.put("fromUser", new JSONObject(msgBean.getFromUser()));
            ext.put("toUser", new JSONObject(msgBean.getToUser()));
            ext.put("noticeSubject", msgBean.getNoticeSubject());
            ext.put("recruitLocIndex", msgBean.getRecruitLocIndex());
            bodyMsg.put("ext", ext);
            bodyMsg.put("timestamp", aliyTimeStamp);
            bodyMsg.put("noticeId", msgBean.getNoticeId());
            bodyMsg.put("recruitId", msgBean.getRecruitId());
            messageContent = bodyMsg.toString();
            String time = XMPPUtils.date2Str(aliyTimeStamp, XMPPUtils.MS_FORMART);
            Message message = new Message();
//            message.setProperty("immessage.time", time);
            message.setSubject("501");
            message.setBody(messageContent);
            sendBean.setMsgContent(msgObj.toString());
            NewsEngine.updateNoticeChatSendContent(this, sendBean, UserPreferencesUtil.getUserId(this));
            DeliveryReceiptManager.addDeliveryReceiptRequest(message);//发送回执消息前
            chat.sendMessage(message);
            sendBean.setMsgStatus(Constants.NEWS_SEND_SCUESS);
            NewsEngine.updateNoticeChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatus(sendBean);
        } catch (Exception e) {
            sendBean.setMsgStatus(Constants.NEWS_SEND_FAITH);
            NewsEngine.updateNoticeChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatus(sendBean);
            e.printStackTrace();
        }

    }

    private void initVoiceMessage(String voicePath, int minute) {
        sendMessageType = Constants.SEND_SOUND;
        this.voiceLength = minute;
        sendBean = new SGNewsBean();
        try {
            // 创建消息实体
            aliyTimeStamp = System.currentTimeMillis();
            JSONObject msgObj = new JSONObject();
            msgObj.put("voiceUrl", voicePath);
            msgObj.put("length", minute);
            // 添加实体对象
            sendBean.setFromUser(msgBean.getFromUser());
            sendBean.setToUser(msgBean.getToUser());
            sendBean.setNoticeSubject(msgBean.getNoticeSubject());
            sendBean.setRecruitLocIndex(msgBean.getRecruitLocIndex());
            sendBean.setNoticeId(msgBean.getNoticeId());
            sendBean.setFriendNickName(msgBean.getFriendNickName());
            sendBean.setFriendUserIcon(msgBean.getFriendUserIcon());
            sendBean.setFriendUserId(msgBean.getFriendUserId());
            // +++++++++++++++++++++++++++++++++++++++++++++++
            sendBean.setMsgContent(msgObj.toString());
            sendBean.setNewType(Constants.NEWS_CHAT);
            sendBean.setRecruitId(msgBean.getRecruitId());
            sendBean.setTimestamp(String.valueOf(aliyTimeStamp));
            sendBean.setType(Constants.SEND_SOUND);
            sendBean.setUserId(UserPreferencesUtil.getUserId(this));
            sendBean.setMessageStatus(Constants.NEWS_READ);
            sendBean.setInout(Constants.NEWS_END);
            sendBean.setIsShow(Constants.NEWS_SHOW);
            sendBean.setMsgStatus(Constants.NEWS_SENDING);
            uploadMedia(voicePath);
            // 新增本地数据
            NewsEngine.inserNoticeChatNew(this, sendBean);
            adapter.addItem(sendBean);
        } catch (Exception e) {
            e.printStackTrace();
            sendBean.setMsgStatus(Constants.NEWS_SEND_FAITH);
            NewsEngine.updateNoticeChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatus(sendBean);
        }
    }

    private void sendTxtMessage(String content) {
        sendBean = new SGNewsBean();
        try {
            // 创建消息实体
            long timestamp = System.currentTimeMillis();
            String messageContent = "";
            JSONObject bodyMsg = new JSONObject();
            bodyMsg.put("msgContent", content);
            bodyMsg.put("type", Constants.SEND_TEXT);
            JSONObject ext = new JSONObject();
            ext.put("fromUser", new JSONObject(msgBean.getFromUser()));
            ext.put("toUser", new JSONObject(msgBean.getToUser()));
            ext.put("noticeSubject", msgBean.getNoticeSubject());
            ext.put("recruitLocIndex", msgBean.getRecruitLocIndex());
            bodyMsg.put("ext", ext);
            bodyMsg.put("timestamp", timestamp);
            bodyMsg.put("noticeId", msgBean.getNoticeId());
            bodyMsg.put("recruitId", msgBean.getRecruitId());
            messageContent = bodyMsg.toString();
            String time = XMPPUtils.date2Str(timestamp, XMPPUtils.MS_FORMART);
            Message message = new Message();
//            message.setProperty("immessage.time", time);
            message.setSubject("501");
            message.setBody(messageContent);
            // 添加实体对象

            sendBean.setFromUser(msgBean.getFromUser());
            sendBean.setToUser(msgBean.getToUser());
            sendBean.setNoticeSubject(msgBean.getNoticeSubject());
            sendBean.setRecruitLocIndex(msgBean.getRecruitLocIndex());
            sendBean.setNoticeId(msgBean.getNoticeId());
            sendBean.setFriendNickName(msgBean.getFriendNickName());
            sendBean.setFriendUserIcon(msgBean.getFriendUserIcon());
            sendBean.setFriendUserId(msgBean.getFriendUserId());
            // +++++++++++++++++++++++++++++++++++++++++++++++
            sendBean.setMsgContent(content);
            sendBean.setNewType(Constants.NEWS_CHAT);
            sendBean.setRecruitId(msgBean.getRecruitId());
            sendBean.setTimestamp(String.valueOf(timestamp));
            sendBean.setType(Constants.SEND_TEXT);
            sendBean.setUserId(UserPreferencesUtil.getUserId(this));
            sendBean.setMessageStatus(Constants.NEWS_READ);
            sendBean.setInout(Constants.NEWS_END);
            sendBean.setIsShow(Constants.NEWS_SHOW);
            sendBean.setMsgStatus(Constants.NEWS_SENDING);
            // 新增本地数据
            NewsEngine.inserNoticeChatNew(this, sendBean);
            adapter.addItem(sendBean);
            // 发送消息
            DeliveryReceiptManager.addDeliveryReceiptRequest(message);//发送回执消息前
//			String messageId=message.getPacketID();
            chat.sendMessage(message);
            sendBean.setMsgStatus(Constants.NEWS_SEND_SCUESS);
            NewsEngine.updateNoticeChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatus(sendBean);
        } catch (Exception e) {
            e.printStackTrace();
            sendBean.setMsgStatus(Constants.NEWS_SEND_FAITH);
            NewsEngine.updateNoticeChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatus(sendBean);
        }
    }


    private void sendActMessage() {
        sendBean = new SGNewsBean();
        try {
            //封装报名活动信息
            JSONObject actObj=new JSONObject();
            actObj.put("actDate",actDate);
            actObj.put("actStartTime",actStartTime);
            actObj.put("actEndTime",actEndTime);
            actObj.put("singlePrice",singlePrice);
            actObj.put("totalPrice",totalPrice);
            actObj.put("actTitle",actTitle);
            actObj.put("actionId",actionId);
            actObj.put("memberId",memberId);

            // 创建消息实体
            long timestamp = System.currentTimeMillis();
            String messageContent = "";
            JSONObject bodyMsg = new JSONObject();
            bodyMsg.put("msgContent", actObj);
            bodyMsg.put("type", Constants.SEND_ACT);
            JSONObject ext = new JSONObject();
            ext.put("fromUser", new JSONObject(msgBean.getFromUser()));
            ext.put("toUser", new JSONObject(msgBean.getToUser()));
            ext.put("noticeSubject", msgBean.getNoticeSubject());
            ext.put("recruitLocIndex", msgBean.getRecruitLocIndex());
            bodyMsg.put("ext", ext);
            bodyMsg.put("timestamp", timestamp);
            bodyMsg.put("noticeId", msgBean.getNoticeId());
            bodyMsg.put("recruitId", msgBean.getRecruitId());
            messageContent = bodyMsg.toString();
            String time = XMPPUtils.date2Str(timestamp, XMPPUtils.MS_FORMART);
            Message message = new Message();
            message.setSubject("5");
//            message.setProperty("immessage.time", time);
            message.setBody(messageContent);
            // 添加实体对象

            sendBean.setFromUser(msgBean.getFromUser());
            sendBean.setToUser(msgBean.getToUser());
            sendBean.setNoticeSubject(msgBean.getNoticeSubject());
            sendBean.setRecruitLocIndex(msgBean.getRecruitLocIndex());
            sendBean.setNoticeId(msgBean.getNoticeId());
            sendBean.setFriendNickName(msgBean.getFriendNickName());
            sendBean.setFriendUserIcon(msgBean.getFriendUserIcon());
            sendBean.setFriendUserId(msgBean.getFriendUserId());
            // +++++++++++++++++++++++++++++++++++++++++++++++
            String content=actObj.toString();
            sendBean.setMsgContent(content);
            sendBean.setNewType(Constants.NEWS_CHAT);
            sendBean.setRecruitId(msgBean.getRecruitId());
            sendBean.setTimestamp(String.valueOf(timestamp));
            sendBean.setType(Constants.SEND_ACT);
            sendBean.setUserId(UserPreferencesUtil.getUserId(this));
            sendBean.setMessageStatus(Constants.NEWS_READ);
            sendBean.setInout(Constants.NEWS_END);
            sendBean.setIsShow(Constants.NEWS_SHOW);
            sendBean.setMsgStatus(Constants.NEWS_SENDING);
            // 新增本地数据
            NewsEngine.inserNoticeChatNew(this, sendBean);
            adapter.addItem(sendBean);
            // 发送消息
            DeliveryReceiptManager.addDeliveryReceiptRequest(message);//发送回执消息前
//			String messageId=message.getPacketID();
            chat.sendMessage(message);
            sendBean.setMsgStatus(Constants.NEWS_SEND_SCUESS);
            NewsEngine.updateNoticeChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatus(sendBean);
        } catch (Exception e) {
            e.printStackTrace();
            sendBean.setMsgStatus(Constants.NEWS_SEND_FAITH);
            NewsEngine.updateNoticeChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatus(sendBean);
        }
    }

    private class ResendMessageListenser implements View.OnClickListener {

        private SGNewsBean item;
        private int position;

        public ResendMessageListenser(SGNewsBean item, int position) {
            // TODO Auto-generated constructor stub
            this.item = item;
            this.position = position;
        }

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            reSendMessage(item, position);
        }

    }

    public void reSendMessage(SGNewsBean item, int position) {
        this.sendBean = item;
        if (Constants.SEND_ACT.equals(item.getType())){
            sendBean.setMsgStatus(Constants.NEWS_SENDING);
            NewsEngine.updateNoticeChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatus(sendBean);
            restSendActMessage();
        }else if (Constants.SEND_TEXT.equals(item.getType())) {
            sendBean.setMsgStatus(Constants.NEWS_SENDING);
            NewsEngine.updateNoticeChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatus(sendBean);
            reSendTxtMessage();
        } else if (Constants.SEND_SOUND.equals(item.getType())) {
            sendBean.setMsgStatus(Constants.NEWS_SENDING);
            NewsEngine.updateNoticeChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatus(sendBean);
            sendMessageType = Constants.SEND_SOUND;
            reSendVoice();
        } else if (Constants.SEND_IMAGE.equals(item.getType())) {
            sendBean.setMsgStatus(Constants.NEWS_SENDING);
            NewsEngine.updateNoticeChatSendStatusForImages(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatusForImages(sendBean);
            sendMessageType = Constants.SEND_IMAGE;
            reSendImage();

        } else if (Constants.SEND_LOCATION.equals(item.getType())) {
            sendBean.setMsgStatus(Constants.NEWS_SENDING);
            NewsEngine.updateNoticeChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatus(sendBean);
            reSendLocation();
        }else if (Constants.SEND_VIDEO.equals(item.getType())){
            sendBean.setMsgStatus(Constants.NEWS_SENDING);
            NewsEngine.updateChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatus(sendBean);
            sendMessageType = Constants.SEND_VIDEO;
            reSendVideo();
        }
    }

    private void reSendLocation() {
        try {
            // 创建消息实体locationBean
            aliyTimeStamp = System.currentTimeMillis();
            // 发送消息
            String messageContent = "";
            JSONObject bodyMsg = new JSONObject();
            bodyMsg.put("msgContent", sendBean.getMsgContent());
            bodyMsg.put("type", Constants.SEND_LOCATION);
            JSONObject ext = new JSONObject();
            ext.put("fromUser", new JSONObject(sendBean.getFromUser()));
            ext.put("toUser", new JSONObject(sendBean.getToUser()));
            ext.put("noticeSubject", sendBean.getNoticeSubject());
            ext.put("recruitLocIndex", sendBean.getRecruitLocIndex());
            bodyMsg.put("ext", ext);
            bodyMsg.put("timestamp", aliyTimeStamp);
            bodyMsg.put("noticeId", sendBean.getNoticeId());
            bodyMsg.put("recruitId", sendBean.getRecruitId());
            messageContent = bodyMsg.toString();
            String time = XMPPUtils.date2Str(aliyTimeStamp, XMPPUtils.MS_FORMART);
            Message message = new Message();
//            message.setProperty("immessage.time", time);
            message.setSubject("501");
            message.setBody(messageContent);
            DeliveryReceiptManager.addDeliveryReceiptRequest(message);//发送回执消息前
            chat.sendMessage(message);
            sendBean.setMsgStatus(Constants.NEWS_SEND_SCUESS);
            NewsEngine.updateNoticeChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatus(sendBean);
        } catch (Exception e) {
            e.printStackTrace();
            sendBean.setMsgStatus(Constants.NEWS_SEND_FAITH);
            NewsEngine.updateNoticeChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatus(sendBean);
        }
    }

    protected void reSendImage() {
        sendMessageType = Constants.SEND_IMAGE;
        String imageUrl = sendBean.getMsgContent();
        if (imageUrl.contains("http://")) {
            sendImageDirect();
        } else {
            uploadImageEngine(imageUrl);
        }
    }

    private void sendImageDirect() {
        try {
            String messageContent = "";
            JSONObject bodyMsg = new JSONObject();
            bodyMsg.put("msgContent", sendBean.getMsgContent());
            bodyMsg.put("type", Constants.SEND_IMAGE);
            JSONObject ext = new JSONObject();
            ext.put("fromUser", new JSONObject(sendBean.getFromUser()));
            ext.put("toUser", new JSONObject(sendBean.getToUser()));
            ext.put("noticeSubject", sendBean.getNoticeSubject());
            ext.put("recruitLocIndex", sendBean.getRecruitLocIndex());
            bodyMsg.put("ext", ext);
            bodyMsg.put("timestamp", aliyTimeStamp);
            bodyMsg.put("noticeId", sendBean.getNoticeId());
            bodyMsg.put("recruitId", sendBean.getRecruitId());
            messageContent = bodyMsg.toString();
            String time = XMPPUtils.date2Str(aliyTimeStamp, XMPPUtils.MS_FORMART);
            Message message = new Message();
//            message.setProperty("immessage.time", time);
            message.setSubject("501");
            message.setBody(messageContent);
            DeliveryReceiptManager.addDeliveryReceiptRequest(message);//发送回执消息前
            chat.sendMessage(message);
            sendBean.setMsgStatus(Constants.NEWS_SEND_SCUESS);
            NewsEngine.updateNoticeChatSendStatusForImages(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatusForImages(sendBean);
        } catch (Exception e) {
            sendBean.setMsgStatus(Constants.NEWS_SEND_FAITH);
            NewsEngine.updateNoticeChatSendStatusForImages(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatusForImages(sendBean);
            e.printStackTrace();
        }
    }

    private void reSendVideo() {
        String msgContent=sendBean.getMsgContent();
        if (msgContent.contains("http://")) {
            sendVideoDirect();
        } else {
            uploadMedia(msgContent);
        }
    }

    private void sendVideoDirect() {
        try {

            String messageContent = "";
            JSONObject bodyMsg = new JSONObject();
            bodyMsg.put("msgContent", sendBean.getMsgContent());
            bodyMsg.put("type", Constants.SEND_VIDEO);
            JSONObject ext = new JSONObject();
            ext.put("fromUser", new JSONObject(sendBean.getFromUser()));
            ext.put("toUser", new JSONObject(sendBean.getToUser()));
            ext.put("noticeSubject", sendBean.getNoticeSubject());
            ext.put("recruitLocIndex", sendBean.getRecruitLocIndex());
            bodyMsg.put("ext", ext);
            bodyMsg.put("timestamp", aliyTimeStamp);
            bodyMsg.put("noticeId", sendBean.getNoticeId());
            bodyMsg.put("recruitId", sendBean.getRecruitId());
            messageContent = bodyMsg.toString();
            String time = XMPPUtils.date2Str(aliyTimeStamp, XMPPUtils.MS_FORMART);
            Message message = new Message();
//            message.setProperty("immessage.time", time);
            message.setSubject("501");
            message.setBody(messageContent);
            DeliveryReceiptManager.addDeliveryReceiptRequest(message);//发送回执消息前
            chat.sendMessage(message);
            sendBean.setMsgStatus(Constants.NEWS_SEND_SCUESS);
            NewsEngine.updateNoticeChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatus(sendBean);
        } catch (Exception e) {
            sendBean.setMsgStatus(Constants.NEWS_SEND_FAITH);
            NewsEngine.updateNoticeChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatus(sendBean);
            e.printStackTrace();
        }
    }

    protected void reSendVoice() {
        try {
            JSONObject judgment = new JSONObject(sendBean.getMsgContent());
            String voiceUrl = judgment.getString("voiceUrl");
            String length = judgment.getString("length");
            if (voiceUrl.contains("http://")) {
                sendVoiceDirect();
            } else {
                voiceLength = Integer.parseInt(length);
                uploadMedia(voiceUrl);
            }
        } catch (Exception e) {
            sendBean.setMsgStatus(Constants.NEWS_SEND_FAITH);
            NewsEngine.updateNoticeChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatus(sendBean);
            e.printStackTrace();
        }

    }

    private void sendVoiceDirect() {
        try {

            String messageContent = "";
            JSONObject bodyMsg = new JSONObject();
            bodyMsg.put("msgContent", sendBean.getMsgContent());
            bodyMsg.put("type", Constants.SEND_SOUND);
            JSONObject ext = new JSONObject();
            ext.put("fromUser", new JSONObject(sendBean.getFromUser()));
            ext.put("toUser", new JSONObject(sendBean.getToUser()));
            ext.put("noticeSubject", sendBean.getNoticeSubject());
            ext.put("recruitLocIndex", sendBean.getRecruitLocIndex());
            bodyMsg.put("ext", ext);
            bodyMsg.put("timestamp", aliyTimeStamp);
            bodyMsg.put("noticeId", sendBean.getNoticeId());
            bodyMsg.put("recruitId", sendBean.getRecruitId());
            messageContent = bodyMsg.toString();
            String time = XMPPUtils.date2Str(aliyTimeStamp, XMPPUtils.MS_FORMART);
            Message message = new Message();
//            message.setProperty("immessage.time", time);
            message.setSubject("501");
            message.setBody(messageContent);
            DeliveryReceiptManager.addDeliveryReceiptRequest(message);//发送回执消息前
            chat.sendMessage(message);
            sendBean.setMsgStatus(Constants.NEWS_SEND_SCUESS);
            NewsEngine.updateNoticeChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatus(sendBean);
        } catch (Exception e) {
            sendBean.setMsgStatus(Constants.NEWS_SEND_FAITH);
            NewsEngine.updateNoticeChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatus(sendBean);
            e.printStackTrace();
        }
    }

    private void restSendActMessage() {
        try {
            // 创建消息实体
            long timestamp = System.currentTimeMillis();
            String messageContent = "";
            JSONObject bodyMsg = new JSONObject();
            bodyMsg.put("msgContent", new JSONObject(sendBean.getMsgContent()));
            bodyMsg.put("type", Constants.SEND_ACT);
            JSONObject ext = new JSONObject();
            ext.put("fromUser", new JSONObject(sendBean.getFromUser()));
            ext.put("toUser", new JSONObject(sendBean.getToUser()));
            ext.put("noticeSubject", sendBean.getNoticeSubject());
            ext.put("recruitLocIndex", sendBean.getRecruitLocIndex());
            bodyMsg.put("ext", ext);
            bodyMsg.put("timestamp", timestamp);
            bodyMsg.put("noticeId", sendBean.getNoticeId());
            bodyMsg.put("recruitId", sendBean.getRecruitId());
            messageContent = bodyMsg.toString();
            String time = XMPPUtils.date2Str(timestamp, XMPPUtils.MS_FORMART);
            Message message = new Message();
//            message.setProperty("immessage.time", time);
            message.setSubject("5");
            message.setBody(messageContent);
            DeliveryReceiptManager.addDeliveryReceiptRequest(message);//发送回执消息前
//			PacketExtension receipt = message.getExtension(DeliveryReceipt.ELEMENT, DeliveryReceipt.NAMESPACE);
            // 发送消息
            chat.sendMessage(message);

            sendBean.setMsgStatus(Constants.NEWS_SEND_SCUESS);
            NewsEngine.updateNoticeChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatus(sendBean);
        } catch (Exception e) {
            e.printStackTrace();
            sendBean.setMsgStatus(Constants.NEWS_SEND_FAITH);
            NewsEngine.updateNoticeChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatus(sendBean);
        }
    }

    private void reSendTxtMessage() {
        try {
            // 创建消息实体
            long timestamp = System.currentTimeMillis();
            String messageContent = "";
            JSONObject bodyMsg = new JSONObject();
            bodyMsg.put("msgContent", sendBean.getMsgContent());
            bodyMsg.put("type", Constants.SEND_TEXT);
            JSONObject ext = new JSONObject();
            ext.put("fromUser", new JSONObject(sendBean.getFromUser()));
            ext.put("toUser", new JSONObject(sendBean.getToUser()));
            ext.put("noticeSubject", sendBean.getNoticeSubject());
            ext.put("recruitLocIndex", sendBean.getRecruitLocIndex());
            bodyMsg.put("ext", ext);
            bodyMsg.put("timestamp", timestamp);
            bodyMsg.put("noticeId", sendBean.getNoticeId());
            bodyMsg.put("recruitId", sendBean.getRecruitId());
            messageContent = bodyMsg.toString();
            String time = XMPPUtils.date2Str(timestamp, XMPPUtils.MS_FORMART);
            Message message = new Message();
//            message.setProperty("immessage.time", time);
            message.setSubject("501");
            message.setBody(messageContent);
            DeliveryReceiptManager.addDeliveryReceiptRequest(message);//发送回执消息前
//			PacketExtension receipt = message.getExtension(DeliveryReceipt.ELEMENT, DeliveryReceipt.NAMESPACE);
            // 发送消息
            chat.sendMessage(message);

            sendBean.setMsgStatus(Constants.NEWS_SEND_SCUESS);
            NewsEngine.updateNoticeChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatus(sendBean);
        } catch (Exception e) {
            e.printStackTrace();
            sendBean.setMsgStatus(Constants.NEWS_SEND_FAITH);
            NewsEngine.updateNoticeChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatus(sendBean);
        }
    }

//	/**
//	 *
//	 * 递归重连，直连上为止.
//	 *
//	 * @author shimiso
//	 * @update 2012-7-10 下午2:12:25
//	 */
//	public void reConnect() {
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				new Thread(new Runnable() {
//
//					@Override
//					public void run() {
//						try {
////							SGApplication.getInstance().openConnection();
//							SGApplication.getInstance().getConnection().connect();
//							SASLAuthentication.supportSASLMechanism("PLAIN", 0);
//							SGApplication.getInstance().getConnection().login(UserPreferencesUtil.getUserId(context), UserPreferencesUtil.getPwd(context), "sgapp"); // 登录
//							OfflineMsgManager.getInstance(context).dealOfflineMsg(SGApplication.getInstance().getConnection());//处理离线消息
//							SGApplication.getInstance().getConnection().sendPacket(new Presence(Presence.Type.available));
//							DeliveryReceiptManager.getInstanceFor(SGApplication.getInstance().getConnection()).enableAutoReceipts();
//						} catch (XMPPException e) {
//							e.printStackTrace();
//						}
//
//					}
//				}).start();
//			}
//		}).start();
//	}
//
//	private void dealWithResult(Integer data) {
//		switch (data) {
//			case XMPPUtils.LOGIN_SECCESS: // 登录成功
//				UserPreferencesUtil.setLoginTime(this,String.valueOf(System.currentTimeMillis()));
//				break;
//			case XMPPUtils.LOGIN_ERROR_ACCOUNT_PASS:// 账户或者密码错误
//				reConnect();
//				break;
//			case XMPPUtils.SERVER_UNAVAILABLE:// 服务器连接失败
//				reConnect();
//				break;
//			case XMPPUtils.LOGIN_ERROR:// 未知异常
//				reConnect();
//				break;
//		}
//	}

//	// 登录
//	private Integer login() {
//		LoginConfig loginConfig=XMPPUtils.getLoginConfig(this);
//		String password= UserPreferencesUtil.getPwd(this);
//		String username = UserPreferencesUtil.getUserId(this);
//		try {
//			XMPPConnection connection = SGApplication.getInstance()
//					.getConnection();
//			connection.connect();
//			connection.login(username, password, "sgapp"); // 登录
////			 OfflineMsgManager.getInstance(activitySupport).dealOfflineMsg(connection);//处理离线消息
//			connection.sendPacket(new Presence(Presence.Type.available));
//			if (loginConfig.isNovisible()) {// 隐身登录
//				Presence presence = new Presence(Presence.Type.unavailable);
//				Collection<RosterEntry> rosters = connection.getRoster()
//						.getEntries();
//				for (RosterEntry rosterEntry : rosters) {
//					presence.setTo(rosterEntry.getUser());
//					connection.sendPacket(presence);
//				}
//			}
//			loginConfig.setUsername(username);
//			if (loginConfig.isRemember()) {// 保存密码
//				loginConfig.setPassword(password);
//			} else {
//				loginConfig.setPassword("");
//			}
//			loginConfig.setOnline(true);
//			DeliveryReceiptManager.getInstanceFor(connection).enableAutoReceipts();
//			return XMPPUtils.LOGIN_SECCESS;
//		} catch (Exception xee) {
//			if (xee instanceof XMPPException) {
//				XMPPException xe = (XMPPException) xee;
//				final XMPPError error = xe.getXMPPError();
//				int errorCode = 0;
//				if (error != null) {
//					errorCode = error.getCode();
//				}
//				if (errorCode == 401) {
//					return XMPPUtils.LOGIN_ERROR_ACCOUNT_PASS;
//				} else if (errorCode == 403) {
//					return XMPPUtils.LOGIN_ERROR_ACCOUNT_PASS;
//				} else {
//					return XMPPUtils.SERVER_UNAVAILABLE;
//				}
//			} else {
//				return XMPPUtils.LOGIN_ERROR;
//			}
//		}
//	}
}