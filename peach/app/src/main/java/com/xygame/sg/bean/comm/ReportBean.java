/*
 * 文 件 名:  ReportBean.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年11月9日
 */
package com.xygame.sg.bean.comm;

import java.io.Serializable;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  王琪
 * @date 2015年11月9日
 * @action  [举报实体类]
 */
public class ReportBean implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private String reportTxt,typeId;
	private boolean isReport;
	/**
	 * 获取 reportTxt
	 * @return 返回 reportTxt
	 */
	public String getReportTxt() {
		return reportTxt;
	}
	/**
	 * 设置 reportTxt
	 * @param 对reportTxt进行赋值
	 */
	public void setReportTxt(String reportTxt) {
		this.reportTxt = reportTxt;
	}
	/**
	 * 获取 isReport
	 * @return 返回 isReport
	 */
	public boolean isReport() {
		return isReport;
	}
	/**
	 * 设置 isReport
	 * @param 对isReport进行赋值
	 */
	public void setReport(boolean isReport) {
		this.isReport = isReport;
	}
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	
	
	
}
