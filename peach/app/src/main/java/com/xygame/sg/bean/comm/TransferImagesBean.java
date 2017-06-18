package com.xygame.sg.bean.comm;

import com.xygame.sg.activity.personal.bean.VideoBean;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class TransferImagesBean implements Serializable{

	private static final long serialVersionUID = 1L;

	private LinkedList<String> selectImagePah;
	
	private List<String> deleteImages;

	private VideoBean videoBean;
	
	

	public List<String> getDeleteImages() {
		return deleteImages;
	}

	public void setDeleteImages(List<String> deleteImages) {
		this.deleteImages = deleteImages;
	}

	public LinkedList<String> getSelectImagePath() {
		return selectImagePah;
	}

	public void setSelectImagePah(LinkedList<String> selectImagePah) {
		this.selectImagePah = selectImagePah;
	}

	public VideoBean getVideoBean() {
		return videoBean;
	}

	public void setVideoBean(VideoBean videoBean) {
		this.videoBean = videoBean;
	}
}
