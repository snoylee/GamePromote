/*
 * 文 件 名:  ResponseRegisterVerify.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月4日
 */
package com.xygame.sg.task.login;

import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xygame.sg.R;
import com.xygame.sg.activity.commen.BangCellPhoneActivity;
import com.xygame.sg.activity.commen.RegisterFristPageActivity;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.NetWorkUtil;
import com.xygame.sg.utils.PatternUtils;

import java.util.List;

import base.action.Action.Param;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author 王琪
 * @date 2015年11月4日
 * @action [注册验证码请求返回]
 */
public class ResponseBangVerify extends NetWorkUtil {

	private EditText cellphone;
	private Button verifyButton;
	private BangCellPhoneActivity activity;

	/**
	 * 重载方法
	 * 
	 * @param result
	 * @return
	 */
	@Override
	public String runResult(String result) {
		// TODO Auto-generated method stub
		String res = result;
		System.out.println(res);
		return super.runResult(result);
	}

	/**
	 * 重载方法
	 * 
	 * @param aparam
	 * @param object
	 */
	@Override
	public void callback(Param aparam, Object object) {
		// TODO Auto-generated method stub
		String resultCode = aparam.getResultunit().getRawMap().get("success");
		if (Constants.RESULT_CODE.equals(resultCode)) {
			Toast.makeText(aparam.getActivity(), "验证码发送成功", Toast.LENGTH_SHORT)
					.show();
		} else {
			String msg = aparam.getResultunit().getRawMap().get("msg");
			Toast.makeText(aparam.getActivity(), msg, Toast.LENGTH_SHORT)
					.show();
		}
		super.callback(aparam, object);
	}

	/**
	 * 重载方法
	 * 
	 * @param methodname
	 * @param params
	 * @param aparam
	 * @return
	 */
	@Override
	public Object run(String methodname, List<String> params, Param aparam) {
		// TODO Auto-generated method stub
		activity = ((BangCellPhoneActivity) aparam.getActivity());
		cellphone = (EditText) aparam.getActivity()
				.findViewById(R.id.cellphone);
		verifyButton = (Button) aparam.getActivity().findViewById(
				R.id.verifyButton);
		String phone = cellphone.getText().toString().trim();
		if (!"".equals(phone)) {
			if (phone.matches(PatternUtils.MOBILE_PHONE)) {
				if (activity.isSend()) {
					activity.setSend(false);
					timerCount(60 * 1000, 1000);
					params.add("telephone=" + phone);
					return super.run(methodname, params, aparam);
				}
			} else {
				Toast.makeText(aparam.getActivity(), "请输入正确的手机号",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(aparam.getActivity(), "手机号码不能为空", Toast.LENGTH_SHORT)
					.show();
		}
		return null;
	}

	private void timerCount(int minute, int second) {
		// TODO Auto-generated method stub
		new CountDownTimer(minute, second) {

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				verifyButton.setText(String.valueOf(millisUntilFinished / 1000)
						.concat("秒后重发"));
				verifyButton.setBackgroundResource(R.drawable.shape_rect_dark_gray);
				activity.setSend(false);
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				verifyButton.setText("发送");
				verifyButton.setBackgroundResource(R.drawable.shape_rect_dark_green);
				activity.setSend(true);
			}
		}.start();

	}
}
