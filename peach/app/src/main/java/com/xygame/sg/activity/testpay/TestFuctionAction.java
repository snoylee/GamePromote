package com.xygame.sg.activity.testpay;

import com.xygame.sg.AudioRecorder2Mp3Util;
import com.xygame.sg.R;
import com.xygame.sg.activity.base.SGBaseActivity;
import com.xygame.sg.activity.commen.ReportFristActivity;
import com.xygame.sg.activity.commen.ShareBoardView;
import com.xygame.sg.activity.personal.CreateHeadPicActivity;
import com.xygame.sg.activity.personal.CreateModelPicActivity;
import com.xygame.sg.activity.personal.EditorBirthdayActivity;
import com.xygame.sg.activity.personal.EditorCountryActivity;
import com.xygame.sg.activity.personal.EditorProvinceActivity;
import com.xygame.sg.activity.personal.PersonalPhotoesActivity;
import com.xygame.sg.http.RequestBean;
import com.xygame.sg.http.ResponseBean;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.utils.AliPreferencesUtil;
import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.VidoHelperUtils;
import com.xygame.sg.vido.VideoPlayerActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import example.chatfragment.MainActivity;

public class TestFuctionAction extends SGBaseActivity{
	private ImageView picImage;
	private Bitmap bitmap = null;
	AudioRecorder2Mp3Util util = null;
	private String recordFilePath;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sg_model_activity);
		View testShare = findViewById(R.id.testShare);
		testShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), ShareBoardView.class);
				startActivity(intent);
			}
		});
		View testOneButton = findViewById(R.id.testOneButton);
		testOneButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (util == null) {
					util = new AudioRecorder2Mp3Util(null,
							"/sdcard/test_audio_recorder_for_mp3.raw",
							"/sdcard/test_audio_recorder_for_mp3.mp3");
				}
				util.startRecording();

				Toast.makeText(TestFuctionAction.this, "请说话", Toast.LENGTH_SHORT).show();


//				canClean = true;
//				OneButtonDialog dialog = new OneButtonDialog(getApplicationContext(), "提示：一个按钮的提示框", R.style.dineDialog,
//						new ButtonOneListener() {
//
//					@Override
//					public void confrimListener(Dialog dialog) {
//						// TODO Auto-generated method stub
//						Toast.makeText(getApplicationContext(), "您按了确定按钮", Toast.LENGTH_SHORT).show();
//					}
//				});
//				dialog.show();
			}
		});
		View testTwoButton = findViewById(R.id.testTwoButton);
		testTwoButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Toast.makeText(TestFuctionAction.this, "正在转换", Toast.LENGTH_SHORT).show();
				util.stopRecordingAndConvertFile();
				Toast.makeText(TestFuctionAction.this, "ok",  Toast.LENGTH_SHORT).show();
				util.cleanFile(AudioRecorder2Mp3Util.RAW);
				util.close();
				util = null;
