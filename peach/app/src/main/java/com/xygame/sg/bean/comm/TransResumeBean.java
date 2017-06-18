/*
 * 文 件 名:  TransStyleBean.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月25日
 */
package com.xygame.sg.bean.comm;

import com.xygame.sg.activity.personal.bean.ModelResumeVo;

import java.io.Serializable;
import java.util.List;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  王琪
 * @date 2015年11月25日
 * @action  [功能描述]
 */
public class TransResumeBean implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private List<ModelResumeVo>  resumeList;

	public List<ModelResumeVo>  getResumeList() {
		return resumeList;
	}

	public void setResumeList(List<ModelResumeVo>  resumeList) {
		this.resumeList = resumeList;
	}

	
}
