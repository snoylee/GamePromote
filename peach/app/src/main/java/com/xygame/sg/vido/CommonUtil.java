package com.xygame.sg.vido;



import java.io.File;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

public class CommonUtil {

	public static boolean hasSDCard() {
		String status = Environment.getExternalStorageState();
		return status.equals(Environment.MEDIA_MOUNTED);
	}
	
//	public static String getRootFilePath() {
//		if (hasSDCard()) {
////			return Environment.getExternalStorageDirectory().getAbsolutePath() + "/";// filePath:/sdcard/
//			return RecordApplication.imageCacher;
//		} 
//		return null;
////		else {
////			return Environment.getDataDirectory().getAbsolutePath() + "/data/"; // filePath: /data/data/
////		}
//	}
	
	 public static String initImageCache(Context context) {
	        String imageCacher = context.getExternalCacheDir().getAbsolutePath() + File.separator + "cacheImage" + File.separator;
	        File f = new File(imageCacher);
	        if (!f.exists()) {
	            f.mkdir();
	        }
	        return imageCacher;
	    }
	
	
	public static boolean checkNetState(Context context){
    	boolean netstate = false;
		ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(connectivity != null)
		{
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++)
				{
					if (info[i].getState() == NetworkInfo.State.CONNECTED) 
					{
						netstate = true;
						break;
					}
				}
			}
		}
		return netstate;
    }
	
	public static void showToask(Context context, String tip){
		Toast.makeText(context, tip, Toast.LENGTH_SHORT).show();
	}

	public static int getScreenWidth(Context context) {
		WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getWidth();
	}
	
	public static int getScreenHeight(Context context) {
		WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getHeight();
	}
	
}
