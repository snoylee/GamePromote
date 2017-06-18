/*
 * 文 件 名:  ResponseRegisterCheckVerify.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月5日
 */
package com.xygame.sg.task.login;

import java.util.List;

import android.widget.EditText;
import android.widget.Toast;
import base.action.Action.Param;

import com.xygame.sg.R;
import com.xygame.sg.activity.commen.ForgetPwdFirstPageActivity;
import com.xygame.sg.utils.Constants;
import com.xygame.sg.utils.NetWorkUtil;
import com.xygame.sg.utils.PatternUtils;
import com.xygame.sg.utils.ShowMsgDialog;
import com.xygame.sg.utils.UserPreferencesUtil;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author 王琪
 * @date 2015年11月5日
 * @action [验证码校验]
 */
public class ResponseForgetCheckVerify extends NetWorkUtil {

	private EditText cellphone, verifyCode;
	private ForgetPwdFirstPageActivity activity;

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
		ShowMsgDialog.cancel();
		String resultCode = aparam.getResultunit().getRawMap().get("success");
		if (Constants.RESULT_CODE.equals(resultCode)) {
			String token=aparam.getResultunit().getRawMap().get("record");
			UserPreferencesUtil.setUserToken(activity, token);
//			activity.intentNextActivity();
		} else {
			Toast.makeText(aparam.getActivity(), "验证码不正确", Toast.LENGTH_SHORT)
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
		activity = ((ForgetPwdFirstPageActivity) aparam.getActivity());
		cellphone = (EditText) aparam.getActivity()
				.findViewById(R.id.cellphone);
		verifyCode = (EditText) aparam.getActivity().findViewById(
				R.id.verifyCode);
		String phone = cellphone.getText().toString().trim();
		String vCode = verifyCode.getText().toString().trim();
		if (!"".equals(phone)) {
			if (phone.matches(PatternUtils.MOBILE_PHONE)) {
				if (!"".equals(vCode)) {
					UserPreferencesUtil.setCellPhone(activity, phone);
					ShowMsgDialog.show(activity, "验证码校验中...", false);
					return super.run(methodname, params, aparam);
				} else {
					Toast.makeText(aparam.getActivity(), "验证码不能为空",
							Toast.LENGTH_SHORT).show();
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
}