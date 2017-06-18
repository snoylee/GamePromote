package com.xygame.sg.utils;
public interface HttpCallBack {
	void onSuccess(String msg, int requestCode);
	void onFailure(int errorCode, String msg, int requestCode);
	void onProgress(String objectKey, int byteCount, int totalSize);
}
