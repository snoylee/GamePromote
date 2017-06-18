package com.xygame.sg.vido;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

import com.xygame.sg.R;
import com.xygame.sg.http.ThreadPool;
import com.xygame.sg.utils.FileUtil;
import com.xygame.sg.vido.VideoView.MySizeChangeLinstener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue.IdleHandler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class VideoPlayerActivity extends Activity {

	private final static String TAG = "VideoPlayerActivity";
	private final static int PROGRESS_CHANGED = 0;
	private final static int HIDE_CONTROLER = 1;
	private boolean isOnline = false;
	private boolean isChangedVideo = false;
	private String remoteUrl;
	private String localUrl;
	private String fileName;
	public static LinkedList<MovieInfo> playList = new LinkedList<MovieInfo>();

	public class MovieInfo {
		String displayName;
		String path;
	}

	private int playedTime;
	private VideoView vv = null;
	private SeekBar seekBar = null;
	private TextView durationTextView = null;
	private TextView playedTextView = null;
	private GestureDetector mGestureDetector = null;

	private ImageButton bn3 = null;

	private View controlView = null;
	private PopupWindow controler = null;

	private static int screenWidth = 0;
	private static int screenHeight = 0;
	private static int controlHeight = 0;
	private final static int TIME = 6868;

	private boolean isControllerShow = true;
	private boolean isPaused = false;
	private boolean isFullScreen = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.vido_play_layout);
		Looper.myQueue().addIdleHandler(new IdleHandler() {

			@Override
			public boolean queueIdle() {

				// TODO Auto-generated method stub
				if (controler != null && vv.isShown()) {
					controler.showAtLocation(vv, Gravity.BOTTOM, 0, 0);
					controler.update(0, 0, screenWidth, controlHeight);
				}
				return false;
			}
		});

		controlView = getLayoutInflater().inflate(R.layout.controler, null);
		controler = new PopupWindow(controlView);
		durationTextView = (TextView) controlView.findViewById(R.id.duration);
		playedTextView = (TextView) controlView.findViewById(R.id.has_played);
		bn3 = (ImageButton) controlView.findViewById(R.id.button3);
		vv = (VideoView) findViewById(R.id.vv);
		vv.setOnErrorListener(new OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {

				vv.stopPlayback();
				isOnline = false;

				new AlertDialog.Builder(VideoPlayerActivity.this).setTitle("对不起").setMessage("您所播的视频格式不正确，播放已停止。")
						.setPositiveButton("֪知道了", new AlertDialog.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {

								vv.stopPlayback();

							}

						}).setCancelable(false).show();

				return false;
			}

		});

		remoteUrl = getIntent().getStringExtra("vidoUrl");
		String[] vidoUrlArray = remoteUrl.split("/");
		fileName = vidoUrlArray[vidoUrlArray.length - 1];

		String vidoPath = FileUtil.MP4_ROOT_PATH.concat(fileName);
		final boolean isExist = FileUtil.isFileExist(vidoPath);
		if (isExist) {
			bn3.setImageResource(R.drawable.pause);
			isOnline = false;
			vv.setVideoPath(vidoPath);
		} else {
			bn3.setImageResource(R.drawable.pause);
			isOnline = true;
			vv.setVideoPath(remoteUrl);
		}


		vv.setMySizeChangeLinstener(new MySizeChangeLinstener() {

			@Override
			public void doMyThings() {
				// TODO Auto-generated method stub
				setVideoScale(SCREEN_DEFAULT);
			}

		});

		bn3.setAlpha(0xBB);
		bn3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cancelDelayHide();
				if (isPaused) {
					vv.start();
					bn3.setImageResource(R.drawable.pause);
					hideControllerDelay();
				} else {
					vv.pause();
					bn3.setImageResource(R.drawable.play);
				}
				isPaused = !isPaused;

			}

		});

		seekBar = (SeekBar) controlView.findViewById(R.id.seekbar);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser) {
				// TODO Auto-generated method stub

				if (fromUser) {

					if (!isOnline) {
						vv.seekTo(progress);
					}

				}

			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				myHandler.removeMessages(HIDE_CONTROLER);
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				myHandler.sendEmptyMessageDelayed(HIDE_CONTROLER, TIME);
			}
		});

		getScreenSize();

		mGestureDetector = new GestureDetector(new SimpleOnGestureListener() {

			@Override
			public boolean onDoubleTap(MotionEvent e) {
				// TODO Auto-generated method stub
				if (isFullScreen) {
					setVideoScale(SCREEN_DEFAULT);
				} else {
					setVideoScale(SCREEN_FULL);
				}
				isFullScreen = !isFullScreen;
				Log.d(TAG, "onDoubleTap");

				if (isControllerShow) {
					showController();
				}
				// return super.onDoubleTap(e);
				return true;
			}

			@Override
			public boolean onSingleTapConfirmed(MotionEvent e) {
				// TODO Auto-generated method stub
				if (!isControllerShow) {
					showController();
					hideControllerDelay();
				} else {
					cancelDelayHide();
					hideController();
				}
				// return super.onSingleTapConfirmed(e);
				return true;
			}

			@Override
			public void onLongPress(MotionEvent e) {
				// TODO Auto-generated method stub
				if (isPaused) {
					vv.start();
					bn3.setImageResource(R.drawable.pause);
					cancelDelayHide();
					hideControllerDelay();
				} else {
					vv.pause();
					bn3.setImageResource(R.drawable.play);
					cancelDelayHide();
					showController();
				}
				isPaused = !isPaused;
				// super.onLongPress(e);
			}
		});

		vv.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer arg0) {
				// TODO Auto-generated method stub

				setVideoScale(SCREEN_DEFAULT);
				isFullScreen = false;
				if (isControllerShow) {
					showController();
				}

				int i = vv.getDuration();
				Log.d("onCompletion", "" + i);
				seekBar.setMax(i);
				i /= 1000;
				int minute = i / 60;
				int hour = minute / 60;
				int second = i % 60;
				minute %= 60;
				durationTextView.setText(String.format("%02d:%02d:%02d", hour, minute, second));

				/*
				 * controler.showAtLocation(vv, Gravity.BOTTOM, 0, 0);
				 * controler.update(screenWidth, controlHeight);
				 * myHandler.sendEmptyMessageDelayed(HIDE_CONTROLER, TIME);
				 */

				vv.start();
				bn3.setImageResource(R.drawable.pause);
				hideControllerDelay();
				myHandler.sendEmptyMessage(PROGRESS_CHANGED);
				if (!isExist) {
					startDownVido();
				}
			}
		});

		vv.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer arg0) {
				// TODO Auto-generated method stub
				isOnline = false;
				String vidoPath = FileUtil.MP4_ROOT_PATH.concat(fileName);
				boolean isExist = FileUtil.isFileExist(vidoPath);
				vv.stopPlayback();
				bn3.setImageResource(R.drawable.play);
				if (isExist) {
					vv.setVideoPath(vidoPath);
				} else {
					vv.setVideoPath(remoteUrl);
				}
				isPaused = !isPaused;
			}
		});
	}

	Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub

			switch (msg.what) {

				case PROGRESS_CHANGED:

					int i = vv.getCurrentPosition();
					seekBar.setProgress(i);

					if (isOnline) {
						int j = vv.getBufferPercentage();
						seekBar.setSecondaryProgress(j * seekBar.getMax() / 100);
					} else {
						seekBar.setSecondaryProgress(0);
					}

					i /= 1000;
					int minute = i / 60;
					int hour = minute / 60;
					int second = i % 60;
					minute %= 60;
					playedTextView.setText(String.format("%02d:%02d:%02d", hour, minute, second));

					sendEmptyMessageDelayed(PROGRESS_CHANGED, 100);
					break;

				case HIDE_CONTROLER:
					hideController();
					break;
			}

			super.handleMessage(msg);
		}
	};

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub

		boolean result = mGestureDetector.onTouchEvent(event);

		if (!result) {
			if (event.getAction() == MotionEvent.ACTION_UP) {

				/*
				 * if(!isControllerShow){ showController();
				 * hideControllerDelay(); }else { cancelDelayHide();
				 * hideController(); }
				 */
			}
			result = super.onTouchEvent(event);
		}

		return result;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub

		getScreenSize();
		if (isControllerShow) {

			cancelDelayHide();
			hideController();
			showController();
			hideControllerDelay();
		}

		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		playedTime = vv.getCurrentPosition();
		vv.pause();
		bn3.setImageResource(R.drawable.play);
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if (!isChangedVideo) {
			vv.seekTo(playedTime);
			vv.start();
		} else {
			isChangedVideo = false;
		}

		// if(vv.getVideoHeight()!=0){
		if (vv.isPlaying()) {
			bn3.setImageResource(R.drawable.pause);
			hideControllerDelay();
		}
