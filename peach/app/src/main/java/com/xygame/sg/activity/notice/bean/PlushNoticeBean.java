/*
 * 文 件 名:  PlushNoticeBean.java
 * 版    权:  玄云网络科技有限公司 Ltd. Copyright 2015-09-28,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  王琪
 * 修改时间:  2015年12月7日
 */
package com.xygame.sg.activity.notice.bean;

import java.io.Serializable;
import java.util.List;

import com.xygame.sg.bean.comm.PhotoesBean;

/**
 * <一句话功能简述> <功能详细描述>
 * 
 * @author 王琪
 * @date 2015年12月7日
 * @action [功能描述]
 */
public class PlushNoticeBean implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private List<ModelRequestBean> modelBeans;

	private PlushNoticeAreaBean cameraArea;

	private String noticeId,noticeTip,cameraParentTypeName, cameraTypeName, cameraParantTypeId,cameraTypeId, cameraTheme, starTime, endTime, reportTime, plushType,
			startTimeDes, endTimeDes, reportTimeDes,camerNum,formatStartTime,formatEndTime,formatReportTime,noticeType;

	private List<PhotoesBean> photoes;
	
	public String getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(String noticeType) {
		this.noticeType = noticeType;
	}

	public String getFormatStartTime() {
		return formatStartTime;
	}

	public void setFormatStartTime(String formatStartTime) {
		this.formatStartTime = formatStartTime;
	}

	public String getFormatEndTime() {
		return formatEndTime;
	}

	public void setFormatEndTime(String formatEndTime) {
		this.formatEndTime = formatEndTime;
	}

	public String getFormatReportTime() {
		return formatReportTime;
	}

	public void setFormatReportTime(String formatReportTime) {
		this.formatReportTime = formatReportTime;
	}

	public String getCameraParentTypeName() {
		return cameraParentTypeName;
	}

	public void setCameraParentTypeName(String cameraParentTypeName) {
		this.cameraParentTypeName = cameraParentTypeName;
	}

	public String getCameraParantTypeId() {
		return cameraParantTypeId;
	}

	public void setCameraParantTypeId(String cameraParantTypeId) {
		this.cameraParantTypeId = cameraParantTypeId;
	}

	public String getCamerNum() {
		return camerNum;
	}

	public void setCamerNum(String camerNum) {
		this.camerNum = camerNum;
	}

	public List<ModelRequestBean> getModelBeans() {
		return modelBeans;
	}

	public void setModelBeans(List<ModelRequestBean> modelBeans) {
		this.modelBeans = modelBeans;
	}

	public PlushNoticeAreaBean getCameraArea() {
		return cameraArea;
	}

	public void setCameraArea(PlushNoticeAreaBean cameraArea) {
		this.cameraArea = cameraArea;
	}

	public String getNoticeTip() {
		return noticeTip;
	}

	public void setNoticeTip(String noticeTip) {
		this.noticeTip = noticeTip;
	}

	public String getCameraTypeName() {
		return cameraTypeName;
	}

	public void setCameraTypeName(String cameraTypeName) {
		this.cameraTypeName = cameraTypeName;
	}

	public String getCameraTypeId() {
		return cameraTypeId;
	}

	public void setCameraTypeId(String cameraTypeId) {
		this.cameraTypeId = cameraTypeId;
	}

	public String getCameraTheme() {
		return cameraTheme;
	}

	public void setCameraTheme(String cameraTheme) {
		this.cameraTheme = cameraTheme;
	}

	public String getStarTime() {
		return starTime;
	}

	public void setStarTime(String starTime) {
		this.starTime = starTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getReportTime() {
		return reportTime;
	}

	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}

	public List<PhotoesBean> getPhotoes() {
		return photoes;
	}

	public void setPhotoes(List<PhotoesBean> photoes) {
		this.photoes = photoes;
	}

	public String getPlushType() {
		return plushType;
	}

	public void setPlushType(String plushType) {
		this.plushType = plushType;
	}

	public String getStartTimeDes() {
		return startTimeDes;
	}

	public void setStartTimeDes(String startTimeDes) {
		this.startTimeDes = startTimeDes;
	}

	public String getEndTimeDes() {
		return endTimeDes;
	}

	public void setEndTimeDes(String endTimeDes) {
		this.endTimeDes = endTimeDes;
	}

	public String getReportTimeDes() {
		return reportTimeDes;
	}

	public void setReportTimeDes(String reportTimeDes) {
		this.reportTimeDes = reportTimeDes;
	}

	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}
	
	

}
