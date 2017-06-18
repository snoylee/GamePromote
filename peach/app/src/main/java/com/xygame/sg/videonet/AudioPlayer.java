package com.xygame.sg.videonet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Handler;
import android.util.Log;
import android.webkit.URLUtil;

/*
 * Android锟结供锟侥接匡拷锟斤拷然锟角诧拷锟斤拷直锟斤拷通锟斤拷锟斤拷锟斤拷锟斤拷Player,锟斤拷锟斤拷Android锟结供锟剿猴拷全锟斤拷锟斤拷募锟较低筹拷涌冢锟斤拷锟斤拷锟斤拷锟揭伙拷锟斤拷碌姆锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷 <br>
 * <1>,锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟叫达拷锟斤拷锟绞憋拷锟斤拷募锟斤拷锟� <br>
 * <2>,锟斤拷锟斤拷时锟侥硷拷锟斤拷锟斤拷Player<br>
 * <1>,锟斤拷锟斤拷没锟斤拷锟矫碉拷锟斤拷时锟侥硷拷锟斤拷<br>
 * <2>,锟斤拷停锟竭硷拷锟斤拷锟脚伙拷锟斤拷---锟阶筹拷锟届常锟斤拷锟斤拷锟斤拷锟斤拷<br>
 * <3>,锟街讹拷锟斤拷锟截碉拷锟竭硷拷锟斤拷锟�?锟斤拷锟�<2>锟叫癸拷系锟斤拷<br>
 * <4>,锟斤拷锟斤拷锟脚伙拷
 */
