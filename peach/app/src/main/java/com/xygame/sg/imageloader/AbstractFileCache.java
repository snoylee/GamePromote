package com.xygame.sg.imageloader;

import java.io.File;

import android.content.Context;
import android.util.Log;



public abstract class AbstractFileCache {

	private String dirString;

	public AbstractFileCache(Context context) {

		dirString = getCacheDir();
		boolean ret = FileHelper.createDirectory(dirString);
		Log.e("", "FileHelper.createDirectory:" + dirString + ", ret = " + ret);
	}

	public File getFile(String url) {
		String path = getSavePath(url);
		if(path != null){
			File f = new File(path);
			return f;
		}
		return null;
	}

	public abstract String  getSavePath(String url);
	public abstract String  getCacheDir();

	public void clear() {
		FileHelper.deleteDirectory(dirString);
	}

}
