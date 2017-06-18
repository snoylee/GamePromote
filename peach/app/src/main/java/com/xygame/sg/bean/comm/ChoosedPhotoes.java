package com.xygame.sg.bean.comm;

import java.io.Serializable;
import java.util.List;

public class ChoosedPhotoes implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String chocedDir,imagePath,imageName,finalDir,finalPath,finalName,paperDir,paperPath,paperName;
	
	private int mDrawableWidth, mDrawableHeight,controlLocation,olderIndex;
	
	private float mDegree,mScale;
	
	private List<String> mImgs;
	
	private double smallTop,smallLeft;
	
	private boolean isSelect,isCut;
	
	public String getChocedDir() {
		return chocedDir;
	}

	public void setChocedDir(String chocedDir) {
		this.chocedDir = chocedDir;
	}

	public List<String> getmImgs() {
		return mImgs;
	}

	public void setmImgs(List<String> mImgs) {
		this.mImgs = mImgs;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}

	public int getmDrawableWidth() {
		return mDrawableWidth;
	}

	public void setmDrawableWidth(int mDrawableWidth) {
		this.mDrawableWidth = mDrawableWidth;
	}

	public int getmDrawableHeight() {
		return mDrawableHeight;
	}

	public void setmDrawableHeight(int mDrawableHeight) {
		this.mDrawableHeight = mDrawableHeight;
	}

	public float getmDegree() {
		return mDegree;
	}

	public void setmDegree(float mDegree) {
		this.mDegree = mDegree;
	}

	public float getmScale() {
		return mScale;
	}

	public void setmScale(float mScale) {
		this.mScale = mScale;
	}

	public String getFinalDir() {
		return finalDir;
	}

	public void setFinalDir(String finalDir) {
		this.finalDir = finalDir;
	}

	public String getFinalPath() {
		return finalPath;
	}

	public void setFinalPath(String finalPath) {
		this.finalPath = finalPath;
	}

	public String getFinalName() {
		return finalName;
	}

	public void setFinalName(String finalName) {
		this.finalName = finalName;
	}

	public int getControlLocation() {
		return controlLocation;
	}

	public void setControlLocation(int controlLocation) {
		this.controlLocation = controlLocation;
	}

	public String getPaperDir() {
		return paperDir;
	}

	public void setPaperDir(String paperDir) {
		this.paperDir = paperDir;
	}

	public String getPaperPath() {
		return paperPath;
	}

	public void setPaperPath(String paperPath) {
		this.paperPath = paperPath;
	}

	public String getPaperName() {
		return paperName;
	}

	public void setPaperName(String paperName) {
		this.paperName = paperName;
	}

	public double getSmallTop() {
		return smallTop;
	}

	public void setSmallTop(double smallTop) {
		this.smallTop = smallTop;
	}

	public double getSmallLeft() {
		return smallLeft;
	}

	public void setSmallLeft(double smallLeft) {
		this.smallLeft = smallLeft;
	}

	public int getOlderIndex() {
		return olderIndex;
	}

	public void setOlderIndex(int olderIndex) {
		this.olderIndex = olderIndex;
	}

	public boolean isCut() {
		return isCut;
	}

	public void setCut(boolean isCut) {
		this.isCut = isCut;
	}
	
	
}