public class AudioPlayer implements OnErrorListener, OnBufferingUpdateListener,
		MediaPlayer.OnCompletionListener {

	private static final String TAG = "AudioPlayer";
	private MediaPlayer mPlayer;
	private String current;
	private static final int MIN_BUFF = 100 * 1024;
	private int totalKbRead = 0;
	private Handler handler = new Handler();
	private File DLTempFile;
	private File BUFFTempFile;
	private final String TEMP_DOWNLOAD_FILE_NAME = "tempMediaData";
	private final String TEMP_BUFF_FILE_NAME = "tempBufferData";
	private final String FILE_POSTFIX = ".mp4";
	private final int PER_READ = 1024;
	private boolean pause;
	private boolean stop;
	private final int UNKNOWN_LENGTH = -1;
	private Handler mHandler = null;

	public void setHandler(Handler handler) {
		mHandler = handler;
	}

	public void play(final String path) {
		downloadOver = false;
		totalKbRead = 0;
		try {
			Log.v(TAG, "playing: " + path);
			if (path.equals(current) && mPlayer != null) {
				mPlayer.start();
				return;
			}
			current = path;
			mPlayer = null;
			new PlayThread(current).start();
		} catch (Exception e) {
		}
	}

	private void setListener() {
		if (mPlayer != null) {
			mPlayer.setOnErrorListener(this);
			mPlayer.setOnBufferingUpdateListener(this);
			mPlayer.setOnCompletionListener(this);
		}
	}

	/*
	 * 锟斤拷锟斤拷锟斤拷荩锟斤拷侄锟斤拷锟斤拷锟� todo:锟斤拷锟斤拷式锟酵分讹拷
	 *
	 * @param mediaUrl
	 *
	 * @param start
	 *
	 * @param end
	 */

	private void playFromNet(String mediaUrl, int start, int end) {
		URLConnection cn = null;
		FileOutputStream out = null;
		InputStream is = null;
		try {
			cn = new URL(mediaUrl).openConnection();
			cn.connect();
			is = cn.getInputStream();
			int mediaLength = cn.getContentLength();
			if (is == null) {
				return;
			}
			// deleteTempFile(true);
			DLTempFile = File.createTempFile(TEMP_DOWNLOAD_FILE_NAME,
					FILE_POSTFIX);
			out = new FileOutputStream(DLTempFile);
			byte buf[] = new byte[PER_READ];
			int readLength = 0;

			while (readLength != -1 && !stop) {
				if (pause) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					continue;
				}

				readLength = is.read(buf);
				if (readLength > 0) {
					try {
						out.write(buf, 0, readLength);
						totalKbRead += readLength;
					} catch (Exception e) {
						Log.e(TAG, e.toString());
					}
				}

				dealWithBufferData();
			}// end while

			if (totalKbRead == mediaLength) {
				downloadOver = true;
				dealWithLastData();

				// 删锟斤拷锟斤拷时锟侥硷拷
				if (DLTempFile != null && DLTempFile.exists()) {
					DLTempFile.delete();
				}
			}

		} catch (MalformedURLException e) {
			Log.e(TAG, e.toString());
		} catch (IOException e) {
			Log.e(TAG, e.toString());
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private boolean downloadOver = false;
	private boolean wasPlayed = false;

	private void dealWithBufferData() {
		if (mPlayer == null || !wasPlayed) {
			if (totalKbRead >= MIN_BUFF) {
				try {
					startMediaPlayer();
				} catch (Exception e) {
				}
			}
		} else if (mPlayer.getDuration() - mPlayer.getCurrentPosition() <= 1000) {
			// deleteTempFile(true);
			transferBufferToMediaPlayer();
		}
	}

	private void startMediaPlayer() {
		try {
			// deleteTempFile(true);
			BUFFTempFile = File.createTempFile(TEMP_BUFF_FILE_NAME,
					FILE_POSTFIX);
			// FileSystemUtil.copyFile(DLTempFile, BUFFTempFile);
			mPlayer = new MediaPlayer();
			setListener();
			mPlayer.setDataSource(BUFFTempFile.getAbsolutePath());
			mPlayer.prepare();
			mPlayer.start();
			wasPlayed = true;
		} catch (IOException e) {
		}
	}

	private void transferBufferToMediaPlayer() {
		try {
			boolean wasPlaying = mPlayer.isPlaying();
			int curPosition = mPlayer.getCurrentPosition();
			mPlayer.pause();
			BUFFTempFile = File.createTempFile(TEMP_BUFF_FILE_NAME,
					FILE_POSTFIX);
			// FileSystemUtil.copyFile(DLTempFile, BUFFTempFile);
			mPlayer = new MediaPlayer();
			mPlayer.setDataSource(BUFFTempFile.getAbsolutePath());
			mPlayer.prepare();
			mPlayer.seekTo(curPosition);
			boolean atEndOfFile = mPlayer.getDuration()
					- mPlayer.getCurrentPosition() <= 1000;
			if (wasPlaying || atEndOfFile) {
				mPlayer.start();
			}

		} catch (Exception e) {
		}
	}

	private void dealWithLastData() {
		Runnable updater = new Runnable() {
			public void run() {
				transferBufferToMediaPlayer();
			}
		};
		handler.post(updater);
	}

	public void onCompletion(MediaPlayer mp) {
		if (mHandler != null) {
			// mHandler.sendEmptyMessage(Preferences.MEDIA_ENDED);
		}
	}

	public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
		}

		if (mHandler != null) {
			// mHandler.sendEmptyMessage(Preferences.MEDIA_ERROR);
		}
		return true;
	}

	public void onBufferingUpdate(MediaPlayer arg0, int percent) {
		Log.d(TAG, "onBufferingUpdate called --->   percent:" + percent);
		if (mHandler != null) {
			// mHandler.sendEmptyMessage(Preferences.EMDIA_BUFF_CHANGE);
		}
	}

	private class PlayThread extends Thread {
		private String url;

		PlayThread(String url) {
			this.url = url;
		}

		public void run() {
			if (!URLUtil.isNetworkUrl(url)) {
				mPlayer = new MediaPlayer();
				setListener();
				try {
					// if (url.startsWith("content://")) {
					// mPlayer.setDataSource(MediaPlayService.this, Uri
					// .parse(url));
					// } else {
					mPlayer.setDataSource(url);
					// }
					// mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
					// setVolume(0f);
					mPlayer.prepare();
					mPlayer.start();
				} catch (IllegalArgumentException e) {
					Log.e(TAG, e.toString());
				} catch (IllegalStateException e) {
					Log.e(TAG, e.toString());
				} catch (IOException e) {
					Log.e(TAG, e.toString());
				}
			} else {
				playFromNet(url, 0, UNKNOWN_LENGTH);
			}
		}
	}
}