//		Log.d("REQUEST", "NEW AD !");
//		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//		}
//		if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
//			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//		}
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		if (controler.isShowing()) {
			controler.dismiss();
		}

		myHandler.removeMessages(PROGRESS_CHANGED);
		myHandler.removeMessages(HIDE_CONTROLER);

		if (vv.isPlaying()) {
			vv.stopPlayback();
		}

		playList.clear();

		super.onDestroy();
	}

	private void getScreenSize() {
		Display display = getWindowManager().getDefaultDisplay();
		screenHeight = display.getHeight();
		screenWidth = display.getWidth();
		controlHeight = screenHeight / 5;
	}

	private void hideController() {
		if (controler.isShowing()) {
			controler.update(0, 0, 0, 0);
			isControllerShow = false;
		}
	}

	private void hideControllerDelay() {
		myHandler.sendEmptyMessageDelayed(HIDE_CONTROLER, TIME);
	}

	private void showController() {
		controler.update(0, 0, screenWidth, controlHeight);
		isControllerShow = true;
	}

	private void cancelDelayHide() {
		myHandler.removeMessages(HIDE_CONTROLER);
	}

	private final static int SCREEN_FULL = 0;
	private final static int SCREEN_DEFAULT = 1;

	private void setVideoScale(int flag) {

		switch (flag) {
			case SCREEN_FULL:

				Log.d(TAG, "screenWidth: " + screenWidth + " screenHeight: " + screenHeight);
				vv.setVideoScale(screenWidth, screenHeight);
				getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

				break;

			case SCREEN_DEFAULT:

				int videoWidth = vv.getVideoWidth();
				int videoHeight = vv.getVideoHeight();
				int mWidth = screenWidth;
				int mHeight = screenHeight - 25;

				if (videoWidth > 0 && videoHeight > 0) {
					if (videoWidth * mHeight > mWidth * videoHeight) {
						// Log.i("@@@", "image too tall, correcting");
						mHeight = mWidth * videoHeight / videoWidth;
					} else if (videoWidth * mHeight < mWidth * videoHeight) {
						// Log.i("@@@", "image too wide, correcting");
						mWidth = mHeight * videoWidth / videoHeight;
					} else {

					}
				}

				vv.setVideoScale(mWidth, mHeight);

				getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

				break;
		}
	}

	public void startDownVido() {
		initParams();
		playvideo();
	}

	private void initParams() {
		localUrl = FileUtil.MP4_ROOT_PATH.concat(fileName);

	}

	/**
	 * 下载和缓冲mp4文件头部数据
	 */
	private void prepareVideo() throws IOException {
		long videoTotalSize = 0;
		URL url = new URL(remoteUrl);
		HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
		httpConnection.setConnectTimeout(3000);
		httpConnection.setRequestProperty("RANGE", "bytes=" + 0 + "-");

		InputStream is = httpConnection.getInputStream();

		videoTotalSize = httpConnection.getContentLength();
		if (videoTotalSize == -1) {
			return;
		}

		File cacheFile = new File(localUrl);

		if (!cacheFile.exists()) {
			cacheFile.getParentFile().mkdirs();
			cacheFile.createNewFile();
		}

		RandomAccessFile raf = new RandomAccessFile(cacheFile, "rws");
		raf.setLength(videoTotalSize);
		raf.seek(0);

		byte buf[] = new byte[10 * 1024];
		int size = 0;
		while ((size = is.read(buf)) != -1) {
			try {
				raf.write(buf, 0, size);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		is.close();
		raf.close();

	}

	private void playvideo() {
		ThreadPool.getInstance().excuseThread(new PrepareTask());
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					prepareVideo();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}).start();
	}

	class PrepareTask implements Runnable{
		@Override
		public void run() {
			try {
				prepareVideo();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}