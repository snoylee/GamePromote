package com.xygame.sg.im;

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
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
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
import com.xy.im.util.EixstMultiRoomsUtils;
import com.xygame.second.sg.Group.bean.GroupBean;
import com.xygame.second.sg.io.vov.vitamio.activity.VideoPlayer;
import com.xygame.second.sg.personal.activity.PersonalDetailActivity;
import com.xygame.second.sg.personal.guanzhu.group_manager.GroupMembers;
import com.xygame.second.sg.personal.guanzhu.group_manager.GroupOnwer;
import com.xygame.second.sg.utils.Contant;
import com.xygame.second.sg.utils.GroupEngine;
import com.xygame.second.sg.utils.RecordResult;
import com.xygame.second.sg.utils.Uuid16;
import com.xygame.sg.AudioRecorder2Mp3Util;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseForChatActivity;
import com.xygame.sg.activity.image.ImageChooserActivity;
import com.xygame.sg.bean.comm.TransferImagesBean;
import com.xygame.sg.define.view.CircularImage;
import com.xygame.sg.define.view.roundimageview.RoundedImageView;
import com.xygame.sg.http.AliySSOHepler;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.image.ImageLoader;
import com.xygame.sg.service.CacheService;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.FileUtil;
import com.xygame.sg.utils.HttpCallBack;
import com.xygame.sg.utils.ImageLocalLoader;
import com.xygame.sg.utils.ImageLocalLoader.Type;
import com.xygame.sg.utils.SGApplication;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.StringUtil;
import com.xygame.sg.utils.TimeUtils;
import com.xygame.sg.utils.UserPreferencesUtil;
import com.xygame.sg.utils.VideoImageLoader;
import com.xygame.sg.utils.XMPPUtils;
import com.xygame.sg.vido.VideoPlayerActivity;

import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * 聊天对话.
 */
