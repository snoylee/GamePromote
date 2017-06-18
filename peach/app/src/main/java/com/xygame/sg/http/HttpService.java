package com.xygame.sg.http;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;

import com.xygame.sg.utils.ConstTaskTag;
import com.xygame.sg.utils.ShowMsgDialog;

public class HttpService extends AsyncTask<Void, Void, ResponseBean> {
	
	private RequestBean requestBean;
	private int constData;
	private ResponseBean responseBean;
	private Handler handler;
	public HttpService(Callback context,RequestBean requestBean,int constData) {
		this.requestBean=requestBean;
		this.constData=constData;
		handler = new Handler(context);
	}

	@Override
	protected ResponseBean doInBackground(Void... params) {
		responseBean= HttpAction.requestService(requestBean);
		return responseBean;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(ResponseBean data) {
		super.onPostExecute(data);
		ShowMsgDialog.cancel();
		data.setPosionSign(constData);
		data.setIsPublic(requestBean.isPublic());
		Message message = Message.obtain(handler, ConstTaskTag.CONST_TAG_PUBLIC, data);
		message.sendToTarget();
	}
}