//				TwoButtonDialog dialog = new TwoButtonDialog(getApplicationContext(), "提示：两个按钮的提示框", R.style.dineDialog,
//						new ButtonTwoListener() {
//
//					@Override
//					public void confrimListener() {
//						// TODO Auto-generated method stub
//						Toast.makeText(getApplicationContext(), "您按了确定按钮", Toast.LENGTH_SHORT).show();
//					}
//
//					@Override
//					public void cancelListener() {
//						// TODO Auto-generated method stub
//						Toast.makeText(getApplicationContext(), "您按了取消按钮", Toast.LENGTH_SHORT).show();
//					}
//				});
//				dialog.show();
			}
		});
		View testReport = findViewById(R.id.testReport);
		testReport.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), ReportFristActivity.class);
				startActivity(intent);
			}
		});
		View testPayment = findViewById(R.id.testPayment);
		testPayment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), TestPayActivity.class);
				startActivity(intent);
			}
		});
		View modelImages = findViewById(R.id.modelImages);
		modelImages.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), CreateModelPicActivity.class);
				startActivity(intent);
			}
		});
		View editorHead = findViewById(R.id.editorHead);
		editorHead.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), CreateHeadPicActivity.class);
				startActivity(intent);
			}
		});
		View personPhotoes = findViewById(R.id.personPhotoes);
		personPhotoes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), PersonalPhotoesActivity.class);
				startActivity(intent);
			}
		});
		View editorBirthday = findViewById(R.id.editorBirthday);
		editorBirthday.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), EditorBirthdayActivity.class);
				startActivity(intent);
			}
		});
		View editorCountry = findViewById(R.id.editorCountry);
		editorCountry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), EditorCountryActivity.class);
				startActivity(intent);
			}
		});
		View editorCity = findViewById(R.id.editorCity);
		editorCity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), EditorProvinceActivity.class);
				startActivity(intent);
			}
		});
		View testList = findViewById(R.id.testList);
		testList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), TestRefreshList.class);
				startActivity(intent);
			}
		});
		picImage = (ImageView) findViewById(R.id.picImage);
		picImage.setImageResource(R.drawable.default_avatar);
		new Thread(new Runnable() {
			@Override
			public void run() {
				bitmap = VidoHelperUtils.createVideoThumbnail(
						"http://app-sgshow.oss-cn-hangzhou.aliyuncs.com/avatar/VMS_1452591571821.mp4", 100, 100);
				myHandler.sendEmptyMessage(0);
			}
		}).start();

		View vidoPlay = findViewById(R.id.vidoPlay);
		vidoPlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), VideoPlayerActivity.class);
				intent.putExtra("vidoUrl",
						"http://app-sgshow.oss-cn-hangzhou.aliyuncs.com/photoalbum/VMS_1450229351905.mp4");
				startActivity(intent);
			}
		});
		View recodView = findViewById(R.id.recodView);
		recodView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent intent = new Intent(getApplicationContext(), FFmpegRecorderActivity.class);
//				startActivityForResult(intent, 0);
			}
		});
		View chatView=findViewById(R.id.chatView);
		chatView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(intent);
			}
		});
		View anmotionWelcomPage=findViewById(R.id.anmotionWelcomPage);
		anmotionWelcomPage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), LensFocusActivity.class);
				startActivity(intent);
			}
		});
		View testRequestNet=findViewById(R.id.testRequestNet);
		testRequestNet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				RequestBean item = new RequestBean();
				try {
					item.setData(new JSONObject());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				item.setServiceURL(ConstTaskTag.REQUEST_ALIY_ADRESS);
				ShowMsgDialog.showNoMsg(TestFuctionAction.this, false);
				ThreadPool.getInstance().excuseAction(TestFuctionAction.this,item,ConstTaskTag.CONST_TAG_TEST);
			}
		});
	}

	@Override
	protected void getResponseBean(ResponseBean data) {
		super.getResponseBean(data);
		switch (data.getPosionSign()){
			case ConstTaskTag.CONST_TAG_TEST:
				parseJson(data);
				break;
		}
	}

	private void parseJson(ResponseBean data) {
		try{
			JSONArray array=new JSONArray(data.getRecord());
			for (int i=0;i<array.length();i++){
				JSONObject obj=array.getJSONObject(i);
				String name=obj.get("propName").toString();
				String value=obj.get("propValue").toString();
				if("ali_oss_secret_id".equals(name)){
					AliPreferencesUtil.setAccessKey(this, value);
				}
				if("ali_oss_secret_key".equals(name)){
					AliPreferencesUtil.setScrectKey(this, value);
				}

				if("ali_oss_bucket_name".equals(name)){
					AliPreferencesUtil.setBuckekName(this, value);
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub

			switch (msg.what) {

			case 0:
				picImage.setImageBitmap(bitmap);
				break;

			}

			super.handleMessage(msg);
		}
	};
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (Activity.RESULT_OK != resultCode || null == data) {
			return;
		}
		switch (requestCode) {
		case 0: {
			String result = data.getStringExtra(Constants.COMEBACK);
			if (result!=null) {
				Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
			}
			break;
		}
		default:
			break;
		}
	}

	/**
	 * 创建本地MP3
	 * @return
	 */
	public void createLocalMp3(){
		try {
			MediaPlayer mediaPlayer=new MediaPlayer();
			mediaPlayer.setDataSource(recordFilePath);
			mediaPlayer.prepare();
			mediaPlayer.start();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
