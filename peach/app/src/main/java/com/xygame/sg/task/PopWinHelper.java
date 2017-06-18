/**
 * 
 */
package com.xygame.sg.task;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.xygame.sg.R;
import com.xygame.sg.utils.ImageActivity.Constants;

/**
 * @author huiming
 *
 */
public class PopWinHelper {
	private static PopWinHelper popWinHelper = new PopWinHelper();
	private PopupWindow photoPopupWindow;
	private PopupWindow sendMessagePopWin;
	private PopWinHelper(){}
	public static PopWinHelper newInstance(){
		return popWinHelper;
	}
	protected InputFilter[] filter = {new ValueLenFilter(Constants.MAX_LEN_DIAL)};
	public interface SendMessageCallBack{
		void send(String content);
	}
	
	public interface OperationCallBack{
		void handle();
	}

    public void showMessagePopWin(Context context,View anchnorView,String target,final SendMessageCallBack callBack){
        showMessagePopWin(context, R.layout.inputmessage,anchnorView,target,callBack);
    }

	public void showMessagePopWin(Context context,int layoutId,View anchnorView,String target,final SendMessageCallBack callBack){
		if(sendMessagePopWin != null){
			sendMessagePopWin.dismiss();
			sendMessagePopWin = null;
		}
		View view = LayoutInflater.from(context).inflate(layoutId, null, false);

		final EditText etxtMessage = (EditText) view.findViewById(R.id.etxtMessage);
		//设置最多能输入多少字符
		etxtMessage.setFilters(filter);
		if(target != null && target.trim().length() > 0){
			etxtMessage.setHint("回复"+target+":");
		}

		Button btnSend = (Button) view.findViewById(R.id.btnSend);
		btnSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String content = etxtMessage.getEditableText().toString();
				if(content == null || content.trim().length() == 0){
					showToast(v.getContext(), "请输入回复内容!");
					return;
				}
				callBack.send(content);
				dismissMessagePopWindow(v.getContext());
			}
		});
		sendMessagePopWin = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,true);
		sendMessagePopWin.setFocusable(true);
		sendMessagePopWin.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Rect outRect = new Rect();
				v.getDrawingRect(outRect);
				if(!outRect.contains((int)event.getRawX(), (int)event.getRawY())){
					dismissMessagePopWindow(v.getContext());
					return true;
				}
				return false;
			}
		});
		sendMessagePopWin.showAtLocation(anchnorView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
	}
	public static void showToast(Context context, int msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	public static void showToast(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	public void dismissMessagePopWindow(Context context){
		if(sendMessagePopWin != null){
			sendMessagePopWin.dismiss();
			sendMessagePopWin = null;
		}
	}
	
	public void showSelectPhotoPopWindow(Activity context,View anchnorView,OnClickListener listener){
		if(photoPopupWindow != null){
			photoPopupWindow.dismiss();
			photoPopupWindow = null;
		}
		View view = LayoutInflater.from(context).inflate(R.layout.select_photo_pop, null, false);
		photoPopupWindow = new PopupWindow(view, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		WindowManager.LayoutParams params = context.getWindow().getAttributes();
		params.alpha = 0.7f;
		context.getWindow().setAttributes(params);
		photoPopupWindow.setFocusable(true);
		photoPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//		photoPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
		photoPopupWindow.showAtLocation(anchnorView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
		View cameraBtn = view.findViewById(R.id.camera);
		View galaryBtn = view.findViewById(R.id.galary);
		View dismissBtn = view.findViewById(R.id.dismiss);
		cameraBtn.setOnClickListener(listener);
		galaryBtn.setOnClickListener(listener);
		dismissBtn.setOnClickListener(listener);
	}
	
	public void dismissPhotoPopWindow(Activity context){
		WindowManager.LayoutParams params2 = context.getWindow().getAttributes();
		params2.alpha = 1;
		context.getWindow().setAttributes(params2);
		if(photoPopupWindow != null){
			photoPopupWindow.dismiss();
			photoPopupWindow = null;
		}
	}
	
	public void showSelectOperationPopWindow(final Activity context,View anchnorView,final OperationCallBack operationCallback){
		if(photoPopupWindow != null){
			photoPopupWindow.dismiss();
			photoPopupWindow = null;
		}
		OnClickListener clickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch(v.getId()){
				case R.id.delete:
					operationCallback.handle();
				case R.id.dismiss:
					dismissOperationPopWindow(context);
					break;
				}
			}
		};
		
		View view = LayoutInflater.from(context).inflate(R.layout.select_operation_pop, null, false);
		photoPopupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		WindowManager.LayoutParams params = context.getWindow().getAttributes();
		params.alpha = 0.7f;
		context.getWindow().setAttributes(params);
		photoPopupWindow.setFocusable(true);
//		photoPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        photoPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//		photoPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
        photoPopupWindow.showAtLocation(anchnorView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//		photoPopupWindow.showAsDropDown(anchnorView);
//		photoPopupWindow.showAtLocation(anchnorView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
		View delete = view.findViewById(R.id.delete);
		View dismissBtn = view.findViewById(R.id.dismiss);
		delete.setOnClickListener(clickListener);
		dismissBtn.setOnClickListener(clickListener);
	}
	
	public void dismissOperationPopWindow(Activity context){
		WindowManager.LayoutParams params2 = context.getWindow().getAttributes();
		params2.alpha = 1;
		context.getWindow().setAttributes(params2);
		if(photoPopupWindow != null){
			photoPopupWindow.dismiss();
			photoPopupWindow = null;
		}
	}

    public void showPopWindow(Activity context,View anchnorView,int layoutResId,int[] resIds,OnClickListener listener){
        if(photoPopupWindow != null){
            photoPopupWindow.dismiss();
            photoPopupWindow = null;
        }
        View view = LayoutInflater.from(context).inflate(layoutResId, null, false);
        photoPopupWindow = new PopupWindow(view, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams params = context.getWindow().getAttributes();
        params.alpha = 0.7f;
        context.getWindow().setAttributes(params);
        photoPopupWindow.setFocusable(true);
        photoPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//		photoPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
        photoPopupWindow.showAtLocation(anchnorView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        for(int resId : resIds){
            View findView = view.findViewById(resId);
            if(findView != null){
                findView.setOnClickListener(listener);
            }
        }
    }

    public void dismissPopWindow(Activity context){
        WindowManager.LayoutParams params2 = context.getWindow().getAttributes();
        params2.alpha = 1;
        context.getWindow().setAttributes(params2);
        if(photoPopupWindow != null){
            photoPopupWindow.dismiss();
            photoPopupWindow = null;
        }
    }

}
