package com.xygame.sg.activity.personal.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 模特详情资料基本信息展现类
 * @author syblike
 *
 */
public class ModelDataVo implements Serializable{

	private static final long serialVersionUID = -8640082800514647588L;
	
	private long seeUserId; //用户ID
	
	private String usernick;//用户昵称
	
	private Date birthday;//生日
	
	private int age; //年龄
	
	private Integer city;//城市
	
	private int gender;//性别
	
	private String userPin;//用户模特号
	
	private String userIcon;//用户基本头像
	
	private List<String> userIcons;//用户头像列表
	
	private List<String> modelStyles;//用户风格类型
	
	private List<UserType> userTypes;//用户身份类型
	
	private int fansCount; //用户粉丝数
	
	private boolean isFans;//是否关注

	private String videoUrl;//视频url
	
	private Integer videoSize;//视频大小
	
	private Integer videoTime;//视频时间

	private ScoreVo score;//评分类
	
	public long getSeeUserId() {
		return seeUserId;
	}

	public void setSeeUserId(long seeUserId) {
		this.seeUserId = seeUserId;
	}
	
	public String getUsernick() {
		return usernick;
	}

	public void setUsernick(String usernick) {
		this.usernick = usernick;
	}

	public String getUserIcon() {
		return userIcon;
	}

	public void setUserIcon(String userIcon) {
		this.userIcon = userIcon;
	}

	public List<String> getUserIcons() {
		return userIcons;
	}

	public void setUserIcons(List<String> userIcons) {
		this.userIcons = userIcons;
	}

	public List<UserType> getUserTypes() {
		return userTypes;
	}

	public void setUserTypes(List<UserType> userTypes) {
		this.userTypes = userTypes;
	}

	public int getFansCount() {
		return fansCount;
	}

	public void setFansCount(int fansCount) {
		this.fansCount = fansCount;
	}

	public boolean getIsFans() {
		return isFans;
	}

	public void setIsFans(boolean isFans) {
		this.isFans = isFans;
	}

	public String getUserPin() {
		return userPin;
	}

	public void setUserPin(String userPin) {
		this.userPin = userPin;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Integer getCity() {
		return city;
	}

	public void setCity(Integer city) {
		this.city = city;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public void setFans(boolean isFans) {
		this.isFans = isFans;
	}

	public List<String> getModelStyles() {
		return modelStyles;
	}

	public void setModelStyles(List<String> modelStyles) {
		this.modelStyles = modelStyles;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public Integer getVideoSize() {
		return videoSize;
	}

	public void setVideoSize(Integer videoSize) {
		this.videoSize = videoSize;
	}

	public Integer getVideoTime() {
		return videoTime;
	}

	public void setVideoTime(Integer videoTime) {
		this.videoTime = videoTime;
	}

	public ScoreVo getScore() {
		return score;
	}

	public void setScore(ScoreVo score) {
		this.score = score;
	}

	
	
}
