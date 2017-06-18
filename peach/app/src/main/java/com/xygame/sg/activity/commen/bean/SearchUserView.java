package com.xygame.sg.activity.commen.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author tianxr
 * @date 2016年3月17日
 */
public class SearchUserView implements Serializable {
	private Long reqTime;
	private List<SearchUserVo> users = new ArrayList<>();

	public SearchUserView() {
	}

	public SearchUserView(Long reqTime, List<SearchUserVo> users) {
		this.reqTime = reqTime;
		this.users = users;
	}

	public Long getReqTime() {
		return reqTime;
	}

	public void setReqTime(Long reqTime) {
		this.reqTime = reqTime;
	}

	public List<SearchUserVo> getUsers() {
		return users;
	}

	public void setUsers(List<SearchUserVo> users) {
		this.users = users;
	}
}