public class TempGroupChatActivity extends SGBaseForChatActivity
        implements OnClickListener, EmojiconGridFragment.OnEmojiconClickedListener,
        EmojiconsFragment.OnEmojiconBackspaceClickedListener, TextWatcher {
    private View titleBack, faceButton, addButton, messageButton, moreOption, picPhoto, tokePhoto, locationButton, tgInfo, personalInfo, videoButton;
    private FrameLayout emojicons;
    private MultiUserChat chat = null;
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
    private SGNewsBean sendBean;
    private int voiceLength = 0;
    private long aliyTimeStamp = 0;
    private ShareLocationBean locationBean;
    private String sendMessageType = "1";
    private boolean isJJR = false;
    private List<SGNewsBean> sendImages = new ArrayList<>();
    private int currSendingIndex = 0;
    private GroupBean item;
    private long tempMemberJoinTimer,tempRecieverMsgTimer;
    private boolean isOwnerGroup=false,isKickedOut=true;

    /**
     * 录音相关
     *
     * @param savedInstanceState
     */
    private static int[] res = {R.drawable.mic_2, R.drawable.mic_3,
            R.drawable.mic_4, R.drawable.mic_5};
    private static ImageView view;
    public final static int MAX_TIME = 60;//一分钟
    private String mFileName = null, fromVoicePath;
    private long startTime;
    private Dialog recordIndicator;
    private static final int MIN_INTERVAL_TIME = 2000;
    private AudioRecorder2Mp3Util util = null;
    private Handler volumeHandler;
    private ObtainDecibelThread thread;
    private long intervalTime;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.le_chat_group);
        initViews();
        initListeners();
        initDatas();
    }

    private void initViews() {
        personalInfo = findViewById(R.id.personalInfo);
        tokePhoto = findViewById(R.id.tokePhoto);
        picPhoto = findViewById(R.id.picPhoto);
        tgInfo = findViewById(R.id.tgInfo);
        locationButton = findViewById(R.id.locationButton);
        videoButton = findViewById(R.id.videoButton);
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

    }

    private void initListeners() {
        personalInfo.setOnClickListener(this);
        tgInfo.setOnClickListener(this);
        messageButton.setOnClickListener(this);
        messageSendBtn.setOnClickListener(this);
        titleBack.setOnClickListener(this);
        faceButton.setOnClickListener(this);
        addButton.setOnClickListener(this);
        picPhoto.setOnClickListener(this);
        tokePhoto.setOnClickListener(this);
        locationButton.setOnClickListener(this);
        videoButton.setOnClickListener(this);
        messageInput.addTextChangedListener(this);
        messageInput.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                // TODO Auto-generated method stub
                updateActionView(2);
                imm.showSoftInput(messageInput, 0);
                return false;
            }
        });
        recordButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if (FileUtil.ExistSDCard()) {
                            recordButton.setBackgroundResource(R.drawable.record_press_bg);
                            recordButton.setTextColor(getResources().getColor(R.color.white));
                            recordButton.setText("松开发送");
                            initDialogAndStartRecord();
                        } else {
                            Toast.makeText(TempGroupChatActivity.this, "请插入SD卡", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (FileUtil.ExistSDCard()) {
                            recordButton.setBackgroundResource(R.drawable.record_unpress_bg);
                            recordButton.setTextColor(getResources().getColor(R.color.black));
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
        LayoutParams lp = recordIndicator.getWindow().getAttributes();
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
            if (file.exists()) {
                file.delete();
            }
            stopThread();
        } else {
            stopRecording();
        }
    }

    private void startRecording() {
        fromVoicePath = "/sdcard/test_audio_recorder_for_mp3.mp3";
        mFileName = FileUtil.RECORD_ROOT_PATH.concat(Constants.getMP3Name(this));
        if (util == null) {
            util = new AudioRecorder2Mp3Util(null,
                    "/sdcard/test_audio_recorder_for_mp3.raw", fromVoicePath);
        }
        util.startRecording();
        if (thread == null) {
            thread = new ObtainDecibelThread();
        }
        thread.start();
    }

    private void stopThread() {
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
        int isSccuss = FileUtil.CopySdcardFile(fromVoicePath, mFileName);
        if (isSccuss == 0) {
            changToMp3(mFileName, (int) (intervalTime / 1000));
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
                int max = 38;
                int min = 1;
                Random random = new Random();

                int s = random.nextInt(max) % (max - min + 1) + min;
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

    private void checkOwnerAndKickedOut(SGNewsBean message){
        ThreadPool.getInstance().excuseThread(new checkOwnerThread(message));
    }

    private class checkOwnerThread implements Runnable{
        private SGNewsBean message;
        public checkOwnerThread(SGNewsBean message){
           this.message=message;
        }

        @Override
        public void run() {
            List<GroupBean> value= CacheService.getInstance().getCacheDiscGroupDatas(UserPreferencesUtil.getUserId(TempGroupChatActivity.this));
            if (value!=null){
                for (GroupBean it:value){
                    if (it.getGroupId().equals(message.getNoticeId())){
                        if (it.getCreateUserId().equals(UserPreferencesUtil.getUserId(TempGroupChatActivity.this))){
                            isOwnerGroup=true;
                        }
                        break;
                    }
                }
            }else{
                isOwnerGroup=true;
            }

//            if (!isOwnerGroup){
//                for (GroupBean it:value){
//                    if (sendBean.getNoticeId().equals(it.getGroupId())){
//                        isKickedOut=false;
//                        break;
//                    }
//                }
//            }else{
//                isKickedOut=false;
//            }
            if (value!=null){
                for (GroupBean it:value){
                    if (sendBean.getNoticeId().equals(it.getGroupId())){
                        isKickedOut=false;
                        break;
                    }
                }
            }

            if (!isKickedOut){
                try {
                    if (SGApplication.getInstance().getConnection().isConnected()) {
                        String serviceName = SGApplication.getInstance().getConnection().getServiceName();
                        String roomId = sendBean.getNoticeId().concat("@").concat("conference.").concat(serviceName);

                        DiscussionHistory history = new DiscussionHistory();
                        history.setMaxChars(0);
                        GroupBean temp = GroupEngine.quaryGroupBean(TempGroupChatActivity.this, item, UserPreferencesUtil.getUserId(TempGroupChatActivity.this));
                        if (temp != null) {
                            if (!TextUtils.isEmpty(temp.getLastIntoTimer())) {
                                history.setSince(new Date(Long.parseLong(temp.getLastIntoTimer())));
                            } else {
                                Date date = new Date(System.currentTimeMillis());
                                history.setSince(date);
                            }
                        } else {
                            Date date = new Date(System.currentTimeMillis());
                            history.setSince(date);
                        }
                        chat= EixstMultiRoomsUtils.getMultiUserChat(sendBean.getNoticeId(),TempGroupChatActivity.this);
                        if (chat==null){
                            chat = new MultiUserChat(SGApplication.getInstance().getConnection(),
                                    roomId);
                            chat.join(UserPreferencesUtil.getUserId(TempGroupChatActivity.this),null,history, SmackConfiguration.getPacketReplyTimeout());
                            EixstMultiRoomsUtils.addMutiRoomsItem(chat, sendBean.getNoticeId(),TempGroupChatActivity.this);
                        }else{
                            chat.join(UserPreferencesUtil.getUserId(TempGroupChatActivity.this), null, history, SmackConfiguration.getPacketReplyTimeout());
                        }
                        tempMemberJoinTimer = System.currentTimeMillis();
                    }
                } catch (XMPPException e) {
                    e.printStackTrace();
                }
            }
        }

        private SGNewsBean insertTipNews(Context context,String content,SGNewsBean groupBean){
            SGNewsBean sendBean = new SGNewsBean();
            // 创建消息实体
            long timestamp = System.currentTimeMillis();
            sendBean.setFromUser(SenderUser.getFromUserJsonStr(context));
            sendBean.setNoticeSubject(groupBean.getNoticeSubject());
            sendBean.setRecruitLocIndex("");
            sendBean.setNoticeId(groupBean.getNoticeId());
            sendBean.setFriendNickName("");
            sendBean.setFriendUserIcon("");
            sendBean.setFriendUserId("");
            // +++++++++++++++++++++++++++++++++++++++++++++++
            sendBean.setMsgContent(content);
            sendBean.setNewType(Constants.GROUP_CHAT);
            sendBean.setRecruitId("");
            sendBean.setTimestamp(String.valueOf(timestamp));
            sendBean.setType(Constants.SEND_TEXT_TIP);
            sendBean.setUserId(UserPreferencesUtil.getUserId(context));
            sendBean.setMessageStatus(Constants.NEWS_READ);
            sendBean.setInout(Constants.NEWS_END);
            sendBean.setIsShow(Constants.NEWS_SHOW);
            sendBean.setMsgStatus(Constants.NEWS_SEND_SCUESS);
            // 新增本地数据
            TempGroupNewsEngine.inserChatNew(context, sendBean);
            return sendBean;
        }
    }

    private void initDatas() {
        isOwnerGroup=getIntent().getBooleanExtra("isHost",false);
        sendBean=(SGNewsBean)getIntent().getSerializableExtra("chatBean");
        sendBean.setFromUser(SenderUser.getFromUserJsonStr(this));
        item = new GroupBean();
        item.setUserId(UserPreferencesUtil.getUserId(this));
        item.setLastIntoTimer(String.valueOf(System.currentTimeMillis()));
        item.setGroupId(sendBean.getNoticeId());
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        volumeHandler = new ShowVolumeHandler();
        checkOwnerAndKickedOut(sendBean);
        registerBoradcastReceiver();
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        emojFragment = new EmojiconsFragment();
        fragmentManager = getSupportFragmentManager();
        alphaIn = AnimationUtils.loadAnimation(this, R.anim.push_bottom_in);
        alphaOut = AnimationUtils.loadAnimation(this, R.anim.push_bottom_out);
        tvChatTitle.setText(sendBean.getNoticeSubject());
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.emojicons, emojFragment);
        transaction.commit();
        adapter = new MessageListAdapter(TempGroupChatActivity.this, null);
        listView.setAdapter(adapter);
        messageInput.setSingleLine(false);
        messageInput.setHorizontallyScrolling(false);
        messageInput.setVerticalScrollBarEnabled(true);
        getDatas();
        MediaRecorder mRecorders = new MediaRecorder();
        mRecorders.setAudioSource(MediaRecorder.AudioSource.MIC);
    }

    @Override
    public void finish() {
        GroupBean temp = GroupEngine.quaryGroupBean(this, item, UserPreferencesUtil.getUserId(this));
        item.setLastIntoTimer(String.valueOf(System.currentTimeMillis()));
        if (temp != null) {
            GroupEngine.updateGroupLastTime(this, item, UserPreferencesUtil.getUserId(this));
        } else {
            GroupEngine.inserGroup(this, item);
        }
        super.finish();
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
                uploadLocalStatus();
                loadHistoryDatas();
                android.os.Message msg = new android.os.Message();
                msg.what = 3;
                handler.sendMessage(msg);
            }
        }).start();
    }

    private void uploadLocalStatus() {
        TempGroupNewsEngine.updateChatNewsStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
    }

    private void loadHistoryDatas() {
        MessageDatas = TempGroupNewsEngine.quaryChatHistory(this, sendBean, UserPreferencesUtil.getUserId(this));
    }

    @Override
    public void onDestroy() {
        unregisterBroadcastReceiver();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        super.onDestroy();
    }

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(XMPPUtils.NEW_MESSAGE_DISC_GROUP_NOTICE);
        myIntentFilter.addAction(XMPPUtils.EDITOR_DISC_GROUP_INFO_ACTION);
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    public void unregisterBroadcastReceiver() {
        unregisterReceiver(mBroadcastReceiver);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (XMPPUtils.NEW_MESSAGE_DISC_GROUP_NOTICE.equals(intent.getAction())) {
                tempRecieverMsgTimer=System.currentTimeMillis();
                Boolean takedOut=intent.getBooleanExtra("takedOut", false);
                if (takedOut){
                    isKickedOut=true;
                }else{
                    isKickedOut=false;
                }
                boolean flag = intent.getBooleanExtra("newsMessage", false);
                if (flag) {
                    SGNewsBean recivedMsgBean = (SGNewsBean) intent.getSerializableExtra("newBean");
                    android.os.Message message = new android.os.Message();
                    message.what = 4;
                    message.obj = recivedMsgBean;
                    handler.sendMessage(message);
                }
            }else  if (XMPPUtils.EDITOR_DISC_GROUP_INFO_ACTION.equals(intent.getAction())) {
                String flag=intent.getStringExtra("flag");
                if (Constants.EDITOR_DISC_GROUP_NAME.equals(flag)){
                    String discGroupName=intent.getStringExtra(Constants.EDITOR_DISC_GROUP_NAME);
                    setEditorGroupName(discGroupName);
                }else if (Constants.LOSE_DISC_GROUP.equals(flag)){
                    finish();
                }else if(Constants.EXIT_DISC_GROUP.equals(flag)){
                    finish();
                }else if(Constants.KIKT_DISC_GROUP.equals(flag)){
                    SGNewsBean resultBean=(SGNewsBean)intent.getSerializableExtra("resultBean1");
                    adapter.addItem(resultBean);
                    adapter.notifyDataSetChanged();
                }else  if (Constants.ADD_DISC_GROUP.equals(flag)){
                    SGNewsBean resultBean1=(SGNewsBean)intent.getSerializableExtra("resultBean1");
                    SGNewsBean resultBean2=(SGNewsBean)intent.getSerializableExtra("resultBean2");
                    if (resultBean1!=null){
                        adapter.addItem(resultBean1);
                        adapter.notifyDataSetChanged();
                    }
                    if (resultBean2!=null){
                        adapter.addItem(resultBean2);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }
    };

    private void setEditorGroupName(String discGroupName){
        sendBean.setNoticeSubject(discGroupName);
        tvChatTitle.setText(sendBean.getNoticeSubject());
    }

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
                Toast.makeText(TempGroupChatActivity.this, "不能为空", Toast.LENGTH_SHORT).show();
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
        } else if (v.getId() == R.id.tgInfo) {
        } else if (v.getId() == R.id.personalInfo) {
            if (isOwnerGroup){
                Intent intent = new Intent(this, GroupOnwer.class);
                intent.putExtra("chatBean", sendBean);
                intent.putExtra("isKickedOut",isKickedOut);
                startActivityForResult(intent, 3);
            }else{
                Intent intent = new Intent(this, GroupMembers.class);
                intent.putExtra("chatBean", sendBean);
                intent.putExtra("isKickedOut",isKickedOut);
                startActivity(intent);
            }
        } else if (v.getId() == R.id.videoButton) {
            Contant.startRecordActivity(this, 5);
            updateActionView(4);
        }
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
//		Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
//		openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//		startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
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
        private VideoImageLoader videoImageLoader;

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

        public void clearDatas() {
            datas.clear();
        }

        public void addDatas(List<SGNewsBean> datas) {
            this.datas.addAll(datas);
            notifyDataSetChanged();
        }

        public void addItem(SGNewsBean it) {
            SGNewsBean item=new SGNewsBean();
            item.setFromUser(it.getFromUser());
            item.setMsgContent(it.getMsgContent());
            item.setNewType(it.getNewType());
            item.setRecruitId(it.getRecruitId());
            item.setTimestamp(it.getTimestamp());
            item.setType(it.getType());
            item.setUserId(it.getUserId());
            item.setMessageStatus(it.getMsgStatus());
            item.setInout(it.getInout());
            item.setIsShow(it.getIsShow());
            item.setMsgStatus(it.getMsgStatus());
            item.setRecruitLocIndex(it.getRecruitLocIndex());
            item.setNoticeSubject(it.getNoticeSubject());
            item.setOperatorFlag(it.getOperatorFlag());
            item.set_id(it.get_id());
            item.setFriendNickName(it.getFriendNickName());
            item.setFriendUserIcon(it.getFriendUserIcon());
            item.setFriendUserId(it.getFriendUserId());
            item.setIsShowMsgTimer(it.isShowMsgTimer());
            item.setNoticeId(it.getNoticeId());
            item.setToUser(it.getToUser());
            datas.add(item);
            notifyDataSetChanged();
        }

        public void updateItemSendStatus(SGNewsBean it) {
            for (int i = 0; i < datas.size(); i++) {
                if (it.getTimestamp().equals(datas.get(i).getTimestamp())) {
                    datas.get(i).setMsgStatus(it.getMsgStatus());
                    break;
                }
            }
            notifyDataSetChanged();
        }

        public void updateItemSendStatusForImages(SGNewsBean it) {
            for (int i = 0; i < datas.size(); i++) {
                if (it.getRecruitLocIndex().equals(datas.get(i).getRecruitLocIndex())) {
                    datas.get(i).setMsgStatus(it.getMsgStatus());
                    break;
                }
            }
            notifyDataSetChanged();
        }

        class HoldView {
            RoundedImageView imageView, mapImageView, imageViewHon;
            TextView useridView, dateView, msgView, mapAdress, voiceTime, messageTimer,commentTextView;
            View soundView, mapImageLayout, photoView, otherView, sendFaith,playIcon,mainBodyView;
            SeekBar skbProgress;
            ProgressBar sending;
            CircularImage from_head;
        }

        private class IntoPersonalDeltail implements OnClickListener {
            private SGNewsBean presenter;

            public IntoPersonalDeltail(SGNewsBean presenter) {
                this.presenter = presenter;
            }

            @Override
            public void onClick(View v) {
                intoPersonalAct(presenter);
            }
        }

        private void intoPersonalAct(SGNewsBean message) {
            Intent intent = new Intent(context, PersonalDetailActivity.class);
            if (Constants.NEWS_RECIEVE.equals(message.getInout())) {
                intent.putExtra("userNick", SenderUser.getFromUserName(message, context));
                intent.putExtra("userId", SenderUser.getFromUserId(message, context));
            } else {
                intent.putExtra("userNick", UserPreferencesUtil.getUserNickName(context));
                intent.putExtra("userId", UserPreferencesUtil.getUserId(context));
            }
            context.startActivity(intent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HoldView holdView;
            final SGNewsBean message = datas.get(position);

            holdView = new HoldView();
            if (Constants.NEWS_RECIEVE.equals(message.getInout())) {
                convertView = LayoutInflater.from(context).inflate(R.layout.le_formclient_chat_in_group, null);
            } else {
                convertView = LayoutInflater.from(context).inflate(R.layout.le_formclient_chat_out_group, null);
                holdView.sending = (ProgressBar) convertView.findViewById(R.id.sending);
                holdView.sendFaith = convertView.findViewById(R.id.sendFaith);
            }
            holdView.from_head = (CircularImage) convertView.findViewById(R.id.from_head);
            holdView.useridView = (TextView) convertView.findViewById(R.id.formclient_row_userid);
            holdView.commentTextView=(TextView)convertView.findViewById(R.id.commentTextView);
            holdView.voiceTime = (TextView) convertView.findViewById(R.id.voiceTime);
            holdView.mapAdress = (TextView) convertView.findViewById(R.id.mapAdress);
            holdView.dateView = (TextView) convertView.findViewById(R.id.formclient_row_date);
            holdView.msgView = (TextView) convertView.findViewById(R.id.formclient_row_msg);
            holdView.messageTimer = (TextView) convertView.findViewById(R.id.messageTimer);
            holdView.otherView = convertView.findViewById(R.id.otherView);
            holdView.photoView = convertView.findViewById(R.id.photoView);
            holdView.mainBodyView=convertView.findViewById(R.id.mainBodyView);
            holdView.playIcon=convertView.findViewById(R.id.playIcon);
            holdView.soundView = convertView.findViewById(R.id.soundView);
            holdView.skbProgress = (SeekBar) convertView.findViewById(R.id.skbProgress);
            holdView.imageView = (RoundedImageView) convertView.findViewById(R.id.imageView);
            holdView.imageViewHon = (RoundedImageView) convertView.findViewById(R.id.imageViewHon);
            holdView.mapImageView = (RoundedImageView) convertView.findViewById(R.id.mapImageView);
            holdView.mapImageLayout = convertView.findViewById(R.id.mapImageLayout);
            holdView.playIcon.setVisibility(View.GONE);
            holdView.dateView.setVisibility(View.GONE);
            holdView.messageTimer.setText(TimeUtils.formatTime(Long.parseLong(message.getTimestamp())));
            if (Constants.SEND_TEXT_TIP.equals(message.getType())){
                holdView.commentTextView.setVisibility(View.VISIBLE);
                holdView.mainBodyView.setVisibility(View.GONE);
                holdView.commentTextView.setText(message.getMsgContent());
            }else{
                holdView.commentTextView.setVisibility(View.GONE);
                holdView.mainBodyView.setVisibility(View.VISIBLE);
                if (Constants.NEWS_RECIEVE.equals(message.getInout())) {
                    String userHead = SenderUser.getFromUserIcon(message, context);
                    holdView.from_head.setImageResource(R.drawable.default_avatar);
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
                if (Constants.SEND_TEXT.equals(message.getType())) {
                    holdView.playIcon.setVisibility(View.GONE);
                    holdView.otherView.setVisibility(View.VISIBLE);
                    holdView.photoView.setVisibility(View.GONE);
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
                } else if (Constants.SEND_VIDEO.equals(message.getType())) {
                    holdView.playIcon.setVisibility(View.VISIBLE);
                    holdView.otherView.setVisibility(View.GONE);
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
                }else if (Constants.SEND_IMAGE.equals(message.getType())) {
                    holdView.playIcon.setVisibility(View.GONE);
                    holdView.otherView.setVisibility(View.GONE);
                    holdView.photoView.setVisibility(View.VISIBLE);
                    holdView.imageView.setVisibility(View.VISIBLE);
                    if (message.getMsgContent().contains(FileUtil.CHAT_IMAGES_ROOT_PATH)) {
                        ImageLocalLoader.getInstance(3, Type.LIFO).loadImage(message.getMsgContent(), holdView.imageView);
                    } else {
                        mImageLoader.loadImage(message.getMsgContent(), holdView.imageView, true);
                    }
                    holdView.imageView.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            Constants.imageBrower(context, 0, new String[]{message.getMsgContent()}, false);
                        }
                    });
                } else if (Constants.SEND_LOCATION.equals(message.getType())) {
                    holdView.playIcon.setVisibility(View.GONE);
                    holdView.otherView.setVisibility(View.GONE);
                    holdView.photoView.setVisibility(View.VISIBLE);
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
                        holdView.mapImageLayout.setOnClickListener(new OnClickListener() {

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
            }
            return convertView;
        }

        private class CopyTextLinesner implements View.OnLongClickListener {
            private String content;

            public CopyTextLinesner(String ss) {
                this.content = ss;
            }


            @Override
            public boolean onLongClick(View v) {
                alertDialog(content);
                return false;
            }
        }

        private class PlayMp3Listener implements OnClickListener {
            private String url;

            public PlayMp3Listener(String url) {
                this.url = url;
            }


            @Override
            public void onClick(View v) {
                playerWorker(url);
            }
        }

        private void playerWorker(String url) {
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

    private void alertDialog(final String content) {
        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        dlg.show();
        LayoutInflater factory = LayoutInflater.from(this);
        View view = factory.inflate(R.layout.chat_history_pop, null);
        View copyView = view.findViewById(R.id.copyView);
        copyView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                StringUtil.copy(content, TempGroupChatActivity.this);
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
//						initImageMessage(picPath);
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
        } else if (requestCode == 3 && resultCode == Activity.RESULT_OK) {
            boolean flag = data.getBooleanExtra(Constants.ACTION_LOGIN_BACK_IS_SUCCESS_FLAG, false);
            if (flag) {
                getDatas();
            }
        } else if (requestCode == 4 && resultCode == Activity.RESULT_OK) {
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
        } else if (requestCode == 5 && resultCode == Activity.RESULT_OK) {
            RecordResult result = new RecordResult(data);
            String videoFile;
            String[] thum;
            videoFile = result.getPath();
            thum = result.getThumbnail();
            result.getDuration();
            ShowMsgDialog.showNoMsg(this, false);
            ThreadPool.getInstance().excuseThread(new MovingMp4(videoFile, thum[0]));
        }
    }

    private class MovingMp4 implements Runnable {
        private String videoFile;
        private String videoImage;

        public MovingMp4(String videoFile, String videoImage) {
            this.videoFile = videoFile;
            this.videoImage = videoImage;
        }

        @Override
        public void run() {
            String mFileName = FileUtil.RECORD_ROOT_PATH.concat(Constants.getMP4Name(TempGroupChatActivity.this));
            int isSccuss = FileUtil.CopySdcardFile(videoFile, mFileName);
            if (isSccuss == 0) {
                Bundle bundle = new Bundle();
                bundle.putString("videoFile", mFileName);
                bundle.putString("videoImage", videoImage);
                android.os.Message msg = new android.os.Message();
                msg.what = 9;
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
        AliySSOHepler.getInstance().uploadMedia(this, Constants.MESSAGE_PATH, path, new HttpCallBack() {

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
                            adapter.clearDatas();
                            adapter.addDatas(MessageDatas);
                        }
                        break;
                    case 4:
                        SGNewsBean recivedMsgBean = (SGNewsBean) msg.obj;
                        if (sendBean.getNoticeId().equals(recivedMsgBean.getNoticeId())) {
                            adapter.addItem(recivedMsgBean);
                            uploadLocalStatus();
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
                            TempGroupNewsEngine.updateChatSendStatusForImages(TempGroupChatActivity.this, sendBean,
                                    UserPreferencesUtil.getUserId(TempGroupChatActivity.this));
                            adapter.updateItemSendStatusForImages(sendBean);
                            excuseSendPicAction();
                        } else {
                            sendBean.setMsgStatus(Constants.NEWS_SEND_FAITH);
                            TempGroupNewsEngine.updateChatSendStatus(TempGroupChatActivity.this, sendBean,
                                    UserPreferencesUtil.getUserId(TempGroupChatActivity.this));
                            adapter.updateItemSendStatus(sendBean);
                        }
                        break;
                    case 8:
                        int resInt = (Integer) msg.obj;
//					dealWithResult(resInt);
                        break;
                    case 9:
                        ShowMsgDialog.cancel();
                        Bundle bundle = msg.getData();
                        String videoFile = bundle.getString("videoFile");
                        String videoImage = bundle.getString("videoImage");
                        initVidelMessage(videoFile, videoImage);
                        break;
                    case 10:
                        if (!isKickedOut){
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    };

    private void initVidelMessage(String videoFile, String videoImage){
        sendMessageType = Constants.SEND_VIDEO;
        try {
            // 创建消息实体
            aliyTimeStamp = System.currentTimeMillis();
            // 添加实体对象
            sendBean.setRecruitLocIndex(videoImage);
            // +++++++++++++++++++++++++++++++++++++++++++++++
            sendBean.setMsgContent(videoFile);
            sendBean.setNewType(Constants.GROUP_CHAT);
            sendBean.setRecruitId("");
            sendBean.setTimestamp(String.valueOf(aliyTimeStamp));
            sendBean.setType(Constants.SEND_VIDEO);
            sendBean.setUserId(UserPreferencesUtil.getUserId(this));
            sendBean.setMessageStatus(Constants.NEWS_READ);
            sendBean.setInout(Constants.NEWS_END);
            sendBean.setIsShow(Constants.NEWS_SHOW);
            sendBean.setMsgStatus(Constants.NEWS_SENDING);
            uploadMedia(videoFile);
            // 新增本地数据
            TempGroupNewsEngine.inserChatNew(this, sendBean);
            adapter.addItem(sendBean);
        } catch (Exception e) {
            e.printStackTrace();
            sendBean.setMsgStatus(Constants.NEWS_SEND_FAITH);
            TempGroupNewsEngine.updateChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatus(sendBean);
        }
    }

    private void sendVideoMessage(String videoUrl) {

        try {
            String serviceName = SGApplication.getInstance().getConnection().getServiceName();
            String roomId = sendBean.getNoticeId().concat("@").concat("conference.").concat(serviceName);
            String messageContent = "";
            JSONObject bodyMsg = new JSONObject();
            bodyMsg.put("msgContent", videoUrl);
            bodyMsg.put("type", Constants.SEND_VIDEO);
            JSONObject ext = new JSONObject();
            ext.put("fromUser", new JSONObject(sendBean.getFromUser()));
            bodyMsg.put("ext", ext);
            bodyMsg.put("timestamp", aliyTimeStamp);
            bodyMsg.put("groupName",sendBean.getNoticeSubject());
            bodyMsg.put("groupId", sendBean.getNoticeId());
            messageContent = bodyMsg.toString();
            //赋值给消息对像
            org.jivesoftware.smack.packet.Message message = new org.jivesoftware.smack.packet.Message();
            message.setBody(messageContent);
            message.setType(org.jivesoftware.smack.packet.Message.Type.groupchat);
            message.setTo(roomId);
            message.setFrom(UserPreferencesUtil.getUserId(this).concat("@moreidols/sgapp"));
            message.setSubject(ConstTaskTag.GROUP_DISC_VIDEO);

            sendBean.setMsgContent(videoUrl);
            TempGroupNewsEngine.updateChatSendContent(this, sendBean, UserPreferencesUtil.getUserId(this));
            if (!isKickedOut){
                chat.sendMessage(message);
                sendBean.setMsgStatus(Constants.NEWS_SEND_SCUESS);
                TempGroupNewsEngine.updateChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
                adapter.updateItemSendStatus(sendBean);
            }else{
                sendBean.setMsgStatus(Constants.NEWS_SEND_FAITH);
                TempGroupNewsEngine.updateChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
                adapter.updateItemSendStatus(sendBean);
            }
        } catch (Exception e) {
            sendBean.setMsgStatus(Constants.NEWS_SEND_FAITH);
            TempGroupNewsEngine.updateChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatus(sendBean);
            e.printStackTrace();
        }

    }

    protected void sendImageMessage(String aliyUrl) {

        try {
            String serviceName = SGApplication.getInstance().getConnection().getServiceName();
            String roomId = sendBean.getNoticeId().concat("@").concat("conference.").concat(serviceName);
            String messageContent = "";
            JSONObject bodyMsg = new JSONObject();
            bodyMsg.put("msgContent", aliyUrl);
            bodyMsg.put("type", Constants.SEND_IMAGE);
            JSONObject ext = new JSONObject();
            ext.put("fromUser", new JSONObject(sendBean.getFromUser()));
            bodyMsg.put("ext", ext);
            bodyMsg.put("timestamp", aliyTimeStamp);
            bodyMsg.put("groupName",sendBean.getNoticeSubject());
            bodyMsg.put("groupId", sendBean.getNoticeId());
            messageContent = bodyMsg.toString();
            //赋值给消息对像
            org.jivesoftware.smack.packet.Message message = new org.jivesoftware.smack.packet.Message();
            message.setBody(messageContent);
            message.setType(org.jivesoftware.smack.packet.Message.Type.groupchat);
            message.setTo(roomId);
            message.setFrom(UserPreferencesUtil.getUserId(this).concat("@moreidols/sgapp"));
            message.setSubject(ConstTaskTag.GROUP_DISC_IMAGE);
            sendBean.setMsgContent(aliyUrl);
            TempGroupNewsEngine.updateChatSendContent(this, sendBean, UserPreferencesUtil.getUserId(this));
            if (!isKickedOut){
                chat.sendMessage(message);
                sendBean.setMsgStatus(Constants.NEWS_SEND_SCUESS);
                TempGroupNewsEngine.updateChatSendStatusForImages(this, sendBean, UserPreferencesUtil.getUserId(this));
                adapter.updateItemSendStatusForImages(sendBean);
                excuseSendPicAction();
            }else{
                sendBean.setMsgStatus(Constants.NEWS_SEND_FAITH);
                TempGroupNewsEngine.updateChatSendStatusForImages(this, sendBean, UserPreferencesUtil.getUserId(this));
                adapter.updateItemSendStatusForImages(sendBean);
                excuseSendPicAction();
            }

        } catch (Exception e) {
            sendBean.setMsgStatus(Constants.NEWS_SEND_FAITH);
            TempGroupNewsEngine.updateChatSendStatusForImages(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatusForImages(sendBean);
            excuseSendPicAction();
            e.printStackTrace();
        }

    }

    private void excuseSendPicAction() {
        if (currSendingIndex < sendImages.size()) {
            sendBean = sendImages.get(currSendingIndex);
            currSendingIndex = currSendingIndex + 1;
            // 发送图片
            uploadImageEngine(sendBean.getMsgContent());
        } else {
            currSendingIndex = 0;
            sendImages.clear();
        }
    }

    private void initImageMessage(String imagePath, String recruitLocIndex) {
        sendMessageType = Constants.SEND_IMAGE;
        // 创建消息实体
        aliyTimeStamp = System.currentTimeMillis();
        // 添加实体对象
        SGNewsBean tempBean=new SGNewsBean();
        tempBean.setRecruitLocIndex(recruitLocIndex);
        // +++++++++++++++++++++++++++++++++++++++++++++++
        tempBean.setMsgContent(imagePath);
        tempBean.setNewType(Constants.GROUP_CHAT);
        tempBean.setRecruitId(sendBean.getRecruitId());
        tempBean.setTimestamp(String.valueOf(aliyTimeStamp));
        tempBean.setType(Constants.SEND_IMAGE);
        tempBean.setUserId(UserPreferencesUtil.getUserId(this));
        tempBean.setMessageStatus(Constants.NEWS_READ);
        tempBean.setInout(Constants.NEWS_END);
        tempBean.setIsShow(Constants.NEWS_SHOW);
        tempBean.setMsgStatus(Constants.NEWS_SENDING);
        tempBean.setFromUser(sendBean.getFromUser());
        tempBean.setNoticeSubject(sendBean.getNoticeSubject());
        tempBean.setOperatorFlag(sendBean.getOperatorFlag());
        tempBean.set_id(sendBean.get_id());
        tempBean.setFriendNickName(sendBean.getFriendNickName());
        tempBean.setFriendUserIcon(sendBean.getFriendUserIcon());
        tempBean.setFriendUserId(sendBean.getFriendUserId());
        tempBean.setIsShowMsgTimer(sendBean.isShowMsgTimer());
        tempBean.setNoticeId(sendBean.getNoticeId());
        tempBean.setToUser(sendBean.getToUser());
        sendImages.add(tempBean);
        // 新增本地数据
        TempGroupNewsEngine.inserChatNew(this, tempBean);
        adapter.addItem(tempBean);
    }

    private void sendBaiduLocation() {
        try {
            String serviceName = SGApplication.getInstance().getConnection().getServiceName();
            String roomId = sendBean.getNoticeId().concat("@").concat("conference.").concat(serviceName);
            // 创建消息实体locationBean
            aliyTimeStamp = System.currentTimeMillis();
            JSONObject msgObj = new JSONObject();
            msgObj.put("lng", locationBean.getLongitude());
            msgObj.put("lat", locationBean.getLatitude());
            msgObj.put("address", locationBean.getAddress());
            // 添加实体对象
            // +++++++++++++++++++++++++++++++++++++++++++++++
            sendBean.setMsgContent(msgObj.toString());
            sendBean.setNewType(Constants.GROUP_CHAT);
            sendBean.setRecruitId(sendBean.getRecruitId());
            sendBean.setTimestamp(String.valueOf(aliyTimeStamp));
            sendBean.setType(Constants.SEND_LOCATION);
            sendBean.setUserId(UserPreferencesUtil.getUserId(this));
            sendBean.setMessageStatus(Constants.NEWS_READ);
            sendBean.setInout(Constants.NEWS_END);
            sendBean.setIsShow(Constants.NEWS_SHOW);
            sendBean.setMsgStatus(Constants.NEWS_SENDING);

            // 发送消息
            String messageContent = "";
            JSONObject bodyMsg = new JSONObject();
            bodyMsg.put("msgContent", msgObj);
            bodyMsg.put("type", Constants.SEND_LOCATION);
            JSONObject ext = new JSONObject();
            ext.put("fromUser", new JSONObject(sendBean.getFromUser()));
            bodyMsg.put("ext", ext);
            bodyMsg.put("timestamp", aliyTimeStamp);
            bodyMsg.put("groupName",sendBean.getNoticeSubject());
            bodyMsg.put("groupId", sendBean.getNoticeId());
            messageContent = bodyMsg.toString();

            //赋值给消息对像
            org.jivesoftware.smack.packet.Message message = new org.jivesoftware.smack.packet.Message();
            message.setBody(messageContent);
            message.setType(org.jivesoftware.smack.packet.Message.Type.groupchat);
            message.setTo(roomId);
            message.setFrom(UserPreferencesUtil.getUserId(this).concat("@moreidols/sgapp"));
            message.setSubject(ConstTaskTag.GROUP_DISC_POSION);

            // 新增本地数据
            TempGroupNewsEngine.inserChatNew(this, sendBean);
            adapter.addItem(sendBean);
            if (!isKickedOut){
                chat.sendMessage(message);
                sendBean.setMsgStatus(Constants.NEWS_SEND_SCUESS);
                TempGroupNewsEngine.updateChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
                adapter.updateItemSendStatus(sendBean);
            }else{
                sendBean.setMsgStatus(Constants.NEWS_SEND_FAITH);
                TempGroupNewsEngine.updateChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
                adapter.updateItemSendStatus(sendBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendBean.setMsgStatus(Constants.NEWS_SEND_FAITH);
            TempGroupNewsEngine.updateChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatus(sendBean);
        }
    }

    protected void sendVoiceMessage(String aliyUrl) {

        try {
            String serviceName = SGApplication.getInstance().getConnection().getServiceName();
            String roomId = sendBean.getNoticeId().concat("@").concat("conference.").concat(serviceName);
            String messageContent = "";
            JSONObject bodyMsg = new JSONObject();
            JSONObject msgObj = new JSONObject();
            msgObj.put("voiceUrl", aliyUrl);
            msgObj.put("length", voiceLength);
            bodyMsg.put("msgContent", msgObj);
            bodyMsg.put("type", Constants.SEND_SOUND);
            JSONObject ext = new JSONObject();
            ext.put("fromUser", new JSONObject(sendBean.getFromUser()));
            bodyMsg.put("ext", ext);
            bodyMsg.put("timestamp", aliyTimeStamp);
            bodyMsg.put("groupName",sendBean.getNoticeSubject());
            bodyMsg.put("groupId", sendBean.getNoticeId());
            messageContent = bodyMsg.toString();
            //赋值给消息对像
            org.jivesoftware.smack.packet.Message message = new org.jivesoftware.smack.packet.Message();
            message.setBody(messageContent);
            message.setType(org.jivesoftware.smack.packet.Message.Type.groupchat);
            message.setTo(roomId);
            message.setFrom(UserPreferencesUtil.getUserId(this).concat("@moreidols/sgapp"));
            message.setSubject(ConstTaskTag.GROUP_DISC_VOICE);

            sendBean.setMsgContent(msgObj.toString());
            TempGroupNewsEngine.updateChatSendContent(this, sendBean, UserPreferencesUtil.getUserId(this));
            if (!isKickedOut){
                chat.sendMessage(message);
                sendBean.setMsgStatus(Constants.NEWS_SEND_SCUESS);
                TempGroupNewsEngine.updateChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
                adapter.updateItemSendStatus(sendBean);
            }else{
                sendBean.setMsgStatus(Constants.NEWS_SEND_FAITH);
                TempGroupNewsEngine.updateChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
                adapter.updateItemSendStatus(sendBean);
            }
        } catch (Exception e) {
            sendBean.setMsgStatus(Constants.NEWS_SEND_FAITH);
            TempGroupNewsEngine.updateChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatus(sendBean);
            e.printStackTrace();
        }

    }

    private void initVoiceMessage(String voicePath, int minute) {
        sendMessageType = Constants.SEND_SOUND;
        this.voiceLength = minute;
        try {
            // 创建消息实体
            aliyTimeStamp = System.currentTimeMillis();
            JSONObject msgObj = new JSONObject();
            msgObj.put("voiceUrl", voicePath);
            msgObj.put("length", minute);
            // 添加实体对象
            // +++++++++++++++++++++++++++++++++++++++++++++++
            sendBean.setMsgContent(msgObj.toString());
            sendBean.setNewType(Constants.GROUP_CHAT);
            sendBean.setRecruitId("");
            sendBean.setTimestamp(String.valueOf(aliyTimeStamp));
            sendBean.setType(Constants.SEND_SOUND);
            sendBean.setUserId(UserPreferencesUtil.getUserId(this));
            sendBean.setMessageStatus(Constants.NEWS_READ);
            sendBean.setInout(Constants.NEWS_END);
            sendBean.setIsShow(Constants.NEWS_SHOW);
            sendBean.setMsgStatus(Constants.NEWS_SENDING);
            uploadMedia(voicePath);
            // 新增本地数据
            TempGroupNewsEngine.inserChatNew(this, sendBean);
            adapter.addItem(sendBean);
        } catch (Exception e) {
            e.printStackTrace();
            sendBean.setMsgStatus(Constants.NEWS_SEND_FAITH);
            TempGroupNewsEngine.updateChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatus(sendBean);
        }
    }

    private void sendTxtMessage(String content) {
        try {
            String serviceName = SGApplication.getInstance().getConnection().getServiceName();
            String roomId = sendBean.getNoticeId().concat("@").concat("conference.").concat(serviceName);
            // 创建消息实体
            long timestamp = System.currentTimeMillis();
            String messageContent = "";
            JSONObject bodyMsg = new JSONObject();
            bodyMsg.put("msgContent", content);
            bodyMsg.put("type", Constants.SEND_TEXT);
            JSONObject ext = new JSONObject();
            ext.put("fromUser", new JSONObject(sendBean.getFromUser()));
            bodyMsg.put("ext", ext);
            bodyMsg.put("timestamp", timestamp);
            bodyMsg.put("groupName",sendBean.getNoticeSubject());
            bodyMsg.put("groupId", sendBean.getNoticeId());
            messageContent = bodyMsg.toString();
            //赋值给消息对像
            org.jivesoftware.smack.packet.Message message = new org.jivesoftware.smack.packet.Message();
            message.setBody(messageContent);
            message.setType(org.jivesoftware.smack.packet.Message.Type.groupchat);
            message.setTo(roomId);
            message.setFrom(UserPreferencesUtil.getUserId(this).concat("@moreidols/sgapp"));
            message.setSubject(ConstTaskTag.GROUP_DISC_TEXT);
            // 添加实体对象
            // +++++++++++++++++++++++++++++++++++++++++++++++
            sendBean.setMsgContent(content);
            sendBean.setNewType(Constants.GROUP_CHAT);
            sendBean.setRecruitId("");
            sendBean.setTimestamp(String.valueOf(timestamp));
            sendBean.setType(Constants.SEND_TEXT);
            sendBean.setUserId(UserPreferencesUtil.getUserId(this));
            sendBean.setMessageStatus(Constants.NEWS_READ);
            sendBean.setInout(Constants.NEWS_END);
            sendBean.setIsShow(Constants.NEWS_SHOW);
            sendBean.setMsgStatus(Constants.NEWS_SENDING);
            // 新增本地数据
            TempGroupNewsEngine.inserChatNew(this, sendBean);
            adapter.addItem(sendBean);
            if (!isKickedOut){
                chat.sendMessage(message);
                sendBean.setMsgStatus(Constants.NEWS_SEND_SCUESS);
                TempGroupNewsEngine.updateChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
                adapter.updateItemSendStatus(sendBean);
            }else{
                sendBean.setMsgStatus(Constants.NEWS_SEND_FAITH);
                TempGroupNewsEngine.updateChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
                adapter.updateItemSendStatus(sendBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
            sendBean.setMsgStatus(Constants.NEWS_SEND_FAITH);
            TempGroupNewsEngine.updateChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatus(sendBean);
        }
    }

    private class ResendMessageListenser implements OnClickListener {

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
        if (Constants.SEND_TEXT.equals(item.getType())) {
            sendBean.setMsgStatus(Constants.NEWS_SENDING);
            NewsEngine.updateChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatus(sendBean);
            reSendTxtMessage();
        } else if (Constants.SEND_SOUND.equals(item.getType())) {
            sendBean.setMsgStatus(Constants.NEWS_SENDING);
            NewsEngine.updateChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatus(sendBean);
            sendMessageType = Constants.SEND_SOUND;
            reSendVoice();
        } else if (Constants.SEND_IMAGE.equals(item.getType())) {
            sendBean.setMsgStatus(Constants.NEWS_SENDING);
            NewsEngine.updateChatSendStatusForImages(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatusForImages(sendBean);
            sendMessageType = Constants.SEND_IMAGE;
            reSendImage();

        } else if (Constants.SEND_LOCATION.equals(item.getType())) {
            sendBean.setMsgStatus(Constants.NEWS_SENDING);
            NewsEngine.updateChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
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
            String serviceName = SGApplication.getInstance().getConnection().getServiceName();
            String roomId = sendBean.getNoticeId().concat("@").concat("conference.").concat(serviceName);
            // 创建消息实体locationBean
            aliyTimeStamp = System.currentTimeMillis();
            // 发送消息
            String messageContent = "";
            JSONObject bodyMsg = new JSONObject();
            bodyMsg.put("msgContent", sendBean.getMsgContent());
            bodyMsg.put("type", Constants.SEND_LOCATION);
            JSONObject ext = new JSONObject();
            ext.put("fromUser", new JSONObject(sendBean.getFromUser()));
            bodyMsg.put("ext", ext);
            bodyMsg.put("timestamp", aliyTimeStamp);
            bodyMsg.put("groupName",sendBean.getNoticeSubject());
            bodyMsg.put("groupId", sendBean.getNoticeId());
            messageContent = bodyMsg.toString();

            //赋值给消息对像
            org.jivesoftware.smack.packet.Message message = new org.jivesoftware.smack.packet.Message();
            message.setBody(messageContent);
            message.setType(org.jivesoftware.smack.packet.Message.Type.groupchat);
            message.setTo(roomId);
            message.setFrom(UserPreferencesUtil.getUserId(this).concat("@moreidols/sgapp"));
            message.setSubject(ConstTaskTag.GROUP_DISC_POSION);
            if (!isKickedOut){
                chat.sendMessage(message);
                sendBean.setMsgStatus(Constants.NEWS_SEND_SCUESS);
                TempGroupNewsEngine.updateChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
                adapter.updateItemSendStatus(sendBean);
            }else{
                sendBean.setMsgStatus(Constants.NEWS_SEND_FAITH);
                TempGroupNewsEngine.updateChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
                adapter.updateItemSendStatus(sendBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
            sendBean.setMsgStatus(Constants.NEWS_SEND_FAITH);
            TempGroupNewsEngine.updateChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
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
            String serviceName = SGApplication.getInstance().getConnection().getServiceName();
            String roomId = sendBean.getNoticeId().concat("@").concat("conference.").concat(serviceName);
            String messageContent = "";
            JSONObject bodyMsg = new JSONObject();
            bodyMsg.put("msgContent", sendBean.getMsgContent());
            bodyMsg.put("type", Constants.SEND_IMAGE);
            JSONObject ext = new JSONObject();
            ext.put("fromUser", new JSONObject(sendBean.getFromUser()));
            bodyMsg.put("ext", ext);
            bodyMsg.put("timestamp", aliyTimeStamp);
            bodyMsg.put("groupName",sendBean.getNoticeSubject());
            bodyMsg.put("groupId", sendBean.getNoticeId());
            messageContent = bodyMsg.toString();
            //赋值给消息对像
            org.jivesoftware.smack.packet.Message message = new org.jivesoftware.smack.packet.Message();
            message.setBody(messageContent);
            message.setType(org.jivesoftware.smack.packet.Message.Type.groupchat);
            message.setTo(roomId);
            message.setFrom(UserPreferencesUtil.getUserId(this).concat("@moreidols/sgapp"));
            message.setSubject(ConstTaskTag.GROUP_DISC_IMAGE);
            if (!isKickedOut){
                chat.sendMessage(message);
                sendBean.setMsgStatus(Constants.NEWS_SEND_SCUESS);
                TempGroupNewsEngine.updateChatSendStatusForImages(this, sendBean, UserPreferencesUtil.getUserId(this));
                adapter.updateItemSendStatusForImages(sendBean);
            }else{
                sendBean.setMsgStatus(Constants.NEWS_SEND_FAITH);
                TempGroupNewsEngine.updateChatSendStatusForImages(this, sendBean, UserPreferencesUtil.getUserId(this));
                adapter.updateItemSendStatusForImages(sendBean);
            }

        } catch (Exception e) {
            sendBean.setMsgStatus(Constants.NEWS_SEND_FAITH);
            TempGroupNewsEngine.updateChatSendStatusForImages(this, sendBean, UserPreferencesUtil.getUserId(this));
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
            String serviceName = SGApplication.getInstance().getConnection().getServiceName();
            String roomId = sendBean.getNoticeId().concat("@").concat("conference.").concat(serviceName);
            String messageContent = "";
            JSONObject bodyMsg = new JSONObject();
            bodyMsg.put("msgContent", sendBean.getMsgContent());
            bodyMsg.put("type", Constants.SEND_VIDEO);
            JSONObject ext = new JSONObject();
            ext.put("fromUser", new JSONObject(sendBean.getFromUser()));
            bodyMsg.put("ext", ext);
            bodyMsg.put("timestamp", aliyTimeStamp);
            bodyMsg.put("groupName",sendBean.getNoticeSubject());
            bodyMsg.put("groupId", sendBean.getNoticeId());
            messageContent = bodyMsg.toString();

            //赋值给消息对像
            org.jivesoftware.smack.packet.Message message = new org.jivesoftware.smack.packet.Message();
            message.setBody(messageContent);
            message.setType(org.jivesoftware.smack.packet.Message.Type.groupchat);
            message.setTo(roomId);
            message.setFrom(UserPreferencesUtil.getUserId(this).concat("@moreidols/sgapp"));
            message.setSubject(ConstTaskTag.GROUP_DISC_VIDEO);
            if (!isKickedOut){
                chat.sendMessage(message);
                sendBean.setMsgStatus(Constants.NEWS_SEND_SCUESS);
                TempGroupNewsEngine.updateChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
                adapter.updateItemSendStatus(sendBean);
            }else{
                sendBean.setMsgStatus(Constants.NEWS_SEND_FAITH);
                TempGroupNewsEngine.updateChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
                adapter.updateItemSendStatus(sendBean);
            }
        } catch (Exception e) {
            sendBean.setMsgStatus(Constants.NEWS_SEND_FAITH);
            TempGroupNewsEngine.updateChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
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
            TempGroupNewsEngine.updateChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatus(sendBean);
            e.printStackTrace();
        }

    }

    private void sendVoiceDirect() {
        try {
            String serviceName = SGApplication.getInstance().getConnection().getServiceName();
            String roomId = sendBean.getNoticeId().concat("@").concat("conference.").concat(serviceName);
            String messageContent = "";
            JSONObject bodyMsg = new JSONObject();
            bodyMsg.put("msgContent", sendBean.getMsgContent());
            bodyMsg.put("type", Constants.SEND_SOUND);
            JSONObject ext = new JSONObject();
            ext.put("fromUser", new JSONObject(sendBean.getFromUser()));
            bodyMsg.put("ext", ext);
            bodyMsg.put("timestamp", aliyTimeStamp);
            bodyMsg.put("groupName",sendBean.getNoticeSubject());
            bodyMsg.put("groupId", sendBean.getNoticeId());
            messageContent = bodyMsg.toString();

            //赋值给消息对像
            org.jivesoftware.smack.packet.Message message = new org.jivesoftware.smack.packet.Message();
            message.setBody(messageContent);
            message.setType(org.jivesoftware.smack.packet.Message.Type.groupchat);
            message.setTo(roomId);
            message.setFrom(UserPreferencesUtil.getUserId(this).concat("@moreidols/sgapp"));
            message.setSubject(ConstTaskTag.GROUP_DISC_VOICE);
            if (!isKickedOut){
                chat.sendMessage(message);
                sendBean.setMsgStatus(Constants.NEWS_SEND_SCUESS);
                TempGroupNewsEngine.updateChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
                adapter.updateItemSendStatus(sendBean);
            }else{
                sendBean.setMsgStatus(Constants.NEWS_SEND_FAITH);
                TempGroupNewsEngine.updateChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
                adapter.updateItemSendStatus(sendBean);
            }

        } catch (Exception e) {
            sendBean.setMsgStatus(Constants.NEWS_SEND_FAITH);
            TempGroupNewsEngine.updateChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatus(sendBean);
            e.printStackTrace();
        }
    }

    private void reSendTxtMessage() {
        try {
            String serviceName = SGApplication.getInstance().getConnection().getServiceName();
            String roomId = sendBean.getNoticeId().concat("@").concat("conference.").concat(serviceName);
            // 创建消息实体
            long timestamp = System.currentTimeMillis();
            String messageContent = "";
            JSONObject bodyMsg = new JSONObject();
            bodyMsg.put("msgContent", sendBean.getMsgContent());
            bodyMsg.put("type", Constants.SEND_TEXT);
            JSONObject ext = new JSONObject();
            ext.put("fromUser", new JSONObject(sendBean.getFromUser()));
            bodyMsg.put("ext", ext);
            bodyMsg.put("timestamp", timestamp);
            bodyMsg.put("groupName",sendBean.getNoticeSubject());
            bodyMsg.put("groupId", sendBean.getNoticeId());
            messageContent = bodyMsg.toString();

            //赋值给消息对像
            org.jivesoftware.smack.packet.Message message = new org.jivesoftware.smack.packet.Message();
            message.setBody(messageContent);
            message.setType(org.jivesoftware.smack.packet.Message.Type.groupchat);
            message.setTo(roomId);
            message.setFrom(UserPreferencesUtil.getUserId(this).concat("@moreidols/sgapp"));
            message.setSubject(ConstTaskTag.GROUP_DISC_TEXT);

            if (!isKickedOut){
// 发送消息
                chat.sendMessage(message);
                sendBean.setMsgStatus(Constants.NEWS_SEND_SCUESS);
                TempGroupNewsEngine.updateChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
                adapter.updateItemSendStatus(sendBean);
            }else{
                sendBean.setMsgStatus(Constants.NEWS_SEND_FAITH);
                TempGroupNewsEngine.updateChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
                adapter.updateItemSendStatus(sendBean);
            }

        } catch (Exception e) {
            e.printStackTrace();
            sendBean.setMsgStatus(Constants.NEWS_SEND_FAITH);
            TempGroupNewsEngine.updateChatSendStatus(this, sendBean, UserPreferencesUtil.getUserId(this));
            adapter.updateItemSendStatus(sendBean);
        }
    }
}
