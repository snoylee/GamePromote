package com.xygame.sg.activity.personal.bean;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tianxr
 * @date 2015年11月26日
 */
public class MeOverview implements Serializable {
	private String usernick;
	private String userIcon;
	private int age;
	private List<UserLogo> extLogos = new ArrayList<>();
	private List<UserTypeVo> utypes = new ArrayList<>();
	private int attenCount;
	private int collectCount;
	private int lastVisitCount;
	private UserVideo video ;

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

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public List<UserLogo> getExtLogos() {
		return extLogos;
	}

	public void setExtLogos(List<UserLogo> extLogos) {
		this.extLogos = extLogos;
	}

	public List<UserTypeVo> getUtypes() {
		return utypes;
	}

	public void setUtypes(List<UserTypeVo> utypes) {
		this.utypes = utypes;
	}

	public int getAttenCount() {
		return attenCount;
	}

	public void setAttenCount(int attenCount) {
		this.attenCount = attenCount;
	}

	public int getCollectCount() {
		return collectCount;
	}

	public void setCollectCount(int collectCount) {
		this.collectCount = collectCount;
	}

	public int getLastVisitCount() {
		return lastVisitCount;
	}

	public void setLastVisitCount(int lastVisitCount) {
		this.lastVisitCount = lastVisitCount;
	}

	public UserVideo getVideo() {
		return video;
	}

	public void setVideo(UserVideo video) {
		this.video = video;
	}
}
